#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <inttypes.h>
#include <stdarg.h>

#define DEBUG 0

#define STACK_WORDS 8192

extern void asm_yeild(void* rdi, void *rsi);
extern void asm_start(void* rdi, void *rsi);
extern void asm_yeild_terminate(void *rsi);

struct tcb {
    uint64_t *rsp;
    uint64_t rbx;
    void (*work)(void *);
    void *info;
    uint64_t r12;     // 16
    uint64_t r13;     // 24
    uint64_t r14;     // 32
    uint64_t r15;     // 40
    uint64_t *stack;
    uint64_t thread_id;
    struct tcb *next;
};

static struct tcb *head = NULL; 
static struct tcb *tail = NULL; 
static struct tcb *to_clean = NULL;
static void debug(const char *fmt, ...) {
    if (!DEBUG) return;
    va_list args;
    va_start(args, fmt);
    vprintf(fmt, args);
    va_end(args);
}

static void dump_ready_list() {
    struct tcb *tmp = head;
    debug("TCB (head->thread_id: %ld, tail->thread_id: %ld)\n", head->thread_id, tail->thread_id);
    debug("head");
    while (tmp != tail) {
        debug("->%ld", tmp->thread_id);
        tmp = tmp->next;
    }
    debug("->%ld", tmp->thread_id);
    debug("<-tail\n");
}

static void dump_thread_state(struct tcb *th) {
    debug("\n|===============================================================================================|\n");
    debug("|------------------------------------ Thread %016ld ----------------------------------|\n", th->thread_id);
    debug("| rsp: %16p | rbx: %016ld |work: %16p |info: %16p |\n| r12: %016ld | r13: %016ld | r14: %016ld | r15: %016ld |\n", th->rsp, th->rbx, th->work, th->info, th->r12, th->r13, th->r14, th->r15);
    debug("|===============================================================================================|\n\n");
}

static void clean_thread(struct tcb *th) {
    free(th->stack);
    free(th);
}

static struct tcb * alloc_tcb() {
    struct tcb *ret = malloc(sizeof(struct tcb));
    memset(ret, 0, sizeof(struct tcb));
    if (ret == NULL) {
        return ret;
    }

    ret->stack = malloc(sizeof(uint64_t) * 8192);
    ret->rsp = (ret->stack) + STACK_WORDS;
    ret->thread_id = (uint64_t)ret;
    return ret;
}

void thread_cleanup(void) {
    struct tcb *thread = head;

    debug("Thread %ld finished\n", thread->thread_id);

    if (to_clean != NULL) {
        debug("cleaning thread %ld\n", to_clean->thread_id);
        struct tcb *prev = to_clean;
        clean_thread(prev);
    }

    to_clean = thread;

    head = head->next;
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
        
        struct tcb *main = alloc_tcb();
        if (main == NULL) {
            return 0;
        }
        main->work = NULL;
        main->info = NULL;
        main->thread_id = 0;
        head = main;
        tail = main;
        debug("Created main thread\n");
        dump_thread_state(main);
    }

    dump_ready_list();
    struct tcb *new_thread = alloc_tcb();

    if (new_thread == NULL) {
        return 0;
    }
    
    
    new_thread->work = work;
    new_thread->info = arg;

    //setup stack
    new_thread->rsp -= 3;
    new_thread->rsp[0] = 0;         
    new_thread->rsp[1] = (uint64_t)asm_start;
    new_thread->rsp[2] = 0;         

    tail->next = new_thread;
    tail = new_thread;
    dump_ready_list();
    debug("Created New Thread with id %ld\n", new_thread->thread_id);
    dump_thread_state(new_thread);
    return new_thread->thread_id;
}

void thread_yield(void) {
    if (head->next == NULL) {
        return;
    }
    
    struct tcb *prev = head;
    head = head->next;
    tail->next = prev;
    tail = prev;

    debug("Thread %ld yeilding to thread %ld\n", prev->thread_id, head->thread_id);
    dump_ready_list();
    dump_thread_state(prev);
    dump_thread_state(head);
    asm_yeild(prev, head);
    if (head->thread_id == 0 && tail->thread_id == 0) {
        
        debug("TCB is empty after main, freeing main and thread waiting to be cleaned\n");
        clean_thread(head);

        if (to_clean != NULL) {
            clean_thread(to_clean);
        }

    }
}


