#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <pthread.h>
#include "vmx20.h"


//Stack/Main Memory size in 4 byte words, total of 50MB
#define STACKSIZE 12500
#define MM_SIZE 50000
#define PC 15
#define SP 14
#define FP 13

//VMX20 instructions
#define OP_HALT     0x00
#define OP_LOAD     0x01
#define OP_STORE    0x02
#define OP_LDIMM    0x03
#define OP_LDADDR   0x04
#define OP_LDIND    0x05
#define OP_STIND    0x06
#define OP_ADDF     0x07
#define OP_SUBF     0x08
#define OP_DIVF     0x09
#define OP_MULF     0x0A
#define OP_ADDI     0x0B
#define OP_SUBI     0x0C
#define OP_DIVI     0x0D
#define OP_MULI     0x0E
#define OP_CALL     0x0F
#define OP_RET      0x10
#define OP_BLT      0x11
#define OP_BGT      0x12
#define OP_BEQ      0x13
#define OP_JMP      0x14
#define OP_CMPXCHG  0x15
#define OP_GETPID   0x16
#define OP_GETPN    0x17
#define OP_PUSH     0x18
#define OP_POP      0x19

//VM handle containing references to threads and all allocated memory
struct vm_h {
    struct thread_h *threads;
    struct in_sym *symtab;
    uint32_t *mem;
};

struct in_sym {
    char *name;
    uint32_t addr;
};

struct thread_h {
    uint32_t *reg;
};

//number of instructions to be executed
static int num_instr;
//number of symbols in table
static int num_syms;
//address of main entry point
static uint32_t entry_point = 0xffffffff;
//number of processors
static int num_threads;
//Mutex for locking the memory bus during CMPXCHNG
static pthread_mutex_t mem_lock;
//Mutex for trace printing
static pthread_mutex_t print_lock;

//helper function to malloc size bytes to ptr
//clear parmeter will init size bytes of ptr to 0 if set to 1
// returns 0 on failure
static void * checked_malloc(size_t size, int clear) {
    void *ptr = malloc(size);
    if (ptr == NULL) {
        return NULL;
    }
    if (clear) {
        memset(ptr, 0, size);
    }
    return ptr;
}  

static const char* opcode_to_string(int opcode) {
    switch (opcode) {
        case 0x00: return "   HALT";
        case 0x01: return "   LOAD";
        case 0x02: return "  STORE";
        case 0x03: return "  LDIMM";
        case 0x04: return " LDADDR";
        case 0x05: return "  LDIND";
        case 0x06: return "  STIND";
        case 0x07: return "   ADDF";
        case 0x08: return "   SUBF";
        case 0x09: return "   DIVF";
        case 0x0A: return "   MULF";
        case 0x0B: return "   ADDI";
        case 0x0C: return "   SUBI";
        case 0x0D: return "   DIVI";
        case 0x0E: return "   MULI";
        case 0x0F: return "   CALL";
        case 0x10: return "    RET";
        case 0x11: return "    BLT";
        case 0x12: return "    BGT";
        case 0x13: return "    BEQ";
        case 0x14: return "    JMP";
        case 0x15: return "CMPXCHG";
        case 0x16: return " GETPID";
        case 0x17: return "  GETPN";
        case 0x18: return "   PUSH";
        case 0x19: return "    POP";
        default:   return " UNKNOWN";
    }
}

static int32_t sign_extend_20_bit(uint32_t val) {
    if ((val & (1 << 19)) > 0) {
        val =  val | 0xfff00000;
    }
    return val;
}
static int32_t sign_extend_16_bit(uint32_t val) {
    if ((val & (1 << 15)) > 0) {
        val =  val | 0xffff0000;
    }
    return val;
}

