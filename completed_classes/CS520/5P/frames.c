#include <stdio.h>
#include <stdlib.h>

long getFP();
long getSP();

typedef struct frame_ptr frame_ptr;

struct frame_ptr {
    frame_ptr *prev;
    long *rbp;
    long rbp_val;
    frame_ptr *next;
};

frame_ptr *head;
frame_ptr *tail;

static frame_ptr * create_frame_ptr(long *rbp, long rbp_val) {
    frame_ptr *new = malloc(sizeof(frame_ptr));
    new->prev = NULL;
    new->rbp = rbp;
    new->rbp_val = rbp_val;
    new->next = NULL;
    return new; 
}   

void printStackFrames() {

    head = create_frame_ptr(NULL, -1);
    tail = create_frame_ptr(NULL, -1);
    long rbp = getFP();
    long *rbp_addr;
    rbp_addr = (long*)rbp;
    rbp = *rbp_addr;
    
    printf("Current %%rbp: %016lx\n", (unsigned long)rbp_addr);
    printf("List of Frames:\n");

    frame_ptr* prev = create_frame_ptr(rbp_addr, rbp);
    head->next = prev;
    prev->prev = head;
    
    while (*rbp_addr > 0xFF) {
        rbp_addr = (long*)rbp;
        rbp = *rbp_addr;
        frame_ptr* next =  create_frame_ptr(rbp_addr, rbp);
        prev->next = next;
        next->prev = prev;
        prev = next;
    }

    prev->next = tail;
    tail->prev = prev;
    
    while (prev->prev != NULL) {
        printf("[%016lx] %016lx\n", (unsigned long)prev->rbp, prev->rbp_val);
        prev = prev->prev;
    }

    long val = getSP();
    long *rsp = &val;
    long *ptr = tail->prev->rbp;
    frame_ptr *curr_frame = tail->prev;
    printf("Current %%rsp: %016lx\n", (unsigned long)rsp);
    printf("Stack Dump:\n");
    while((ptr + 1) != rsp) {
        if (ptr == rsp) {
            printf("\t  %%rsp -->  ");
        }
        else if (ptr == curr_frame->rbp) {
            if (curr_frame != head->next) {
                printf("       %%old rbp --> ");
            } else {
                printf("\t   %%rbp --> ");
            }
        } else {
            printf("\t\t    ");
        }
        printf("[%016lx] %016lx", (unsigned long)ptr, *ptr);
        if ((curr_frame->prev != head) && (ptr - 1) == curr_frame->prev->rbp) {
            printf(" --> %%saved rip\n");
            curr_frame = curr_frame->prev;
        }
        printf("\n");
        --ptr;
    }
}
