#include <stdio.h>
#include <math.h>
int main()
{
    float a,b;
    printf("Input major axis:");
    scanf("%f", &a);
    printf("Input minor axis:");
    scanf("%f", &b);
    float PI = 3.14159;
  
    //compute circumference using ramanujan's 1st equation
    float ramanujan1;
        ramanujan1 = PI * ((3.0*(a + b) - sqrt((3.0*a + b)*(a + 3.0*b))));
          printf("1) %.6f", ramanujan1);
 
    //compute circumference using ramanujan's 2nd equation
        float ramanujan2;
        float h;
          h = (pow ((a-b),2))/(pow((a+b),2));
          ramanujan2 = PI * ((a+b) * (1.0+(3.0*h / (10.0 + sqrt((4.0-(3.0*h)))))));
            printf("\n2) %.6f", ramanujan2);
  
    //compute circumference using muir's equation
        float muir;
        float s = 1.5;
          muir = (2 * PI) * (pow(((pow(a,s))/2.0)+((pow(b,s))/2.0),(1.0/s)));
           printf ("\n3) %.6f", muir);
   
    //compute cirumference using hudson's equation
        float hudson;
          hudson = ((PI * (a+b)) / 4.0) * (3.0*(1.0+(h/4.0))+1.0/(1.0-(h/4.0)));
           printf("\n4) %.6f", hudson);
    
    //compute circumference using holder's equation
        float holder;
        float s2;
          s2 = log10(2.0) / log10((PI/2.0));
          holder = 4 * pow(((pow(a,s2)+pow(b,s2))),(1.0/s2));
           printf("\n5) %.6f", holder);
    
    //compute circumference using cantrell's equation
        float cantrell;
        float s3 = 0.825056;
          cantrell = (4.0 * (a+b)) - ((2.0 * (4.0 - PI)) * a * b) / (pow((pow(a,s3))/2.0 + (pow(b,s3)/2.0),(1.0/s3)));
           printf("\n6) %.6f", cantrell);

printf("\n\n\n Ellipse Circumference for Major Axis:   %.2f and Minor Axis:   %.2f", a, b);
printf("\n+------------------------------------------------------------+");
printf("\n|      Ramanujan's First Approximation |           %.6lf|", ramanujan1);
printf("\n+------------------------------------------------------------+");
printf("\n|     Ramanujan's Second approximation |           %.6f|", ramanujan2);
printf("\n+------------------------------------------------------------+");
printf("\n|                       Muir's Formula |           %.6f|", muir);
printf("\n+------------------------------------------------------------+");
printf("\n|                       Hudson Formula |           %.6f|", hudson);
printf("\n+------------------------------------------------------------+");
printf("\n|                          Holder mean |           %.6f|", holder);
printf("\n+------------------------------------------------------------+");
printf("\n|             David Cantrell's formula |           %.6f|", cantrell);
printf("\n+------------------------------------------------------------+");

    return (0);
}