static void disassemble_instr(uint32_t instr) {
    int opcode = instr & 0xff;
    printf("%s", opcode_to_string(opcode));

    switch (opcode) {
        // Format 1: op
        case 0x00: // halt
        case 0x10: // ret
            printf("                 ");
            break;

        // Format 2: op addr
        case 0x0F: // call
        case 0x14: // jmp
            printf(" %05x           ", sign_extend_20_bit((instr & 0xfffff000) >> 12));
            break;

        // Format 3: op reg
        case 0x16: // getpid
        case 0x17: // getpn
        case 0x18: // push
        case 0x19: // pop
            printf(" r%-2d             ", (instr & 0x00000f00) >> 8);
            break;

        // Format 4: op reg,const
        case 0x03: // ldimm
            printf(" r%-2d, ", (instr & 0x00000f00) >> 8);
            printf("%05x      ", (sign_extend_20_bit((instr & 0xfffff000) >> 12)));
            break;

        // Format 5: op reg,addr
        case 0x01: // load
        case 0x02: // store
        case 0x04: // ldaddr
            printf(" r%-2d, ", (instr & 0x00000f00) >> 8);
            printf("%05x      ", sign_extend_20_bit((instr & 0xfffff000) >> 12));
            break;

        // Format 6: op reg1,reg2
        case 0x07: // addf
        case 0x08: // subf
        case 0x09: // divf
        case 0x0A: // mulf
        case 0x0B: // addi
        case 0x0C: // subi
        case 0x0D: // divi
        case 0x0E: // muli
            printf(" r%-2d, ", (instr & 0x00000f00) >> 8);
            printf("r%-2d        ", (instr & 0x0000f000) >> 12);
            break;

        // Format 7: op reg1,reg2, offset
        case 0x05: // ldind
        case 0x06: // stind
            printf(" r%-2d, ", (instr & 0x00000f00) >> 8);
            printf("r%-2d, ", (instr & 0x0000f000) >> 12);
            printf("%04x  ", (sign_extend_16_bit((instr & 0xffff0000) >> 16)));
            break;

        // Format 8: op reg1,reg2,addr
        case 0x11: // blt
        case 0x12: // bgt
        case 0x13: // beq
        case 0x15: // cmpxchg
            printf(" r%-2d, ", (instr & 0x00000f) >> 8);
            printf("%04x ", sign_extend_16_bit((instr & 0xffff0000) >> 16));
            printf("(r%-2d)", (instr & 0x0000f) >> 8);
            break;
        default:
            break;
    }
}

static void print_trace(struct thread_h *th, uint32_t instr, int thread_num) {
    printf("=================================================================================================\n");
    printf("|             THREAD %d EXECUTING INSTRUCTION", thread_num);
    printf(" (0x%08x):", instr);
    disassemble_instr(instr);
    
    printf("              |");
    for(int i = 0; i < 13; ++ i) {
        if (i % 6 == 0) {
            printf("\n|");
        }
        printf(" r%-2d: %-8x |", i, th->reg[i]);
    }
    printf(" fp : %-8x | sp : %-8x | pc : %-8x |               |               |\n", th->reg[FP], th->reg[SP], th->reg[PC]);
    printf("=================================================================================================\n\n");
}

static void print_symtab(struct vm_h *vm) {
    printf("|=========================|\n");
    printf("|       SYMBOL TABLE      |\n");
    printf("|=========================|\n");
    printf("|    LABEL   |  ADDRESS   |\n");
    printf("|+++++++++++++++++++++++++|\n");
    
    for (int i = 0; i < num_syms; ++i) {
        printf("|%-12s|", vm->symtab[i].name);
        printf("0x%-10x|\n",vm->symtab[i].addr); 
        printf("|____________|____________|\n");
    }
}

static int check_addr(uint32_t addr) {
    if (addr < 0 || addr > MM_SIZE) {
        return 0;
    }
    return 1;
}

static int check_stack_addr(uint32_t addr, uint32_t frame_p) {
    if (addr <= MM_SIZE || addr >= frame_p) {
        return 0;
    }
    return 1;
}
//=======================================================================================================
//VMX20 Instruction specific execution functions
//=======================================================================================================

//========================================== 
// CONTROL FLOW INSTRUCTIONS 
//==========================================

