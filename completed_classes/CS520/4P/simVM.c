//
// A virtual memory simulation.
//
// Virtual memory is a sequence of 32-bit words.
//
// Physical memory is a shorter sequence of 32-bit words.
//
// Virtual memory addresses are 32 bits.
//
// Virtual and physical memory is managed in fixed-size pages.
//
// A page table, which is logically in physical memory, maps virtual
// memory addresses to physical memory addresses. Since physical
// memory is smaller than virtual memory, the page table might indicate
// that a particular page is not currently present in physical memory,
// and that it is logically stored on disk.
//
// However this is a simulation, so all memory words are actually stored
// in the memory of this program, and disk is not used. Similarly the page
// table does not actually exist in the simulated physical memory, but
// rather exists only in the data structures of the simulation.
//
// A translation lookaside buffer (TLB) is simulated. The TLB stores
// recent virtual-to-physical address translations.
//
// The following properties of the virtual memory are set when it is
// created:
//   1. size of the virtual memory in pages
//   2. size of the physical memory in pages
//   3. size of a page in words
//   4. number of TLB entries
//   5. page replacement algorithm
//   6. TLB replacement algorithm
//
// The size of the virtual memory must be larger than the size of the
// physical memory.
//
// The size of a page must be a power of two.
//
// The size of the virtual memory times the size of a page must be less
// than or equal to 2^32.
//
// The size of the TLB should be less than or equal to the size of physical
// memory.
//
// The replacement algorithms are either 0 for round-robin replacement or
// 1 for LRU replacement.
//
// A virtual memory system is initialized to have the first K pages
// of virtual memory loaded into physical memory, where K is the
// number of pages in the physical memory. Initially the virtual
// memory system contains arbitrary values (i.e. not necessarily
// zeros).
//
// The TLB is initialized to have the VM to PM mapping for the
// first N pages loaded into physical pages (i.e. starting at physical
// page 0), where N is the number of TLB entries.
//
// The goal of the simulation is to report the number of page misses,
// the number of TLB misses, and the number of disk writes.
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define VM_ROUNDROBIN_REPLACEMENT 0
#define VM_LRU_REPLACEMENT 1

#define TRACE 0
#define DEBUG 1
#define CURR_DEBUG 0

//Stucture for a Page Table Entry
typedef struct {
    int ppn;
    int vpn;
    unsigned int page_time;
    unsigned int tlb_time;
    int dirty_bit;
} page_table_entry;

//array to represent physical and virtual memory
void **memory;

//array of pointers to pages which are in physical memory
page_table_entry** p_memory;

//array of pointers to pages which are in TLB
page_table_entry** tlb;

//read/write count
int curr_time;

//sizes
unsigned int vm_size;
unsigned int pm_size;
unsigned int page_size;
unsigned int tlb_size;

//replacement algorithms
int tlb_replace_alg;
int pm_replace_alg;

int rr_pm_next_replace;
int rr_tlb_next_replace;

//statistics
int page_faults;
int disk_writes;
int num_tlb_hits;
int num_tlb_miss;

static void dump_tlb() {
    printf("\n========== CURRENT TLB ============\n");
    for (int i = 0; i < tlb_size; ++i) {
        
        printf("| %d ", tlb[i]->ppn);
    }
    printf("|\n");
}

static void dump_memory() {
    for(int i = 0; i < vm_size * page_size; ++i) {
        if (i % page_size == 0) {
            printf("\n");
        }

        int* val = (int*)memory[i];
        printf("/%x/ ", *val);
        
    }
    printf("\n");
}

static void dump_p_memory() {
    printf("Pages in PM:\n\t");
    for(int i = 0; i < pm_size; ++i) {
        printf("/%d", p_memory[i]->vpn);
        
    }
    printf("/\n");
}

