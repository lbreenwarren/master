
#include "thread.h"

#define DEBUG 0

#define STACK_WORDS 8192

extern void asm_yeild(void* rdi, void *rsi);
extern void asm_start(void* rdi, void *rsi);
extern void asm_yeild_terminate(void *rsi);



static tcb *head = NULL; 
static tcb *tail = NULL; 
static tcb *waiting = NULL;
static tcb *to_clean = NULL;
static finished_t *finished_ids = NULL;

//==================================================================
// DEBUG PRINT FUNCTIONS
//==================================================================

static void debug(const char *fmt, ...) {
    if (!DEBUG) return;
    va_list args;
    va_start(args, fmt);
    vprintf(fmt, args);
    va_end(args);
}

static void dump_ready_list() {
    tcb *tmp = head;
    debug("TCB (head->thread_id: %ld, tail->thread_id: %ld)\n", head->thread_id, tail->thread_id);
    debug("head");
    while (tmp != tail) {
        debug("->%ld", tmp->thread_id);
        tmp = tmp->next;
    }
    debug("->%ld", tmp->thread_id);
    debug("<-tail\n");
}

static void dump_waiting_list() {
    tcb *tmp = waiting;
    while(tmp != NULL) {
        debug("%ld->", tmp->thread_id);
        tmp = tmp->next;
    }
    debug("NULL\n");
}

static void dump_mutex_queue(thread_mutex_t *mut) {
    mutex_q *tmp = mut->owner;
    debug("Owner->");
    while(tmp != NULL) {
        debug("%ld ->", tmp->th->thread_id);
        tmp=tmp->next;
    }
    debug("NULL\n");
}

static void dump_thread_state(tcb *th) {
    debug("\n|===============================================================================================|\n");
    debug("|------------------------------------ Thread %016ld ----------------------------------|\n", th->thread_id);
    debug("| rsp: %16p | rbx: %016ld |work: %16p |info: %16p |\n| r12: %016ld | r13: %016ld | r14: %016ld | r15: %016ld |\n", th->rsp, th->rbx, th->work, th->info, th->r12, th->r13, th->r14, th->r15);
    debug("|===============================================================================================|\n\n");
}

// static void dump_finished_ids() {
//     finished_t *tmp = finished_ids;
//     while(tmp != NULL) {
//         debug("| %ld |", tmp->thread_id);
//         tmp = tmp->next;
//     }
//     debug("\n\n");
// }
//=========================================================
// HELPER FUNCTIONS
//=========================================================

static void clean_thread(tcb *th) {
    free(th->stack);
    free(th);
}

static tcb * alloc_tcb() {
    tcb *ret = malloc(sizeof(tcb));
    memset(ret, 0, sizeof(tcb));
    if (ret == NULL) {
        return ret;
    }

    ret->stack = malloc(sizeof(uint64_t) * 8192);
    ret->rsp = (ret->stack) + STACK_WORDS;
    ret->thread_id = (uint64_t)ret->stack;
    return ret;
}

static tcb *get_thread(long id) {
    tcb *tmp = head;

    while (tmp->thread_id != id && tmp->next != NULL) {
        tmp = tmp->next;
    }
    
    if (tmp->next == NULL && tmp->thread_id != id) {
        if (waiting == NULL) {
            return NULL;
        }
        tmp = waiting;
        while (tmp->thread_id != id && tmp->next != NULL) {
            tmp = tmp->next;
        }

        if (tmp->next == NULL && tmp->thread_id != id) {
            return NULL;
        }
    }
    return tmp;
}



static void wake_thread(tcb *th) {
    
    if (waiting == NULL) {
        debug("ERROR: No threads Waiting\n");
        dump_waiting_list();
        dump_ready_list();
        return;
    }

    tcb *tmp = waiting;
    tcb *prev = waiting;
    
    while (tmp != NULL && tmp->thread_id != th->thread_id) {
        prev = tmp;
        tmp = tmp->next;
    }

    if (tmp == NULL) {
        debug("ERROR: thread with id %ld not on list of waiting threads\n", th->thread_id);
        dump_waiting_list();
        dump_ready_list();
        return;
    }
    
    if (waiting->thread_id == th->thread_id) {
        waiting = waiting->next;
    } else {
        tmp = tmp->next;
        prev->next = tmp;
    }

    if (head == NULL) {
        head = th;
        tail = th;
    } else {
        tail->next = th;
        tail = th;
    }
    th->next = NULL;
}