static int jmp(struct thread_h *th, int32_t addr) {

    if (!check_addr(th->reg[PC] + addr)) {
        return VMX20_ADDRESS_OUT_OF_RANGE;
    }

    th->reg[PC] = th->reg[PC] + addr;
    return 0;
}

static int cond_jmp(struct thread_h *th, uint8_t reg1, uint8_t reg2, uint32_t addr, int bool) {
    if (bool) {
        return jmp(th, addr);
    } 
    return 0;
}

static int call(struct thread_h *th, struct vm_h *vm, uint32_t addr) { 
    if (!check_addr(th->reg[PC] + addr)) {
        return VMX20_ADDRESS_OUT_OF_RANGE;
    }

    //assuming here that initial stack pointers provided to execute were valid 
    //push pc onto stack
    th->reg[SP] -= 1;
    printf("SP:%d\n", th->reg[SP]);
    printf("MEM at SP:%d\n", vm->mem[th->reg[SP]]);
    vm->mem[th->reg[SP]] = th->reg[PC];
    printf("RET ADDR: %d", vm->mem[th->reg[SP]]);
    //set pc to addr of called func
    th->reg[PC] += addr;
    
    //push fp onto stack
    th->reg[SP] -= 1;
    putWord(vm, th->reg[SP], th->reg[FP]);
    th->reg[FP] = th->reg[SP];
    
    //push 0 onto stack
    th->reg[SP] -= 1;
    putWord(vm, th->reg[SP], 0);
    return 0;
}

static int ret(struct thread_h *th, struct vm_h *vm) { 
    if (!check_stack_addr(th->reg[SP] + 1, th->reg[FP])) {
        return VMX20_ADDRESS_OUT_OF_RANGE;
    }
    //pop return value
    uint32_t return_val = vm->mem[th->reg[SP]];
    th->reg[SP] += 1;
    //pop old fp
    uint32_t old_fp = vm->mem[th->reg[SP]];
    th->reg[SP] += 1;
    //pop return addr
    uint32_t ret_addr = vm->mem[th->reg[SP]];
    th->reg[SP] += 1;
    //set fp and pc to previous values
    th->reg[FP] = old_fp;
    th->reg[PC] = ret_addr;
    //push return value onto calling functions stack
    vm->mem[th->reg[FP] - 1] = return_val;
    return 0;
}

//========================================== 
// LOAD INSTRUCTIONS 
//==========================================

int load(struct vm_h *vm, struct thread_h *th, uint8_t reg, int32_t addr) {
    if (th->reg[PC] + addr < 0 || th->reg[PC] + addr > MM_SIZE) {
        return VMX20_ADDRESS_OUT_OF_RANGE;
    }
    th->reg[reg] = vm->mem[th->reg[PC] + addr];
    return 0;
}

static int ldimm(struct thread_h *th, uint8_t reg, int32_t constant) {
    th->reg[reg] = constant;
    return 0;
}

static int ldaddr(struct thread_h *th, uint8_t reg, int32_t addr) {
    if (th->reg[PC] + addr < 0 || th->reg[PC] + addr > MM_SIZE) {
        return VMX20_ADDRESS_OUT_OF_RANGE;
    }
    th->reg[reg] = th->reg[PC] + addr;
    return 0;
}

static int ldind(struct vm_h *vm, struct thread_h *th, uint8_t reg1, uint8_t reg2, int32_t offset) {
    if (th->reg[reg2] + offset < 0 || th->reg[reg2] + offset > MM_SIZE) {
        return VMX20_ADDRESS_OUT_OF_RANGE;
    }
    th->reg[reg1] = vm->mem[th->reg[reg2] + offset];
    return 0;
}

//==========================================  
// STORE INSTRUCTIONS 
//========================================== 

