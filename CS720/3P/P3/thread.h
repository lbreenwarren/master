// interface for threads library
//
// 
//

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <inttypes.h>
#include <stdarg.h>

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
    struct tcb *joining;
}; typedef struct tcb tcb;

struct finished_t {
    uint64_t thread_id;
    struct finished_t *next;
}; typedef struct finished_t finished_t;

struct mutex_q {
    tcb *th;
    struct mutex_q *next;
}; typedef struct mutex_q mutex_q;

struct thread_mutex_t {
    int lock;
    mutex_q *owner;
}; typedef struct thread_mutex_t thread_mutex_t;

struct cond_q {
    tcb *th;
    thread_mutex_t *mut;
    struct cond_q *next;
}; typedef struct cond_q cond_q;


struct thread_cond_t {
    cond_q *head;
}; typedef struct thread_cond_t thread_cond_t;

long thread_create(void (*)(void*), void*);
void thread_yield(void);
void thread_cleanup(void);
int thread_join(long id);
long thread_self(void);

int thread_mutex_init(thread_mutex_t *mutex);
int thread_mutex_lock(thread_mutex_t *mutex);
int thread_mutex_unlock(thread_mutex_t *mutex); 
int thread_cond_init(thread_cond_t *cond);
int thread_cond_wait(thread_cond_t *cond, thread_mutex_t *mutex);
int thread_cond_signal(thread_cond_t *cond);