//Liam Warren
//CS420
//Program 3
//3.4.23

#include <stdio.h>
#include "stats.h"

int read_data(char *filename, char player[], int field_goals[], int three_pts[], int free_throws[]) {

 //read data from file until end of file or Max games reached
    FILE *infilep;
    infilep = fopen (filename, "r");
    if (infilep == NULL){
        return -1;
    }
    int i = 0;
    fscanf(infilep, "%s", player);
    while(((fscanf(infilep, "%d %d %d", &field_goals[i], &three_pts[i], &free_throws[i])) != EOF) && (i < MAXIMUM)){
        ++i;
    }
 return i;
}

float compute_stats(int field_goals[], int three_pts[], int free_throws[], int size, float *avg_fg, float *avg_tpt, float *avg_ft){
    //declarations
    float totalfg = 0;
    float totaltpt = 0;
    float totalft = 0;

    //sum arrays
    for (int i =0; i<size; ++i){
        totalfg += field_goals[i];
        totaltpt += three_pts[i];
        totalft += free_throws[i];
    }

    //compute total points and averages
    int totalPts = (totalfg * 2) + (totalft) + (totaltpt *3);
    *avg_fg = totalfg / size;
    *avg_tpt = totaltpt / size;
    *avg_ft = totalft / size;
    
    return (totalPts / size);
}
