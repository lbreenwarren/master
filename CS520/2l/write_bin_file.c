#include <stdio.h>
int main() {


    FILE *fp = fopen("utf32_val_a000-le", "wb");
    putc(0xff, fp);
    putc(0xfe, fp);
    putc(0x00, fp);
    putc(0x00, fp);
    putc(0x99, fp);
    putc(0xa9, fp);
    putc(0x00, fp);
    putc(0x00, fp);
    


}