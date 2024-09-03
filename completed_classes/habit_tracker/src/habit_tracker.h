#include <stdio.h>

#define HABIT_LEN 50
#define MAX_HABITS 10

typedef struct {
    char name[HABIT_LEN];
    FILE *data;

} habit;