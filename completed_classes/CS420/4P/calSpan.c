#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "calSpan.h"


int calSpan(int maxSize, char *filename, double data[]){
     
   //create file pointer to filename
     FILE *infilep;
     infilep = fopen (filename, "r");
        
        if (infilep == NULL){
         return -1;
        }

     int i = 0;
     char charData[6];

      //read from file and convert to float, if atof returns zero set data to -200
      while(((fscanf(infilep, "%s", charData)) != EOF) && i <= maxSize) { 
      
         if (atof(charData) == 0){

          data[i] = -200;
         
         }

         else{

          data[i] = atof(charData);
         
         }

       ++i; 
      
      }
    
    fclose(infilep);
      
      //verify data is between -100 and 100 and not zero, set inadequate data to -200
      for (int j = 0; j<i; ++j) {
            
         if (data[j]<-100 || data[j]> 100 || data[j] == 0) {
          data[j] = -200;
         }
      }

     char query[5];
     int queryMinute = 0;
     puts("Which minute to query?");
     scanf("%s", query);

      //repetitively ask user for a minute to query, end loop if exit is inputted
      while (strcmp(query, "exit") != 0){

       int minuteHigh = 0;
       queryMinute = atoi(query);
    
         if(data[queryMinute] < -100){
          printf("Data at minute %d is corrupted", queryMinute);            
         }

         else{
          int j = 0;
            
            //check minutes before untill a higher number is found print count as minute high
            while ((data[queryMinute] >= data[queryMinute - j]) && (j <= queryMinute)) {
             ++minuteHigh;
             ++j;
            }
                
          printf("Data at minute %d is a %d-minute(s) high", queryMinute, minuteHigh);
         }

       puts("\nWhich minute to query?");
       scanf("%s", query);
      }


    return i;
}