static int store(struct vm_h *vm, struct thread_h *th, uint8_t reg, int32_t addr) {
    pthread_mutex_lock(&mem_lock);
    if (!putWord(vm, th->reg[PC] + addr, th->reg[reg])) {
        pthread_mutex_unlock(&mem_lock);
        return VMX20_ADDRESS_OUT_OF_RANGE;
    }
    pthread_mutex_unlock(&mem_lock);
    return 0;
}

static int stind(struct vm_h *vm, struct thread_h *th, int32_t addr, uint8_t reg1, uint8_t reg2) {
    pthread_mutex_lock(&mem_lock);
    if (!putWord(vm, th->reg[reg2] + addr, th->reg[reg1])) {
        return VMX20_ADDRESS_OUT_OF_RANGE;
        pthread_mutex_unlock(&mem_lock);
    }
    pthread_mutex_unlock(&mem_lock);
    return 0;
}

//========================================== 
//ARITHMATIC INSTRUCTIONS 
//========================================== 

static int addi(struct thread_h *th, uint8_t reg1, uint8_t reg2) {
    th->reg[reg1] += th->reg[reg2];
    return 0;
}

static int addf(struct thread_h *th, uint8_t reg1, uint8_t reg2) {
    float sum = (float)th->reg[reg1] + (float)th->reg[reg2];
    th->reg[reg1] = sum;
    return 0;
}

static int subi(struct thread_h *th, uint8_t reg1, uint8_t reg2) {
    th->reg[reg1] -= th->reg[reg2];
    return 0;
}

static int subf(struct thread_h *th, uint8_t reg1, uint8_t reg2) {
    float diff = (float)th->reg[reg1] - (float)th->reg[reg2];
    th->reg[reg1] = diff;
    return 0;
}

static int muli(struct thread_h *th, uint8_t reg1, uint8_t reg2) {
    th->reg[reg1] *= th->reg[reg2];
    return 0;
}

static int mulf(struct thread_h *th, uint8_t reg1, uint8_t reg2) {
    float prod = (float)th->reg[reg1] * (float)th->reg[reg2];
    th->reg[reg1] = prod;
    return 0;
}

static int divi(struct thread_h *th, uint8_t reg1, uint8_t reg2) { 
    th->reg[reg1] /= th->reg[reg2];
    return 0;
}

static int divf(struct thread_h *th, uint8_t reg1, uint8_t reg2) { 
    float div = (float)th->reg[reg1] / (float)th->reg[reg2];
    th->reg[reg1] = div;
    return 0;
}

//========================================== 
// STACK INSTRUCTIONS 
//========================================== 

static int push(struct thread_h *th, struct vm_h *vm, uint8_t reg1) {
    if (check_stack_addr(th->reg[SP] - 1, th->reg[FP])) {
        return VMX20_ADDRESS_OUT_OF_RANGE;
    }
    th->reg[SP] -= 1;
    putWord(vm, th->reg[SP], th->reg[reg1]);
    return 0;
}
static int pop(struct thread_h *th, struct vm_h *vm, uint8_t reg1) {
    if (check_stack_addr(th->reg[SP] + 1, th->reg[FP])) {
        return VMX20_ADDRESS_OUT_OF_RANGE;
    }
    th->reg[reg1] = th->reg[SP];
    th->reg[SP] += 1;
    return 0;
}

//==========================================
// THREAD INSTRUCTIONS
//========================================== 

static int cmpxchg(struct thread_h *th, struct vm_h *vm, uint8_t reg1, uint8_t reg2, uint32_t addr) { 
    pthread_mutex_lock(&mem_lock);
    if (!check_addr(th->reg[PC] + addr)) {
        pthread_mutex_unlock(&mem_lock);
        return VMX20_ADDRESS_OUT_OF_RANGE;
    }
    if (th->reg[reg1] == vm->mem[th->reg[PC] + addr]) {
        putWord(vm, th->reg[PC] + addr, th->reg[reg2]);
    } else {
        th->reg[reg1] = vm->mem[th->reg[PC] + addr];
    }
    pthread_mutex_unlock(&mem_lock);
    return 0;
}


