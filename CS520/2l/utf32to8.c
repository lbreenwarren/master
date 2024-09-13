#include <stdio.h>

unsigned int get_be_utf32(FILE *fp) {
    unsigned int ret = 0;
    for (int i = 0; i < 4; ++i) {
        int byte = getc(fp);
        if (byte == EOF) {
            return -1;
        }
        ret |= (byte << (8 * (3 - i)));
    }
    return ret;
}

unsigned int get_le_utf32(FILE *fp) {
    unsigned int ret = 0;
    for (int i = 0; i < 4; ++i) {
        ret |= (getc(fp) << (8 *  i));
    }
    return ret;
}

void utf32_to_8(unsigned int utf32_char, FILE *output_fp) {
    unsigned int mask_bit = 1 << 31;
    int num_bits = 32;
    printf("%d", utf32_char);
    
    while ((utf32_char & mask_bit) < 1) {
        mask_bit = mask_bit >> 1;
        --num_bits;
    }
  
    int num_trail_bytes = num_bits / 6; 
    unsigned int head_byte = 0;
   
    for (int i  = 0; i < num_trail_bytes + 1; ++i) {
        head_byte |= (1 << (7 - i));
    }
  
    int num_data_bits_in_head = num_bits % 6;  
    int head_data_mask = 1 << (num_data_bits_in_head - 1);
   
    for (int i = 0; i < num_data_bits_in_head; ++i) {
        
        if ((utf32_char & mask_bit) > 0) {
            head_byte |= head_data_mask;
        }
        head_data_mask >>= 1;
        mask_bit >>= 1;
    }
    
    printf("\n0x%x ", head_byte);
    putc(head_byte, output_fp);

    
   
    for(int i = 0; i < num_trail_bytes; ++i) {
        unsigned int trail_byte = 0x80;
        trail_byte |= (utf32_char & (0x3f << (6 * (num_trail_bytes - (i + 1)))));
        putc(trail_byte, output_fp);
        printf("0x%x ", trail_byte);
    }
    //printf("]");

}

int main(int argc, char* argv[]) {
    
    if (argc < 2) {
        printf("Not Enough Arguments");
        return -1;
    }

    FILE *input_fp = fopen(argv[1], "rb");

    if (input_fp == NULL) {
        printf("Error: Could not open file\n");
        return 1;
    }
    
    FILE *output_fp = fopen(argv[2], "wb");

    if (output_fp == NULL) {
        printf("Error: Could not open file\n");
        return 1;
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
        printf("Error: No BOM present");
        return -1;
    }

    // prime the loop by getting first utf32 character
    if (big_endian_bool == 1) {
        utf32_char = get_be_utf32(input_fp);
    } else {
        utf32_char = get_le_utf32(input_fp);
    }
    int count = 1;
    while (utf32_char != EOF) {
        
        printf("%x\n", utf32_char);
        //printf("[0x%08x", utf32_char);
        //convert to utf8
        //if utf32 character is less than 0x80, just chop of leading 0's
        if (utf32_char < 0x80) {
            unsigned int utf8_char = utf32_char & 0x7f;
            putc(utf8_char, output_fp);
        } else {
            //printf("->0x");
            utf32_to_8(utf32_char, output_fp);
        }
        
        if (count % 16 == 0) {
            //printf("\n");
        }
        ++count;
        //get next utf32 character
        if (big_endian_bool == 1) {
            utf32_char = get_be_utf32(input_fp);
        } else {
            utf32_char = get_le_utf32(input_fp);
        }

        
       
    }
    printf("\n");
    fclose(input_fp);

    // check Bom and get utf32 char
    
    //if less than 0x80 just write

    // else num bits // 6 
}