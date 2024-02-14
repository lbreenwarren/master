//bitfunctions,h
//function proto-type declarations
void printIntasBin (unsigned int bin);

unsigned int circular_shift_left(unsigned int bin, int n);
/*
Arguments: 
(1)	input: unsigned integer, value of the number to shift
(2)	input: integer, number of bits to shift left
Function return value: unsigned integer, new value after circular shift left has been performed
*/

unsigned int reverse_it(unsigned int);
/*
Arguments: 
(1)	input: unsigned integer, value of the number to be reversed
Function return value: unsigned integer, reversed value
*/

unsigned short int every_other_bit(unsigned int);
/*
Arguments: 
(1)	input: unsigned integer, value of the number whose bits are to be extracted
Function return value: unsigned short integer, extracted bits which is half the size of the original
*/

unsigned short int date_to_binary(int, int, int);
/*
Arguments: 
(1)	input: integer, day value
(2)	input: integer, month value
(3)	input: int, year value
Function return value: unsigned short integer, date packed into a 16-bit value 
*/

void date_from_binary(unsigned short int, int *, int *, int *);
/*
Arguments: 
(1)	input: unsigned short integer, date packed into a 16-bit value
(2)	output: integer, comes back with day value
(3)	output: integer, comes back with month value
(4)	output: integer, comes back with year value
Function return value: void
*/
