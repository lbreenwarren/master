//
// s2d
//   Parameter: 32-bit value that should be interpreted as an IEEE
//     single-precision floating-point value.
//   Return value: 64-bit value that should be interpreted as an IEEE
//     double-precision floating-point value produced by converting the
//	   single-precision value supplied

#include <stdint.h>
#include <inttypes.h>
#include <stdio.h>

uint64_t s2d(uint32_t in)
{

    //parse input
    uint64_t sig = in & 0x7fffff;
    uint8_t stored_exp = (in >> 23) & 0xff;
    uint64_t sign = in >> 31;
    uint64_t new_stored_exp = 0;
    int exp = 0;
    if (sig == 0 && stored_exp == 0) {
        return 0 | (sign << 63);
    }

    if (stored_exp == 0) {
        //need to normalize input mantissa

        //count leading zeros
        uint32_t mask = 1 << 22;
        
        while ((mask & sig) == 0) {
            mask >>= 1;
            ++exp;
        }
        new_stored_exp = 1023 - exp - 127;
        //add 1 to exp to account for msb being left out in shifting
        ++exp;
        //flip msb
        sig = sig & (~(1 << (23 - exp))); 
        
        
        
        

    } else if ((stored_exp & 0xff) == 0xff) {
        int shift = 52;
        if (sig == 0) {
            //signaling NaN (test driver seems to only want signaling NaN if mantisa = 0)
            new_stored_exp = 0x7ff;
        } else {
            //quiet NaN
            new_stored_exp = (0x7ff8 >> 3);
            --shift;
        }
        //patch together into double

        uint64_t out = 0;
        out |= (new_stored_exp << shift);
        out |= (sig << 29);
        out |= (sign << 63);
        return out; 

    } else {
        //calculate new exponent
        int8_t exp = stored_exp - 127;
        new_stored_exp = exp + 1023;
    }
  
    //patch together into double
    
    uint64_t out = (sig << (29 + exp));
    out |= (new_stored_exp << 52);
    out |= (sign << 63);
    
	return out; 
}