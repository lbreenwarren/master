//Liam Warren
//02.06.2023
//CS420
//Program 1P

#include <stdio.h>
#include <time.h>
#include <stdlib.h>

int main(){
    
    //seed random function

    srand(time(NULL));

    //declaration and user propt

    int x = 0;
    int y = 0;
    int repeat = 0;
    int consecutives[6] = {0};
    printf("how many times would you like to roll?\n");
    int result = scanf("\n%d", &repeat);

    //input validation against non integer inputs

    if (result != 1){
        printf("Non-integer input. goodbye!");
        return 1;
    }

    //input validation against inputs less than 2

    while (repeat < 2){
        printf("Invalid Input, please enter an integer greater than 1.\n");
        scanf("\n%d", &repeat);
    }

        //generate random number 1-6 amount of times desired by user
        //increment index of array of ints corresponding to which number repeats everytime a that number repeats

        for(int i = 1; i<=repeat; ++i){
         
         y = x;
         x = (rand() % 6) + 1;
         printf("%d  ", x);

            if (i % 50 == 0){
                puts("");
            }
               
            if(x == y){
             
             ++consecutives[x-1];
                
            }
        }
    
    printf("\nConsecutive Count:\n");
    printf("Ones:   %d\n", consecutives[0]);
    printf("Twos:   %d\n", consecutives[1]);
    printf("Threes:   %d\n", consecutives[2]);
    printf("Fours:   %d\n", consecutives[3]);
    printf("Fives:   %d\n", consecutives[4]);
    printf("Sixes:   %d\n", consecutives[5]); 

return 0;
}