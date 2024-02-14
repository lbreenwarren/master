//Liam Warren
//CS420
//Program 5
//4.7.23

#include <stdio.h>
#include <assert.h>
#include "bitFunctions.h"

int main() {
    
    //assert tests for functions reverse_it, circular_shift_left, every_other_bit, and date_to_binary

    assert(reverse_it(0x000000f8) == 0x1f000000);
    assert(reverse_it(654) == 0x71400000);
    assert(reverse_it(0xffffffff) == 0xffffffff);
    assert(circular_shift_left(654,4 == 0x28e0));
    assert(circular_shift_left(0x0, 10) == 0x0);
    assert(circular_shift_left(0xf0f0f0f0, 32) == 0xf0f0f0f0);
    assert(every_other_bit(0xf0f0f0f0) == 0xcccc);
    assert(every_other_bit(0x55555555) == 0xffff);
    assert(every_other_bit(0xaaaaaaaa) == 0x0);
    assert(date_to_binary(12, 7, 80) == 0x63d0);
    assert(date_to_binary(2, 3, 4 == 0x1184));

    //explicit test for date_from_binary

    int day = 0;
    int month = 0;
    int year = 0;
    date_from_binary(0x1184, &day, &month, &year);
    
    if (day != 2 || month != 3 || year != 4) {
        printf("date_from_binary(0x1184) failed");
        return 1;
    }

    day = 0;
    month = 0;
    year = 0;
    date_from_binary(0x63d0, &day, &month, &year);
    
    if (day != 12 || month != 7 || year != 80) {
        printf("date_from_binary(0x63d0) failed");
        return 1;
    }
    
}