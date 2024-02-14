//Liam Warren
//CS420
//Program 3
//3.4.23


#include <stdio.h>
#include "stats.h"

int main(int argc, char *argv[]){

    //declaraions
    char player[MAXIMUM];
    int field_goals[MAXIMUM];
    int three_pts[MAXIMUM];
    int free_throws[MAXIMUM];
    float avg_fg;
    float avg_tpt;
    float avg_ft;

    //read stats from file and compute averages
    int size = read_data(argv[1], player, field_goals, three_pts, free_throws);
    float avg_pts = compute_stats(field_goals, three_pts, free_throws, size, &avg_fg, &avg_tpt, &avg_ft);

    //print and format
    printf("Statistics for: %s\n", player);
    printf("Total games played: %d\n", size);
    printf("Season Averages: Field Goals: %.2f   3 Points: %.2f Free Throws: %.2f  Points per game: %.2f", avg_fg, avg_tpt, avg_ft, avg_pts);
}