//stuct to store arguments for worker funcion
struct worker_args {
    struct vm_h *vm;
    struct thread_h *th;
    int *terminationStatus;
    int32_t trace;
    int thread_num;
};

//worker function for multi threaded instruction execution
void* execute_worker(void *arg) {
    //store args from stuct in local vars
    struct worker_args *args = (struct worker_args*)arg;
    struct vm_h *vm = args->vm;
    struct thread_h *th = args->th;
    int *terminationStatus = args->terminationStatus;
    int thread_num = args->thread_num;
    int32_t trace = args->trace;

    //get first instruction and init program counter
    uint32_t instr = vm->mem[entry_point];
    th->reg[PC] = entry_point;

    if (trace) {
        pthread_mutex_lock(&print_lock);
        printf("ENTRY POINT ADDR: %d\n", entry_point);
        print_trace(th, instr, thread_num);
        pthread_mutex_unlock(&print_lock);
    }
    
    //exectue instructions until HALT or RET
    uint8_t opcode = instr & 0xff;
    
    while (opcode != OP_HALT && *terminationStatus == 0) {
        ++th->reg[PC];
        //I dont want to write out the bit shifts anymore so im gonna store them in variables whether needed or not
        uint8_t r1 = (instr & 0xf00) >> 8;
        uint8_t r2 = (instr & 0xf000) >> 12;
        uint32_t addr_20 = sign_extend_20_bit((instr & 0xfffff000) >> 12);
        uint32_t addr_16 = sign_extend_16_bit((instr & 0xffff0000) >> 16);

        switch(opcode) {
            case OP_JMP:
                *terminationStatus = jmp(th, addr_20);
                break;
            case OP_BEQ:
                *terminationStatus = cond_jmp(th, r1, r2, addr_16, r1 == r2);
            case OP_BGT:
                *terminationStatus = cond_jmp(th, r1, r2, addr_16, r1 > r2);
            case OP_BLT:
                *terminationStatus = cond_jmp(th, r1, r2, addr_16, r1 < r2);
            case OP_LOAD:
                *terminationStatus = load(vm, th, r1, addr_20);
                break;
            case OP_CALL:
                *terminationStatus = call(th, vm, addr_20);
            case OP_RET:
                *terminationStatus = ret(th, vm);
            case OP_LDIMM: 
                *terminationStatus = ldimm(th, r1, (instr & 0xfffff000) >> 12);
                break;
            case OP_LDADDR:
                *terminationStatus = ldaddr(th, r1, addr_20);
                break;
            case OP_LDIND:
                *terminationStatus = ldind(vm, th, r1, r2, addr_16);
                break;
            case OP_STORE:
                *terminationStatus = store(vm, th, r1, addr_20);
                break;
            case OP_STIND:
                *terminationStatus = stind(vm, th, addr_16, r1, r2);
            case OP_ADDI:
                *terminationStatus = addi(th, r1, r2);
                break;
            case OP_SUBI:
                *terminationStatus = subi(th, r1, r2);
                break;
            case OP_ADDF:
                *terminationStatus = addf(th, r1, r2);
                break;
            case OP_SUBF:
                *terminationStatus = subf(th, r1, r2);
                break;
            case OP_MULI:
                *terminationStatus = muli(th, r1, r2);
            case OP_MULF:
                *terminationStatus = mulf(th, r1, r2);
            case OP_DIVI:
                *terminationStatus = divi(th, r1, r2);
            case OP_DIVF:
                *terminationStatus = divf(th, r1, r2);
            case OP_POP:
                *terminationStatus = pop(th, vm, r1);
            case OP_PUSH:
                *terminationStatus = push(th, vm, r1);
            case OP_CMPXCHG:
                *terminationStatus = cmpxchg(th, vm, r1, r2, addr_16);
            case OP_GETPID:
                *terminationStatus = 0;
                th->reg[r1] = thread_num;
            case OP_GETPN:
                th->reg[r1] = num_threads;

        }

        //Increment prog counter before executing next instruciton so pc is pointing at the next instruction
        instr = vm->mem[th->reg[PC]];
        
        opcode = instr & 0xff;
        if (trace) {
            pthread_mutex_lock(&print_lock);
            print_trace(th, instr, thread_num);
            pthread_mutex_unlock(&print_lock);
        }
    } 
    return NULL;
}

