#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>
#include <elf.h>

const char *sh_type_to_str(uint32_t type) {
    switch (type) {
        case SHT_NULL:     return "NULL";
        case SHT_PROGBITS: return "PROGBITS";
        case SHT_SYMTAB:   return "SYMTAB";
        case SHT_STRTAB:   return "STRTAB";
        case SHT_RELA:     return "RELA";
        case SHT_HASH:     return "HASH";
        case SHT_DYNAMIC:  return "DYNAMIC";
        case SHT_NOTE:     return "NOTE";
        case SHT_NOBITS:   return "NOBITS";
        case SHT_REL:      return "REL";
        case SHT_SHLIB:    return "SHLIB";
        case SHT_DYNSYM:   return "DYNSYM";
        case SHT_INIT_ARRAY: return "INIT_ARRAY";
        case SHT_FINI_ARRAY: return "FINI_ARRAY";
        case SHT_PREINIT_ARRAY: return "PREINIT_ARRAY";
        case SHT_GROUP:    return "GROUP";
        case SHT_SYMTAB_SHNDX: return "SYMTAB_SHNDX";
        case SHT_GNU_ATTRIBUTES: return "GNU_ATTRIBUTES";
        case SHT_GNU_HASH:     return "GNU_HASH";
        case SHT_GNU_LIBLIST:  return "GNU_LIBLIST";
        case SHT_CHECKSUM:     return "CHECKSUM";
        case SHT_SUNW_move:    return "SUNW_move";
        case SHT_SUNW_COMDAT:  return "SUNW_COMDAT";
        case SHT_SUNW_syminfo: return "SUNW_syminfo";
        case SHT_GNU_verdef:   return "GNU_verdef";
        case SHT_GNU_verneed:  return "GNU_verneed";
        case SHT_GNU_versym:   return "GNU_versym";
        default: return "UNKNOWN";
    }
}

const char *st_bind_to_str(uint32_t type) {
    switch(type) {
        case STB_LOCAL:         return "LOCAL     ";
        case STB_GLOBAL:        return "GLOBAL    ";
        case STB_WEAK:          return "WEAK      ";
        case STB_GNU_UNIQUE:    return "GNU_UNIQUE";
        default:                return "UNKNOWN   ";
    }
}

const char *stt_to_str(uint32_t stt) {
    switch (stt) {
        case STT_NOTYPE:  return "NOTYPE ";
        case STT_OBJECT:  return "OBJECT ";
        case STT_FUNC:    return "FUNC   ";
        case STT_SECTION: return "SECTION";
        case STT_FILE:    return "FILE   ";
        case STT_COMMON:  return "COMMON ";
        case STT_TLS:     return "TLS    ";
        default:          return "UNKNOWN";
    }
}

const char *st_vis_to_str(uint32_t st_vis) {
    switch(st_vis) {
        case STV_DEFAULT:   return "DEFAULT  ";
        case STV_INTERNAL:  return "INTERNAL ";
        case STV_HIDDEN:    return "HIDDEN   ";
        case STV_PROTECTED: return "PROTECTED";
        default:            return "UNKNOWN  ";
    }
}

void dumpBytes(int num_bytes, uint8_t *bytes, FILE *stream) {
    for (int i = 0; i < num_bytes; ++i) {
        if ((i) % 16 == 0) {
            fprintf(stream, "\n[0x%04x]:", i);
        }
        fprintf(stream, " %02x", bytes[i]);
    }
    printf("\n");
}

