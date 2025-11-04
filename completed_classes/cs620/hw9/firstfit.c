#include <stdio.h>
#include <stdlib.h>

//struct to store the location and size of a hole in memory
typedef struct hole hole;
struct hole {
    int start;
    int size;
    hole *next;
};

//struct to store pid, start, and size of active programs 
typedef struct pgm pgm;
struct pgm {
    int pid;
    int start;
    int size;
    pgm *next;
};

//head of linked lists
hole *_root_hole;
pgm *_root_pgm;

//memory statistics
int allocated;
int free_bytes;
int size;

//number of pgms and holes currently in memory
int num_holes;
int num_pgms;

//list of pid's with allocated memory
int *pids;

//allocate and init a new hole
hole * new_hole(int start, int size) {
    hole * new_hole = malloc(sizeof(hole));
    if (new_hole == NULL) {
        printf("Failed to allocate memory for hole info\n");
        exit(-1);
    }
    new_hole->size = size;
    new_hole->start = start;
    new_hole->next = NULL; 
}

pgm * new_pgm(int start, int size, int pid) {
    pgm * new_pgm = malloc(sizeof(pgm));
    if (new_pgm == NULL) {
        printf("Failed to allocate memory for program info\n");
        exit(-1);
    }
    new_pgm->size = size;
    new_pgm->start = start;
    new_pgm->pid = pid;
    new_pgm->next = NULL; 
}

void MemoryManager(int bytes){ 
// initialize memory with these many bytes.
    
    //initilize linked list roots
    _root_hole = new_hole(-1, -1);
    _root_pgm = new_pgm(-1, -1, -1);

    //memory starts as one big hole
    _root_hole->next = new_hole(0, bytes);

    //init statistics
    allocated = 0;
    free_bytes = bytes;
    size = bytes;
    num_holes = 1;
    num_pgms = 0;
}

int allocate(int bytes, int pid){ 
// allocate these many bytes to the process with this id
// assume that each pid is unique to a process
// return 1 if successful
// return -1 if unsuccessful; print an error indicating
// whether there wasn’t sufficient memory or whether
// there you ran into external fragmentation

    //find where the program should go in the list and check if its a duplicate
    pgm *tmp = _root_pgm;
    
    while (tmp->next != NULL && tmp->next->pid < pid) {
        tmp = tmp->next;
    }
    
    //check for duplicate PID's
    if (tmp->next != NULL && tmp->next->pid == pid) {
        printf("Process with same pid(%d) has already existed.\n", pid);
        return -1;
    }

    if (bytes <= 0) {
        printf("Process size should be positive.");
        return -1;
    }

    //check if theres enough free memory
    if (free_bytes < bytes) {
        printf("Not enough memory space for process %d.\n", pid);
        return -1;
    }
    
    //find where it goes, keep track of the one before it
    hole *tmp_hole = _root_hole->next;
    hole *prev = _root_hole;
    while(tmp_hole != NULL && tmp_hole->size < bytes) {
        prev = tmp_hole;
        tmp_hole = tmp_hole->next;
    }
    
    if (tmp_hole == NULL) {
        //no holes large enough (exeternal fragmentation)
        printf("Due to external fragmentation, can't allocate memory space to process %d.\n", pid);
        return -1;
    }

    //insert pgm into hole
    int pid_start = tmp_hole->start;
    tmp_hole->start = tmp_hole->start + bytes;
    tmp_hole->size = tmp_hole->size - bytes;

    //if perfect fit, remove hole
    if (tmp_hole->size == 0) {
        prev->next = tmp_hole->next;
        free(tmp_hole);
        --num_holes;
    }

    //insert pgm in list
    pgm *new_pid = new_pgm(pid_start, bytes, pid);
    if (tmp->next != NULL) {
        //if were not at the end of the list
        new_pid->next = tmp->next;
    }
    tmp->next = new_pid;

    //update stats
    free_bytes = free_bytes - bytes;
    allocated = allocated + bytes;
    ++num_pgms;

}

