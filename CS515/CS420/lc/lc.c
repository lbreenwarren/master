//Liam Warren
//CS420
//Program 2P
//2.17.23

#include <stdio.h>
#include <math.h>
#define PI 3.14159


int main(){

    //initial declarations and user input     
     int cap1 = 0;
     int induct1 = 0;
     int cap2 = 0;
     int induct2 = 0;

     printf("\nEnter capacitance values between 2 pF and 20pF (integer values):");
     scanf("%d", &cap1);
     scanf("%d", &cap2);
     printf("\nEnter inductance values between 1 mH and 5mH (integer values)");
     scanf("%d", &induct1);
     scanf("%d", &induct2);

        //input validation
        while((cap1<2)||(cap2>20)||(cap2<cap1)||(induct1<1)||(induct2>5)||(induct2<induct1)){
         printf("\nInvalid inputs, pleas try again\n");
         printf("\nEnter capacitance values between 2 pF and 20pF (integer values):");
         scanf("%d", &cap1);
         scanf("%d", &cap2);
         printf("\nEnter inductance values between 1 mH and 5mH (integer values)");
         scanf("%d", &induct1);
         scanf("%d", &induct2);
        }

     //convert units 
     double mhen1 = induct1*pow(10, -3);
     double mhen2 = induct2*pow(10, -3);
     int count = 0;

     //calculate and format results
     printf("\nOscillating Frequency (kHz) of LC Circuits for a range of %d-%dpF and %d-%dmH", cap1, cap2, induct1, induct2); 
     printf("\n             C(pF)-->");
        
        for (int i = cap1; i<=cap2; i=i+2){
         printf("%9d", i);
        }

     printf("\n      L(H)");

        for (double i = mhen1; i<mhen2+.00025; i=i+.00025){
         printf("\n%10.5lf           ", i);

            for (double j = cap1; j<=cap2; j=j+2){
             double hz = (1.0/(2.0*PI*(sqrt(i*(j*(pow(10,-12)))))))/1000.0;
             printf("%9.0lf", hz);

                if((hz>=600)&&(hz<=1500)){
                 ++count;
                }
            
            }
        }
     printf("\nThere are a total of %d frequencies between 600kHz and 1500 kHz", count);
}