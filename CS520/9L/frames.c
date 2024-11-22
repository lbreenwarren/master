#include <stdio.h>

long getFP();
void testFunc();

void printFramePointers() {
    printf("Frame Pointers & Contents:\n");
    long rbp = getFP();
    long *rbp_addr;
    rbp_addr = (long*)rbp;
    rbp = *rbp_addr;
    printf("%016lx %016lx\n", (unsigned long)rbp_addr, rbp);
    while (*rbp_addr > 0xFF) {
        rbp_addr = (long*)rbp;
        rbp = *rbp_addr;
        printf("%016lx %016lx\n", (unsigned long)rbp_addr, rbp);
    }
}
