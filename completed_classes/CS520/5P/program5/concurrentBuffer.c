//
// defines a concurrent buffer that supports the producer-consumer pattern
//
// the buffer elements are simply void* pointers
// note: the buffer does not make a copy of the data.
//       it simply stores a pointer to the data.
// 
// the buffer is FIFO (first in, first out)
//

#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include "concurrentBuffer.h"

//Concurrent buffer handle
typedef struct buf_handle {
    void **data_entries;
    int size;
} buf_handle;

//index of the next unnoccupied entry
int head;
//index of the next entry to be consumed
int tail;
//mutex for buffer
pthread_mutex_t mu;
//condition to wake thread waiting on a full/empty buffer
pthread_cond_t cond;
//boolean to indicate whether the buffer is full
int full;

// create a concurrent buffer
//   size: the number of elements in the buffer
//   returns: a handle to be used to operate on the buffer
void *createConcurrentBuffer(unsigned int size) {
    if (size <= 0) {
        return NULL;
    }
    buf_handle *ret = malloc(sizeof(buf_handle));
    ret->data_entries = malloc(sizeof(void *) * size);
    
    if (ret == NULL) {
        return NULL;
    }

    ret->size = size;
    pthread_mutex_init(&mu, NULL);
    pthread_cond_init(&cond, NULL);
    head = 0;
    tail = 0;
    full = 0;
    return ret;
}

// put a value in a buffer
//   handle: handle for a concurrent buffer
//   p: pointer to be put in the buffer
//   note: calling thread will block until there is space in the buffer
void putConcurrentBuffer(void *handle, void *p) {
    
    if (handle == NULL) {
        fprintf(stderr, "Invalid Buffer Handle");
        exit(-1);
    }
 
    //lock the mutex (Critical Section start)
    if (pthread_mutex_lock(&mu) != 0) {
        fprintf(stderr, "error in mutex_lock by thread");
        exit(-1);
    }

    buf_handle * buf = (buf_handle*)handle;
    
    //if the head == tail and full boolean is set, block thread
    while((head == tail) && (full)) {
        if (pthread_cond_wait(&cond, &mu) != 0) {
            fprintf(stderr, "Error in cond_wait by thread");
            exit(-1);
        }
    }
    
    //put to buffer and increment head
    buf->data_entries[head] = p;
    head = (head + 1) % buf->size;
    
    //if after procudcing head == tail, the buffer is full
    if (head == tail) {
        full = 1;
    } 

    //wake up any consumers blocked by an empty buffer
    if (pthread_cond_signal(&cond) != 0) {
            fprintf(stderr, "error in cond_signal by thread");
            exit(-1);
    }
    
    //unlock the mutex (critical section end)
    if (pthread_mutex_unlock(&mu) != 0) {
        fprintf(stderr, "error in mutex_unlock by thread");
        exit(-1);
    }
}

// get a value from a buffer
//   handle: handle for a concurrent buffer
//   returns: pointer retrieved from buffer
//   note: calling thread will block until there is a value available
void *getConcurrentBuffer(void *handle)
{
    if (handle == NULL) {
        fprintf(stderr, "Invalid Buffer Handle");
        exit(-1);
    }

    //lock the mutex (Critical Section start)
    if (pthread_mutex_lock(&mu) != 0) {
        fprintf(stderr, "error in mutex_lock by thread");
        exit(-1);
    }
    buf_handle * buf = (buf_handle*)handle;

    //if the head == tail and full boolean is not set, block thread
    while((head == tail) && (!full)) {
        if (pthread_cond_wait(&cond, &mu) != 0) {
            fprintf(stderr, "Error in cond_wait by thread");
            exit(-1);
        }
    }

    //get from buffer and increment tail
    void * ret = buf->data_entries[tail];
    tail = (tail + 1) % buf->size;

    //wake up any blocked producers and make sure the full boolean is cleared
    full = 0;
    if (pthread_cond_signal(&cond) != 0) {
            fprintf(stderr, "error in cond_signal by thread");
            exit(-1);
    }
    
    //unlock the mutex (critical section end)
    if (pthread_mutex_unlock(&mu) != 0) {
        fprintf(stderr, "error in mutex_unlock by thread");
        exit(-1);
    }
    return ret;

}

// delete a buffer
//   handle: handle for the concurrent buffer to be deleted
void deleteConcurrentBuffer(void *handle)
{
    if (handle == NULL) {
        fprintf(stderr, "Invalid Buffer Handle");
        exit(-1);
    }
    buf_handle *buf = (buf_handle*)handle;
    free(buf->data_entries);
    free(buf);
}