static void clean_main() {
    clean_thread(head);

    if (to_clean != NULL) {
        clean_thread(to_clean);
        to_clean = NULL;
    }
    //finished_t *tmp = finished_ids;
    // while(tmp != NULL) {
    //     finished_t *prev = tmp;
    //     tmp = tmp->next;
    //     free(prev);
    // }
    head=NULL;
        
}

static void add_to_waiting(tcb *th) {
    
    if (waiting == NULL) {
        waiting = th;
    
    } else {
        tcb *tmp = waiting;
        while(tmp->next != NULL) {
            if (tmp->thread_id == th->thread_id) {
                return;
            }
            tmp=tmp->next;
        } 
        tmp->next = th;
    }
    
    th->next = NULL;
}

static int check_deadlock(tcb *th) {
    tcb *tmp = head;
    while(tmp->joining != NULL) {
        if (tmp->joining->thread_id == th->thread_id) {
            return 1;
        }
        tmp = tmp->joining; 
    }
    return 0;
}

static void init_threads() {

    tcb *main = alloc_tcb();
    main->work = NULL;
    main->info = NULL;
    main->thread_id = 0;
    head = main;
    tail = main;
    main->joining = NULL;
    main->next = NULL;
    debug("Created main thread\n");
    dump_thread_state(main);
}

//=========================================================
// THREAD.H PRIMATIVES
//=========================================================


long thread_self(void) {
    return head->thread_id;
}

int thread_join(long id) {
   
    if (head == NULL) {
        init_threads();
    }
    debug("Thread %ld joining on thread %ld\n", thread_self(), id);
    tcb *target = get_thread(id);
    // debug("thread %ld joining on thread %ld\n", thread_self(), target->thread_id);
    if (target == NULL) {
        debug("\nchecking finished threads\n");
        finished_t *tmp = finished_ids;
        finished_t *prev = tmp;
        while(tmp != NULL) {
            if (tmp->thread_id == id) {
                if (finished_ids->thread_id == id) {
                    finished_ids = finished_ids->next;
                } else {
                    prev->next = tmp->next;
                }
                free(tmp);
                if(thread_self() == 0 && head->next == NULL && waiting == NULL) {
                    clean_main();
                }
                
                return 0;
            }
            tmp=tmp->next;
            prev=tmp;
        }
        
        return -3;
    }

    if (id == thread_self())  {
        return -1;
    }

    if (check_deadlock(target)) {
        return -1;
    }

    if (target->joining != NULL) {
        return -3;
    }
  
    
    target->joining = head;
    tcb *prev = head;
    head = head->next;
    add_to_waiting(prev);
    asm_yeild(prev, head);
    return 0;

}

void thread_cleanup(void) {
    if (head == NULL) {
        return;
    }
    tcb *thread = head;

    debug("Thread %ld finished\n", thread->thread_id);

    if (to_clean != NULL) {
        debug("cleaning thread %ld\n", to_clean->thread_id);
        tcb *prev = to_clean;
        clean_thread(prev);
    }

    if (head->joining != NULL) {
        debug("thread %ld finished, waking thread %ld\n", head->thread_id, head->joining->thread_id);
        wake_thread(head->joining);
        dump_ready_list();
        head->joining = NULL;
    } else {
        debug("Adding thread %ld to finished thread ids\n", thread_self());
        
        finished_t *new = malloc(sizeof(finished_t));
        new->thread_id = thread->thread_id;
        new->next = NULL;
    
        if (finished_ids == NULL) {
            finished_ids = new;
        }else {
            finished_t *tmp = finished_ids;
            while(tmp->next != NULL) { 
                tmp = tmp->next;
            }

            tmp->next = new;
        }
    }

    to_clean = thread;
    head = head->next;
    thread->next = NULL;

    if (head == NULL) {
        debug("DEADLOCK");
        return;
    }
    
    debug("yeilding to thread %ld\n", head->thread_id);
    dump_thread_state(head);
    asm_yeild_terminate(head);
}

void thread_start(void (*work)(void *), void *info) {
    work(info);
    thread_cleanup();
}