int checkHeader(Elf64_Ehdr *elf_header) {
    int num_errs = 0;
    char * err_msgs = "";
    uint8_t *ident = elf_header->e_ident;
    
    if(ident[EI_MAG0] != ELFMAG0 || ident[EI_MAG1] != ELFMAG1 || ident[EI_MAG2] != ELFMAG2 || ident[EI_MAG3] != ELFMAG3) {
        err_msgs = "\tInvalid Identification Bytes";
        ++num_errs;
    }

    if (ident[EI_CLASS] < 1 || ident[EI_CLASS] > 2) {
        strcat(err_msgs, "\tInvalid Class");
        ++num_errs;
    }

    if (ident[EI_DATA] < 1 || ident[EI_DATA] > 2) {
        strcat(err_msgs, "\tInvalid Data Encoding");
        ++num_errs;
    }

    if (elf_header->e_type < 1 || elf_header->e_type > 4) {
        strcat(err_msgs, "\tInvalid File Type");
        ++num_errs;
    }

    if (elf_header->e_version != EV_CURRENT) {
        strcat(err_msgs, "\tInvalid Version");
    }

    if (num_errs > 0) {
        fprintf(stderr, "Invalid header values in the following feilds:\n%s", err_msgs);
        dumpBytes(sizeof(Elf64_Ehdr), (uint8_t *)elf_header, stderr);
        return -1;
    }
    return 0;
}

int printHeader(char *filename, Elf64_Ehdr *elf_header) {
    // elf_header->e_ident[0] = 0;
    
    if (checkHeader(elf_header) < 0) {
        return -1;
    }
    
    puts("ELF Header:");
    printf("Magic:");
    
    for(int i = 0; i < 16; ++i) {
        printf(" %02x", elf_header->e_ident[i]);
    }

    printf("\nClass: ELF%d", elf_header->e_ident[EI_CLASS] * 32);
    char * endianess = elf_header->e_ident[EI_DATA] == ELFDATA2LSB ? "little" : "big";
    printf("\nData: 2's complement, %s encoding", endianess);
    char *f_type = "";
    switch(elf_header->e_type) {
        case ET_REL:
            f_type= "REL (Relocatable file)";
            break;
        case ET_EXEC:
            f_type = "EXEC (Executable file)";
            break;
        case ET_DYN:
            f_type = "DYN (Shared object file)";
            break;
        case ET_CORE:
            f_type = "CORE (Core file)";
            break;
        default:
            fprintf(stderr, "Invalid Header: Unkown File Type\n");
    }

    printf("\nType: %s", f_type);
    printf("\nVersion: 0x%x", elf_header->e_version);
    printf("\nEntry Point Address: 0x%lx", elf_header->e_entry);
    printf("\nStart of program headers: %ld (bytes into file)", elf_header->e_phoff);
    printf("\nStart of section headers: %ld (bytes into file)", elf_header->e_shoff);
    printf("\nFlags: 0x%x", elf_header->e_flags);
    printf("\nSize of this header: %d (bytes)", elf_header->e_ehsize);
    printf("\nSize of progream headers: %d (bytes)", elf_header->e_phentsize);
    printf("\nNumber of section headers: %d", elf_header->e_phnum);
    printf("\nSize of section headers: %d (bytes)", elf_header->e_shentsize);
    printf("\nNumber of section headers: %d", elf_header->e_shnum);
    printf("\nSection header string table index: %d", elf_header->e_shstrndx);
    puts("");
    return 0;
}

void printStringTable(uint8_t *str_table, int size) {
   printf("Section names from 'shstrtab':");
   for(int i = 0; i < size; ++i) {
    if(str_table[i] == '.') {
        printf("\n");
    }
    putc(str_table[i], stdout);
   }
   printf("\n");
}

void printSections(uint8_t *strtable, Elf64_Shdr *sh_table, uint64_t entry_size, uint64_t num_entries) {
    printf("Section Headers:");
    Elf64_Shdr *entry = sh_table;
    for (int i = 0; i < num_entries; ++i) {
        entry = &sh_table[i];
        printf("\n[%2d] ", i);
        printf("%s", &strtable[entry->sh_name]);
        printf(" %s", sh_type_to_str(entry->sh_type));
        printf(" %016lx", entry->sh_addr);
        printf(" %06lx", entry->sh_offset);
        printf(" %06lx", entry->sh_size);
    }
    printf("\n");
}

