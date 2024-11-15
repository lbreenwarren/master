/*
 * assemble.c - handles the details of assembly for the asx20 assembler
 *
 *              This currently just contains stubs with debugging output.
 */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "defs.h"
#include "symtab.h"

// enable debugging printout
#define DEBUG 0

#define ERROR_PROGRAM_SIZE "Program consumes more than 2^20 words"
#define ERROR_LABEL_DEFINED "Label %s already defined"
#define ERROR_OPCODE_UNKNOWN "Unknown Opcode %s"
#define ERROR_OPERAND_FORMAT "Opcode does not match the given operands"
#define ERROR_CONSTANT_ZERO "Constant must be greater than zero"
#define ERROR_CONSTANT_INVALID "Constant %d will not fit into 20 bits"
#define ERROR_OFFSET_INVALID "Offset %d will not fit into 16 bits"
#define ERROR_MULTIPLE_EXPORT "Symbol %s exported more than once"
#define ERROR_MULTIPLE_IMPORT "Symbol %s imported more than once"
#define ERROR_LABEL_REFERENCE_NOT_FOUND "Label %s is referenced but not defined or imported"
#define ERROR_SYMBOL_IMPORT_EXPORT "Symbol %s is both imported and exported"
#define ERROR_SYMBOL_IMPORT_DEFINED "Symbol %s is both imported and defined"
#define ERROR_SYMBOL_IMPORT_NO_REFERENCE "Symbol %s is imported but not referenced"
#define ERROR_SYMBOL_EXPORT_NO_DEFINITION "Symbol %s is exported but not defined"
#define ERROR_SYMBOL_IMPORT_SIZE "Symbol %s is imported and longer than 16 characters"
#define ERROR_SYMBOL_EXPORT_SIZE "Symbol %s is exported and longer than 16 characters"
#define ERROR_LABEL_SIZE16 "Reference to label %s at address %d won't fit in 16 bits"
#define ERROR_LABEL_SIZE20 "Reference to label %s at address %d won't fit in 20 bits"

//Valid instructions
const char *vmx20_instructions[] = {
    "halt",     // 0x00 (0) - halt
    "load",     // 0x01 (1) - load reg,addr
    "store",    // 0x02 (2) - store reg,addr
    "ldimm",    // 0x03 (3) - ldimm reg,const
    "ldaddr",   // 0x04 (4) - ldaddr reg,addr
    "ldind",    // 0x05 (5) - ldind reg1,offset(reg2)
    "stind",    // 0x06 (6) - stind reg1,offset(reg2)
    "addf",     // 0x07 (7) - addf reg1,reg2
    "subf",     // 0x08 (8) - subf reg1,reg2
    "divf",     // 0x09 (9) - divf reg1,reg2
    "mulf",     // 0x0A (10) - mulf reg1,reg2
    "addi",     // 0x0B (11) - addi reg1,reg2
    "subi",     // 0x0C (12) - subi reg1,reg2
    "divi",     // 0x0D (13) - divi reg1,reg2
    "muli",     // 0x0E (14) - muli reg1,reg2
    "call",     // 0x0F (15) - call addr
    "ret",      // 0x10 (16) - ret
    "blt",      // 0x11 (17) - blt reg1,reg2,addr
    "bgt",      // 0x12 (18) - bgt reg1,reg2,addr
    "beq",      // 0x13 (19) - beq reg1,reg2,addr
    "jmp",      // 0x14 (20) - jmp addr
    "cmpxchg",  // 0x15 (21) - cmpxchg reg1,reg2,addr
    "getpid",   // 0x16 (22) - getpid reg
    "getpn",    // 0x17 (23) - getpn reg
    "push",     // 0x18 (24) - push reg
    "pop",       // 0x19 (25) - pop reg
    "word",     // 0x1A (26) - word const
    "alloc",    // 0x1B (27) - alloc const
    "export",   // 0x1C (28) - export label
    "import"    // 0x1D (29) - import label
};

