#include "gol.h"

//program to simulate the game of life
//Liam Warren
//CS420
//4.20.23

int initializeGrid(char filename[], GridCell grid[][25], int *size) {

    //initialize grid of cells 

    int totalOrganismsAlive = 0;
    FILE *infilep = fopen(filename, "r");
    fscanf(infilep, "%d", &*size);
    int i = 0;
    int k = 0;

    //initilize state of cell (alive/dead)

    while((fscanf(infilep,"%d", &grid[i][k].state)) != EOF) {
        
        //initilize statistics to zero
    
        grid[i][k].births = 0;
        grid[i][k].deaths = 0;
        grid[i][k].iterations = 0;

        //increment iteration and total live organisms if cell has an organism in it 
        if (grid[i][k].state == 1) {
            ++grid[i][k].iterations;
            ++totalOrganismsAlive;
        }

        //increment through rows

        if (k == (*size - 1)) {
            ++i;
            k = 0;
        }

        //increment through column

        else {
            ++k;
        }
        
    }
    
    return totalOrganismsAlive;
}

int computeNextGeneration(GridCell origGrid[][25], GridCell newGrid[][25], int size) {

    //determine state of each cell for next generation

    int totalOrganismsAlive = 0;

    for (int i = 0; i < size; ++i) {
        
        for(int k = 0; k < size; ++k) {

            //count number of neighbors
            
            int neighborCount = 0;

            if((i != size -1) && (origGrid[i + 1][k].state == 1)) {
                ++neighborCount;
            }

            if((k != size - 1) &&  (i != size - 1) && (origGrid[i + 1][k + 1].state == 1)) {
                ++neighborCount;
            }

            if((k != size - 1) && (origGrid[i][k + 1].state == 1)) {
                ++neighborCount;
            }

            if((k != size - 1) && (i != 0) && (origGrid[i - 1][k + 1].state == 1)) {
                ++neighborCount;
            }

            if((i != 0) && (origGrid[i - 1][k].state == 1)) {
                ++neighborCount;
            }
            
            if((i != 0) && (k != 0) && (origGrid[i - 1][k - 1].state == 1)) {
                ++neighborCount;
            }

            if((k != 0) && (origGrid[i][k - 1].state == 1)) {
                ++neighborCount;
            }

            if((i != size - 1) &&  (k != 0) && (origGrid[i + 1][k - 1].state == 1)) {
                ++neighborCount;
            }


            //compute state of cells in next generation based on number of neighboors

            if((origGrid[i][k].state ==  1) && (neighborCount <= 1)) {
               
                newGrid[i][k].state = 0;
                ++origGrid[i][k].deaths;
               
            }

            if((origGrid[i][k].state ==  1) && (neighborCount >= 4)) {
               
                newGrid[i][k].state = 0;
                ++origGrid[i][k].deaths;
                
            }

            if((origGrid[i][k].state ==  1) && ((neighborCount == 2) || (neighborCount==3))) {
               
                newGrid[i][k].state = 1;
                ++origGrid[i][k].iterations;
                ++totalOrganismsAlive;

            }

            if((origGrid[i][k].state ==  0) && (neighborCount == 3)) {
            
                newGrid[i][k].state = 1;
                ++origGrid[i][k].births;
                ++origGrid[i][k].iterations;
                ++totalOrganismsAlive;
            
            }

            if (origGrid[i][k].state == 0 && neighborCount != 3) {
                newGrid[i][k].state = 0;
            }

        }
    }

    //set state of each cell to that of the new generation

    for (int i = 0; i < size; ++i) {
        
        for (int k = 0; k < size; ++k) {
            origGrid[i][k].state = newGrid[i][k].state;
        }
    }
    return totalOrganismsAlive;
    
}

void printGrid (GridCell grid[][25], int size, int generationCount, int totalOrganisms) {

    //print and format grid

    printf("    #### GENERATION %d #### Total Organisms: %d ####\n", generationCount, totalOrganisms);

    for (int i = 0; i < size; ++i) {


        
        for (int j = 0; j < size; ++j) {
            printf("----");
        }
        
        printf("\n");

        for (int k = 0; k < size; ++k) {
            printf("|");
            if (grid[i][k].state == 1) {
                printf(" x ");
            }
            else {
                printf("   ");
            }
        
        }

        printf("|\n");
    
    }
    
    for (int j = 0; j < size; ++j) {
        printf("----");
    }

    printf("\n");
}

void getCellStatistics(GridCell grid[][25], int row, int col, int *deaths, int *births, int *iterations) {

    //retrieve cell statistics

    *deaths =  grid[row][col].deaths;
    *births =  grid[row][col].births;
    *iterations =  grid[row][col].iterations;
}

void printStatistics(GridCell grid[][25], int size, int statisticToPrint){

    //print and format statistics

    if(statisticToPrint == DEATHS) {
        printf("    #### Final Death Statistics ####\n");
    } 
    if(statisticToPrint == BIRTHS) {
        printf("    #### Final Birth Statistics ####\n");
    } 

    if(statisticToPrint == ITERATIONS) {
        printf("    #### Final Iteration Statistics ####\n");
    } 

    for (int i = 0; i < size; ++i) {
            
        for (int j = 0; j < size; ++j) {
            printf("-----");
        }
            
        printf("\n");

        for (int k = 0; k < size; ++k) {
            int deaths = 0;
            int births = 0;
            int iterations = 0;
            getCellStatistics(grid, i, k, &deaths, &births, &iterations);
            printf("|");

            if (statisticToPrint == DEATHS) {
                printf("%3d ", deaths);
            }

            if (statisticToPrint == BIRTHS) {
                printf("%3d ", births);
            }
                
            if (statisticToPrint == ITERATIONS) {
                printf("%3d ", iterations);
            }
  
            
        }

        printf("|\n");
        
    }
    
    for (int j = 0; j < size; ++j) {
        printf("-----");
    }
    printf("\n");
}
     

