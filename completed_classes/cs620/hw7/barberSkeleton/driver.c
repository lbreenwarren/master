/* CS620
 * File: driver.c
 * Author: Bo-Cheng Cui
 * Desc: This is the driver program for the shell.
 *       It reads in the command line arguments and
 *       calls the functions in barber.c
 * 
 *       You should not modify this file.
*/

#include "barber.h"

int main(int argc, char **argv)
{
    int opt;
    char *filename_in = NULL;
    FILE *file_in;
    while ((opt = getopt(argc, argv, "i:")) != -1)
    {
        switch (opt)
        {
        // -i flag to set the input file directory.
        case 'i':
            filename_in = optarg;
            break;
        default:
            fprintf(stderr, "Usage: %s -i <input file>\n", argv[0]);
            return ERROR;
        }
    }

    if (!filename_in) {
        fprintf(stderr, "Usage: %s -i <input file>\n", argv[0]);
        return ERROR;
    }

    file_in = fopen(filename_in, "r");

    if (!file_in) {
        perror("Failed to open the input file");
        return ERROR;
    }

    readFromInputFile(file_in);

    fclose(file_in);
    
    printConfig();

    simulator();

    return 0;
}