// Format 1
int format1_opcodes[] = {
    0x00,   // halt
    0x10    // ret
};

// Format 2
int format2_opcodes[] = {
    0x0F,   // call
    0x14,    // jmp
    0x1C,   // export
    0x1D    // import
};

// Format 3
int format3_opcodes[] = {
    0x16,   // getpid
    0x17,   // getpn
    0x18,   // push
    0x19    // pop
};

// Format 4
int format4_opcodes[] = {
    0x03   // ldimm
};

// Format 5
int format5_opcodes[] = {
    0x01,   // load
    0x02,   // store
    0x04    // ldaddr
};

// Format 6
int format6_opcodes[] = {
    0x07,   // addf
    0x08,   // subf
    0x0A,   // mulf
    0x0B,   // addi
    0x0C,   // subi
    0x0E,   // muli
    0x09,   // divf
    0x0D    // divi
};

// Format 7
int format7_opcodes[] = {
    0x05,   // ldind
    0x06    // stind
};

// Format 8
int format8_opcodes[] = {
    0x11,   // blt
    0x12,   // bgt
    0x13,   // beq
    0x15    // cmpxchg
};

// Format 9 (used for word and alloc)
int format9_opcodes[] = {
    0x1A,   //word
    0x1B,   //alloc
};

//Symbol Table Size
#define TABLE_SIZE 50

//Output File reference to be obtained between passes
//also used to indicate 1st or 2nd pass
FILE* out = NULL;

//error count
int num_errs;

//Program Counter
int pc;

//Symbol Structure
typedef struct {
    int address;    //address where the symbol was defined (-1 if not defined locally)
    int referenced; //boolean indicating if the symbol was referenced
    int num_refs;   //number of times referenced for outsymbols
    int exported;   //boolean indicating if the symbol was exported
    int imported;   //boolean indicating if the symbol was imported
    void* import_ref; //pointer to another symbol structure containing the address of the insymbol reference
} symbol;

//symtab_handle pointer
void *sym_tab;

// this is called once so that the assembler can initialize any internal
// data structures.
void initAssemble(void)
{
    sym_tab = symtabCreate(TABLE_SIZE);
    pc = 0;
    num_errs = 0;

#if DEBUG
    fprintf(stderr, "initAssemble called\n");
#endif
}

// Helper function to initilize symbols 
static void *initSymbol() {
    symbol *new_symbol = malloc(sizeof(symbol));
    if (new_symbol == NULL) {
        return NULL;
    }
    new_symbol->address = -1;
    new_symbol->exported = 0;
    new_symbol->imported = 0;
    new_symbol->referenced = 0;
    new_symbol->num_refs = 0;
    new_symbol->import_ref = NULL;
    return new_symbol;
}

