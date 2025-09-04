#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <pthread.h>

#include "concurrentBuffer.h"

// allow this to be overridden at compile time
// e.g. gcc -DBUFFER_SIZE=1 ...
#ifndef BUFFER_SIZE
#define BUFFER_SIZE 10
#endif

// the work functions for the threads
static void *work1(void *);
static void *work2(void *);

// struct to be used to pass information to the first thread
typedef struct {
FILE *fp;
void *buffer;
} INFO;

int main(int argc, char *argv[])
    {
    if (argc < 2)
    {
        fprintf(stderr, "Usage: countChars file\n");
        exit(-1);
    }

    void *cBuffer = createConcurrentBuffer(BUFFER_SIZE);
    if (cBuffer == NULL)
    {
        fprintf(stderr, "can't create the concurrent buffer\n");
        exit(-1);
    }
    
    // create info structs 
    INFO **ips = malloc(sizeof(INFO*) * (argc - 1));

    pthread_t **consumers = malloc(sizeof(pthread_t *) * (argc - 1));
    pthread_t **producers = malloc(sizeof(pthread_t *) * (argc - 1));
    
    for (int i = 0; i < argc - 1; ++i) {
        
        ips[i] = malloc(sizeof(INFO));
        if (ips[i] == NULL) {
            fprintf(stderr, "can't malloc the INFO struct\n");
            exit(-1);
        }

        ips[i]->fp = fopen(argv[i + 1], "r");
        ips[i]->buffer = cBuffer;

        // create threads
        producers[i] = malloc(sizeof(pthread_t));
        if (pthread_create(producers[i], NULL, work1, ips[i]) != 0) {
            fprintf(stderr, "can't create producer thread %d\n", i);
            exit(-1);
        }
        consumers[i] = malloc(sizeof(pthread_t));
        if (pthread_create(consumers[i], NULL, work2, cBuffer) != 0) {
            fprintf(stderr, "can't create consumer thread %d\n", i);
            exit(-1);
        }
    }

    // wait for the producer threads to finish
    for (int i = 0; i < argc - 1; ++i) {
        if (pthread_join(*producers[i], NULL)) {
            fprintf(stderr, "join for producer thread %d fails\n", i);
            exit(-1);
        }
    }
    int finalWordCounts[45] = {0};
    // wait for consumer threads to finish 
    for (int i = 0; i < argc - 1; ++i) {
        void *ret;
        if (pthread_join(*consumers[i], &ret)) {
            fprintf(stderr, "join for consumer thread %d fails\n", i);
            exit(-1);
        }
        int *thread_counts = (int*)ret;
        for (int i = 0; i < 43; ++i) {
            finalWordCounts[i] += thread_counts[i];
        }

        //need to free the threads returned array
        free(ret);
    }

    for (int i = 0; i < 45; ++i) {
        printf("%d %d\n", (i + 6), finalWordCounts[i]);
    }

    //cleanup
    // free the INFO structs, threads, and close the files
    for (int i = 0; i < argc - 1; ++i) {
        fclose(ips[i]->fp);
        free(ips[i]);
        free(producers[i]);
        free(consumers[i]);
    }
    free(producers);
    free(consumers);
    free(ips);

    // free the concurrent buffer   
    deleteConcurrentBuffer(cBuffer);
}

// get a line from an opened file
//   returns NULL at EOF
//   assumes a line is less than MAX_LINE_LENGTH characters long
//   mallocs space for the return value
//   line returned includes the newline on the end
//   last line might not include newline if last line is terminated by EOF
//   in this case one more call to readLine is required for NULL to be returned
#define MAX_LINE_LENGTH 1024

static int isAlpha(char c) {
    if (!((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))) {
        return 0;
    }
    return 1;
}

static char *getLine(FILE *fp)
{
    char buf[MAX_LINE_LENGTH];
    int i = 0;
    int c = getc(fp);
    if (c == EOF) return NULL;
    while (c != EOF && c != '\n')
    {
        buf[i] = c;
        i += 1;
        if (i == MAX_LINE_LENGTH)
        {
        fprintf(stderr, "maximum line length (%d) exceeded\n", MAX_LINE_LENGTH);
        exit(-1);
        }
        c = getc(fp);
    }
    if (c == '\n')
    {
        buf[i] = c;
        i += 1;
    }
 
    buf[i] = 0;
    char *s = malloc(i+1);
    if (s == NULL)
    {
        fprintf(stderr, "malloc failed in getLine\n");
        exit(-1);
    }
    strcpy(s, buf);
    return s;
}

// work function for the first thread (the producer)
static void *work1(void *info) {
    INFO *ip = info;

    // read the lines in the file
    char * line = getLine(ip->fp);
    while (line != NULL)
    {   
        char *block = calloc(1000, sizeof(char));
        strcat(block, line);
        free(line);
        while((line=getLine(ip->fp)) != NULL && (strlen(line) + strlen(block) < 1000)) {
            strcat(block, line);
            free(line);
        }

        putConcurrentBuffer(ip->buffer, block);
         
    }

    // put a NULL to indicate EOF
    putConcurrentBuffer(ip->buffer, NULL);

    return NULL;
}

// work function for the second thread (the consumer)
static void *work2(void *buffer) {
    // array to store word lenght counts to
    int* wordCounts = calloc(45, sizeof(int));

    // get lines from the buffer
    // and sum the number of characters in the line
    char *line;
    while ((line = getConcurrentBuffer(buffer)) != NULL)
    {
        int i = 0;
        
        while(line[i] != '\0') {

            int word_len = 0;
            
            //count chars until next non alpha char
            while(line[i] != '\0' && isAlpha(line[i])) {
                ++word_len;
                ++i;
            }
            
            //if word is within valid range, increment that index of wordCounts
            if (word_len >= 6 && word_len <= 50) {
                ++wordCounts[word_len - 6];
            }

            //find start of next word
            while(line[i] != '\0' && !isAlpha(line[i])) {
                ++i;
            }
        }
        free(line);
    }

    // return the character count via the void* return value
    return (void *) wordCounts;
}