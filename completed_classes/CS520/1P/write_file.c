#include <stdio.h>

int main() {
    FILE *fp = fopen("err_file.txt", "wb");
    putc(0x00, fp);
    putc(0x00, fp);
    putc(0xfe, fp);
    putc(0xff, fp);
    putc(0x00, fp);
    putc(0x01, fp);
    putc(0x23, fp);
    putc(0x45, fp);
   
}