static char* pass1_error_check(INSTR instr, char* label) {
    char* addr = NULL;
    int check_format = 0;
    switch (instr.format){

        case 1:
            for (int i = 0; i < 2; ++i) {
                if (strcmp(instr.opcode, vmx20_instructions[format1_opcodes[i]]) == 0) {
                    check_format = 1;
                }
            }

            if (check_format == 0) {
                error(ERROR_OPERAND_FORMAT);
                addr = NULL;
                ++num_errs;
                break;
            }

            addr = NULL;
            break;

        case 2:
            for (int i = 0; i < 4; ++i) {
                if (strcmp(instr.opcode, vmx20_instructions[format2_opcodes[i]]) == 0) {
                    check_format = 1;
                }
            }

            if (check_format == 0) {
                error(ERROR_OPERAND_FORMAT);
                addr = NULL;
                ++num_errs;
                break;
            }

            if (strcmp(instr.opcode, "export") == 0) {
                symbol *sym = symtabLookup(sym_tab, instr.u.format2.addr);
                if (sym != NULL && sym->exported) {
                    error(ERROR_MULTIPLE_EXPORT, label);
                    addr = NULL;
                    ++num_errs;
                    break;
                }
                if (sym != NULL && sym->exported) {
                    error(ERROR_SYMBOL_IMPORT_EXPORT, label);
                    addr = NULL;
                    ++num_errs;
                    break;
                }
            }

            if (strcmp(instr.opcode, "import") == 0) {
                symbol *sym = symtabLookup(sym_tab, instr.u.format2.addr);
                if (sym != NULL && sym->imported) {
                    error(ERROR_MULTIPLE_IMPORT, label);
                    addr = NULL;
                    ++num_errs;
                    break;
                }
                if (sym != NULL && sym->exported) {
                    error(ERROR_SYMBOL_IMPORT_EXPORT, label);
                    addr = NULL;
                    ++num_errs;
                    break;
                }
            }

            addr = instr.u.format2.addr;
            break;
            

        case 3:
            for (int i = 0; i < 4; ++i) {
                if (strcmp(instr.opcode, vmx20_instructions[format3_opcodes[i]]) == 0) {
                    check_format = 1;
                }
            }

            if (check_format == 0) {
                error(ERROR_OPERAND_FORMAT);
                addr = NULL;
                ++num_errs;
                break;
            }

            addr = NULL;
            break;

        case 4:
            for (int i = 0; i < 1; ++i) {
                if (strcmp(instr.opcode, vmx20_instructions[format4_opcodes[i]]) == 0) {
                    check_format = 1;
                }
            }

            if (check_format == 0) {
                error(ERROR_OPERAND_FORMAT);
                addr = NULL;
                ++num_errs;
                break;
            }

            int constant = instr.u.format4.constant;

            if (constant < -524288 || constant > 524287) {
                error(ERROR_CONSTANT_INVALID, constant); 
                addr = NULL;
                ++num_errs;
                break;
            }
            addr = NULL;
            break;

        case 5:
            for (int i = 0; i < 3; ++i) {
                if (strcmp(instr.opcode, vmx20_instructions[format5_opcodes[i]]) == 0) {
                    check_format = 1;
                }
            }

            if (check_format == 0) {
                error(ERROR_OPERAND_FORMAT);
                addr = NULL;
                ++num_errs;
                break;
            }

            else {
                addr = instr.u.format5.addr;
                break;
            }

        case 6:
            for (int i = 0; i < 8; ++i) {
                if (strcmp(instr.opcode, vmx20_instructions[format6_opcodes[i]]) == 0) {
                    check_format = 1;
                }
            }

            if (check_format == 0) {
                error(ERROR_OPERAND_FORMAT);
                addr = NULL;
                ++num_errs;
                break;
            }

            addr = NULL;
            break;

        case 7:
            for (int i = 0; i < 2; ++i) {
                if (strcmp(instr.opcode, vmx20_instructions[format7_opcodes[i]]) == 0) {
                    check_format = 1;
                }
            }

            if (check_format == 0) {
                error(ERROR_OPERAND_FORMAT);
                addr = NULL;
                ++num_errs;
                break;
            }

            int offset = instr.u.format7.offset;
            if (offset > ((1 << 16) - 1)) {
                error(ERROR_OFFSET_INVALID, offset); 
                addr = NULL;
                ++num_errs;
                break;
            }

            addr = NULL;
            break;

        case 8:
            for (int i = 0; i < 4; ++i) {
                if (strcmp(instr.opcode, vmx20_instructions[format8_opcodes[i]]) == 0) {
                    check_format = 1;
                }
            }

            if (check_format == 0) {
                error(ERROR_OPERAND_FORMAT);
                addr = NULL;
                ++num_errs;
                break;
            }

            else {
                addr = instr.u.format8.addr;
                break;
            }

        case 9:
            for (int i = 0; i < 2; ++i) {
               
                if (strcmp(instr.opcode, vmx20_instructions[format9_opcodes[i]]) == 0) {
                    
                    check_format = 1;
                }
            }

            if (check_format == 0) {
                error(ERROR_OPERAND_FORMAT);
                addr = NULL;
                ++num_errs;
                break;
            }

            int val = instr.u.format9.constant;

            if (val < 0) {
                error(ERROR_CONSTANT_ZERO);
                addr = NULL;
                ++num_errs;
                break;
            }
            
            if (val > 0xffffffff) {
                error(ERROR_CONSTANT_INVALID, val); 
                addr = NULL;
                ++num_errs;
                break;
            }

            addr = NULL;
            break;
            
        default:
            addr = NULL;
    }

    return addr;
}

