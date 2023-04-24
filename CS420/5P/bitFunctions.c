#include <stdio.h>
#include <stdlib.h>
#include <math.h>

unsigned int circular_shift_left(unsigned int bin, int n){
/*
Arguments: 
(1)	input: unsigned integer, value of the number to shift
(2)	input: integer, number of bits to shift left
Function return value: unsigned integer, new value after circular shift left has been performed
*/

bin = bin << n;
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
binRev += (bin & 0x8000) * (pow(10,i));
bin = bin << 1;
}
return binRev;

}


void main() {
    int bin = 654;
    int binRev = reverse_it(bin);
    printf("\n%x", bin);
    printf("\n%032d", binRev);
}