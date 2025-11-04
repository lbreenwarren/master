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


int check_bom(FILE *fp, FILE *out) {
    unsigned int byte1 = getc(fp);
    if (byte1 == EOF) {
        fprintf(stderr, ERR_INCORRECT_START_BYTE "0");
        return -1;
    }

    unsigned int byte2 = getc(fp);
    if (byte2 == EOF) {
        fprintf(stderr, ERR_INCORRECT_START_BYTE "1");
        return -1;
    }

    unsigned int byte3 = getc(fp);
    if (byte3 == EOF) {
        fprintf(stderr, ERR_INCORRECT_START_BYTE "2");
        return -1;
    }

    if ((byte1 != 0xef) || (byte2 != 0xbb) || (byte3 != 0xbf)) {
        fprintf(stderr, ERR_MISSING_BOM " %x %x %x", byte1, byte2, byte3);
        
        return -1;
    }
}


void write_utf32_char(unsigned int character, FILE *fp) {
    
    unsigned int mask = 0xff000000;

    for (int i = 0; i < 4; ++i) {
       
        int out = character & mask;
        out = out >> ((3-i) * 8);
        putc(out, fp);
        mask = mask >> 8;
    }
}

unsigned int peek_next(FILE *fp) {
    long curr_fp_offset = ftell(fp);
    unsigned int next_char = getc(fp);
    fseek(fp, curr_fp_offset, SEEK_SET);
    return next_char;
}

unsigned int utf8_to_utf32(unsigned int byte, FILE *fp, FILE *out) {
    unsigned int ret = 0;
    unsigned int bit_mask = 0x80;
    
    
    if (((byte & 0x80) == 0)) {

        return ret | byte;
        
    } else {

        int start_byte_offset = ftell(fp);
        
        // if ((byte & 0xff) == 0xf4) {
        //     fprintf(stderr, ERR_OUT_OF_RANGE " %d (%x)", start_byte_offset, byte);
        //     return -1;
        // } 
        // if ((byte & 0xff) > 0xf4) {
        //     fprintf(stderr, ERR_INCORRECT_START_BYTE " %d", start_byte_offset);
        //     return -1;
        // }

        int mask = 0x80;
        int num_bytes = 0;
       
        while ((mask & byte)) {
            ++num_bytes;
            mask = mask >> 1;
        }
        //printf("num_byte: %d\n", num_bytes);
        int byte_1_mask = 0xff >> (num_bytes + 1);
        int byte_n_mask = 0x3f;
        
        //printf("byte1 & mask: %x\n", byte_1_mask & byte);
        if ((byte_1_mask & byte) == 0) {
            int leading_0s = 0;
            unsigned int next_char = peek_next(fp);
            leading_0s += (7 - num_bytes);
            int overlong_mask = 0x20;

            while ((overlong_mask & next_char) == 0) {
                overlong_mask >>= 1;
                ++leading_0s;
            }
        
            if (leading_0s >= 5) {
                fprintf(stderr, ERR_OVERLONG_ENCODING " %d", start_byte_offset);
            }
        }
        ret |= (byte & byte_1_mask);
       
        for (int i = 1; i < num_bytes; ++i) {
            unsigned int byte_n = getc(fp);
            
            if (byte_n == EOF) {
                fprintf(stderr, ERR_INCORRECT_START_BYTE " %ld", ftell(fp));
                return -1;
            }

            if ((byte_n & 0xc0) != 0x80) {
                fprintf(stderr, ERR_CONTINUATION_BYTE " %ld ", ftell(fp));
                return -1;
            }

            ret = ret << 6;
            ret |= (byte_n & byte_n_mask);
        }
       
        unsigned int next_char = peek_next(fp);
        //Error handling

        if ((next_char & 0xc0) == 0x80) {
            fprintf(stderr, ERR_CONTINUATION_BYTE " %ld (%x)", ftell(fp), next_char);
            
            return -1;
        } 
        
        if (ret > 0x10FFFF) {
            write_utf32_char(ret, out);
            fprintf(stderr, ERR_OUT_OF_RANGE " %d", start_byte_offset);

            return -1;
        }
        
        if (((ret & 0xffff) == 0xFFFF) || ((ret & 0xffff) == 0xfffe)) {
            write_utf32_char(ret, out);
            fprintf(stderr, ERR_NONCHARACTER " %d", start_byte_offset);
            
            return -1;
        }
        
        if ((ret >= 0xFDD0) && (ret <= 0xFDEF)) {
            write_utf32_char(ret, out);
            fprintf(stderr, ERR_NONCHARACTER " %d", start_byte_offset);
            
            return -1;
        }   
        
        if ((ret >= 0xD800) && (ret <= 0xDFFF)) {
            write_utf32_char(ret, out);
            fprintf(stderr, ERR_RESERVED_SURROGATES " %d", start_byte_offset);
            
            return -1;
        }

        return ret;
        
    } 
}

int main(int argc, char *argv[]) {
  
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

    //check bom
    if (check_bom(input_fp, output_fp) == -1) {
        return -1;
    }

    //write bom
    write_utf32_char(0xfeff, output_fp);

    unsigned int byte = getc(input_fp);
    int reserved_val = 0;
    int num_bytes = 0;
    while (byte != EOF) {
        unsigned int utf32_char = utf8_to_utf32(byte, input_fp, output_fp);
        if (utf32_char == -1) {
            return -1;
        }
        write_utf32_char(utf32_char, output_fp);

        ++num_bytes;
        byte = getc(input_fp);
    }
    fclose(input_fp);
    fclose(output_fp);
    return 0;



}