static void between_pass_error_check(symbol *next_symbol, const char *label) {
    if (next_symbol->referenced) {
        if ((next_symbol->address == -1) && (!next_symbol->imported)) {
            error(ERROR_LABEL_REFERENCE_NOT_FOUND, label);
            ++num_errs;
        }
    }
    if (next_symbol->imported) {
        if (next_symbol->address >= 0) {
            error(ERROR_SYMBOL_IMPORT_DEFINED, label);
            ++num_errs;
        }
        if (!next_symbol->referenced) {
            error(ERROR_SYMBOL_IMPORT_NO_REFERENCE, label);
            ++num_errs;
        }
    }
    if (next_symbol->exported) {
        if(next_symbol->address < 0) {
            error(ERROR_SYMBOL_EXPORT_NO_DEFINITION, label);
            ++num_errs;
        }
    }
}

static void be_write_word(unsigned int word, FILE *outf) {

    for (int i = 0; i < 4; ++i) {
       
        putc(word & (0xff << (8 * i)), outf);
       
    }

}

static void writeInsymbolToHeader(char* label, symbol *data, FILE *outf){
    char *str = label;
    while(*str != '\0') {
        putc(*str, outf);
        ++str;
    }
    for(int i=0; i < 16 - strlen(label); ++i) {
        putc(0x0, outf);
    }
    unsigned int addr = data->address;
    for (int i=0; i < 4; ++i) {
        putc(addr & (0xff << (i * 8)), outf);
    }
}

static void writeOutsymbolToHeader(char *label, symbol *data, FILE *outf) {
    
    data = (symbol*)data->import_ref;
   
    while (data != NULL) {
        char *str = label;
        while(*str != '\0') {
            putc(*str, outf);
            ++str;
        }
        for(int i=0; i < 16 - strlen(label); ++i) {
            putc(0x0, outf);
        }
        unsigned int addr = data->address;
        for (int i=0; i < 4; ++i) {
            putc(addr & (0xff << (i * 8)), outf);
        }
        data = (symbol*)data->import_ref;
    }
}

static void assembleFirstPass(char *label, INSTR instr) {
    
    if (label) {
        
        symbol* label_symbol = symtabLookup(sym_tab, label);

        if (label_symbol == NULL) {
            label_symbol = initSymbol();
            if (label_symbol == NULL) {
                fprintf(stderr, "Failed to allocate memory for symbol %s", label);
                exit(1); 
            }
            symtabInstall(sym_tab, label, label_symbol);
        } else if (label_symbol->address >= 0) {
            error(ERROR_LABEL_DEFINED, label);
            ++num_errs;
        }
        
        label_symbol->address = pc;
    }

    if (instr.format != 0){
        int valid_opcode = 0;
        for (int i = 0; i < 30; ++i) {

            if (!strcmp(instr.opcode, vmx20_instructions[i])) {
                
                valid_opcode = 1;
                break;
            }

        }
        
        if (valid_opcode == 0) {
            error(ERROR_OPCODE_UNKNOWN, instr.opcode);
            ++num_errs;
            return;
        }

        char *addr = NULL;
        
        addr = pass1_error_check(instr, label);
        if (addr != NULL) {
    
            symbol* instr_symbol = symtabLookup(sym_tab, addr);
            
            if (instr_symbol == NULL) {
                instr_symbol = initSymbol();
                if (instr_symbol == NULL) {
                    fprintf(stderr, "Failed to allocate memory for symbol %s", label);
                    exit(1); 
                }
                symtabInstall(sym_tab, addr, instr_symbol);
            }

            if (strcmp(instr.opcode, "export") == 0) {
                instr_symbol->exported = 1;
            } else if (strcmp(instr.opcode, "import") == 0) {
                instr_symbol->imported = 1;
            } else {
                instr_symbol->referenced = 1;
                ++(instr_symbol->num_refs);
                symbol *tmp = instr_symbol;

                while (tmp->import_ref != NULL) {
                    tmp = (symbol *)tmp->import_ref;
                } 
                
                tmp->import_ref = initSymbol();
                symbol *ptr = (symbol*) tmp->import_ref;
                ptr->address = pc;
            
                ++pc;
            }
            
        } else if (strcmp(instr.opcode, "alloc") == 0) {
            pc += instr.u.format9.constant;
        } else {
            ++pc;
        }
    }
}

