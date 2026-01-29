#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <inttypes.h>
#include <stdarg.h>

struct regs {
    unsigned long rdi;
    unsigned long rsi;
    unsigned long rbx;
};

#define FREE_MASK 0x40000000
#define MARKED_MASK 0x80000000
#define LEN_MASK 0x3fffffff

extern struct regs reg_ptrs(void);
extern void global_ptrs(uint64_t **rdi, uint64_t **rsi);
extern void stack_ptrs(uint64_t **rdi, uint64_t **rsi);

static uint64_t * check_word(uint64_t word);

static uint64_t mem_size;
static uint64_t *mem;

static void clear_marks() {
    uint64_t offset = 0;
    while(offset <= mem_size) {
        mem[offset] &= (~MARKED_MASK);
        offset += (mem[offset] & LEN_MASK);
    }
}

static uint64_t * get_free(unsigned long size) {
    uint64_t offset = 0;
    uint64_t header = mem[offset];

    while(((header & FREE_MASK) == 0 || (header & LEN_MASK) < size) && (offset < mem_size)) {
        //printf("offset: %lx\n", offset);
        offset += (header & LEN_MASK);
        header = mem[offset];
    } 
    
    if (offset >= mem_size) {
        return NULL;
    }
    
    return mem + offset;
}

int memInitialize(unsigned long size) {
    
    mem_size = size;
    
    mem = malloc(sizeof(uint64_t) * size);
    memset(mem, 0, size);
    if (mem == NULL) {
        return 0;
    }
    
    mem[0] = 1 << 30;

    if (size > LEN_MASK) {
        //Mem size would be too big to represent in header
        return 0;
    }
    mem[0] |= (size & LEN_MASK);
    mem[0] |= FREE_MASK;
    return 1;
}

static void collector_helper(uint64_t *start, uint64_t len) {
    uint64_t word = *start;
    uint64_t offset = 0;
    while(offset < len) {
        uint64_t *addr = check_word(word);
        if (addr != NULL && ((*addr & MARKED_MASK) == 0)) {
            *addr |= MARKED_MASK;
            collector_helper(addr + 2, (*addr & LEN_MASK));
        }
        ++offset;
        word = start[offset];
        
    }

}


static uint64_t * check_word(uint64_t word) {
    uint64_t *mem_ptr = mem;
    uint64_t offset = 0;
    if (word >= (uint64_t)mem_ptr && word < (uint64_t)mem_ptr + mem_size){
        while(offset < mem_size) {
            if (word >= (((uint64_t)mem_ptr) - 1) && (word <= (((uint64_t)mem_ptr + (*mem_ptr & LEN_MASK)) + 1))) {
                return mem_ptr;
                
            }
            offset += ((*mem_ptr) & LEN_MASK);
            mem_ptr = mem_ptr + offset;
    }   
    }
    return NULL;
} 

static void collector(void) {
    clear_marks();
    uint64_t* globl_start = NULL;
    uint64_t* globl_end = NULL;
    uint64_t* rsp = NULL;
    uint64_t* rbp = NULL;
    
    global_ptrs(&globl_start, &globl_end);
    uint64_t globl_len = (uint64_t)globl_end - (uint64_t)globl_start;
    uint64_t *globl_ptr = malloc(sizeof(uint64_t) * globl_len);
    globl_ptr = globl_start;
    
    collector_helper(globl_ptr, globl_len);
    stack_ptrs(&rsp, &rbp);
    uint64_t *stack_start = rsp;
    uint64_t *fp = rbp;

    while(*fp > (uint64_t)fp) {
        fp = (uint64_t *)*fp;
    }

    uint64_t* stack_end = rbp;
    long int stack_len = (uint64_t)stack_end - (uint64_t)stack_start;
    uint64_t *stack_ptr = malloc(sizeof(uint64_t) * stack_len);
    memcpy(stack_ptr, stack_start, stack_len * (sizeof(uint64_t)));
    
    collector_helper(stack_ptr, stack_len);
    
    uint64_t offset = 0;
    while(offset < mem_size) {

        if((mem[offset] & MARKED_MASK) == 0 && ((mem[offset] & FREE_MASK) == 0)) {
            mem[offset] &= (~MARKED_MASK);
            mem[offset] |= FREE_MASK;
            if (mem[offset + 1] != 0) {
                void (*finalize)(void) = (void (*)(void))mem[offset + 1];
                finalize();
            }

            uint64_t *next_block = mem + offset + (mem[offset] & LEN_MASK);
        
            if((mem[offset + (mem[offset] & LEN_MASK)] & FREE_MASK) > 0) {
                uint64_t new_len = (mem[offset] & LEN_MASK) + (*next_block & LEN_MASK);
                mem[offset] = new_len;
            }
        
          
        }
        offset += (mem[offset] & LEN_MASK);
    }
}

