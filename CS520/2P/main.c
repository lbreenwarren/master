#include <stdio.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include "symtab.h"

//Test Driver for Symbol Table interface

int main()
{

    //try to create a symtab of size 0
    void *symtab = symtabCreate(0);
    if (symtab != NULL) {
        fprintf(stderr, "Erroneously created a symtab of size 0\n");
        symtabDelete(symtab);
        return -1;
    }

    //create a tables of size 1 through 500
    for (int i = 1; i < 500; ++i) {
        symtab = symtabCreate(i);
        if (symtab == NULL) {
            fprintf(stderr, "Failed to create symtab of size %d\n", i);
            return -1;
        }
        symtabDelete(symtab);
    }

    //create a table of size 50 and try to install 50 symbol data pairs
    symtab = symtabCreate(50);
    int *data = malloc(sizeof(int) * 50);
    for (int i = 0; i < 50; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        data[i] = i;
        if (symtabInstall(symtab, str, &data[i]) == 0) {
            fprintf(stderr, "Failed to install data pair \"%d\":%d.(main.c:36)\n", i, i);
            return -1;
        }
    }
    //look up each symbol
    for (int i = 0; i < 50; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        int *ret_data = symtabLookup(symtab, str);
        if (ret_data == NULL) {
            fprintf(stderr, "Failed to lookup data pair \"%s\":%d.(main.c:46)\n", str, i);
            return -1;
        }
        if (*ret_data != i) {
            fprintf(stderr, "Data associated to symbol \"%d\" inproperly set to %d (should be %d).(main.c:46)\n", i,*data, i);
            return -1;
        }
    }

    //try to install same data for each symbol
    for (int i = 0; i < 50; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        if (symtabInstall(symtab, str, &data[i]) == 0) {
            fprintf(stderr, "Failed to install data pair \"%d\":%d.(main.c:61)\n", i, i);
            return -1;
        }
    }
    
    free(data);
    data = malloc(sizeof(int) * 50);
    
    //install new data at each symbol
    for (int i = 0; i < 50; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        data[i] = i + 1;
        if (symtabInstall(symtab, str, &data[i]) == 0) {
            fprintf(stderr, "Failed to install data pair \"%d\":%d.(main.c:64)\n", i, i);
            return -1;
        }
    }

    //do a lookup for each symbol and make sure it was changed
    for (int i = 0; i < 50; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        
        int *ret_data = symtabLookup(symtab, str);
        if (ret_data == NULL) {
            fprintf(stderr, "Failed to lookup data pair \"%d\":%d.(main.c:74)\n", i, i);
            return -1;
        }
        if (*ret_data != i + 1) {
            fprintf(stderr, "Data associated to symbol \"%d\" inproperly set to %d (should be %d).(main.c:78)\n", i,*data, i+1);
            return -1;
        }
    }
    symtabDelete(symtab);

    free(data);
    
    
    //repeat above tests for symtab with size 50 and 100 symbols to test side chaining
    symtab = symtabCreate(50);
    data = malloc(sizeof(int) * 100);
    for (int i = 0; i < 100; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        data[i] = i;
        if (symtabInstall(symtab, str, &data[i]) == 0) {
            fprintf(stderr, "Failed to install data pair \"%d\":%d.(main.c:35)\n", i, i);
            return -1;
        }
    }
    //look up each symbol
    for (int i = 0; i < 100; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        int *data = 0;
        data = symtabLookup(symtab, str);
        if (data == NULL) {
            fprintf(stderr, "Failed to lookup data pair \"%s\":%d.(main.c:117)\n", str, i);
            return -1;
        }
        if (*data != i) {
            fprintf(stderr, "Data associated to symbol \"%d\" inproperly set to %d (should be %d).(main.c:45)\n", i,*data, i);
            return -1;
        }
    }

    //try to install same data for each symbol
    for (int i = 0; i < 100; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        if (symtabInstall(symtab, str, &data[i]) == 0) {
            fprintf(stderr, "Failed to install data pair \"%d\":%d.(main.c:55)\n", i, i);
            return -1;
        }
    }
    
    free(data);
    data = malloc(sizeof(int) * 100);
    
    //install new data at each symbol
    for (int i = 0; i < 100; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        data[i] = i + 1;
        if (symtabInstall(symtab, str, &data[i]) == 0) {
            fprintf(stderr, "Failed to install data pair \"%d\":%d.(main.c:64)\n", i, i);
            return -1;
        }
    }

    //do a lookup for each symbol and make sure it was changed
    for (int i = 0; i < 100; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        
        int *ret_data = symtabLookup(symtab, str);
        if (ret_data == NULL) {
            fprintf(stderr, "Failed to lookup data pair \"%d\":%d.(main.c:74)\n", i, i);
            return -1;
        }
        if (*ret_data != i + 1) {
            fprintf(stderr, "Data associated to symbol \"%d\" inproperly set to %d (should be %d).(main.c:78)\n", i,*data, i+1);
            return -1;
        }
    }
    symtabDelete(symtab);
    free(data);

    //create a symtab of size 1 and install 50 symbols (should just be a linked list)
    symtab = symtabCreate(1);
    data = malloc(sizeof(int) * 50);
    for (int i = 0; i < 50; ++i) {
        char str[3];
        snprintf(str, sizeof(str), "%d", i);
        data[i] = i;
        if (symtabInstall(symtab, str, &data[i]) == 0) {
            fprintf(stderr, "Failed to install data pair \"%d\":%d.(main.c:138)\n", i, i);
            return -1;
        }
    }
    free(data);
    symtabDelete(symtab);


}