static int check_params(
  unsigned int sizeVM,   // size of the virtual memory in pages
  unsigned int sizePM,   // size of the physical memory in pages
  unsigned int pageSize, // size of a page in words
  unsigned int sizeTLB,  // number of translation lookaside buffer entries
  char pageReplAlg,      // page replacement alg.: 0 is Round Robin, 1 is LRU
  char tlbReplAlg        // TLB replacement alg.: 0 is Round Robin, 1 is LRU
  ) {
    
    //the size of the virtual memory must be larger than the size of the physical memory. 
    if (sizeVM <= sizePM) {
        if (DEBUG) {
            fprintf(stderr, "VM size (%d) < PM size (%d)\n", sizeVM, sizePM);
        }
        return 0;
    }

    //the size of the physical memory must be greater than zero. 
    if (sizePM <= 0) {
        if (DEBUG) {
            fprintf(stderr, "PM size (%d) < 0\n", sizePM);
        }
        return 0;
    }

    //the size of a page must be a power of two.
    int factor = pageSize / 2;
    while (factor != 2) {

        if (factor % 2 == 1) {
            if (DEBUG) {
                fprintf(stderr, "Page Size (%d) not a power of 2\n", page_size);
            }
            return 0;
        }
        
        factor = factor / 2;
    }

    unsigned int max = 0;
    max = ~max;
    //the size of the virtual memory times the size of a page must be less than or equal to 2^32. 
    if (sizeVM > max) {
        if (DEBUG) {
            fprintf(stderr, "VM size (%d) > 2^32\n", sizeVM);
        }
        return 0;
    }

    return 1;
}


static void *read(void *handle, unsigned int address) {
    
    if (TRACE) {
        printf("Reading value at address %d in Virtual Page Number %d\n", address, address/page_size);
    }
    
    page_table_entry **page_table = (page_table_entry **)handle;
    int page_num = address / page_size;

    if (page_table[page_num]->ppn >= 0) {   //entry is in physical memory
        
        if (pm_replace_alg) {
            page_table[page_num]->page_time = curr_time;
        }
        
        //check if entry is in TLB
        int cached = 0;
        
        for (int i = 0; i < tlb_size; ++i) {
            if (page_table[page_num]->ppn == tlb[i]->ppn) {
                cached = 1;
            }
        }

        if (cached == 0) {
            //TLB miss, need to replace
            if (tlb_replace_alg) {
                int oldest_time = curr_time;
                int replace_index = -1;
            
                for(int i = 0; i < tlb_size; ++i) {
                    if (tlb[i]->tlb_time < oldest_time) {
                        replace_index = i;
                        oldest_time = tlb[i]->tlb_time;
                    }
                }

                tlb[replace_index] = page_table[page_num];
                tlb[replace_index]->tlb_time = curr_time;
            
            } else {
                tlb[rr_tlb_next_replace] = page_table[page_num];
                rr_tlb_next_replace = (rr_tlb_next_replace + 1) % tlb_size;
            }
            ++num_tlb_miss;
        } else {
            //TLB Hit
            if (TRACE) {
                printf("(TLB Hit on adress %d in page %d)", address, page_table[page_num]->ppn);
                dump_tlb();
            }
            if (tlb_replace_alg) {
                page_table[page_num]->tlb_time = curr_time;
            }
            if (pm_replace_alg) {
                page_table[page_num]->page_time = curr_time;
            }
            
            ++num_tlb_hits;
        }
        ++curr_time;
        return memory[address];
        
        
    } else {    //entry is not in physical memory
        if (TRACE) {
            printf("value at address %d in Virtual Page Number %d not in physical memory", address, page_num);
        }

        ++page_faults;
        page_table_entry* replace;
        int replace_index = -1;
        if (pm_replace_alg) {  // entry to replace
            int oldest_time = curr_time;
            for(int i = 0; i < pm_size; ++i) {
                if (p_memory[i]->page_time < oldest_time) {
                    replace_index = i;
                    oldest_time = p_memory[i]->page_time;
                }
            } 
            replace = p_memory[replace_index];
            if (TRACE) {
                printf("Replacing least recently updated page in PM \n\tcurrent time:%d \n\toldest entry time:%d\n", curr_time, oldest_time);
            }
        } else {
            replace = p_memory[rr_pm_next_replace];    
            replace_index = rr_pm_next_replace;
        }

        //check if its in TLB
        int tlb_index = -1;

        for (int i = 0; i < tlb_size; ++i) {
            if (replace->ppn == tlb[i]->ppn) {
                tlb_index = i;
            }
        }

        if (replace->dirty_bit) {
            ++disk_writes;
            replace->dirty_bit = 0;
        }
        
        //Move to physical memory
        if (pm_replace_alg) {
            p_memory[replace_index] = page_table[page_num];
            p_memory[replace_index]->page_time = curr_time;
            p_memory[replace_index]->ppn = replace->ppn;
            replace->page_time = -1;
            replace->ppn = -1;
        } else {
            replace->ppn = -1;
            page_table[page_num]->ppn = rr_pm_next_replace;
            p_memory[rr_pm_next_replace] = page_table[page_num];
            ++rr_pm_next_replace;
            rr_pm_next_replace = rr_pm_next_replace % pm_size;
        }

        //insert in TLB
        if (tlb_index >= 0) {
            tlb[tlb_index] = page_table[page_num];
            if (tlb_replace_alg) {
                tlb[tlb_index]->tlb_time = curr_time;
            }
        } else {
            if (tlb_replace_alg) {
                int oldest_time = curr_time;
                int replace_index = -1;
                for(int i = 0; i < tlb_size; ++i) {
                    if (tlb[i]->tlb_time < oldest_time) {
                        replace_index = i;
                        oldest_time = tlb[i]->tlb_time;
                    }
                }
                tlb[replace_index]->tlb_time = -1;
                tlb[replace_index] = page_table[page_num];
                tlb[replace_index]->tlb_time = curr_time;
            } else {
                tlb[rr_tlb_next_replace] = page_table[page_num];
                rr_tlb_next_replace = (rr_tlb_next_replace + 1) % tlb_size;
            }
        }
        ++num_tlb_miss;
        ++curr_time;
        return memory[address];
        
    }
}

