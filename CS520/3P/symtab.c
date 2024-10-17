#include <stdio.h>
#include <stdlib.h>
#include "symtab.h"
#include <string.h>

//
// This is the interface for a generic symbol table. A table stores
// (symbol, data) pairs.
//
// A symbol is simply a C string (null-terminated char sequence).
//
// The data associated with a symbol is simply a void*.
//

unsigned int hash_alg(const char *str);

typedef struct {
    int size;
    void **symtab;
} symtab_handle;


typedef struct  {
    char* symbol;
    void* data;
    void *next;
} symtab_entry;

typedef struct {
    int curr_index;
    void *table_handle;
    void *next;
} symtab_it;

static void* checked_malloc(size_t size, int clear) {
    // Allocate memory
    void* ptr = malloc(size);
    
    // Check if allocation failed
    if (ptr == NULL) {
        fprintf(stderr, "Memory allocation failed!\n");
        return NULL;
    }
    
    // If 'clear' is 1, set all memory to 0
    if (clear == 1) {
        memset(ptr, 0, size);
    }
    
    return ptr;  // Return the allocated memory
}

void *symtabCreate(int sizeHint) {
  // Creates a symbol table.
  // If successful, returns a handle for the new table.
  // If memory cannot be allocated for the table, returns NULL.
  // The parameter is a hint as to the expected number of (symbol, data)
  //   pairs to be stored in the table.

    
    if (sizeHint <= 0) {
        return NULL;
    }
    
    //allocate for handle
    symtab_handle *ret = (symtab_handle *)checked_malloc(sizeof(symtab_handle), 1);
    
    if (ret == NULL) {
        return ret;
    }

    ret->size = sizeHint;

    // allocate for table
    ret->symtab = (void**)checked_malloc(sizeof(void*) * sizeHint, 1);

    //set all table entries to NULL
    for (int i = 0; i < sizeHint; ++i) {
        ret->symtab[i] = NULL;
    }

    return ret;

}

void symtabDelete(void *symtabHandle){
  // Deletes a symbol table.
  // Reclaims all memory used by the table.
  // Note that the memory associate with data items is not reclaimed since
  //   the symbol table does not know the actual type of the data. It only
  //   manipulates pointers to the data.
  // Also note that no validation is made of the symbol table handle passed
  //   in. If not a valid handle, then the behavior is undefined (but
  //   probably bad).

    //cast void pointer to handle pointer
    symtab_handle *handle = (symtab_handle*)symtabHandle;

    for (int i = 0; i < handle->size; ++i) {
        //set entry to indexed symtab entry
        symtab_entry *entry = (symtab_entry *)handle->symtab[i];

        //loop until end of linked list
        while (entry != NULL) {
            //store pointer to next entry in list
            symtab_entry *next_entry = entry->next;

            //free the entry and its members
            free(entry->symbol);
            free(entry);
            
            //set entry pointer to next entry
            entry = next_entry;
        }
    }
    free(handle->symtab);
    free(handle);
}
int symtabInstall(void *symtabHandle, const char *symbol, void *data){
  // Install a (symbol, data) pair in the table.
  // If the symbol is already installed in the table, then the data is
  //   overwritten.
  // If the symbol is not already installed, then space is allocated and
  //   a copy is made of the symbol, and the (symbol, data) pair is then
  //   installed in the table.
  // If successful, returns 1.
  // If memory cannot be allocated for a new symbol, then returns 0.
  // Note that no validation is made of the symbol table handle passed
  //   in. If not a valid handle, then the behavior is undefined (but
  //   probably bad).

    symtab_handle *handle = (symtab_handle*)symtabHandle; 
    
    //get hashed index
    unsigned int index = hash_alg(symbol) % (handle->size);
    
    //set pointer to that index
    symtab_entry *entry = handle->symtab[index];

    if (entry == NULL) {
        //initilize a new Entry
        symtab_entry *new_entry = checked_malloc(sizeof(symtab_entry), 1);
        
        if (new_entry == NULL) {
            return 0;
        }

        new_entry->symbol = checked_malloc(strlen(symbol) + 1, 1);
        
        if (new_entry->symbol == NULL) {
            return 0;
        }

        strcpy(new_entry->symbol, symbol);
        new_entry->data = data;
        new_entry->next = NULL;

        //if the index is NULL set the table pointer to new entry
        handle->symtab[index] = new_entry;
        return 1;
    } else {
        //else follow next pointer until next is NULL
        while (entry->next != NULL) {

            //if symbol is already in table break and next if will handle
            if(strcmp(symbol, entry->symbol) == 0) {
                break;
            }
            entry = entry->next;
        }

        if(strcmp(symbol, entry->symbol) == 0) {
            entry->data = data;
            return 1;
        }
        //malloc new entry
        symtab_entry *new_entry = checked_malloc(sizeof(symtab_entry), 1);
        if (new_entry == NULL) {
            return 0;
        }

        //malloc symbol
        new_entry->symbol = checked_malloc(strlen(symbol) + 1, 1);
        if (new_entry->symbol == NULL) {
            return 0;
        }
        //initilize new entry
        strcpy(new_entry->symbol, symbol);
        new_entry->data = data;
        new_entry->next = NULL;
        //set the next pointer of the previous entry to new entry
        entry->next = new_entry;
        return 1;
    }
    
}

