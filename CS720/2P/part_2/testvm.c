#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include "vmx20.h"

//Stack/Main Memory size in 4 byte words, total of 50MB
#define STACKSIZE 12500
#define MM_SIZE 50000
#define NUM_THREADS 1
#define TRACE 1

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

int main (int argc, char *argv[]) {

    if (argc < 2) {
        fprintf(stderr, "Please provide the name of an executable file\n");
        return -1;
    }
    
    int32_t error_number = 0;
    void *vm_handle = initVm(&error_number);
    
    if(error_number) {
        fprintf(stderr, "failed to init VM (errno: %d)\n", error_number);
    }

    loadExecutableFile(vm_handle, argv[1], &error_number);
    if(error_number) {
        fprintf(stderr, "failed to load executable file (errno: %d)\n", error_number);
    }
    char *ret = NULL;
    for(int i = 2; i < argc; ++i) {
        char *arg = argv[i];
        char *param = strrchr(arg, '=');
        if(param == NULL) {
            if (ret == NULL) {
                ret = arg;
            } else {
                fprintf(stderr, "Multiple return labels not supported\n");
                return -1;
            }
        } else {
            uint32_t arg_addr = 0;
            int arg_len = strlen(arg) - strlen(param);
            char label[arg_len + 1];
            strncpy(label, arg, arg_len);
            label[arg_len] = '\0';
            int val = atoi(param + 1);
    
            if (!getAddress(vm_handle, label, &arg_addr)) {
                fprintf(stderr, "Provided argument label not in executable\n");
                return -1;
            }
            
            putWord(vm_handle, arg_addr, val);
            int test = 0;
            getWord(vm_handle, arg_addr, &test);
            
        }
    }
    uint32_t *init_sp = checked_malloc(sizeof(uint32_t) * NUM_THREADS, 1);
    for (int i = 1; i <= NUM_THREADS; ++i) {
        init_sp[i - 1] = MM_SIZE + (STACKSIZE * i);
        printf("INIT SP FOR THREAD %d: %d\n", i - 1, init_sp[i - 1]);
    }

    int ret_stat = 0;
    int *termStat = checked_malloc(sizeof(int) * NUM_THREADS, 1);
    
    execute(vm_handle, NUM_THREADS, init_sp, termStat, TRACE);
    for (int i = 0; i < NUM_THREADS; ++i) {
        if (termStat[i] != 0) {
        fprintf(stderr, "THREAD %d: Execution terminated with error code %x\n", i, termStat[i]);
        ret_stat = -1;
        
    }
    }
    

    uint32_t result_addr = -1;
    printf("return label: %s\n", ret);
    if (ret != NULL){
        
        if (!getAddress(vm_handle, ret, &result_addr)){
            fprintf(stderr, "Requested return label not in executable\n");
            ret_stat = -1;
            goto cleanup;  
        }

        int32_t result = 0; 
        getWord(vm_handle, result_addr, &result);
        printf("%s : %d\n", ret, result);
    }

    cleanup:
        free(init_sp);
        free(termStat);
        return ret_stat;
}   