static void write(void *handle, unsigned int address, void* value) {
    
    if (TRACE) {
        printf("Writing %d to address %d in Virtual Page Number %d\n", *(int*)value, address, address/page_size);
    }
    
    page_table_entry **page_table = (page_table_entry **)handle;
    int page_num = address / page_size;

    if (page_table[page_num]->ppn >= 0) {   //entry is in physical memory

        if (pm_replace_alg) {
            page_table[page_num]->page_time = curr_time;
        }

        //check if entry is in TLB
        int cached = 0;
        
        for (int i = 0; i < tlb_size; ++i) {
            if (page_table[page_num]->ppn == tlb[i]->ppn) {
                cached = 1;
            }
        }

        if (cached == 0) {
            //TLB miss, need to replace
            if (tlb_replace_alg) {
                int oldest_time = curr_time;
                int replace_index = -1;
                
                for(int i = 0; i < tlb_size; ++i) {
                    if (tlb[i]->tlb_time < oldest_time) {
                        replace_index = i;
                        oldest_time = tlb[i]->tlb_time;
                    }
                }
                
                tlb[replace_index] = page_table[page_num];
                tlb[replace_index]->tlb_time = curr_time;

            } else {
                tlb[rr_tlb_next_replace] = page_table[page_num];
                rr_tlb_next_replace = (rr_tlb_next_replace + 1) % tlb_size;
            }
            ++num_tlb_miss;
        } else {
            //TLB Hit
            if (TRACE) {
                printf("(TLB Hit on adress %d in page %d)", address, page_table[page_num]->ppn);
                dump_tlb();
            }
            if (tlb_replace_alg) {
                page_table[page_num]->tlb_time = curr_time;
            }
            ++num_tlb_hits;
        }

        memcpy(memory[address], value, 32);
        page_table[page_num]->dirty_bit = 1; //physical memory has diverged from disc, set dirty bit
        ++curr_time;
        
    } else {    //entry is not in physical memory
        if (TRACE) {
            printf("value at address %d in Virtual Page Number %d not in physical memory\n", address, page_num);
        }
        ++page_faults;
        page_table_entry* replace; //entry to replace
        int replace_index = -1;
        if (pm_replace_alg) { 
            int oldest_time = curr_time;
            
            for(int i = 0; i < pm_size; ++i) {
                if (p_memory[i]->page_time < oldest_time) {
                    replace_index = i;
                    oldest_time = p_memory[i]->page_time;
                }
            } 
            if (TRACE) {
                
                printf("Replacing least recently updated page in PM \n\tpage num:%d \n\tcurrent time:%d \n\toldest entry time:%d\n", replace_index, curr_time, oldest_time);
                dump_p_memory();
            }
            replace = p_memory[replace_index];
        } else {
            replace = p_memory[rr_pm_next_replace]; // entry to replace    
        }   
        
        //check if its in TLB
        int tlb_index = -1;
        
        for (int i = 0; i < tlb_size; ++i) {
            if (replace->ppn == tlb[i]->ppn) {
                tlb_index = i;
            }
        }

        if (replace->dirty_bit) {
            ++disk_writes;
            replace->dirty_bit = 0;
        }

        //Move to physical memory
        if (pm_replace_alg) {
            p_memory[replace_index] = page_table[page_num];
            p_memory[replace_index]->page_time = curr_time;
            p_memory[replace_index]->ppn = replace->ppn;
            replace->page_time = -1;
            replace->ppn = -1;
           
        } else {
            replace->ppn = -1;
            page_table[page_num]->ppn = rr_pm_next_replace;
            p_memory[rr_pm_next_replace] = page_table[page_num];
            ++rr_pm_next_replace;
            rr_pm_next_replace = rr_pm_next_replace % pm_size;
        }
        
        //insert in TLB
        if (tlb_index >= 0) {
            tlb[tlb_index] = page_table[page_num];
            if (tlb_replace_alg) {
                tlb[tlb_index]->tlb_time = curr_time;
            }
        } else {
            if (tlb_replace_alg) {
                int oldest_time = curr_time;
                int replace_index = -1;
                for(int i = 0; i < tlb_size; ++i) {
                    if (tlb[i]->tlb_time < oldest_time) {
                        replace_index = i;
                        oldest_time = tlb[i]->tlb_time;
                    }
                }
                tlb[replace_index]->tlb_time = -1;
                tlb[replace_index] = page_table[page_num];
                tlb[replace_index]->tlb_time = curr_time;
            } else {
                tlb[rr_tlb_next_replace] = page_table[page_num];
                rr_tlb_next_replace = (rr_tlb_next_replace + 1) % tlb_size;
            }
        }

        ++num_tlb_miss;
        ++curr_time;
        memcpy(memory[address], value, 32);
        page_table[page_num]->dirty_bit = 1;
    }
}  