static void writeFormat1ObjectCode(INSTR instr, char* label, int opcode) {
    putc((opcode & 0xff), out);
    for(int i = 0; i < 3; ++i) {
        putc(0x0, out);
    }
}

static void writeFormat2ObjectCode(INSTR instr, char* label, int opcode) {
    int addr = 0;
    symbol* target = symtabLookup(sym_tab, instr.u.format2.addr);
   
    if (target->imported) {
        addr = 0;
    } else {
        addr = target->address - pc; 
    }

    if (addr < -524288 || addr > 524287) {
        error(ERROR_LABEL_SIZE20, instr.u.format2.addr, target->address);
    }
    
    int byte1 = (addr >> 12) & 0xff;
    int byte2 = (addr >> 4) & 0xff;
    int byte3 = addr & 0xf;
    byte3 <<= 4;
    putc((opcode & 0xff), out);
    putc(byte3, out);
    putc(byte2, out);
    putc(byte1, out);
    
}

static void writeFormat3ObjectCode(INSTR instr, char* label, int opcode) {
    putc((opcode & 0xff), out);
    putc(instr.u.format3.reg & 0xf, out);
    putc(0x0, out);
    putc(0x0, out);    
}

static void writeFormat4ObjectCode(INSTR instr, char* label, int opcode) {
    int constant = instr.u.format4.constant;
    int byte1 = (constant >> 12) & 0xff;
    int byte2 = (constant >> 4) & 0xff;
    int byte3 = constant & 0xf;
    byte3 <<= 4;
    byte3 |= ((instr.u.format4.reg & 0xf));
    putc((opcode & 0xff), out);
    putc(byte3, out);
    putc(byte2, out);
    putc(byte1, out);
    
}

static void writeFormat5ObjectCode(INSTR instr, char* label, int opcode) {
    int addr = -1;
    symbol* target = symtabLookup(sym_tab, instr.u.format5.addr);
   
    if (target->imported) {
        addr = 0;
    } else {
        addr = target->address - pc; 
    }

    if (addr < -524288 || addr > 524287) {
        error(ERROR_LABEL_SIZE20, instr.u.format5.addr, target->address);
    }
    
    int byte1 = (addr >> 12) & 0xff;
    int byte2 = (addr >> 4) & 0xff;
    int byte3 = addr & 0xf;
    byte3 <<= 4;
    byte3 = (byte3 | (instr.u.format5.reg & 0xf));
    putc((opcode & 0xff), out);
    putc(byte3, out);
    putc(byte2, out);
    putc(byte1, out);
    
}

static void writeFormat6ObjectCode(INSTR instr, char* label, int opcode) {
    
    int byte = instr.u.format6.reg1;
    byte |= (instr.u.format6.reg2 << 4);
    putc((opcode & 0xff), out);
    putc(byte, out); 
    putc(0x0, out);
    putc(0x0, out);
}