long thread_create(void (*work)(void*), void* arg) {
    
    if (head == NULL) {
        init_threads();
    }

    dump_ready_list();
    tcb *new_thread = alloc_tcb();

    if (new_thread == NULL) {
        return 0;
    }
    
    
    new_thread->work = work;
    new_thread->info = arg;

    new_thread->joining = NULL;

    //setup stack
    new_thread->rsp -= 3;
    new_thread->rsp[0] = 0;         
    new_thread->rsp[1] = (uint64_t)asm_start;
    new_thread->rsp[2] = 0;         

    tail->next = new_thread;
    tail = new_thread;
    new_thread->next = NULL;
    new_thread->joining = NULL;

    dump_ready_list();
    debug("Created New Thread with id %ld\n", new_thread->thread_id);
    dump_thread_state(new_thread);
    return new_thread->thread_id;
}

void thread_yield(void) {
    if (head == NULL) {
        init_threads();
    }
    if (head->next == NULL) {
        return;
    }
    
    tcb *prev = head;
    head = head->next;
    tail->next = prev;
    tail = prev;
    prev->next = NULL;

    debug("Thread %ld yeilding to thread %ld\n", prev->thread_id, head->thread_id);
    
    dump_ready_list();
    dump_thread_state(prev);
    dump_thread_state(head);
    
    asm_yeild(prev, head);
    dump_waiting_list();
    if (head->thread_id == 0 && tail->thread_id == 0 && waiting == NULL) {
        
        debug("TCB is empty after main, freeing main and thread waiting to be cleaned\n");
        clean_main();

    }
}

int thread_mutex_init(thread_mutex_t *mutex) {
   
    if (mutex == NULL) {
        return 0;
    }
    if (head == NULL) {
        init_threads();
    } 
    mutex->lock = 0;
    mutex->owner = NULL;
    return 1;
}

int thread_mutex_lock(thread_mutex_t *mutex) {
    debug("Thread %ld attempting to aquire mutex\n", thread_self());
    if (mutex == NULL) {
        debug("ERROR: mutex is NULL\n");
        return 0;
    }
    if (mutex->lock) {
        debug("Mutex is locked by thread %ld\n", mutex->owner->th->thread_id);
        if (head->next == NULL) {
            return 0;
        }
        if (mutex->owner->th->thread_id == head->thread_id) {
            debug("DEADLOCK: Current owner of mutex attempting to aquire mutex\n");
            return 0;
        }
        debug("Adding thread %ld to queue of mutex\n", thread_self());
        debug("Previous Mutex Queue:\n");
        dump_mutex_queue(mutex);
        mutex_q *tmp = mutex->owner;
        
        while (tmp->next != NULL) {
            tmp = tmp->next;
        }
        
        tcb *th = head;
        head = head->next;
        mutex_q *new = malloc(sizeof(mutex_q));
        new->th = th;
        new->next = NULL;
        tmp->next = new;
        debug("Current Mutex Queue:\n");
        dump_mutex_queue(mutex);

        debug("Adding thread %ld to list of blocked threads\n", th->thread_id);
        debug("Previous waiting list:");
        dump_waiting_list();
        add_to_waiting(th);
        debug("current waiting list:\n");
        dump_waiting_list();
        debug("Removing thread %d from ready list and yeilding to next thread\n", th->thread_id);
        dump_ready_list();
        debug("Thread %ld yeilding to thread %ld\n\n\n", th->thread_id, head->thread_id);
        asm_yeild(th, head);
        return 1;

    } else {
        mutex->lock = 1;
        mutex->owner = malloc(sizeof(mutex_q));
        mutex->owner->th = head;
        mutex->owner->next = NULL;
        return 1;
    }
}

int thread_mutex_unlock(thread_mutex_t *mutex) {
    dump_mutex_queue(mutex);
    if (mutex == NULL || mutex->lock == 0) {
        return 0;
    }
    if (mutex->owner == NULL || mutex->owner->th->thread_id != head->thread_id) {
        return 0;
    }

    if (mutex->owner->next == NULL) {
        mutex->lock = 0;
        mutex->owner = NULL;
    } else {
        mutex_q *prev = mutex->owner;
        mutex->owner = mutex->owner->next;
        tcb *th = mutex->owner->th;
        free(prev);
        wake_thread(th);
    }
   
    return 1;
}