// cleanupVM
//
// Cleanup the memory used by the simulation of the virtual memory system.
//
// If the handle is not one returned by createVM, the behavior is
// undefined.
//
void cleanupVM(void *handle) {
    page_table_entry **page_table = (page_table_entry **)handle;
    for (int i = 0; i < vm_size; ++i) {
        free(page_table[i]);
    }
    for(int i = 0; i < vm_size * page_size; ++i) {
        free(memory[i]);
    }
    free(memory);
    free(tlb);
    free(p_memory);
    free(page_table);
}

static void check_address(int address, void* handle) {
    if (address > (vm_size * page_size) || address < 0) {
        fprintf(stderr, "Address %d out of range", address);
        cleanupVM(handle);
        exit(1);
    }
}

// createVM
//
// Create the virtual memory system and return a "handle" for it.
//
// If there is a violation of any of the constraints on the properties
// of the virtual memory system, this function will return NULL.
//
// If this function fails for any other reason (e.g. malloc returns NULL),
// an error message will be printed to stderr and the program will be
// terminated.
//
void *createVM(
  unsigned int sizeVM,   // size of the virtual memory in pages
  unsigned int sizePM,   // size of the physical memory in pages
  unsigned int pageSize, // size of a page in words
  unsigned int sizeTLB,  // number of translation lookaside buffer entries
  char pageReplAlg,      // page replacement alg.: 0 is Round Robin, 1 is LRU
  char tlbReplAlg        // TLB replacement alg.: 0 is Round Robin, 1 is LRU
) {

    if (!check_params(sizeVM, sizePM, pageSize, sizeTLB, pageReplAlg, tlbReplAlg)) {
        if (DEBUG) {
            fprintf(stderr, "Invalid Params\n");
        }
        return NULL;
    }

    if (DEBUG) {
        printf("Creating page table with:\n\tVM Size: %d\n\tPM Size: %d\n\tPage Size: %d\n\tTLB Size: %d\nUsing Algorithms: \n\tPage Table ALG:%d\n\tTLB ALG: %d\n", sizeVM, sizePM, pageSize, sizeTLB, pageReplAlg, tlbReplAlg);
    }

    vm_size = sizeVM;
    pm_size = sizePM;
    page_size = pageSize;
    tlb_size = sizeTLB;
    rr_pm_next_replace = 0;
    rr_tlb_next_replace = 0;
    tlb_replace_alg = tlbReplAlg;
    pm_replace_alg = pageReplAlg;
    
    

    page_table_entry **page_table = malloc(sizeof(page_table_entry*) * vm_size);

    if (page_table == NULL) {
        if (DEBUG) {
            fprintf(stderr, "Failed to allocate memory for page table\n");
        }
        return NULL;
    }
    
    memory = malloc(sizeof(void*) * vm_size * page_size);
    if (memory == NULL) {
        if (DEBUG) {
            fprintf(stderr, "Failed to allocate memory for memory\n");
        }
        return NULL;
    }

    for (int i = 0; i < vm_size * page_size; ++i) {
        
        memory[i] = malloc(32);

        if (memory[i] == NULL) {
            if (DEBUG) {
                fprintf(stderr, "Failed to allocate memory for memory at index %d\n", i);
            }
            return NULL;
        }
    }
    

    p_memory = malloc(sizeof(page_table_entry*) * pm_size);
    if (p_memory == NULL) {
        if (DEBUG) {
            fprintf(stderr, "Failed to allocate phyisical memory\n");
        }
        return NULL;
    }
    
    tlb = malloc(sizeof(page_table_entry*) * tlb_size);
    if (tlb == NULL) {
        if (DEBUG) {
            fprintf(stderr, "Failed to allocate memory for TLB\n");
        }
        return NULL;
    }

    for (int i = 0; i < pm_size; ++i) {
        page_table[i] = malloc(sizeof(page_table_entry));
        page_table[i]->ppn = i;
        page_table[i]->vpn = i;
        page_table[i]->dirty_bit = 0;
        page_table[i]->page_time = pm_replace_alg ? i:-1;
        page_table[i]->tlb_time = -1;
        p_memory[i] = page_table[i];
    }

    for (int i = 0; i < tlb_size; ++i) {
        tlb[i] = p_memory[i];
        tlb[i]->tlb_time = tlb_replace_alg ? i:-1;
    }

    curr_time = pm_size + 1;

    for (int i = pm_size; i < vm_size; ++i) {
        page_table[i] = malloc(sizeof(page_table_entry));
        page_table[i]->ppn = -1;
        page_table[i]->vpn = pm_size + i;
        page_table[i]->dirty_bit = 0;
        page_table[i]->page_time = -1;
        page_table[i]->tlb_time = -1;
    }
    
    return page_table;

}

