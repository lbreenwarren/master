#include <stdio.h>
#include <stdlib.h>
#include <inttypes.h>
#include <string.h>

struct symbol_sec {
    int num_syms;
    struct symbol ** syms;
};

struct symbol {
    uint32_t addr;
    char* name;
};

struct obj_file {
    uint32_t *obj_code;
    struct symbol_sec in_syms;
    struct symbol_sec out_syms;
};

//debug func
void dump_bytes(int size, uint8_t *data) {
    for (int i = 0; i < size; ++i) {
        if (i % 16 == 0) {
            printf("\n\t");
        }
        printf("%x ", data[i]);
    }
    printf("\n");
}

void get_syms(struct symbol_sec * sym_sec, int num_syms, int sec_off, uint32_t *bin) {
    uint8_t *data = (uint8_t *)&bin[sec_off];
    
    for(int i = 0; i < num_syms; ++i) {
        sym_sec->syms[i] = malloc(sizeof(struct symbol));
       
        uint32_t addr = 0;
        for (int k = 0; k < 4; ++k) {
            addr |= (data[16 + k]) << (8 * k);
        }
       
        sym_sec->syms[i]->addr = addr;
        sym_sec->syms[i]->name = malloc(sizeof(char) * 17);
        strcpy(sym_sec->syms[i]->name, (char *)&data[0]);
        data += 20;
    }
}

