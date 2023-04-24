#include <stdio.h>
#include <stdlib.h>

int calSpan(int maxSize, char *filename, double data[]){
     
     FILE *infilep;
     infilep = fopen (filename, "r");
        
        if (infilep == NULL){
         return -1;
        }

    int i = 0;
    char charData[6];

      while(((fscanf(infilep, "%s", charData)) != EOF) && i <= maxSize) { 
       
         if(atof(charData) == 0){
          data[i] = -200;
         }

         else{
          data[i] = atof(charData);
         }

       ++i; 
      
      }
    
    fclose(infilep);
        
      for (int j = 0; j<i; ++j) {
            
         if (data[j]<-100 || data[j]> 100 || data[j] == 0) {
          data[j] = -200;
         }

      }

     int queryMinute = 0;
     puts("Which minute to query?");
     scanf("%d", &queryMinute);
     int minuteHigh = 0;

        if(data[queryMinute] < -100){
         printf("Data at minute %d is corrupted", queryMinute);
        }

        else{
         int j = queryMinute;
     
            while ((data[j] > data[j-1]) && (j != 0)) {
             ++minuteHigh;
             --j;
            }
         printf("Data at minute %d is a %d-minute(s) high", queryMinute, minuteHigh);
        }

     return i;
}

int main() {
    puts("input file name");
    int maxSize = 1000;
    char filename[10];
    double data[maxSize];
    scanf("%s", filename);
    calSpan(maxSize, filename, data);
}