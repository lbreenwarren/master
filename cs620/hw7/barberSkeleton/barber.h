/* CS620
 * File: barber.h
 * Author: Bo-Cheng Cui
 * Desc: This is the header file for barber problem.
 *       You should not modify this file.
*/

#ifndef BARBER_H
#define BARBER_H

#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>
#include <unistd.h>
#include <math.h>
#include <sys/types.h>
#include <sys/timeb.h>
#include <sys/time.h>
#include <pthread.h>
#include <semaphore.h>
#include <stdatomic.h>
#include <stdbool.h>

#define  MAX_CUSTOMERS  1000  //Maximum number of customers 
#define  ERROR      -1 

extern int duration_s; //duration of the simulation in seconds
extern int mean_inter_arrival_time_s; //mean time between arrivals of customers in seconds
extern int mean_service_time_s; //mean service time in seconds
extern int num_chairs_total; //Total waiting chairs plus barber chair

long get_current_time();
void readFromInputFile(FILE *file);
void printConfig();
void simulator();
#endif // BARBER_H