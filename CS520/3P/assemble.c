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

//error count
int num_errs;

//Program Counter
int pc;

//Symbol Structure
typedef struct {
    int address;
    int referenced;
    int num_refs;
    int exported;
    int imported;
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

            if (constant < 0) {
                error(ERROR_CONSTANT_ZERO);
                addr = NULL;
                ++num_errs;
                break;
            }

            if (constant > ((1 << 20) - 1)) {
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
            
            if (val > ((1 << 20) - 1)) {
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

static void le_write_word(unsigned int word, FILE *outf) {

    for (int i = 0; i < 4; ++i) {
       
        putc(word & (0xff << (8 * i)), outf);
       
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
                ++pc;
            }
            
        } else if (strcmp(instr.opcode, "alloc") == 0) {
            pc += instr.u.format9.constant;
        } else {
            ++pc;
        }
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
    const char *label = symtabNext(it, &data);

    int insymbols = 0;
    int outsymbols = 0;

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
            ++insymbols;
        }
        if (next_symbol->imported > 0) {
            fprintf(stdout, " imported");
            outsymbols += next_symbol->num_refs;
        }
       
        fprintf(stdout, "\n");
        label = symtabNext(it, &data);
        next_symbol = (symbol*)data;
    }

    unsigned int insym_size = insymbols * 5;
    unsigned int outsym_size = outsymbols * 5;
    unsigned int objcode_size = pc;
    printf("in:%x, out:%x, obj:%x\n", insymbols, outsymbols, objcode_size);
    le_write_word(insym_size, outf);
    le_write_word(outsym_size, outf);
    le_write_word(objcode_size, outf);

    return num_errs;
#if DEBUG
  fprintf(stderr, "betweenPasses called\n");
#endif
  return 0;
}

