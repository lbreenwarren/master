#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "habit_tracker.h"

int main(){

    //Open tracked habbits file
    FILE *tracked_fields;
    tracked_fields = fopen("../data/tracked_fields.txt", "a+");
    
    if (tracked_fields == NULL) {
        printf("Failed to open file");
        return 1;
    }

    char field[HABIT_LEN];
    printf("Currently tracked data:\n");
    int num_habits = 0;
    char **tracked_habits = (char **)malloc(MAX_HABITS * sizeof(char *));

    if (tracked_habits == NULL) {
        perror("Memory allocation failed");
        return 1;
    } 
    
    //read in each field as a string
    while(fgets(field, sizeof(field), tracked_fields) != NULL) {
        printf("%s", field);
        if (strlen(field) > 0 && field[strlen(field) - 1] == '\n') {
            field[strlen(field) - 1] = '\0';
        }
        tracked_habits[num_habits] = (char *)malloc(HABIT_LEN * sizeof(char));

        if (tracked_habits[num_habits] == NULL) {
            perror("Memory allocation failed");
            return 1;
        }
        strcpy(tracked_habits[num_habits], field);
        ++num_habits;
    }
    
    
    //allocate habbit for each field
    habit *habitArray = (habit *)malloc(num_habits * sizeof(habit));

    if (habitArray == NULL) {
        perror("Memory allocation failed");
        return 1;
    }

    char *filestr = "../data/";
    char *filestr2 = ".txt";
    
    if (habitArray != NULL) {

        for (int i = 0; i < num_habits; ++i) {
            //store name
            strcpy(habitArray[i].name, tracked_habits[i]);

            //build file path and store file pointer in data field
            char data_file[HABIT_LEN + 12];
            strcpy(data_file, filestr);
            strcat(data_file, habitArray[i].name);
            strcat(data_file, filestr2);
            habitArray[i].data = fopen(data_file, "a+");
        }
    }

    //
    //TO DO: prompt in a while loop
    //
    
    
    char sel;
    

    
    while (sel != 'x') {

        printf("\nSelect habit to track:\n");
        printf("(0): add new habit\n");
        
        for(int i = 0; i < num_habits; ++i) {
            int sel = i + 1;
            printf("(%d):%s\n", sel, habitArray[i].name);
        }

        printf("(x): exit\n");
        scanf("%c", &sel);

        //
        //TO DO: move as function to new file
        //
        if (sel == '0') {
            ++num_habits;
            int i = num_habits - 1;
            printf("Enter new habit name:\n");
            
            //reallocate array of habits to include new habbit
            habitArray = (habit *)realloc(habitArray, num_habits * sizeof(habit));

            if (habitArray == NULL) {
                perror("Memory allocation failed");
                return 1;
            }
            
            // Clear the input buffer
            int c;
            while ((c = getchar()) != '\n' && c != EOF);

            //store name
            fgets(habitArray[i].name, HABIT_LEN, stdin);

            // Remove trailing newline character, if present
            if (habitArray[i].name[strlen(habitArray[i].name) - 1] == '\n') {
                habitArray[i].name[strlen(habitArray[i].name) - 1] = '\0';
            }

            //construct file name from habbit name
            char data_file[HABIT_LEN + 12];
            strcpy(data_file, filestr);
            strcat(data_file, habitArray[i].name);
            strcat(data_file, filestr2);

            //store file pointer and open file 
            habitArray[i].data = fopen(data_file, "a+");
            if (habitArray[i].data == NULL) {
                perror("Error opening file");
                return 1;
            }

            fprintf(tracked_fields, "%s\n", habitArray[i].name);

            printf("\nHabits:\n");
            for(int i = 0; i < num_habits; ++i) {
                printf("%s\n", habitArray[i].name);
            }
        }
    }

    //Clean up
    for (int i = 0; i < num_habits; ++i) {
        fclose(habitArray[i].data);
    }

    free(habitArray);
    free(tracked_habits);
    fclose(tracked_fields);

}