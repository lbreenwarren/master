//Liam Warren
//CS420
//Program 0P
//1.31.2023

#include <stdio.h>
#include <math.h>
#define PI 3.14159


int main()
{
    //declare variables and read axis lengths from user
    float a,b;
    printf("Input major axis:");
    scanf("%f", &a);
    printf("Input minor axis:");
    scanf("%f", &b);
    
    //declare variable and calculate ellipse circumference using Ramanjujans first apporximation
    float ramanujan1;
    ramanujan1 = PI * ((3.0*(a + b) - sqrt((3.0*a + b)*(a + 3.0*b))));
    
    //declare variable and calculate ellipse circumference using Ramanjujans second apporximation
    float ramanujan2;
    float h;
    h = (pow ((a-b),2))/(pow((a+b),2));
    ramanujan2 = PI * ((a+b) * (1.0+(3.0*h / (10.0 + sqrt((4.0-(3.0*h)))))));
    
    //declare variable and calculate ellipse circumference using Muir's formula
    float muir;
    float s = 1.5;
    muir = (2 * PI) * (pow(((pow(a,s))/2.0)+((pow(b,s))/2.0),(1.0/s)));
    
   //declare variable and calculate ellipse circumference using Hudson's formula
    float hudson;
    hudson = (PI * (a+b)) * 0.25 * (3.0*(1.0+(h/4.0))+1.0/(1.0-(h/4.0)));
    
   //declare variable and calculate ellipse circumference using Holder mean
    float holder;
    float holder_s = log(2.0)/log(PI/2.0);
    holder = 4.0 *(pow((pow(a,holder_s)+pow(b, holder_s)), (1.0/holder_s)));
    
    //declare variable and calculate ellipse circumference using David Cantrell's formula
    float cantrell;
    float cantrell_s = 0.825056;
    cantrell = (4*(a+b)) - ((2*(4 - PI))*a*b)/(pow(((pow(a,cantrell_s)/2.0) +(pow(b,cantrell_s)/2.0)),(1.0/cantrell_s)));
   
    //format and print calculations
    printf("Ellipse Circumference for Major Axis:   %.2f and Minor Axis:    %.2f\n", a,b);
    printf("+-----------------------------------------------------+\n");
    printf("|     Ramanujan's First Approximation |      %.6f|\n", ramanujan1);
    printf("+-----------------------------------------------------+\n");
    printf("|    Ramanujan's Second Approximation |      %.6f|\n", ramanujan2);
    printf("+-----------------------------------------------------+\n"); 
    printf("|                      Muir's Formula |      %.6f|\n", muir);
    printf("+-----------------------------------------------------+\n");    
    printf("|                      Hudson Formula |      %.6f|\n", hudson);
    printf("+-----------------------------------------------------+\n"); 
    printf("|                         Holder mean |      %.6f|\n", holder);
    printf("+-----------------------------------------------------+\n"); 
    printf("|            David Cantrell's formula |      %.6f|\n", cantrell);
    printf("+-----------------------------------------------------+\n"); 
    return (0);

}