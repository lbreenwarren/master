//Liam Warren
//CS420
//Program 5
//4.7.23

#include <stdio.h>
#include <stdlib.h>
#include <math.h>

void printIntasBin (unsigned int bin) {
    
    //helper function to print binary representation of integer for bug testing

    int out = 0;
    for (int i = 0; i < 32; ++i) {

        out =  (bin & 0x80000000 ? 1 : 0);
        printf("%d", out);

        if (i % 4 == 3){
            printf(" ");
        }

        bin = bin << 0x1;
    }

    puts("");
}


unsigned int circular_shift_left(unsigned int bin, int n){
    
    /*
    Arguments: 
    (1)	input: unsigned integer, value of the number to shift
    (2)	input: integer, number of bits to shift left
    Function return value: unsigned integer, new value after circular shift left has been performed
    */
    
    for (int i = 0; i < n; ++i) {

        int MSB = 0;

        if (bin & 0x80000000) {
            MSB = 1;
        }

        bin = bin << 1;

        if (MSB == 1) {
            bin = bin + 1;
        }
    
    }
    
    return bin;
}

unsigned int reverse_it(unsigned int bin){
   
    /*
    Arguments: 
    (1)	input: unsigned integer, value of the number to be reversed
    Function return value: unsigned integer, reversed value
    */

    int binRev = 0;

    for (int i = 0; i<32; ++i){
        int add = (bin & 0x80000000 ? 1 : 0) * (pow(2,i));
        binRev += add;
        bin = bin << 1;
    }

    return binRev;

}


unsigned short int every_other_bit(unsigned int bin) {
   
    /*
    Arguments: 
    (1)	input: unsigned integer, value of the number whose bits are to be extracted
    Function return value: unsigned short integer, extracted bits which is half the size of the original
    */

    unsigned short int everyOther = 0;
    for (int i = 0; i < 32; ++i) {

        if (i % 2 == 0) {
            everyOther += (bin & 0x1 ? 1 : 0) * pow(2, (i/2));
        }

        bin = bin >> 1;

    }

    return everyOther;
}

unsigned short int date_to_binary(int day, int month, int year){

    /*
    Arguments: 
    (1)	input: integer, day value
    (2)	input: integer, month value
    (3)	input: int, year value
    Function return value: unsigned short integer, date packed into a 16-bit value 
    */

    unsigned short int binDate = 0;

    for (int i = 0; i < 7; ++i) {
        binDate +=  (year & 0x1 ? 1 : 0) * pow(2,i);
        year =  year >> 1;
    }

    for (int i = 7; i < 11; ++i) {
        binDate +=  (month & 0x1 ? 1 : 0) * pow(2,i);
        month = month >> 1;
    }

    for (int i = 11; i < 16; ++i) {
        binDate +=  (day & 0x1 ? 1 : 0) * pow(2,i);
        day =  day >> 1;
    }

   
    return binDate;
   
}

void date_from_binary(unsigned short int date, int *day, int *month, int *year){

    /*
    Arguments: 
    (1)	input: unsigned short integer, date packed into a 16-bit value
    (2)	output: integer, comes back with day value
    (3)	output: integer, comes back with month value
    (4)	output: integer, comes back with year value
    Function return value: void
    */


    for (int i =  0; i < 7; ++i){
        *year += (date & 0x1 ? 1 : 0) * pow(2, i);
        date =  date >> 1;
    }

    int k = 0;

    for (int i = 7; i < 11; ++i) {
        *month += (date & 0x1 ? 1 : 0) * pow(2,k);
        ++k;
        date =  date >> 1;
    }

    int j = 0;

    for (int i = 11; i < 16; ++i) {
        *day += (date & 0x1 ? 1 : 0) * pow(2, j);
        ++j;
        date =  date >> 1;
    }

}   