void *symtabLookup(void *symtabHandle, const char *symbol){
  // Return the data item stored with the given symbol.
  // If the symbol is found, return the associated data item.
  // If the symbol is not found, returns NULL.
  // Note that no validation is made of the symbol table handle passed
  //   in. If not a valid handle, then the behavior is undefined (but
  //   probably bad).
    
    symtab_handle *handle = (symtab_handle*)symtabHandle; 
    
    //get hashed index
    unsigned int index = hash_alg(symbol) % (handle->size);
    
    //set pointer to that index
    symtab_entry *entry = handle->symtab[index];

    while(((entry != NULL) && (strcmp(symbol, entry->symbol) != 0))) {
        
        entry = entry->next;
    }
   
    if (entry == NULL) {
        return NULL;
    } 
    return entry->data;
}

void *symtabCreateIterator(void *symtabHandle){
  // Create an iterator for the contents of the symbol table.
  // If successful, a handle to the iterator is returned which can be
  // repeatedly passed to symtabNext to iterate over the contents
  // of the table.
  // If memory cannot be allocated for the iterator, returns NULL.
  // Note that no validation is made of the symbol table handle passed
  //   in. If not a valid handle, then the behavior is undefined (but
  //   probably bad).
    symtab_handle *handle = (symtab_handle*)symtabHandle; 
    symtab_it *ret = checked_malloc(sizeof(symtab_it), 0);
    int index = 0;
    symtab_entry *entry = handle->symtab[index];
    while (entry == NULL) {
        ++index;
        entry = handle->symtab[index];
    }
    ret->next = entry;
    ret->curr_index = index;
    ret->table_handle = handle;
    return ret;
}

const char *symtabNext(void *iteratorHandle, void **returnData){
  // Returns the next (symbol, data) pair for the iterator.
  // The symbol is returned as the return value and the data item
  // is placed in the location indicated by the second parameter.
  // If the whole table has already been traversed then NULL is
  //   returned and the location indicated by the second paramter
  //   is not modified.
  // Note that no validation is made of the iterator table handle passed
  //   in. If not a valid handle, then the behavior is undefined (but
  //   probably bad).
  // Also note that if there has been a symbtabInstall call since the
  //   iterator was created, the behavior is undefined (but probably
  //   benign).
    
    symtab_it *it = (symtab_it*)iteratorHandle;
    symtab_handle* handle = (symtab_handle*) it->table_handle;
    
    if (it->curr_index >= handle->size) {
        return NULL;
    }
    //cast next pointer to an entry pointer
    symtab_entry *entry = (symtab_entry*)it->next;

    //store return values
    char* ret = entry->symbol;
    *returnData = entry->data;

    //increment iterator
    //if entry has next, set iterator next to next in list
    if (entry->next != NULL) {
        it->next = entry->next;
        return ret;
    } else {
    //else traverse table untill non NULL index found
        int index = it->curr_index + 1;
        entry = handle->symtab[index];
        while (entry == NULL) {
            ++index;

            if (index >= handle->size) {
                it->next = NULL;
                it->curr_index = index;
                return ret;
            }

            entry = handle->symtab[index];
        }
        it->next = entry;
        it->curr_index = index;
        return ret;
    }
    
}

void symtabDeleteIterator(void *iteratorHandle){
  // Delete the iterator indicated by the only parameter.
  // Reclaims all memory used by the iterator.
  // Note that no validation is made of the iterator table handle passed
  //   in. If not a valid handle, then the behavior is undefined (but
  //   probably bad).

    free(iteratorHandle);
}

// modified FNV hash (see
// http://en.wikipedia.org/wiki/Fowler_Noll_Vo_hash)

unsigned int hash_alg(const char *str){

    const unsigned int p = 16777619;
    unsigned int hash = 2166136261u;
    while (*str){
        hash = (hash ^ *str) * p;
        str += 1;
    }

    hash += hash << 13;
    hash ^= hash >> 7;
    hash += hash << 3;
    hash ^= hash >> 17;
    hash += hash << 5;

    return hash;

}