static void writeFormat7ObjectCode(INSTR instr, char* label, int opcode) {
    putc((opcode & 0xff), out);
    int byte = instr.u.format7.reg1;
    byte |= (instr.u.format7.reg2 << 4);
    putc(byte, out);
    int offset = instr.u.format7.offset;
    putc(offset & 0xff, out);
    putc((offset >> 8) & 0xff, out);
   
}

static void writeFormat8ObjectCode(INSTR instr, char* label, int opcode) {
    int addr = -1;
    symbol* target = symtabLookup(sym_tab, instr.u.format5.addr);
   
    if (target->imported) {
        addr = 0;
    } else {
        addr = target->address - pc; 
    }

    if (addr < -32768 || addr > 32768) {
        error(ERROR_LABEL_SIZE20, instr.u.format5.addr, target->address);
    }
    putc((opcode & 0xff), out);
    int byte = instr.u.format8.reg1;
    byte |= (instr.u.format8.reg2 << 4);
    putc(byte,out);
    putc(addr & 0xff, out);
    putc((addr >> 8) & 0xff, out);
    
    
}

static void writeFormat9ObjectCode(INSTR instr, char* label, int opcode) {
    if (strcmp(instr.opcode, "word") == 0) {

        for(int i = 0; i < 4; ++i) {
            putc((instr.u.format9.constant >> (i * 8)) & 0xff, out);
        }
    }
    if (strcmp(instr.opcode, "alloc") == 0) {
        
        for(int i = 0; i < instr.u.format9.constant; ++i) {
            for(int i = 0; i < 4; ++i) {
                putc(0x0, out);
            }
        }
    }
}

static void assembleSecondPass(char* label, INSTR instr) {
    
    int opcode = -1;
    
    for (int i = 0; i < 30; ++i) {
        if ((instr.format != 0) && (strcmp(vmx20_instructions[i], instr.opcode) == 0)) {
            opcode = i;
            break;
        }
    }
    switch(instr.format) {
        case 1:
            ++pc;
            writeFormat1ObjectCode(instr, label, opcode);
            break;
        case 2:
            if ((strcmp(instr.opcode, "import") == 0) || (strcmp(instr.opcode, "export") == 0)){
                break;
            }
            ++pc;
            writeFormat2ObjectCode(instr, label, opcode);
            break;
        case 3:
            ++pc;
            writeFormat3ObjectCode(instr, label, opcode);
            break;
        case 4:
            ++pc;
            writeFormat4ObjectCode(instr, label, opcode);
            break;
        case 5:
            ++pc;
            writeFormat5ObjectCode(instr, label, opcode);
            break;
        case 6:
            ++pc;
            writeFormat6ObjectCode(instr, label, opcode);
            break;
        case 7:
            ++pc;
            writeFormat7ObjectCode(instr, label, opcode);
            break;
        case 8:
            ++pc;
            writeFormat8ObjectCode(instr, label, opcode);
            break;
        case 9:
            if (strcmp(instr.opcode, "alloc") == 0) {
                pc += instr.u.format9.constant;
            } else {
                ++pc;
            }
            writeFormat9ObjectCode(instr, label, opcode);
            break;
        default:
            break;
    }
    
}

