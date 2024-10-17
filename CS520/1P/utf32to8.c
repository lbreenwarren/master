#include <stdio.h>

#define ERR_TWO_ARGUMENTS "Requires 2 arguments, input file and output file"
#define ERR_INVALID_FILE "Cannot open file:"
#define ERR_CONTINUATION_BYTE "Missing or Unexpected Continuation byte at offset:"
#define ERR_OVERLONG_ENCODING "Overlong Encoding at offset:"
#define ERR_NONCHARACTER "The sequence decodes to a noncharacter: start byte at offset:"
#define ERR_RESERVED_SURROGATES "The sequence decodes to a value reserved for surrogates: start byte at offset:"
#define ERR_OUT_OF_RANGE "The sequence decodes to a value greater than 0x10FFFF: start byte at offset:"
#define ERR_INCOMPLETE_CODE_UNIT "Incomplete UTF-16 code unit at offset:"
#define ERR_UNPAIRED_SURROGATE "Unpaired surrogate at offset:"
#define ERR_MISSING_BOM "Missing BOM" // followed by the 2 or 3 bytes that you see at the start of the file in hex (if there are at least 2 or 3 bytes)
#define ERR_INCORRECT_START_BYTE "Incorrect start byte at offset:" //default error if all other specific error cases fail

unsigned int peek_next(FILE *fp) {
    long curr_fp_offset = ftell(fp);
    unsigned int next_char = getc(fp);
    fseek(fp, curr_fp_offset, SEEK_SET);
    return next_char;
}

int check_errors(unsigned int utf32_char, int start_byte_offset) {
        if (utf32_char > 0x10FFFF) {
            fprintf(stderr, ERR_OUT_OF_RANGE " %d", start_byte_offset);
            return -1;
        }
        
        if (((utf32_char & 0xffff) == 0xFFFF) || ((utf32_char & 0xffff) == 0xfffe)) {
            fprintf(stderr, ERR_NONCHARACTER " %d", start_byte_offset);
            return -1;
        }
        
        if ((utf32_char >= 0xFDD0) && (utf32_char <= 0xFDEF)) {
            fprintf(stderr, ERR_NONCHARACTER " %d", start_byte_offset);
            return -1;
        }   
        
        if ((utf32_char >= 0xD800) && (utf32_char <= 0xDFFF)) {
            fprintf(stderr, ERR_RESERVED_SURROGATES " %d", start_byte_offset);    
            return -1;
        }
}

unsigned int get_be_utf32(FILE *fp) {
    
    unsigned int ret = 0;
    for (int i = 0; i < 4; ++i) {
        int byte = getc(fp);
        
        if (byte == EOF) {
            fprintf(stderr, ERR_INCORRECT_START_BYTE "%ld", ftell(fp));
            return -1;
        }
        
        ret |= (byte << (8 * (3 - i)));
    }
    return ret;
}

unsigned int get_le_utf32(FILE *fp) {
    unsigned int ret = 0;
    for (int i = 0; i < 4; ++i) {
        int byte = getc(fp);

        if (byte == EOF) {
            fprintf(stderr, ERR_INCORRECT_START_BYTE "%ld", ftell(fp));
            return -1;
        }
        
        ret |= (byte << (8 *  i));
    }
    return ret;
}

void utf32_to_8(unsigned int utf32_char, FILE *output_fp) {
    unsigned int mask_bit = 1 << 31;
    int num_bits = 32;
    
    while ((utf32_char & mask_bit) < 1) {
        mask_bit = mask_bit >> 1;
        --num_bits;
    }
  
    int num_trail_bytes = num_bits / 6; 
    if (num_bits == 17) {
        num_trail_bytes = 3;
    }
    unsigned int head_byte = 0;
    
   
    for (int i  = 0; i < num_trail_bytes + 1; ++i) {
        head_byte |= (1 << (7 - i));
    }
  
    int num_data_bits_in_head = 6 - num_trail_bytes;  
    int head_data_mask = 1 << (num_data_bits_in_head - 1);
    unsigned int char_mask = 1 << ((6 * num_trail_bytes) + (num_data_bits_in_head - 1));
    // printf("num_data_in_head: %d\n num_trail_bytes:%d\n", num_data_bits_in_head, num_trail_bytes);
    // printf("head_data_mask: %x\n char_mask: %x", head_data_mask, char_mask);
    for (int i = 0; i < num_data_bits_in_head; ++i) {
        
        if ((utf32_char & char_mask) > 0) {
            head_byte |= head_data_mask;
        }
        head_data_mask >>= 1;
        char_mask >>= 1;
    }

    putc(head_byte, output_fp);

    
   
    for(int i = 1; i <= num_trail_bytes; ++i) {
        unsigned int trail_byte = 0x80;
        trail_byte |= ((utf32_char >> (6 * (num_trail_bytes - i))) & (0x3f));
        putc(trail_byte, output_fp);
    }
   
}

int main(int argc, char* argv[]) {
    
    if (argc != 3) {
        fprintf(stderr, ERR_TWO_ARGUMENTS);
        return -1;
    }

    FILE *input_fp = fopen(argv[1], "rb");

    if (input_fp == NULL) {
        fprintf(stderr, ERR_INVALID_FILE " %s", argv[1]);
        return -1;
    }
    
    FILE *output_fp = fopen(argv[2], "wb");

    if (output_fp == NULL) {
        fprintf(stderr, ERR_INVALID_FILE " %s", argv[2]);
        return -1;
    }

    // Write Bom to output file
     
    putc(0xef, output_fp);
    putc(0xbb, output_fp);
    putc(0xbf, output_fp);
    
    // check first character, assume big endian, if BOM is wrong, check if little endian, if still wrong error
    int big_endian_bool = 1;
    unsigned int utf32_char = get_be_utf32(input_fp);
    
    if (utf32_char != 0xfeff) {
        fseek(input_fp, 0, SEEK_SET);
        utf32_char = get_le_utf32(input_fp);
        big_endian_bool = 0;
    }
    if (utf32_char != 0xfeff) {
        fseek(input_fp, 0, SEEK_SET);
        fprintf(stderr, ERR_MISSING_BOM "%x %x", getc(input_fp), getc(input_fp));
        return -1;
    }

    // prime the loop by getting first utf32 character
    if (big_endian_bool == 1) {
        utf32_char = get_be_utf32(input_fp);
    } else {
        utf32_char = get_le_utf32(input_fp);
    }
   
    while (utf32_char != EOF) {
       
        //convert to utf8
        //if utf32 character is less than 0x80, just chop of leading 0's and write to output
        if (utf32_char < 0x80) {
            unsigned int utf8_char = utf32_char & 0x7f;
            putc(utf8_char, output_fp);
        } else {
            //else convert into UTF8 and write to output
            utf32_to_8(utf32_char, output_fp);
        }
        if (peek_next(input_fp) == EOF) {
            utf32_char = EOF;
        
        //get next utf32 character
        } else {
            long int file_offset = ftell(input_fp);    
            if (big_endian_bool == 1) {
                utf32_char = get_be_utf32(input_fp);
            } else {
                utf32_char = get_le_utf32(input_fp);
            }
            check_errors(utf32_char, file_offset);
        }
    }
    fclose(input_fp);
    fclose(output_fp);
    return 0;
}
 