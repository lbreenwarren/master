// interface for threads library
//
// 
//

long thread_create(void (*)(void*), void*);
void thread_yield(void);
void thread_cleanup(void);