// this is the "guts" of the assembler and is called for each line
// of the input that contains a label, instruction or directive
//
// note that there may be both a label and an instruction or directive
// present on a line
//
// note that the assembler directives "export" and "import" have structure
// identical to instructions with format 2, so that format is used for them
//
// for the directives "word" and "alloc" a special format, format 9, is used
//
// see defs.h for the details on how each instruction format is represented
// in the INSTR struct.
//
void assemble(char *label, INSTR instr)
{
    if (out == NULL) {
        assembleFirstPass(label, instr);
    } else {
        assembleSecondPass(label, instr);
    }


#if DEBUG
  fprintf(stderr, "assemble called:\n");
  if (label)
  {
    fprintf(stderr, "  label is %s\n", label);
  }
  if (instr.format != 0)
  {
    fprintf(stderr, "  instruction is %s", instr.opcode);
    switch(instr.format)
    {
      case 1:
        fprintf(stderr, "\n");
        break;
      case 2:
        fprintf(stderr, " %s\n", instr.u.format2.addr);
        break;
      case 3:
        fprintf(stderr, " r%d\n", instr.u.format3.reg);
        break;
      case 4:
        fprintf(stderr, " r%d,%d\n", instr.u.format4.reg,
          instr.u.format4.constant);
        break;
      case 5:
        fprintf(stderr, " r%d,%s\n", instr.u.format5.reg,
          instr.u.format5.addr);
        break;
      case 6:
        fprintf(stderr, " r%d,r%d\n", instr.u.format6.reg1,
          instr.u.format6.reg2);
        break;
      case 7:
        fprintf(stderr, " r%d,%d(r%d)\n", instr.u.format7.reg1,
          instr.u.format7.offset, instr.u.format7.reg2);
        break;
      case 8:
        fprintf(stderr, " r%d,r%d,%s\n", instr.u.format8.reg1,
          instr.u.format8.reg2, instr.u.format8.addr);
        break;
      case 9:
        fprintf(stderr, " %d\n", instr.u.format9.constant);
        break;
      default:
        bug("unexpected instruction format (%d) in assemble", instr.format);
        break;
    }
  }
#endif
    
}

// this is called between passes and provides the assembler the file
// pointer to use for outputing the object file
//
// it returns the number of errors seen on pass1
//
int betweenPasses(FILE *outf)
{   
    
    if (pc > (1 << 20)) {
        error(ERROR_PROGRAM_SIZE);
        ++num_errs;
    }

    void *it = symtabCreateIterator(sym_tab);
    void *data;
    char *label = (char*)symtabNext(it, &data);

    int num_insymbols = 0;
    int num_outsymbols = 0;

    char *out_symbols[TABLE_SIZE];
    int num_imports = 0;
    char *in_symbols[TABLE_SIZE];

    symbol *next_symbol = (symbol*)data;
    
    while (label != NULL) {
        between_pass_error_check(next_symbol, label);
        fprintf(stdout, "%s", label);
    
        if (next_symbol->address >= 0) {
            fprintf(stdout, " %d", next_symbol->address);
            
        }
        if (next_symbol->referenced > 0) {
            fprintf(stdout, " reference");
        }
        if (next_symbol->exported > 0) {
            fprintf(stdout, " exported");
            in_symbols[num_insymbols] = label;
            ++num_insymbols;
        }
        if (next_symbol->imported > 0) {
            fprintf(stdout, " imported");
            num_outsymbols += next_symbol->num_refs;
            out_symbols[num_imports] = label;
            ++num_imports;
        }
       
        fprintf(stdout, "\n");
        label = (char*)symtabNext(it, &data);
        next_symbol = (symbol*)data;
    }

    unsigned int insym_size = num_insymbols * 5;
    unsigned int outsym_size = num_outsymbols * 5;
    unsigned int objcode_size = pc;
    //printf("in:%x, out:%x, obj:%x\n", num_insymbols, num_outsymbols, objcode_size);
    be_write_word(insym_size, outf);
    be_write_word(outsym_size, outf);
    be_write_word(objcode_size, outf);
    
    for (int i = 0; i < num_insymbols; ++i) {
        void *data = symtabLookup(sym_tab, in_symbols[i]);
        writeInsymbolToHeader(in_symbols[i], data, outf);
    }

    for (int i = 0; i < num_imports; ++i) {
        symbol *data = (symbol *) symtabLookup(sym_tab, out_symbols[i]);
        writeOutsymbolToHeader(out_symbols[i], (symbol *)data, outf);
    }

    out = outf;
    pc = 0;
    return num_errs;

#if DEBUG
  fprintf(stderr, "betweenPasses called\n");
#endif
  return 0;
}

