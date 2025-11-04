//
// i2f: convert 32-bit two's complement integer to IEEE
// single-precision floating point.
//
// Remember when you implement the function you may not use the
// float or double types. Everything must be done using only integer
// types.

#include <stdint.h>
#include <inttypes.h>
#include <stdio.h>

// i2f
//   Parameter: 32-bit two's complement integer value.
//   Return value: 32-bit value that should be interpreted as an IEEE
//     single-precision floating-point value produced converting the
//     parameter to floating point.
//   Note: This routine should duplicate what the Intel 64 hardware
//     does when converting ints to floats.
uint32_t i2f(int32_t in)
{   
    if (in == 0) {
        return 0;
    }
    
    uint32_t sign = 0;
    
    if (in < 0) {
        sign = 1;
        in = in * -1;
    }

    //find the most significant bit, actual exponent value will be 31 minus the bit place of the MSB 
    uint32_t msb = 1 << 31;
    uint32_t exp = 31;

    while ((msb & in) == 0) {
        --exp;
        msb >>= 1;
    }

    //stored exponent = actual exponent + 127
    uint8_t stored_exp = exp + 127;

    //Flip the most significant bit, msb var will be all zeros except the first bit of the significand, so ~msb is all 1's except the MSB of the significand
    uint32_t out = (in & (~msb)); 
    //edge case for only 1 truncated bit (round to even)
    if (exp == 24) {

        if ((out & 3) == 3) {
            out >>= 1;
            ++out;
            
            if (out == 0x800000) {
                    ++stored_exp;
                    out = 0;
                } 
            
        } else {
            out >>= 1;
        }
        exp = 23;
    }
   
    //rounding
    if (exp > 24) {
        
        uint8_t sticky_bits = 0;
        uint8_t guard_bits = 0;
        int num_tuncd_bits = exp - 23;
        uint16_t truncd_bits = out & ((1 << num_tuncd_bits) - 1);
        
        guard_bits = truncd_bits >> (num_tuncd_bits - 2);
        sticky_bits = truncd_bits & ((1 << (num_tuncd_bits - 2)) - 1);
        out >>= (exp - 23);
        exp = 23;
      
        if (guard_bits >= 2) {
            if ((sticky_bits > 0) || ((sticky_bits == 0) && ((out & 0x1) == 1)) || (guard_bits == 3)) {
                
                ++out;
            
                if (out == 0x800000) {
                    ++stored_exp;
                    out = 0;
                } 
            }
        }
    }

    //patch it together
    out <<= (23 - exp);
    out |= (stored_exp << 23);
    out |= (sign << 31);

    return out;
}
