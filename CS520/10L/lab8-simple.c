#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>

#define MAX_FILES 12

typedef struct {
    char *filename;
    int line_count;
} file_info;

int num_files;          //number of files to be processed
pthread_t** pt;         //array of threads
file_info** files;      //array of structs to pass into thread work function per file

void * work(void * in) {
    file_info *file = (file_info *)in;
    FILE *fp = fopen(file->filename, "r");
    file->line_count = 0;
    char next_char;
    while ((next_char = getc(fp)) != EOF) {
        if (next_char == '\n') {
            ++file->line_count;
        }
    }
    return 0;
}

int main(int argc, char* argv[]){
    if (argc > MAX_FILES) {
        fprintf(stderr, "Only up to %d files supported", MAX_FILES);
    }
    if (argc <= 1) {
        fprintf(stderr, "No File");
        return 1;
    }
    num_files = argc - 1;

    files = malloc(sizeof(file_info*) * num_files);
    pt = malloc(sizeof(pthread_t*) * num_files);

    for (int i = 0; i < num_files; ++i) {
        files[i] = malloc(sizeof(file_info));
        pt[i] = malloc(sizeof(pthread_t));
        files[i]->filename = argv[i + 1];
        pthread_create(pt[i], NULL, work, files[i]);
    }
    int total_lines = 0;
    for(int i = 0; i < num_files; ++i) {
        pthread_join(*pt[i], NULL);
        printf("%s=%d\n", files[i]->filename, files[i]->line_count);
        total_lines += files[i]->line_count;
    }

    printf("Total Count=%d\n", total_lines);
}