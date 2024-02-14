#include "gol.h"




int main() {
    char filename[10] = "in1.txt";
    int size = 0;
    int totalOrganismsAlive;
    GridCell grid[25][25];
    int generationCount = 0;

    totalOrganismsAlive = initializeGrid(filename, grid, &size);

    printGrid(grid, size, generationCount, totalOrganismsAlive);

    int repeats = 0;
    int popCheck = 0;


    while (totalOrganismsAlive != 0 && repeats < 4) {
        
       

        GridCell buffgrid[25][25];
        popCheck = totalOrganismsAlive;
        totalOrganismsAlive = computeNextGeneration(grid, buffgrid, size);

        ++generationCount;

        

         if (totalOrganismsAlive == popCheck) {
            ++repeats;
        }
    

    }

    printStatistics(grid, size, DEATHS);
    printStatistics(grid, size, BIRTHS);
    printStatistics(grid, size, ITERATIONS);
}

    
