//program to simulate the game of life
//Liam Warren
//CS420
//4.20.23

#include <stdio.h>
#define DEATHS 1
#define BIRTHS 2
#define ITERATIONS 3

typedef struct {
    int state;
    int deaths;
    int births;
    int iterations;
} GridCell;

int computeNextGeneration(GridCell [][25] , GridCell [][25], int);

int initializeGrid(char [], GridCell [][25], int *);

void printGrid (GridCell grid[][25], int size, int generationCount, int totalOrganisms);

void getCellStatistics(GridCell grid[][25], int row, int column, int *deaths, int *births, int *iterations);

void printStatistics(GridCell grid[][25], int size, int statisticToPrint);