// initialize the vm
//   function returns a handle to the structure holding the vm
//	 an error number is returned through the second
//     parameter
//   the following error numbers are supported:
//	   VMX20_NORMAL_TERMINATION
//     VMX20_INITIALIZE_FAILURE
void *initVm(int32_t *errorNumber) {
    *errorNumber = VMX20_NORMAL_TERMINATION;
    struct vm_h* handle = checked_malloc(sizeof(struct vm_h), 0);

    //allocate handle
    if (handle == NULL) {
        *errorNumber = VMX20_INITIALIZE_FAILURE;
    }
    handle->mem = checked_malloc(1000000, 1);
    //allocate and init main memory
    if (handle->mem == NULL) {
        *errorNumber = VMX20_INITIALIZE_FAILURE;
    }
    handle->threads = checked_malloc(sizeof(struct thread_h) * VMX20_MAX_PROCESSORS, 1);
    for(int i = 0; i < VMX20_MAX_PROCESSORS; ++i) {
        handle->threads[i].reg = checked_malloc(sizeof(uint32_t) * 15, 1);
    }
    //allocate and init threads
    if (handle->threads == NULL) {
        *errorNumber = VMX20_INITIALIZE_FAILURE;
    }
    return handle;
}

// load an executable file
//   only one executable file may be loaded at a time
//   the function returns 1 if successful and 0 otherwise
//   if 0 is returned then an error number is returned through the third
//     parameter
//   the following error numbers are supported:
//     VMX20_FILE_NOT_FOUND
//     VMX20_FILE_CONTAINS_OUTSYMBOLS
//     VMX20_FILE_IS_NOT_VALID

int32_t loadExecutableFile(void *handle, char *filename, int32_t *errorNumber) {
    struct vm_h *vm = handle;
    FILE *fp = fopen(filename, "rb");
    
    if (fp == NULL) {
        *errorNumber = VMX20_FILE_NOT_FOUND;
        return 0;
    }

    //check if file is a .exe
    char *file_type = strrchr(filename, '.');
    if (strcmp(file_type, ".exe") != 0) {
        *errorNumber = VMX20_FILE_IS_NOT_VALID;
        return 0;
    }
    
    //read data from file
    fseek(fp, 0, SEEK_END);
    int filesize = ftell(fp);
    fseek(fp, 0, SEEK_SET);
    uint32_t *data = malloc(filesize);
    fread(data, 1, filesize, fp);
    fclose(fp);

    //get section lengths
    
    uint32_t insym_len = data[0];
    num_syms = insym_len / 5;
    uint32_t outsym_len = data[1];
    uint32_t instr_len = data[2];
    num_instr = outsym_len;
  
    //create symbol table
    uint32_t* in_syms = &data[3];
    vm->symtab = checked_malloc(sizeof(uint32_t) * insym_len, 0);
    int offset = 0;
    for(int i = 0; i < num_syms; ++i) {
        vm->symtab[i].name = checked_malloc(4 * sizeof(uint32_t), 0);
        strcpy(vm->symtab[i].name, (char *)&in_syms[offset]);
        vm->symtab[i].addr = in_syms[offset + 4];
        offset += 5;
        if (strcmp(vm->symtab[i].name, "mainx20") == 0) {
            entry_point = vm->symtab[i].addr;
        }
    }
    
    //no mainx20 label
    if (entry_point > 0xfffff) {
        *errorNumber = VMX20_FILE_IS_NOT_VALID;
    }

    if (outsym_len > 0) {
        *errorNumber = VMX20_FILE_CONTAINS_OUTSYMBOLS;
        return 0;
    }
    
    //file is only in-symbols
    if (filesize <= 3 + (insym_len * 4)) {
        *errorNumber = VMX20_FILE_IS_NOT_VALID;
        return 0;
    }
    memcpy(vm->mem, &data[3 + insym_len], instr_len * 4); //set main memory to the start of object code
    free(data);
    return 1;
}

