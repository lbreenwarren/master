#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>

const char* opcode_to_string(int opcode) {
    switch (opcode) {
        case 0x00: return "halt   ";
        case 0x01: return "load   ";
        case 0x02: return "store  ";
        case 0x03: return "ldimm  ";
        case 0x04: return "ldaddr ";
        case 0x05: return "ldind  ";
        case 0x06: return "stind  ";
        case 0x07: return "addf   ";
        case 0x08: return "subf   ";
        case 0x09: return "divf   ";
        case 0x0A: return "mulf   ";
        case 0x0B: return "addi   ";
        case 0x0C: return "subi   ";
        case 0x0D: return "divi   ";
        case 0x0E: return "muli   ";
        case 0x0F: return "call   ";
        case 0x10: return "ret    ";
        case 0x11: return "blt    ";
        case 0x12: return "bgt    ";
        case 0x13: return "beq    ";
        case 0x14: return "jmp    ";
        case 0x15: return "cmpxchg";
        case 0x16: return "getpid ";
        case 0x17: return "getpn  ";
        case 0x18: return "push   ";
        case 0x19: return "pop    ";
        default:   return "unknown";
    }
}

void dumpSyms(int offset, int num_syms, uint32_t* data) {
    
    uint8_t *in_sym = (uint8_t *)&data[offset];
    for(int i = 0; i < num_syms; ++i) {
        printf("%s ", in_sym);
        printf("%x\n", in_sym[16]);
        in_sym += 20;
    }
}

uint32_t calc_addr(int pc, uint32_t addr) {
    uint32_t pc_rel_addr = 0;
    if ((addr & 0x80000) > 0) {
        // printf("\naddr: %x, ~addr: %x, pc: %d", addr, (~addr), pc);
        int pc_back = ((~addr) & 0xfffff) + 1;
        pc_rel_addr = pc - pc_back;
    } else {
        pc_rel_addr = pc + addr;
    }
    return pc_rel_addr;
}

void dissasemble(uint32_t *data, int offset, int obj_len) {
    printf("\nObject Code (%d words)\n\n", obj_len);
    int pc = 1;
    for (int i = 0; i <= obj_len; ++i) {
        uint32_t instr = data[offset + i];
        printf("%07x", i);
        printf("\t%08x", instr);
        int opcode = instr & 0xff;
        printf("    %s", opcode_to_string(opcode));

        switch (opcode) {
            // Format 1: op
            case 0x00: // halt
            case 0x10: // ret
                printf("\n");
                break;

            // Format 2: op addr
            case 0x0F: // call
            case 0x14: // jmp
                printf("    \t%x\n", calc_addr(pc, (instr & 0xfffff000) >> 12));
                break;

            // Format 3: op reg
            case 0x16: // getpid
            case 0x17: // getpn
            case 0x18: // push
            case 0x19: // pop
                printf("    \tr%x\n", (instr & 0x00000f00) >> 8);
                break;

            // Format 4: op reg,const
            case 0x03: // ldimm
                printf("    \tr%x, ", (instr & 0x00000f00) >> 8);
                printf("%x\n", (instr & 0xfffff000) >> 12);
                break;

            // Format 5: op reg,addr
            case 0x01: // load
            case 0x02: // store
            case 0x04: // ldaddr
                printf("    \tr%x, ", (instr & 0x00000f00) >> 8);
                printf("%x\n", calc_addr(pc, (instr & 0xfffff000) >> 12));
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
                printf("    \tr%x, ", (instr & 0x00000f00) >> 8);
                printf("r%x\n", (instr & 0x0000f000) >> 12);
                break;

            // Format 7: op reg1,reg2, offset
            case 0x05: // ldind
            case 0x06: // stind
                printf("    \tr%x, ", (instr & 0x00000f00) >> 8);
                printf("r%x, ", (instr & 0x0000f000) >> 12);
                printf("%x\n", (instr & 0xffff0000) >> 16);
                break;

            // Format 8: op reg1,reg2,addr
            case 0x11: // blt
            case 0x12: // bgt
            case 0x13: // beq
            case 0x15: // cmpxchg
                printf("    \tr%x, ", (instr & 0x00000f) >> 8);
                printf("%x ", calc_addr(pc, (instr & 0xffff0000) >> 12));
                printf("(r%x)\n", (instr & 0x0000f000) >> 12);
                break;

            default:
                printf("\n");
                break;
        }
        ++pc;
    }
}

int main(int argc, char* argv[]) {

    if (argc < 2) {
        fprintf(stderr, "Please provide an asx20 object file");
    }

    FILE *fp = fopen(argv[1], "rb");
    if(fp == NULL) {
        fprintf(stderr, "Failed to open file %s", argv[1]);
        return -1;
    }

    fseek(fp, 0, SEEK_END);
    int filesize = ftell(fp);
    fseek(fp, 0, SEEK_SET);

    uint32_t *data = malloc(filesize);
    fread(data, 1, filesize, fp);

    uint32_t in_len = data[0];
    int num_in = in_len / 5;
    uint32_t out_len = data[1];
    int num_out = out_len / 5;
    uint32_t obj_len = data[2];

    printf("Insymbol Section (%d entries)\n\n", num_in);
    dumpSyms(3, num_in, data);
    
    int outsym_off = 3 + in_len;
    printf("\nOutsymbol Section (%d entries)\n\n", num_out);
    dumpSyms(outsym_off, num_out, data);
    int obj_off = 3 + in_len + out_len;
    dissasemble(data, obj_off, obj_len);
    puts("");
}