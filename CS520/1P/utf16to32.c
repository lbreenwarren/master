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

unsigned int get_be_utf16(FILE *fp) {
    unsigned int ret = 0;
    for (int i = 0; i < 2; ++i) {
        unsigned int byte = getc(fp);

        if (byte == EOF) {
            fprintf(stderr, ERR_INCOMPLETE_CODE_UNIT "%ld", ftell(fp));
            return -1;
        }

        ret |= (byte << (8 * (1-i)));
       
    }

    return ret;
}

unsigned int get_le_utf16(FILE *fp) {
    unsigned int ret = 0;

    for (int i = 0; i < 2; ++i) {
        unsigned int byte = getc(fp);
    
        if (byte == EOF) {
            fprintf(stderr, ERR_INCOMPLETE_CODE_UNIT "%ld", ftell(fp));
            return -1;
        }
        
        ret |=(byte  << (8 * (i)));
        
    }

    return ret;
}

void write_bom(int big_endian_bool, FILE *fp) {

    if (big_endian_bool) {
    
        putc(0x00, fp);
        putc(0x00, fp);
        putc(0xFE, fp);
        putc(0xFF, fp); 
    
    } else {
    
        putc(0xFF, fp);
        putc(0xFE, fp);
        putc(0x00, fp);
        putc(0x00, fp);
    }
}

void write_utf32_char(int big_endian_bool, unsigned int character, FILE *fp) {
    if (big_endian_bool){
        unsigned int mask = 0xff000000;

        for (int i = 0; i < 4; ++i) {
            int out = character & mask;
            out = out >> ((3-i) * 8);
            putc(out, fp);
            mask = mask >> 8;
        }

    } else {
        unsigned int mask = 0x000000ff;
       
        for (int i = 0; i < 4; ++i) {
            int out = character & mask;
            out = out >> (i * 8);
            putc(out, fp);
            mask = mask << 8;
        }
    }
}

unsigned int utf16_to_32(unsigned int lead_surr, unsigned int trail_surr, FILE *fp, long int start_byte_offset) {
    // head surrogate without tail
    if ((trail_surr < 0xDC00) || (trail_surr > 0xDFFF)) {
        fprintf(stderr, ERR_UNPAIRED_SURROGATE " %ld", start_byte_offset);
        return -1;
    }

    unsigned int ms10_bits = lead_surr - 0xD800;
    unsigned int ls10_bits = trail_surr - 0xDC00;
    unsigned int ret = ms10_bits << 10;
    ret |= ls10_bits;
    ret = ret + 0x10000;
    return ret;
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

    int big_endian_bool = 1;
    unsigned int utf16_char = get_be_utf16(input_fp);

    if (utf16_char != 0xfeff) {
        
        fseek(input_fp, 0, SEEK_SET);
        utf16_char = get_le_utf16(input_fp);
        big_endian_bool = 0;
        
        if (utf16_char != 0xfeff) {
            fseek(input_fp, 0, SEEK_SET);
            fprintf(stderr, ERR_MISSING_BOM " %x %x", getc(input_fp), getc(input_fp));
            return -1;
        }
    }
    
    write_bom(big_endian_bool, output_fp);
    
    if (big_endian_bool == 1) {
        utf16_char = get_be_utf16(input_fp);
    } else {
        utf16_char = get_le_utf16(input_fp);
    }
    
    while (utf16_char != EOF) {
        long int start_byte_offset = ftell(input_fp);
        
        //trail surrogate without head
        if ((utf16_char > 0xDC00) && (utf16_char < 0xDFFF)) {
            fprintf(stderr, ERR_UNPAIRED_SURROGATE " %ld", start_byte_offset);
            return -1;
        }

        if ((utf16_char < 0xD800) || (utf16_char > 0xDFFF)) {
            unsigned int utf32_char = utf16_char;
            write_utf32_char(big_endian_bool, utf32_char, output_fp);
        } else {
            unsigned int trail_surr = 0;
            if (peek_next(input_fp) == EOF) {
                fprintf(stderr, ERR_UNPAIRED_SURROGATE "%ld", start_byte_offset);
                return -1;
            }
            if (big_endian_bool) {
                trail_surr = get_be_utf16(input_fp);
            } else {
                trail_surr = get_le_utf16(input_fp);
            }
            
            if (trail_surr == -1) {
                return -1;
            }
            
            unsigned int utf32_char = utf16_to_32(utf16_char, trail_surr, input_fp, start_byte_offset);
            
            if (utf32_char == -1) {
                return -1;
            }
            
            if (check_errors(utf32_char, start_byte_offset) == -1) {
                return -1;
            }
            
            write_utf32_char(big_endian_bool, utf32_char, output_fp);
        }

        if (peek_next(input_fp) == EOF) {
            utf16_char = EOF;
        
        } else {
        
            if (big_endian_bool) {
                utf16_char = get_be_utf16(input_fp);
            } else {
                utf16_char = get_le_utf16(input_fp);
            }
            
            if (utf16_char == -1) {
                return -1;
            }
        }
    }
    fclose(input_fp);
    fclose(output_fp);
    return 0;
}
