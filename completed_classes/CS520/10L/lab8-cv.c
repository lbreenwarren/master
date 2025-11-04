#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>

#define MAX_FILES 12
#define DEBUG 0

typedef struct {
    char *filename;
    int line_count;
    long id;
} thread_info; //struct to pass file name to and store count from threads

int num_files;          //number of files to be processed
pthread_mutex_t mu;     //mutex to protect global sum of total lines
pthread_cond_t cv;      //condition variable to signal parent that thread is done
int cnt;                //count for number of finished threads
pthread_t** pt;         //array of threads
thread_info** t_info;      //array of structs to pass into thread work function per file
int total_lines;        //global sum of line counts

//work function
void * work(void * in) {
    //open file
    thread_info *file = (thread_info *)in;
    FILE *fp = fopen(file->filename, "r");
    if (DEBUG) {
        printf("spun up thread %ld\n", file->id);
    }
    //if bad file name count = 0
    if (fp == NULL) {
        printf("Could not open file %s\n", file->filename);
        file->line_count = 0;
    } else {
        //read character by character, increment line count if character=='\n'
        file->line_count = 0;
        char next_char;
        while ((next_char = getc(fp)) != EOF) {
            if (next_char == '\n') {
                ++file->line_count;
            }
        }
    }
    //aquire and lock mutex
    if (pthread_mutex_lock(&mu) != 0) {
        fprintf(stderr, "error in mutex_lock by thread");
        exit(-1);
    }
    if (DEBUG) {
        printf("thread %ld acquired the mutex\n", file->id);
    }

    //update total
    total_lines += file->line_count;
   
    //update completed threads count
    ++cnt;
    //signal parent to wakeup if all threads are done
    if (cnt == num_files) {
        if (DEBUG) {
            printf("thread %ld signaling parent to wake up\n", file->id);
        }
        if (pthread_cond_signal(&cv) != 0) {
            fprintf(stderr, "error in cond_signal by thread");
            exit(-1);
        }
    }
    //unlock and release mutex
    if (pthread_mutex_unlock(&mu) != 0) {
        fprintf(stderr, "error in mutex_unlock by thread");
        exit(-1);
    }
    if (DEBUG) {
        printf("thread %ld released the mutex\n", file->id);
    }
    if (fp != NULL) {
        fclose(fp);
    }
    return 0;
}

int main(int argc, char* argv[]){

    if (argc > MAX_FILES + 1) {
        fprintf(stderr, "Only up to %d files supported", MAX_FILES);
        return 1;
    }
    if (argc <= 1) {
        fprintf(stderr, "No File");
        return 1;
    }

    if (pthread_mutex_init(&mu, NULL) != 0)
        fprintf(stderr, "can't init mutex");

    if (pthread_cond_init(&cv, NULL) != 0)
        fprintf(stderr, "can't init condition variable");

    cnt = 0;
    num_files = argc - 1;
    t_info = malloc(sizeof(thread_info*) * num_files);
    pt = malloc(sizeof(pthread_t*) * num_files);
    
    for (int i = 0; i < num_files; ++i) {
        t_info[i] = malloc(sizeof(thread_info));
        pt[i] = malloc(sizeof(pthread_t));
        t_info[i]->filename = argv[i + 1];
        t_info[i]->id = i;
        if (pthread_create(pt[i], NULL, work, t_info[i]) != 0) {
            fprintf(stderr, "Error in pthread_create by parent");
            return 1;
        }
    }
  
    //lock mutex before checking cnt
    if (pthread_mutex_lock(&mu) != 0) {
        fprintf(stderr, "Error in mutex_lock by parent");
    }
    if (DEBUG) {
        printf("parent aquired the mutex");
    }
    while (cnt < num_files) {
        if (DEBUG) {
            printf("Blocking parent until all threads are done and condition is signalled\n");
        }
        if (DEBUG) {
            printf("Parent releasing mutex and waiting on condition");
        }
        if (pthread_cond_wait(&cv, &mu) != 0) {
            fprintf(stderr, "Error in cond_wait by parent");
            return 1;
        }
        if (DEBUG) {
            printf("parent recieved signal, proceding");
        }
    }
    //if all threads finish before we call wait then it will just pass the while loop and we can just unlock the mutex
    if (pthread_mutex_unlock(&mu) != 0) {
        fprintf(stderr, "Error in mutex unlock by parent\n");
        return 1;
    }
    if (DEBUG) {
        printf("Parent released the mutex\n");
    }
    for(int i = 0; i < num_files; ++i) {
        printf("%s=%d\n", t_info[i]->filename, t_info[i]->line_count);
    }
    
   
    printf("Total Count=%d\n", total_lines);
    
    for (int i = 0; i < num_files; ++i) {
        free(t_info[i]);
    }
    for (int i = 0; i < num_files - 1; ++i) {
        free(pt[i]);
    }

    free(t_info);
    free(pt);
}