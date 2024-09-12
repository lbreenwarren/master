#include <stdio.h>

#define NUM_RESERVED 3

int check_reserved(int byte) {

    int RESERVED[NUM_RESERVED] = {0xbb, 0xbf, 0xef};

    for (int i = 0; i < NUM_RESERVED; ++i) {
            if (byte == RESERVED[i]) {
                return 1;
            }
    }
    
    return 0;
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
 
unsigned int utf8_to_utf32(unsigned int byte, FILE *fp) {
    unsigned int ret = 0;
    unsigned int bit_mask = 0x80;
    while (check_reserved(byte)) {
        byte = getc(fp);
    }
    if (((byte & 0x80) == 0)) {

        return ret | byte;
        
    } else {
        
        int mask = 0x80;
        int num_bytes = 0;
       
        while ((mask & byte)) {
            ++num_bytes;
            mask = mask >> 1;
        }
        
        int byte_1_mask = 0xff >> (7  - num_bytes);
        int byte_n_mask = 0x3f;
        
        ret |= (byte & byte_1_mask);
       
        for (int i = 1; i < num_bytes; ++i) {
            unsigned int byte_n = getc(fp);
            ret = ret << 6;
            ret |= (byte_n & byte_n_mask);
        }
        return ret;
    } 
}

int main(int argc, char *argv[]) {
    
  
    if (argc > 3) {
        printf("Too Many Arguements!");
        return 1;
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

    //write bom
    write_utf32_char(0xfeff, output_fp);

    unsigned int byte = getc(input_fp);
    int reserved_val = 0;
    int num_bytes = 0;
    while (byte != EOF) {

        write_utf32_char(utf8_to_utf32(byte, input_fp), output_fp);

        ++num_bytes;
        byte = getc(input_fp);
    }

    return 0;



}