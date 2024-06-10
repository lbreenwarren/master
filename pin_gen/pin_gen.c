#include <stdio.h>
#include <stdlib.h>
#include <time.h>
#ifdef _WIN32
//  For Windows (32- and 64-bit)
#   include <windows.h>
#   define SLEEP(msecs) Sleep(msecs)
#elif __unix
//  For linux, OSX, and other unixes
#   define _POSIX_C_SOURCE 199309L // or greater
#   include <time.h>
#   define SLEEP(msecs) do {            \
        struct timespec ts;             \
        ts.tv_sec = msecs/1000;         \
        ts.tv_nsec = msecs%1000*1000;   \
        nanosleep(&ts, NULL);           \
        } while (0)
#else
#   error "Unknown system"
#endif
 
#include <stdio.h>

int gen_new_pin() {
    srand(time(NULL));
    int seed_upper = rand();
    int seed_lower = rand();
    int seed = (seed_upper << 16) | seed_lower;
    srand(seed);
    int pin = rand();
    return seed;
}

int get_curr_pin(int seed, int timeout) {
    int min = timeout - 1;

    // if (timeout < 5) {
    //     printf("You are a cheater and cheaters must wait");
    //     timeout = 60;
    // }
    
    printf("\tWaiting %d Minutes:\n", timeout);
    for (int i = 1; i <= timeout * 60; ++i) {
        SLEEP(1000);
        printf("\r\t%d:%02d ", min, 60 - (i % 60));
        if (i % 60 == 0) {
            --min;
        }
        
        fflush(stdout);
    }
    srand(seed);
    return rand() % 10000;
}

int main() {
    FILE *fp;
    int timeout_mins = 20;
    fp = fopen("timeout.txt", "r");
    if (fp != NULL) {
        fscanf(fp, "%d", &timeout_mins);
    } else {
        fp = fopen("./timeout.txt", "w");
        fprintf(fp, "20");
    }
    fclose(fp);
    printf("\n===============================");
    printf("\nOPTIONS:");
    printf("\nn: Generate new PIN");
    printf("\ng: Get current PIN");
    printf("\nm: Set Timeout (Defaults to 20 minutes)");
    printf("\nt: Test");
    printf("\nh: Help");
    printf("\nx: Exit");
    printf("\n===============================\n\n");
    printf("> ");

    char opt;
    scanf("%c", &opt);
    
    while (opt != 'x') {
            if (opt == 'n') {

                int new_seed = gen_new_pin();
               
                FILE *fp;
                fp = fopen("./seed.txt", "w");
                fprintf(fp, "%d", new_seed);
                
                srand(new_seed);
                printf("\tNEW PIN: %04d\n\n", (rand() % 10000));
                fclose(fp);

            } else if (opt == 'g') {
                
                FILE *fp;
                fp = fopen("seed.txt", "r");
                int seed;
                fscanf(fp, "%d", &seed);
                
                int pin = get_curr_pin(seed, timeout_mins);
                
                printf("\n\tCURRENT PIN: %04d\n\n", pin);
                fclose(fp);

            } else if (opt == 'm') {
                
                printf("\tCURRENT TIMEOUT: %d Minutes", timeout_mins);
                timeout_mins = 0;

                while (timeout_mins < 5){
                    printf("\n\nInput timeout (Minutes):\n> ");
                    scanf("%d", &timeout_mins);
                    if (timeout_mins < 5) {
                        printf("\n\tTimeout cannot be less than 5 minutes");
                    }
                }
                
                FILE *fp;
                fp = fopen("./timeout.txt", "w");
                fprintf(fp, "%d", timeout_mins);
                fclose(fp);

                printf("\tTimeout set to %d minutes\n\n", timeout_mins);
                
            } else if (opt == 'h') {

                printf("\t------------------------------------------------------------------\n");
                printf("\t|  Generate a random PIN to use with a distrcating app blocker.  | \n");
                printf("\t------------------------------------------------------------------\n\n");
                
                printf("\tUse the n option to instantly generate a new PIN\n");
                printf("\tUse the g option to retrieve the most recently generated key after a 20 minute wait while you think about your choices in life, if you change your mind press ctrl/cmd C to exit the program\n");
                printf("\tUse the m option to set a desired timeout to a minimum of 10 minutes\n");
                printf("\tUse the t option to test the timeout\n");
                printf("\tUse x to exit\n\n");
                
                printf("\t***In case of emergency call me for a backdoor to skip timeout.***\n\n");

            } else if (opt == 't') {
                printf("\tWaiting 10 seconds then printing \"TEST\"\n");
                SLEEP(10000);
                printf("\n\tTEST\n\n");
            
            }else if(opt == 'j'){
                printf("\tInput Seed:\n> ");
                int seed;
                scanf("%d", &seed);
                srand(seed);
                int pin = rand();
                printf("\n\tCURRENT PIN: %d\n\n", pin % 10000);
            
            } else {
                printf("\tUnrecongnized Option\n\n");
            }

        printf("> ");
        scanf(" %c", &opt);
    }
}