void printSymtab(Elf64_Sym *symtab, uint8_t *strtab, int num_entries) {
    Elf64_Sym *sym_entry = (Elf64_Sym *)symtab;
    printf("Symbol table '.symtab' contains %d entries:\n", num_entries);
    printf("Num:    Value          Size Type   Bind       Vis      Ndx Name");
    for (int i = 0; i < num_entries; ++i) {
        printf("\n%3d: ", i);
        printf("%016lx", sym_entry->st_value);
        printf("%6ld", sym_entry->st_size); 
        printf(" %s", stt_to_str(ELF64_ST_TYPE(sym_entry->st_info)));
        printf("%s", st_bind_to_str(ELF64_ST_BIND(sym_entry->st_info)));
        printf(" %s", st_vis_to_str(ELF64_ST_VISIBILITY(sym_entry->st_other)));
        if (sym_entry->st_shndx == STN_UNDEF) {
            printf("UND");
        } else if (sym_entry->st_shndx == SHN_ABS) {
            printf("ABS");
        } else {
            printf("%3d", sym_entry->st_shndx);    
        }
        printf(" %s", &strtab[sym_entry->st_name]);
        
        ++sym_entry;
    }
    printf("\n");
}

int main(int argc, char* argv[]) {
    
    if (argc < 3) {
        printf("USAGE:[FILENAME] [FLAGS]"); 
        printf("[FILENAME]: file name of a object or executable file");
        printf("[FLAGS]:");
        printf("\t-h: display executable header information");
        printf("\t-sn: String dump of all names in .shstrtab section");
        printf("\t-sec: list all section headers");
        printf("\t-sym: dump of .symtab symbol table");
    }
    char * filename = argv[2];
    char * flag = argv[1];
    FILE *fp = fopen(filename, "rb");

    fseek(fp, 0, SEEK_END);
    int filesize = ftell(fp);
    fseek(fp, 0, SEEK_SET);

    uint8_t *bytes = malloc(filesize);
    fread(bytes, 1, filesize, fp);

    Elf64_Ehdr *elf_header = (Elf64_Ehdr *)bytes;
    Elf64_Shdr *sh_table = (Elf64_Shdr *)(bytes + elf_header->e_shoff);
    
    Elf64_Shdr shstrtab_hdr = sh_table[elf_header->e_shstrndx];
    uint64_t strings_offset = shstrtab_hdr.sh_offset;
    uint64_t strings_size   = shstrtab_hdr.sh_size;
    uint8_t *shstr_table = shstr_table = bytes + strings_offset;

    Elf64_Shdr *symtab_hdr = sh_table;
    Elf64_Shdr *strtab_hdr = sh_table;
    Elf64_Shdr *ptr = sh_table;
    for (int i = 0; i < elf_header->e_shnum; ++i) {
        if (ptr->sh_type == SHT_SYMTAB) {
            symtab_hdr = ptr;
        }
        if (ptr->sh_type == SHT_STRTAB) {
            strtab_hdr = ptr;
        }
        ++ptr;
    }
    int num_entries = symtab_hdr->sh_size / sizeof(Elf64_Sym);
    Elf64_Sym *symtab = (Elf64_Sym *)(bytes + symtab_hdr->sh_offset);
    Elf64_Sym *strtab = (Elf64_Sym *)(bytes + strtab_hdr->sh_offset);

    printf("File: %s\n", filename);
    if (strcmp(flag, "-h") == 0) {
        printHeader(filename, elf_header);
    }
    else if (strcmp(flag, "-sn") == 0) {
        printStringTable(shstr_table, strings_size);
    }
    else if (strcmp(flag, "-sec") == 0) {
        printf("There are %d section headers, starting at offset %lx\n", elf_header->e_shnum, elf_header->e_shoff);
        printSections(shstr_table, sh_table, elf_header->e_shentsize, elf_header->e_shnum);
    }
    else if (strcmp(flag, "-sym") == 0) {
        printSymtab(symtab, (uint8_t *)strtab, num_entries);
    }
    else {
        fprintf(stderr, "Unreckognized Option");
    }
    
    free(bytes);

}

