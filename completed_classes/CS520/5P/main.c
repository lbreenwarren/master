#include <stdio.h>

long getFP();
void printFramePointers();
void testFunction(int recurses);
void printStackFrames();

#define CALLS 4
#define TEST 0

void main() {
    
    if (TEST) {
        printf("\n==============TEST 1==============\n");
        long rbp = getFP();
        long *main_fp = (long *)rbp;
        printf("Frame of main function: \n\tPointer: %016lx\n\tContent: %016lx\n", (unsigned long)main_fp, *main_fp);
        printf("\nCalling printFramePointers, expecting first frame to have content equal to frame pointer of main and second frame to be main\n\n");
        //the Calling function to getFP will be printFramePointers and the caller of that is main
        //so the frist print should have rbp content with mains frame pointer and the next one should have a frame pointer equal to mains
        //printFramePointers();

        // for each recursion of this test function, the number of frames on the stack should increment
        // this will indicate whether the correct number of frames are being displayed independent of how many frames exist when main is called
        printf("\n==============TEST 2==============\n");
        printf("Making %d recursive calls to test function, expecting incrementing number of frames with each call\n", CALLS); 
        printf("\n========INITIAL STACK========\n");
        //printFramePointers();
        testFunction(CALLS - 1);
    } else {
        printStackFrames();
    }
}

void testFunction(int recurses){
    printf("\nPrinting Frame Pointers, expecting %d more frame(s) than initial stack\n", CALLS - recurses);
    //printFramePointers();
    if (recurses > 0) {
        testFunction(--recurses);
    }
    return;
}