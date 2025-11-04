// compile using gcc AAB.c -o aab. 
// If it does not work, use gcc AAB.c -lrt -lpthread -lm -o aab

/* This code lists some semaphore system calls; look at AsB.c for a more complete list of calls.
*/

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/types.h>
#include <pthread.h>
#include <semaphore.h>

#define  MAX        10  
#define  ERROR      -1
#define  FALSE      0
#define  TRUE       1


void   *procThreadA(void *arg);
void   *procThreadB(void *arg);

sem_t  mutexa[2];  //the two A threads use sem[0] and sem[1]		
sem_t  semb; 	 //

int main(int argc, char *argv[]) {
	pthread_t  threadA[2];
	pthread_t  threadB;               
	void       *valueA0_ptr, *valueA1_ptr;
        int        i, threadindex[2]={0,1};


	//Initialize all semaphores here.
        for(i=0; i<2; i++)
	   if (sem_init(&mutexa[i], 1, 1) == -1) { 
		perror("Could not initialize mutexa\n");
		exit (ERROR);
	   } 

        if (sem_init(&semb, 1, 0) == -1) {
                perror("Could not initialize semb\n");
                exit (ERROR);
        } 
        
	// Create two ThreadA
        for (i=0; i<2; i++)
	   if (pthread_create(&threadA[i], NULL, procThreadA, (void*)&threadindex[i]) != 0) {
		perror("threadA pthread_create error\n");
		exit (ERROR);
	   }


        // Create ThreadB
        if (pthread_create(&threadB, NULL, procThreadB, NULL) != 0) {
                perror("threadB pthread_create error\n");
                exit (ERROR);
        }

        pthread_join(threadA[0], &valueA0_ptr);
        pthread_join(threadA[1], &valueA1_ptr);
        pthread_join(threadB, NULL);

	printf("\n******************************************************\n");
	printf("	End of run.			      \n");
	printf("******************************************************\n");

	exit  (0);
}

// ThreadA
void *procThreadA(void *arg) {
        int i;
        int index;
        void *value_ptr;

        index = (*(int *)arg);
        //printf("In threadA[%d]\n", index);

	for (i=0;i<MAX;i++) {
		if (sem_wait(&mutexa[index]) == -1) {
			perror("Invalid mutex wait\n");
			exit (ERROR);
		}  

                printf("%d",index);

		if (sem_post(&semb) == -1) {
                        perror("Semaphore semb error\n");
                        exit (ERROR);
                }
   	}
        pthread_exit(&value_ptr);
}

// ThreadB
void *procThreadB( void *arg ) {
        int i;
        void *value_ptr;        
 
        //printf("In threadB\n");
	// removed error checks; difficult to read code with all the error checks.
        for (i=0;i<MAX;i++) {
                sem_wait(&semb);  
                sem_wait(&semb);
                printf("B");
                sem_post(&mutexa[0]);
                sem_post(&mutexa[1]);
        }
        pthread_exit(NULL);
}