// get the address of a symbol in the current executable file
//   the label must be a symbol in the insymbol section of the executable file
//   the address is returned through the third parameter
//   the function returns 1 if successful and 0 otherwise
int32_t getAddress(void *handle, char *label, uint32_t *outAddr) {
    struct vm_h* vm = (struct vm_h*) handle;
    int found = 0;
    for(int i = 0; i < num_syms; ++i) {
        if(strcmp(vm->symtab[i].name, label) == 0) {
            *outAddr = vm->symtab[i].addr;
            found = 1;
        }
    }

    if (!found) {
        return 0;
    }
    return 1;
}

// write a word to memory
//   the function returns 1 if successful and 0 otherwise
int32_t putWord(void *handle, uint32_t addr, int32_t word) {
    struct vm_h* vm = (struct vm_h*) handle;
    if (!check_addr(addr)) {
        return 0;
    }
    vm->mem[addr] = word;
    return 1;
}

// read a word from memory
//   the word is returned through the third parameter
//   the function returns 1 if successful and 0 otherwise
int32_t getWord(void *handle, uint32_t addr, int32_t *outWord) {
    struct vm_h* vm = (struct vm_h*) handle;
    if (addr < 0 || addr >= MM_SIZE) {
        return 0;
    }
    *outWord = vm->mem[addr];
    return 1;
}

// execute the current loaded executable file
//   the function returns 1 if all processors are successfully started and
//     0 otherwise
//   the second parameter specifies the number of processors to use
//   the third parameter provides an initial SP value for each processor
//   the fourth parameter is used to return the termination status for
//     each processor
//   the following termination statuses are supported:
//     VMX20_NORMAL_TERMINATION
//     VMX20_DIVIDE_BY_ZERO
//     VMX20_ADDRESS_OUT_OF_RANGE
//     VMX20_ILLEGAL_INSTRUCTION
//   the fourth parameter is a Boolean indicating whether an instruction
//     trace should be be printed to stderr
//   Note: that all other registers will be initialized to 0, including
//     the PC and the FP.
//
int32_t execute(void *handle, uint32_t numProcessors, uint32_t initialSP[], int terminationStatus[], int32_t trace) {
    
    struct vm_h *vm = (struct vm_h*)handle;
    if (trace) {
        print_symtab(vm);
    }
    num_threads = numProcessors;
    pthread_t *th = checked_malloc(sizeof(pthread_t) * numProcessors, 0);
    struct worker_args* args = checked_malloc(sizeof(struct worker_args) * numProcessors, 1);
    pthread_mutex_init(&print_lock, NULL);
    pthread_mutex_init(&mem_lock, NULL);
    for (int i = 0; i < numProcessors; ++i) {
        //init args struct
        
        args[i].vm = vm;
        args[i].th = &vm->threads[i];
        args[i].terminationStatus = &terminationStatus[i];
        args[i].trace = trace;
        args[i].thread_num = i;
        //init stack pointers
        vm->threads[i].reg[SP] = initialSP[i];
        //execute
        if (pthread_create(&th[i], NULL, execute_worker, &args[i])) {
            return 1;
        }
    }

    int ret = 1;
    for (int i = 0; i < numProcessors; ++i){
        pthread_join(th[i], NULL);
    }

    free(th);
    free(args);
    return ret;
}

// cleanup everything after simulation is done
void cleanup(void *handle) {
    struct vm_h *vm = (struct vm_h*)handle;
    free(vm->mem);
    for(int i = 0; i < VMX20_MAX_PROCESSORS; ++i) {
        free(vm->threads[i].reg);
    }
    free(vm->threads);
    for (int i = 0; i < num_syms; ++i) {
        free(vm->symtab->name);
    }
    free(vm->symtab);
    free(vm);
    
}