void *memAllocate(unsigned long size, void (*finalize)(void *)) {
    
    if (size > mem_size || size > LEN_MASK) {
        return 0;
    }
    
    uint64_t *addr = get_free(size + 2);
    
    if (addr == NULL) {
        collector();
    }

    addr = get_free(size + 2);
    
    if (addr != NULL){
        
        uint64_t len_free = (*addr) & LEN_MASK; 
        *addr = ((size + 2) & LEN_MASK);
        *addr &= ~FREE_MASK;
        *addr &= 0x7FFFFFFF; //Marked bit
        if (finalize != NULL) {
            addr[1] = (uint64_t)finalize;
        } else {
            addr[1] = 0;
        }
        uint64_t *next = addr + size + 2;
        uint64_t new_len = len_free - (size + 2);

        if ((uint64_t)next < (uint64_t)mem + (mem_size * 8)) {
            next[0] |= new_len;
            next[0] |= FREE_MASK;
        }
    }
    
    return addr;
}
    


void memDump(void) {
    uint64_t *globl_start = NULL;
    uint64_t *globl_end = NULL;
    uint64_t *rsp = NULL;
    uint64_t *rbp = NULL;
    global_ptrs(&globl_start, &globl_end);
    stack_ptrs(&rsp, &rbp);

    uint64_t globl_len = globl_end - globl_start;
    uint64_t *globl_ptr = malloc(sizeof(uint64_t) * globl_len);
    memcpy(globl_ptr, globl_start, globl_len * (sizeof(uint64_t)));

    printf("Global Memory: start = %016lx  end=%016lx length=%ld words\n\n", (uint64_t)globl_start, (uint64_t)globl_end, globl_len);

    for (int i = 0; i < globl_len; ++i) {
        printf("%016lx %016lx", ((uint64_t)globl_start) + (i * 8), *(globl_ptr + i));
        uint64_t *addr = check_word(*(globl_ptr + i));
        if (addr != NULL) {
            printf(" *");
        }
        printf("\n");
    }
    
    uint64_t *stack_start = rsp;
    uint64_t *fp = rbp;
    while(*fp > (uint64_t)fp) {
        fp = (uint64_t *)*fp;
    }

    uint64_t* stack_end = rbp;
    long int stack_len = (uint64_t)stack_end - (uint64_t)stack_start;
    uint64_t *stack_ptr = malloc(sizeof(uint64_t) * stack_len);
    memcpy(stack_ptr, stack_start, stack_len * (sizeof(uint64_t)));


    printf("\nStack Memory: start = %016lx  end=%016lx length=%ld words\n\n", (uint64_t)stack_start, (uint64_t)stack_end, stack_len);
    
    for(int i = 0; i < stack_len; ++i) {
        printf("%016lx %016lx", ((uint64_t)stack_start) + (i * 8), *(stack_ptr + i));
        uint64_t *addr = check_word(*(stack_ptr + i));
        if (addr != NULL) {
            printf(" *");
        }
        printf("\n");
    }

    struct regs registers = reg_ptrs();
    printf("\nRegisters:\n\n");
    printf("rbx: %016lx\n", registers.rbx);
    printf("rsi: %016lx\n", registers.rsi);
    printf("rdi: %016lx\n", registers.rdi);

    printf("\nHeap:\n");
    uint64_t *mem_ptr = mem;
    uint64_t offset = 0;
    uint64_t block_len = *mem_ptr & LEN_MASK;
    while(offset < mem_size && block_len != 0) {
        block_len = *mem_ptr & LEN_MASK;
        printf("\n(2 word header [%lx])\n", mem_ptr[0]);
        printf("Block %ld ", block_len);

        if (*mem_ptr & MARKED_MASK) {
            printf("Marked ");
        } else {
            printf("Unmarked ");
        }

        if (*mem_ptr & FREE_MASK) {
            printf("Free ");
        } else {
            printf("Allocated ");
        }
        for(int k = 2; k < block_len; ++k) {
            if ((k - 2) % 7 == 0) {
                printf("\n %016lx : ", (uint64_t)mem_ptr + (k * 8));
            }
            printf("%016lx ", mem_ptr[k]);
        }
        printf("\n");
        offset += block_len;
        mem_ptr = mem + offset;
    }
    printf("\n");
    free(globl_ptr);
    free(stack_ptr);
}