int deallocate(int pid){
//deallocate memory allocated to this process
// return 1 if successful, -1 otherwise with an error message

    //locate pgm to deallocate (getting its previous entry for list rearranging)
    pgm *rm_pgm = _root_pgm;
    while (rm_pgm->next != NULL && rm_pgm->next->pid != pid) {
        rm_pgm = rm_pgm->next;
    }

    if (rm_pgm->next == NULL) {
        printf("Process with pid(%d) does not exist.\n", pid);
        return -1;
    }
    
    //store its start and size for hole management
    int deall_start = rm_pgm->next->start;
    int deall_size = rm_pgm->next->size;
    int deall_end = rm_pgm->next->start + rm_pgm->next->size;
    
    //rm pid from list
    pgm *tmp = rm_pgm->next;
    rm_pgm->next = tmp->next;
    free(tmp);

    //check if it has holes on either end
    hole *before_hole = _root_hole;
    hole *after_hole = _root_hole->next;
    
    while (after_hole != NULL && after_hole->start < deall_end) {
        before_hole = after_hole;
        after_hole = after_hole->next;
    }
    
    if (before_hole->start + before_hole->size == deall_start) {
        //if theres a hole directly before the deallocated program, add the pgms size to the holes size
        before_hole->size += deall_size;

        if (after_hole != NULL && after_hole->start == deall_end) {
            //if there is a hole directly before and after the deallocated program, 
            //add the pgms size and after holes size to the one before and remove the after hole
            before_hole->size += after_hole->size;
            before_hole->next = after_hole->next;
            free(after_hole);
            --num_holes;
        }

    } else if (after_hole != NULL && after_hole->start == deall_end) {
        //if there is only a hole directly after, move its start to the start of the deallocated pgm and add the pgm's size to the hole
        after_hole->start = deall_start;
        after_hole->size += deall_size;
    } else {
        //if the programs is not adjacent to any holes, theres a new hole
        hole * new = new_hole(deall_start, deall_size);
        before_hole->next = new;
        new->next = after_hole;
        ++num_holes;
    }
    
    //update stats
    --num_pgms;
    free_bytes += deall_size;
    allocated -= deall_size;

}
void printMemoryState()
{ // print out current state of memory
// Example:
// Memory size = 1024 bytes, allocated bytes = 24, free = 1000
// There are currently 10 holes and 3 active process
// Hole list:
// hole 1: start location = 0, size = 202
// ...
// Process list:
// process id=34, start location=203, size=35
// ...
    printf("Memory size = %d bytes, allocated bytes = %d, free = %d\n", size, allocated, free_bytes);
    printf("There're currently %d holes and %d processes:\n", num_holes, num_pgms);
    printf("Hole list:\n");
    int i = 1;
    hole *tmp_hole = _root_hole->next;
    while (tmp_hole != NULL) {
        printf("hole %d: start location=%d, size=%d\n", i, tmp_hole->start, tmp_hole->size);
        ++i;
        tmp_hole = tmp_hole->next;
    }
    printf("Process list:\n");
    i = 1;
    pgm *tmp_pid = _root_pgm->next;
    while(tmp_pid != NULL) {
        printf("process %d: id=%d, start location=%d, size=%d\n", i, tmp_pid->pid, tmp_pid->start, tmp_pid->size);
        tmp_pid = tmp_pid->next;
        ++i;
    }

}

int main(int argc, char * argv[]) {

    FILE *fp = fopen(argv[1], "r");
    char * str;
    int mem_size = 0;
    fscanf(fp, "%d", &mem_size);
    
    //move to next line
    char buffer[1024];
    fgets(buffer, sizeof(buffer), fp);
    
    printf("%d\n", mem_size);
    MemoryManager(mem_size);

    char op = getc(fp);
    int pid = -1;
    while (op != EOF) {
        
        printf("%c\n", op);

        switch(op) {
            case 'A':
                int pgm_size = 0;
                fscanf(fp, "%d", &pgm_size);
                fscanf(fp, "%d", &pid);
                printf("%d %d\n", pgm_size, pid);
                allocate(pgm_size, pid);
                break;
            case 'D':
                fscanf(fp, "%d", &pid);
                printf("%d\n", pid);
                deallocate(pid);
                break;
            case 'P':
                printMemoryState();
                break;
            default:
                printf("UNRECOGNIZED OPTION (%c)\n", op);
        }

        //move to next line
        char buffer[1024];
        fgets(buffer, sizeof(buffer), fp);
        
        //get next instruction
        op = getc(fp);
    }

    //cleanup
    pgm * tmp_pgm = _root_pgm->next;
    while(tmp_pgm != NULL) {
        pgm* free_pgm = tmp_pgm;
        tmp_pgm = tmp_pgm->next;
        free(free_pgm);
    } 
    hole* tmp_hole = _root_hole->next;

    while(tmp_hole != NULL) {
        hole *free_hole = tmp_hole;
        tmp_hole = tmp_hole->next;
        free(free_hole);
    }
    free(_root_hole);
    free(_root_pgm);
    return 0;
}