// readInt
//
// Read an int from virtual memory.
//
// If the handle is not one returned by createVM, the behavior is
// undefined.
//
// If the address is out of range, an error message will be printed to
// stderr and the program will be terminated.
//
int readInt(void *handle, unsigned int address) {
    check_address(address, handle);
    int* ret = (int*)read(handle, address);
    if (TRACE) {
        printf("Read value %d from address %d\n", *ret, address);
    }
    return *ret;
}

// readFloat
//
// Read a float from virtual memory.
//
// If the handle is not one returned by createVM, the behavior is
// undefined.
//
// If the address is out of range, an error message will be printed to
// stderr and the program will be terminated.
//
float readFloat(void *handle, unsigned int address) {
    check_address(address, handle);
    float *ret = (float*)read(handle, address);
    if (TRACE) {
        printf("Read value %f from address %d\n", *ret, address);
    }
    return *ret;
}

// writeInt
//
// Write an int to virtual memory.
//
// If the handle is not one returned by createVM, the behavior is
// undefined.
//
// If the address is out of range, an error message will be printed to
// stderr and the program will be terminated.
//
void writeInt(void *handle, unsigned int address, int value) {
    check_address(address, handle);
    write(handle, address, (void*)&value);
    if (TRACE) {
        printf("wrote value %d to address %d\n", value, address);
    }
}

// writeFloat
//
// Write a float to virtual memory.
//
// If the handle is not one returned by createVM, the behavior is
// undefined.
//
// If the address is out of range, an error message will be printed to
// stderr and the program will be terminated.
//
void writeFloat(void *handle, unsigned int address, float value) {
    check_address(address, handle);
    write(handle, address, (void*)&value);
    if (TRACE) {
        printf("wrote value %f from address %d\n", value, address);
    }
}

// printStatistics
//
// Print the total number of page faults, the total number of TLB misses
// and the total number of disk writes.
//
// If the handle is not one returned by createVM, the behavior is
// undefined.
//
// Here is a sample output:
//
//   Number of page faults: 123
//   Number of TLB misses: 125
//   Number of disk writes: 64
//
void printStatistics(void *handle) {
    if (TRACE) {
        dump_memory();
    }
    printf("Number of page faults: %d\n", page_faults);
    printf("Number of TLB hits: %d\n", num_tlb_hits);
    printf("Number of TLB misses: %d\n", num_tlb_miss);
    printf("Number of disk writes: %d\n", disk_writes);
    if (DEBUG) {
        printf("Number of accesses: %d\n", curr_time);
    }
}