int main(int argc, char* argv[]) {
    if (argc < 4) {
        fprintf(stderr, "Please provide the name of an object file along with the desired executable name\n");
        return -1;
    }
    
    int num_files = argc - 3;
    uint32_t *bins[num_files];
    char * out_name = malloc(sizeof(char) * 20);
    for (int i=1; i <= num_files + 2; ++i) {
        
        if (strcmp(argv[i], "-o") == 0) {
            strcpy(out_name, argv[i + 1]);
            break;
        }
        FILE *fp = fopen(argv[i], "rb");
        if (fp == NULL) {
            fprintf(stderr, "File '%s' not found.", argv[i]);
        }
        
        fseek(fp, 0, SEEK_END);
        int filesize = ftell(fp);
        fseek(fp, 0, SEEK_SET);
        uint32_t *data = malloc(filesize);
        fread(data, 1, filesize, fp);
        fclose(fp);
        
        bins[i - 1] = data;

    }

    if (strcmp(out_name, "") == 0) {
        fprintf(stderr, "Please provide a desired executable name with the '-o' flag\n");
        return -1;
    }

    struct obj_file objs[num_files];
    char* file_type = ".exe";
    int main_found = 0; //boolean
    int tot_obj_size = 0;
    int tot_in_sym = 0; 
    int tot_out_sym = 0;

    for (int i = 0; i < num_files; ++i) {
        
        uint32_t *data = bins[i];
        tot_obj_size += data[2];
        uint32_t num_in_syms = data[0] / 5;
        uint32_t num_out_syms = data[1] / 5;
        tot_in_sym += num_in_syms;
        tot_out_sym += num_out_syms;
        objs[i].in_syms.num_syms = num_in_syms;
        objs[i].out_syms.num_syms = num_out_syms;
        objs[i].in_syms.syms = malloc(sizeof(struct symbol) * num_in_syms);
        objs[i].out_syms.syms = malloc(sizeof(struct symbol) * num_out_syms);
        objs[i].obj_code = malloc(data[2] * 4);
        int obj_off = 3 + (num_in_syms * 5) + (num_out_syms * 5);
        objs[i].obj_code = &data[obj_off];
        get_syms(&objs[i].in_syms, num_in_syms, 3, bins[i]);
        
        for (int k = 0; k < num_in_syms; ++k) {
            if (strcmp(objs[i].in_syms.syms[k]->name, "mainx20")) {
                main_found = 1;
            }
        }
        get_syms(&objs[i].out_syms, num_out_syms, 3 + (num_in_syms * 5), bins[i]);
    
    }
    
    uint32_t* linked_obj = malloc(tot_obj_size * sizeof(uint32_t));

    struct symbol_sec *final_in_syms = malloc(sizeof(struct symbol_sec));
    final_in_syms->syms = malloc(sizeof(struct symbol) * tot_in_sym);
    final_in_syms->num_syms = 0;

    struct symbol_sec *final_out_syms = malloc(sizeof(struct symbol_sec));
    final_out_syms->syms = malloc(sizeof(struct symbol) * tot_out_sym);
    final_out_syms->num_syms = tot_out_sym;

    if (!main_found) {
        fprintf(stderr, "No mainx20 label found in provided object files. Creating resulting object file\n");
        file_type = ".obj";
    }
    
    int offset = 0;
    int in_sym_ind = 0;
    int out_sym_ind = 0;

    struct symbol_sec *unresolved = malloc(sizeof(struct symbol_sec));
    unresolved->syms = malloc(sizeof(struct symbol) * tot_out_sym);
    unresolved->num_syms = 0;
    for(int i = 0; i < num_files; ++i) {
        for (int k = 0; k < bins[i][2]; ++k) {
            linked_obj[offset + k] = objs[i].obj_code[k];
        }
        for(int k = 0; k < objs[i].in_syms.num_syms; ++k) {
            for (int j = 0; j < final_in_syms->num_syms; ++j) {
                if (strcmp(final_in_syms->syms[j]->name, objs[i].in_syms.syms[k]->name) == 0) {
                    fprintf(stderr, "Duplicate in-symbols found\n");
                    goto cleanup;
                }
            }
            final_in_syms->syms[in_sym_ind] = objs[i].in_syms.syms[k];
            final_in_syms->syms[in_sym_ind]->addr += offset;
            ++in_sym_ind;
            ++final_in_syms->num_syms;

        }

        for(int k = 0; k < objs[i].out_syms.num_syms; ++k) {
            final_out_syms->syms[out_sym_ind] = objs[i].out_syms.syms[k];
            final_out_syms->syms[out_sym_ind]->addr += offset;
            ++out_sym_ind;
        }

        offset += bins[i][2];
    }
    
    for (int i = 0; i < final_out_syms->num_syms; ++i) {
        struct symbol* out_sym = final_out_syms->syms[i];
        int found_sym = 0; //boolean
        for (int k = 0; k < final_in_syms->num_syms; ++k) {
            if (strcmp(final_in_syms->syms[k]->name, out_sym->name) == 0) {
                int pc = out_sym->addr + 1; 
                int addr = final_in_syms->syms[k]->addr;
                linked_obj[out_sym->addr] |= (((addr - pc) & 0xfffff) << 12);
                found_sym = 1;
            }
        }
        if (!found_sym) {
            unresolved->syms[unresolved->num_syms] = out_sym;
            ++unresolved->num_syms;
        }
    }

    if (unresolved->num_syms > 0) {
        fprintf(stderr, "Cannot resolve all outsymbols. Creating resulting object file\n");
        file_type = ".obj";
    }
    
    int header_size = 3 + (((final_in_syms->num_syms + unresolved->num_syms) * 5));
    uint32_t *header = malloc((header_size * sizeof(uint32_t)));
    
    header[0] = final_in_syms->num_syms * 5;
    header[1] = unresolved->num_syms * 5;
    header[2] = tot_obj_size;
    
    uint8_t *byt_p = (uint8_t *)&header[0];
    
    offset = 3;
    for(int i = 0; i < final_in_syms->num_syms; ++i) {
        strcpy((char *)&byt_p[offset * 4], final_in_syms->syms[i]->name);
        offset += 4;
        header[offset] = final_in_syms->syms[i]->addr;
        offset += 1;
    }
    for (int i = 0; i < unresolved->num_syms; ++i) {
        strcpy((char *)&byt_p[offset * 4], unresolved->syms[i]->name);
        offset += 4;
        header[offset] = unresolved->syms[i]->addr;
        offset += 1;
    }

    strcat(out_name, file_type);
    FILE *out = fopen(out_name, "ab");

   
    fwrite(header, 4, header_size, out);
    fwrite(linked_obj, 4, tot_obj_size, out);
    

    goto cleanup;

    cleanup:
        for(int i = 0; i < num_files; ++i) {
            
            struct symbol_sec *in_sec = &objs[i].in_syms;
            for(int k = 0; k < in_sec->num_syms; ++k) {
                free(in_sec->syms[k]->name);
            }
            free(in_sec->syms);
            struct symbol_sec *out_sec = &objs[i].out_syms;
            for(int k = 0; k < out_sec->num_syms; ++k) {
                free(out_sec->syms[k]->name);
            }
            free(out_sec->syms);

            free(bins[i]);
        }
        free(final_in_syms);
        free(final_out_syms);
        free(unresolved);
        free(linked_obj);

}