#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <inttypes.h>

#define STACK_WORDS 8192

extern void asm_yeild(void* rdi, void *rsi);
extern void asm_start(void* rdi, void *rsi);

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
};

struct ready_list {
    struct tcb *curr;
    struct ready_list *next;
};

static struct ready_list *head = NULL; 
static struct tcb *to_clean = NULL;

static struct ready_list * get_tail() {
    
    struct ready_list *ret = head;
    
    while(ret->next != NULL) {
        ret = ret->next;
    }
    return ret;
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
    struct tcb *thread = head->curr;
    struct tcb *prev = to_clean;

    free(prev->stack);
    free(prev);
    to_clean = thread;

    struct ready_list *done = head;
    head = head->next;
    free(done);
    
    asm_yeild(NULL, head->curr);
}


void thread_start(void (*work)(void *), void *info) {
    work(info);
    thread_cleanup();
}

long thread_create(void (*work)(void*), void* arg) {
    
    if (head == NULL) {
        head = malloc(sizeof(struct ready_list));
        struct tcb *main = alloc_tcb();
        if (main == NULL) {
            return 0;
        }
        main->work = NULL;
        main->info = NULL;
        main->thread_id = 0;
        head->curr = main;
        head->next = NULL;
        to_clean = head->curr;
    }

    struct ready_list *last = get_tail();
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
    
    last->next = malloc(sizeof(struct ready_list));
    last->next->curr = new_thread;
    last->next->next = malloc(sizeof(struct ready_list));
    last->next->next = NULL;
    return new_thread->thread_id;
}

void thread_yield(void) {
    if (head->next == NULL) {
        return;
    }
    
    struct ready_list *last = get_tail();
    struct ready_list *old_head = head;
    head = head->next;
    last->next = old_head;
    old_head->next = NULL;
    asm_yeild(old_head->curr, head->curr);
}


