#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define MAX_FILES 12

typedef struct {
    char *filename;
    int line_count;
} file_info;

//mutex to protect global sum of total lines
pthread_mutex_t mu;

int num_files;          //number of files to be processed

//array of threads
pthread_t** pt;

//array of structs to pass into thread work function per file
file_info** files;

//global sum of line counts
int total_lines;

//work function
void * work(void * in) {
    //open file
    file_info *file = (file_info *)in;
    FILE *fp = fopen(file->filename, "r");

    //if bad file name count = 0
    if (fp == NULL) {
        fprintf(stderr, "Could not open file %s\n", file->filename);
        file->line_count = 0;
        return (void *)file;
    }

    //read character by character, increment line count if character=='\n'
    file->line_count = 0;
    char next_char;
    while ((next_char = getc(fp)) != EOF) {
        if (next_char == '\n') {
            ++file->line_count;
        }
    }
    
    //aquire and lock mutex
    pthread_mutex_lock(&mu);
    //update total
    total_lines += file->line_count;
    //unlock and release mutex
    pthread_mutex_unlock(&mu);

    return 0;
}

int main(int argc, char* argv[]){
    if (argc > MAX_FILES + 1) {
        fprintf(stderr, "Only up to %d files supported", MAX_FILES);
    }
    if (argc <= 1) {
        fprintf(stderr, "No File");
        return 1;
    }

    pthread_mutex_init(&mu, NULL);
    num_files = argc - 1;

    files = malloc(sizeof(file_info*) * num_files);
    pt = malloc(sizeof(pthread_t*) * num_files);
    printf("num files = %d\n", num_files);
    
    for (int i = 0; i < num_files; ++i) {
        files[i] = malloc(sizeof(file_info));
        pt[i] = malloc(sizeof(pthread_t));
        files[i]->filename = argv[i + 1];
        pthread_create(pt[i], NULL, work, files[i]);
    }
    
    for(int i = 0; i < num_files; ++i) {
        pthread_join(*pt[i], NULL);
        printf("%s=%d\n", files[i]->filename, files[i]->line_count);
    }
    printf("Total Count=%d\n", total_lines);
}