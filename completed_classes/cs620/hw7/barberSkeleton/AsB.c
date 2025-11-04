// compile using gcc AsB.c -o asb
// if program does not compile, use gcc AsB.c -lrt -lpthread -lm -o asb

/* This code lists the thread, semaphore, random number generator , atomic variable,
   and time system calls you need for the assignment.
   the code is not meaningul; the purpose is to just list all the system calls you need for the assignment.
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <pthread.h>
#include <semaphore.h>
#include <sys/timeb.h>
#include <sys/time.h>
#include <math.h>
#include <stdatomic.h>


#define  MAX        10  
#define  ERROR      -1
#define  FALSE      0
#define  TRUE       1


void   *procThreadA(void *arg);
void   *procThreadB(void *arg);

double drand48();
void   srand48(long seedval);
long   get_current_time();
int    generateRandom(int factor);

//global variables
sem_t  mutexa;  
sem_t  semb; 	 
atomic_int count=0; //keeps count of the number of threads created 

typedef struct {
    int  index;
    long ttime;
} ThreadInfo;



int main(int argc, char *argv[])
{
	pthread_t  *threadA[100];
	pthread_t  threadB;               

        long       start_time;
        long       end_time;
        long       current_time;
        long       simul_time;
        double     random, exp_no;
        long       seed_value;     /* Initialization of random generator */
        int        arrival;
        long       duration;


	//Initialize all semaphores here.
	sem_init(&mutexa, 1, 0);
        sem_init(&semb, 1, 2);

        // Create ThreadB
        if (pthread_create(&threadB, NULL, procThreadB, NULL) != 0) {
                perror("threadB pthread_create error\n");
                exit (ERROR);
        }

        /************* MODEL SIMULATION ************************************/
        /* Initialize random sequence */
        seed_value = get_current_time();  /* seconds since Jan. 1, 1970 */
        srand48(seed_value);  /* Initialization entry pt. for drand48 */
        
        duration=30;
        arrival=2;

        /* Start of main simulation */
        printf("Simulation start time = 0\n");
        current_time = get_current_time();
        end_time = current_time + (duration);
        start_time = current_time;

        while (current_time <= end_time) {
            threadA[count] = malloc(sizeof(pthread_t));
            ThreadInfo *tInfo = malloc(sizeof(ThreadInfo));
            tInfo->index = count;
            tInfo->ttime = current_time - start_time;  
            if (pthread_create(threadA[count], NULL, procThreadA, (void*)tInfo) != 0) {
                  perror("threadA pthread_create error\n");
                  exit (ERROR);
            }

            //Calling program sleeps during the interarrival time.
            exp_no = generateRandom(arrival);
            sleep((int)exp_no);

            current_time = get_current_time();
	    count++;
        }

	printf("\n******************************************************\n");
	printf("	End of run.			      \n");
	printf("******************************************************\n");
	exit(0);
}

// ThreadA
void *procThreadA(void *arg)
{
        int i;
        ThreadInfo *tInfo;


        tInfo = (ThreadInfo *)arg;
        sem_wait(&mutexa);
        printf("Athread index=%d; time=%ld ", tInfo->index, tInfo->ttime);
        sem_post(&semb);
        free(tInfo);
        pthread_exit(NULL);
}

// ThreadB
void *procThreadB( void *arg )
{
        int i;
        void *value_ptr;        
 
        for (;;) {
            sem_wait(&semb);
            sem_wait(&semb);
            printf("\nnumber of A threads=%d\n",count+1);
            sem_post(&mutexa);
            sem_post(&mutexa);
        }
}

int generateRandom(int factor)
{
    double random;

    /* random number generated must not be a 0 */
    while ((random = drand48()) == 0);
    /* Generated an exponentially distributed random variable with rate as 1/arrival  */
    return ((int)(-factor) * log(random));
}


/* Returns the current time as seconds since Jan 1, 1970 */
long get_current_time()
{
        int    flag;
        long   current_time;
        struct timeval tp;
        struct timezone tzp;

        flag = 0;
        if ((flag = gettimeofday(&tp, &tzp)) == ERROR) {
                fprintf(stderr, "\ngettimeofday: error returned.\n");
                exit (ERROR);
        }
        current_time = (long)tp.tv_sec;  /* seconds since Jan. 1, 1970 */
        return(current_time);
}

