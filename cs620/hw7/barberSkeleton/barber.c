/* CS620
 * File: barber.c
 * Author: Bo-Cheng Cui
 * Desc: This is the skeleton file for barber problem.
 *       You implement the functions in this file.
 *       And feel free to add more functions as you need.
*/

#include "barber.h"

// the number of jobs, second line in the input file.
int numJob=0;
int duration_s; //duration of the simulation in seconds
int mean_inter_arrival_time_s; //mean time between arrivals of customers in seconds
int mean_service_time_s; //mean service time in seconds
int num_chairs_total; //Total waiting chairs plus barber chair
pthread_t  *threadA[MAX_CUSTOMERS];/* max 1000 customer threads can be made */
pthread_t  threadB; /* one barber thread */
long startTime;
atomic_int count;
sem_t sem_empty_chairs;
sem_t sem_full_chairs;
sem_t sem_barber_chair;

typedef struct {
    int  index;
    long ttime;
} ThreadInfo;

static int generateRandom(int factor)
{
    double random;

    /* random number generated must not be a 0 */
    while ((random = drand48()) == 0);
    /* Generated an exponentially distributed random variable with rate as 1/arrival  */
    return ((int)(-factor) * log(random));
}

void readFromInputFile(FILE *infile){
    char line[64];
    if (fgets(line, sizeof(line), infile) != NULL)
        duration_s = atoi(line);
    if (fgets(line, sizeof(line), infile) != NULL)
        mean_inter_arrival_time_s = atoi(line);
    if (fgets(line, sizeof(line), infile) != NULL)
        mean_service_time_s = atoi(line);
    if (fgets(line, sizeof(line), infile) != NULL)
        num_chairs_total = atoi(line);
}

// the barber thread
void barberThread(void *arg){
    for(;;) {
        if (sem_trywait(&sem_full_chairs) == 0) {
            sleep(mean_service_time_s);
            sem_post(&sem_barber_chair);
            sem_post(&sem_empty_chairs);
            
        } else {
            if (get_current_time() >= (startTime + duration_s)) {
                pthread_exit(NULL);
            }
        }
        
    }
}

//  the customer thread
void customerThread(void *arg){
    ThreadInfo *tInfo = (ThreadInfo*) arg;
    
    printf("Customer %d arrives at time %ld\n", tInfo->index, tInfo->ttime);
    if (sem_trywait(&sem_empty_chairs) == 0) {
        sem_post(&sem_full_chairs);
        sem_wait(&sem_barber_chair);
        printf("Customer %d leaves barber shop at time %ld\n", tInfo->index, get_current_time() - startTime);
    } else {
        printf("Cusotmer %d leaves since no vacant seats\n", tInfo->index);
    }
    pthread_exit(NULL);
}

void simulator(){
    //Initialize all semaphores
    sem_init(&sem_empty_chairs, 1, num_chairs_total);
    sem_init(&sem_full_chairs, 1, 0);
    sem_init(&sem_barber_chair, 1, 0);

    long seed_value = get_current_time();  /* seconds since Jan. 1, 1970 */
  	srand48(seed_value);  /* Initialization entry pt. for drand48 */
    startTime = get_current_time();
    long current_time = startTime;
    long endTime = startTime + duration_s;

    if (pthread_create(&threadB, NULL, (void*)barberThread, NULL) != 0) {
        perror("threadB pthread_create error\n");
        exit (ERROR);
    }

    //simulating barber and custumer problem.
    
    printf("Simulation start time = 0\n");
    printf("Barber opens shop at time 0\n");
   
    while((current_time <= endTime) && count < MAX_CUSTOMERS){
        threadA[count] = malloc(sizeof(pthread_t));
       
        ThreadInfo *tInfo = malloc(sizeof(ThreadInfo));
        tInfo->index = count;
        tInfo->ttime = current_time - startTime; 
       
        if (pthread_create(threadA[count], NULL, (void*)customerThread, (void*)tInfo) != 0) {
            perror("threadA pthread_create error\n");
            exit (ERROR);
        }
       
        //Calling program sleeps during the interarrival time.
        double exp_no = generateRandom(mean_inter_arrival_time_s);
        sleep((int)exp_no);
        current_time=get_current_time();
        ++count;
    }

    printf("******************************************************\n");
    printf("Barber will not let in any new customers, but will service seated customers.\n");
    printf("******************************************************\n");
    
    pthread_join(threadB, NULL);
   
    printf("Barber closes shop at time %ld\n", get_current_time() - startTime);
   
    for (int i = 0; i < count; ++i) {
        free(threadA[i]);
    }
}

void printConfig(){
    printf("Duration of program execution (in seconds): %d\n", duration_s);
    printf("Student mean inter-arrival time (in seconds): %d\n", mean_inter_arrival_time_s);
    printf("Mean service time of barber (in seconds): %d\n", mean_service_time_s);
    printf("Total waiting chairs plus barber chair: %d\n", num_chairs_total);
}

/* Returns the current time as seconds since Jan 1, 1970 */
long get_current_time() {
  	int    flag;
  	long   current_time;
  	struct timeval tp;
  	// struct timezone tzp;

  	flag = 0;
  	if ((flag = gettimeofday(&tp, NULL)) == ERROR) {
    		fprintf(stderr, "\ngettimeofday: error returned.\n");
    		exit (ERROR);
  	}
	current_time = (long)tp.tv_sec;  /* seconds since Jan. 1, 1970 */
  	return(current_time);
}
