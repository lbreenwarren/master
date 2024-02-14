//Liam Warren
//CS420
//Program 4
//3.26.23

#include <stdio.h>
#include <math.h>
#include "dataDump.h"

int dataDump (char *filename) {

 //create file pointer to filename
 FILE *infilep;
 infilep = fopen (filename, "r");
        
    if (infilep == NULL){
     return -1;
    }

 int c;
 int i = 0;
 int bin = 0;
 int dec = 0;
   
   //read from file
   while ((c = getc(infilep)) != EOF) {
      
    //convert char to binary
    dec = 0;
    int rem = 0;
      for (int k = 0; k<8; ++k) {
         
       bin += ((c >> k) & 1 ? 1 : 0) * pow(10, (k));
         
      }

      //convert binary to decimal
      for (int j = 0; j < 8; ++j) {

       rem = bin % 10;
       bin /= 10;
       dec += rem * pow(2,j);   
      }
      
      //print decimal as hex, new line every 16 values
      if (i % 16 == 0) {
         
         if (i != 0){puts("");}

       printf("%08d", i);
      }

    printf(" %02x", dec);
    ++i;
   }

 printf("\n%08d", i);
 return i;
}

