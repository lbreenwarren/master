//Liam Warren
//CS420
//7P
//5.6.23

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "equake.h"

/*Arguments: filename, numberOfRegions
	Function takes filename (input argument), total number of regions found  (output argument) 
	File supplied contains names of actual data files containing quake records one for each region
	Open input file specified (data.txt for example) 
		Each line in data.txt is a filename to be opened - create a linked list of region headers
			Each data file (like NorthEast.txt) contains quake records one per link
			Best to use fscanf to read items in each record and create a data node in that region's linked list 
			Repeat for every line in region input/data file until EOF
		Repeat for every data file 
	Keep track of how many regions exist and return it via output argument
  Function value return is the header to the region headers, which will lead us to the entire set of data
*/
region_header_t *readData(char *filename, int *numRegions) {
    
    FILE *infilep = fopen(filename, "r");

    region_header_t *tmp;
    region_header_t *head;
    
    *numRegions = 0;

    char region_name[30];

    //allocate first node
    tmp = (region_header_t *)malloc(sizeof(region_header_t));
    tmp->next = NULL;
    head = tmp;
    int result = (fscanf(infilep, "%s", region_name));


    //allocate and read into next node on loop untill EOF
    //make sure we dont allocate an extra node by checking for EOF before allocating
    while (result != EOF) {
        
        tmp->region_name = (char *)malloc(strlen(region_name) + 1);
        strcpy(tmp->region_name, region_name);
        result =(fscanf(infilep, "%s", region_name));
        ++*numRegions;
        if (result != EOF) {
            tmp->next = (region_header_t *)malloc (sizeof(region_header_t));
            tmp = tmp->next;
            tmp->next = NULL;

        }

    }

    

    fclose(infilep);

    tmp = head;
    equake_data_t *tmpData; 

    while(tmp != NULL) {

        infilep = fopen(tmp->region_name, "r");
        
        char timeofQuake[10];
        char location[50];

        
        //allocate first node
        tmpData=(equake_data_t *)malloc(sizeof(equake_data_t)); 
        tmp->data = tmpData;
        tmpData->next = NULL;

        //allocate and read into each node on loop
        //I cant find a way to stop the loop from allocating an extra node that doesnt break everything

        while(( fscanf(infilep, "%d%d%d%s%f%f%f%f%s", &tmpData->year, &tmpData->month, &tmpData->day, timeofQuake, &tmpData->latitude, &tmpData->longitude, &tmpData->magnitude, &tmpData->depth, location)) != EOF){
            
            tmpData->timeOfQuake=(char *)malloc(strlen(timeofQuake) + 1);
            strcpy(tmpData->timeOfQuake, timeofQuake);
            tmpData->location = (char *)malloc(strlen(location) + 1);
            strcpy(tmpData->location, location);

            tmpData->next=(equake_data_t *)malloc(sizeof(equake_data_t)); 
            tmpData = tmpData->next;
            tmpData->next = NULL;
            
        }
        
        tmp=tmp->next;
        fclose(infilep);
    }

    return head;
}



/*Arguments: header pointer to region headers, filename
	Function takes header pointing to the region headers (input argument), filename for creating output file (input argument)
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Keep track of all the statistics needed for the region
		Write the summary of statistics onto the output file
	Repeat for every region
	Close output file
  Function value return 0 if output file open/write fails or 1 if file open/write is successful
*/
int printSummary(region_header_t *head, char *outfile) {
    
    FILE *infilep = fopen(outfile, "w");

    if(infilep == NULL) {
        puts("file did not open correctly");
        return 0;
    }

    region_header_t *tmp = head;

    while (tmp != NULL) {
        
        equake_data_t *dataTmp = tmp->data;
        int totalQuakes = 0;
        float maxLat = 0;
        float minLat = 180;
        float maxLong = 0;
        float minLong = 180;
        float maxDepth = 0;
        float minDepth = 500;
        float maxMag = 0;
        float minMag = 10;
        equake_data_t *highestmagquake = NULL;

        while (dataTmp->location != NULL) {

            //calculate latitiude, longitude, magnitude, and depth range
           
            if (dataTmp->latitude > maxLat) {
                maxLat = dataTmp->latitude;
            }
            if (dataTmp->latitude < minLat) {
                minLat = dataTmp->latitude;
                //printf("%f\n", minLat);
            }
            if (dataTmp->longitude > maxLong) {
                maxLong = dataTmp->longitude;
            }
            if (dataTmp->longitude < minLong) {
                minLong = dataTmp->longitude;
            }
            if (dataTmp->depth > maxDepth) {
                maxDepth = dataTmp->depth;
            }
            if (dataTmp->depth < minDepth) {
                minDepth = dataTmp->depth;
            }
            if (dataTmp->magnitude > maxMag) {
                maxMag = dataTmp->magnitude;
            }
            if (dataTmp->magnitude < minMag) {
                minMag = dataTmp->magnitude;
                highestmagquake = dataTmp;
            }
            
            //increment total quakes for each node parsed
            ++totalQuakes;

            
            dataTmp = dataTmp->next;

        }
        //print and format results to output file
        fprintf(infilep, "\n*************************************************************************************\n");
        fflush(infilep);
        fprintf(infilep, "EARTHQUAKE SUMMARY FROM DATA FILE:%s\n", tmp->region_name);
        fflush(infilep);
        fprintf(infilep, "Geographical Range: Latitudes %.6f to %.6f, Longitudes %.6f to %.6f\n", minLat, maxLat, minLong, maxLong);
        fflush(infilep);
        fprintf(infilep, "Total Number of Earthquakes recorded: %d\n", totalQuakes);
        fflush(infilep);
        fprintf(infilep, "Depth Range (Kilometers): %.6f to %.6f\n", minDepth, maxDepth);
        fflush(infilep);
        fprintf(infilep, "Magnitude Range: %.1f to %.1f\n", minMag, maxMag);
        fflush(infilep);
        fprintf(infilep, "Largest magnitude of %.1f occured on %d/%d/%d  %s at: %s\n", highestmagquake->magnitude, highestmagquake->month, highestmagquake->day, highestmagquake->year, highestmagquake->timeOfQuake,highestmagquake->location);
        fflush(infilep);

        tmp = tmp->next;
        if (tmp == NULL) {
             puts("test");
        } 
       
    }
    fclose(infilep);
    //puts("test");
    return 1;
    
}


/*Arguments: header pointer to region headers, 2-D Array of integers
	Function takes header pointing to the region headers (input argument), 2-D array of integers filled in for daily totals (input/output argument)
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Pick up the day of quake occurence and bump up the count for that day in the 2-D array of daily records
		Continue until the end of the linked list of data nodes for that region
	Repeat for every region
*/
void buildDailyTotalsArray(region_header_t *head, int totals[][MAX_DAYS]) {
    
    region_header_t *tmp = head;
    equake_data_t *tmpData; 
    int i = 0;


    //move through each list and increment corresponding values of the array
    while (tmp != NULL) {

        tmpData = tmp->data;

        while (tmpData != NULL) {
            switch (tmpData->day) {
                case 1:
                    ++totals[i][0];
                    break;
                case 2:
                    ++totals[i][1];
                    break;
                case 3:
                    ++totals[i][2];
                    break;
                case 4:
                    ++totals[i][3];
                    break;
                case 5:
                    ++totals[i][4];
                    break;
                case 6:
                    ++totals[i][5];
                    break;
                case 7:
                    ++totals[i][6];
                    break;
                case 8:
                    ++totals[i][7];
                    break;
                case 9:
                    ++totals[i][8];
                    break;
                case 10:
                    ++totals[i][9];
                    break;
                case 11:
                    ++totals[i][10];
                    break;
                case 12:
                    ++totals[i][11];
                    break;
                case 13:
                    ++totals[i][12];
                    break;
                case 14:
                    ++totals[i][13];
                    break;
                case 15:
                    ++totals[i][14];
                    break;
                case 16:
                    ++totals[i][15];
                    break;
                case 17:
                    ++totals[i][16];
                    break;
                case 18:
                    ++totals[i][17];
                    break;
                case 19:
                    ++totals[i][18];
                    break;
                case 20:
                    ++totals[i][19];
                    break;
                case 21:
                    ++totals[i][20];
                    break;
                case 22:
                    ++totals[i][21];
                    break;
                case 23:
                    ++totals[i][22];
                    break;
                case 24:
                    ++totals[i][23];
                    break;
                case 25:
                    ++totals[i][24];
                    break;
                case 26:
                    ++totals[i][25];
                    break;
                case 27:
                    ++totals[i][26];
                    break;
                case 28:
                    ++totals[i][27];
                    break;
                case 29:
                    ++totals[i][28];
                    break;
                case 30:
                    ++totals[i][29];
                    break;
                case 31:
                    ++totals[i][30];
                    break;
                default:
                    break;
            }

            tmpData = tmpData->next;
        }

        ++i;
        tmp = tmp->next;
    } 
}


/*Arguments: 2-D Array of integers, number of regions
	Function takes 2-D array of integers filled in for daily totals (input/output argument), 
	header pointing to the region headers (input argument), number of regions/rows in 2-D array (input argument)
	Print 2-D array for daily totals
*/
void printDailyTotals(int totals [][MAX_DAYS], region_header_t *head, int numRegions) {
    region_header_t *tmp = head;

    //print and format parsed data in array

    printf("*******************************************************ARTHQUAKE TOTALS FOR THE MONTH BY REGION**************************************************");
    printf("\n-------------------------------------------------------------------------------------------------------------------------------------------------");
    printf("\n                    |");

    for (int i = 1; i < 32; ++i) {
        printf("%4d", i);
    }

    for (int i = 0; i < numRegions; ++i) {

        printf("\n-------------------------------------------------------------------------------------------------------------------------------------------------\n");
        printf("%20s|", tmp->region_name);

        for (int k = 0; k <31; ++k) {
            printf("%4d", totals[i][k]);
        }

        tmp = tmp->next;
    }

    printf("\n-------------------------------------------------------------------------------------------------------------------------------------------------\n");
    printf("*************************************************************************************************************************************************\n");

}


/*Arguments: header pointer to region headers, 2-D Array of integers 
	Function takes header pointing to the region headers (input argument), 2-D array of integers filled in for magnitude totals by range
	(input/output argument)
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Pick up the magnitude of quake, truncate to an integer value and bump up the count for that range
		Continue until the end of the linked list of data nodes for that region
	Repeat for every region
*/
void buildMagnitudeTotalsArray(region_header_t *head, int totals[][MAX_COLS]) {

    region_header_t *tmp = head;
    equake_data_t *tmpData; 
    int i = 0;
   
   //move through each list and increment corresponding values of the array
    while (tmp != NULL) {
       
        tmpData = tmp->data;

        while (tmpData!= NULL) {
            
            float mag = tmpData->magnitude;

            if (mag >= 0 && mag < 1) {
                ++totals[i][0];
            }
            
            if (mag >= 1 && mag < 2) {
                ++totals[i][1];
            }
            
            if (mag >=2 && mag <3) {
                ++totals[i][2];
            }

            if (mag >= 3 && mag < 4) {
                ++totals[i][3];
            }
            
            if (mag >= 4 && mag < 5) {
                ++totals[i][4];
            }

            if (mag >= 5 && mag < 6) {
                ++totals[i][5];
            }

            if (mag >= 6 && mag < 7) {
                ++totals[i][6];
            }

            if (mag >= 7 && mag < 8) {
                ++totals[i][7];
            }
            
            if (mag >= 8 && mag < 9) {
                ++totals[i][8];
            }

            if (mag >= 9 && mag < 10) {
                ++totals[i][9];
            }

            tmpData = tmpData ->next;
    
        }      

        //totals for first index ended up being 1 more than they should have been, probably due to allocating an extra unnecessary node
        --totals[i][0];
        tmp = tmp->next;
        ++i;
    
    }


}

/*Arguments: 2-D Array of integers, number of regions
	Function takes 2-D array of integers filled in for quake magnitudes (input/output argument), 
	header pointing to the region headers (input argument), number of regions/rows in 2-D array (input argument)
	Print 2-D array for magnitude totals (by range)
*/
void printMagnitudeTotals(int totals[][MAX_COLS], region_header_t *head, int numRegions) {
    
    region_header_t *tmp = head;
    printf("************EARTHQUAKE TOTALS BY MAGNITUDE BY REGION***********\n");\
    printf("                    |Magnitude Range: 0 to 10 (increments of 1)");
    printf("\n---------------------------------------------------------------\n                     ");

    for (int i = 0; i < 10; ++i) {
        printf("%4d", i);
    }

    for (int i = 0; i < numRegions; ++i) {
        printf("\n---------------------------------------------------------------\n");
        printf("%20s|", tmp->region_name);

        for (int k = 0; k < 10; ++k) {
            printf("%4d", totals[i][k]);
            
        }
        
        tmp = tmp->next;
        
    }
    
    printf("\n----------------------------------------------------------------\n");
    printf("****************************************************************");

}

/*Arguments: header pointer to region headers, 2-D Array of integers
	Function takes header pointing to the region headers (input argument), 2-D array of integers filled in for depth totals by range 
	(input/output argument)
	(input/output argument)
	Start with the first region (via region header) and navigate through the region's (data) linked list 
		Pick up the depth of quake, truncate to an integer value and bump up the count for that range
		Continue until the end of the linked list of data nodes for that region
	Repeat for every region
*/
void buildDepthTotalsArray(region_header_t *head, int totals[][MAX_COLS+1]) {

    region_header_t *tmp = head;
    equake_data_t *tmpData; 
    int i = 0;
   
    while (tmp  != NULL) {
       
        tmpData = tmp->data;
        
        //move through each list and increment corresponding values of the array
        while (tmpData != NULL) {

           float depth = tmpData->depth;

            if (depth >= 0 && depth < 10) {
                ++totals[i][0];
            }

            if (depth >= 10 && depth < 20) {
                ++totals[i][1];
            }

            if (depth >= 20 && depth < 30) {
                ++totals[i][2];
            }

            if (depth >= 30 && depth < 40) {
                ++totals[i][3];
            }

            if (depth >= 40 && depth < 50) {
                ++totals[i][4];
            }

            if (depth >= 50 && depth < 60) {
                ++totals[i][5];
            }

            if (depth >= 60 && depth < 70) {
                ++totals[i][6];
            }

            if (depth >= 70 && depth < 80) {
                ++totals[i][7];
            }

            if (depth >= 80 && depth < 90) {
                ++totals[i][8];
            }

            if (depth > 90 && depth < 100) {
                ++totals[i][9];
            }

            if (depth > 100) {
                ++totals[i][10];
            }

            
            tmpData = tmpData->next;
    
        }      
        
        //same problem as above
        --totals[i][0];
        tmp = tmp->next;
        ++i;
    }


}



/*Arguments: 2-D Array of integers, number of regions
	Function takes 2-D array of integers filled in for quake depths (input/output argument), 
	header pointing to the region headers (input argument), number of regions/rows in 2-D array (input argument)
	Print 2-D array for depth totals (by range)
*/
void printDepthTotals(int totals[][MAX_COLS+1], region_header_t *head, int numRegions) {
   
    region_header_t *tmp = head;
    printf("\n***************EARTHQUAKE TOTALS BY DEPTH BY REGION**************\n");\
    printf("                    |Depth Range: 0 to 10 (increments of 10)");
        printf("\n-----------------------------------------------------------------\n                     ");

    for (int i = 0; i < 11; ++i) {
        int depInc = i * 10;
        printf("%4d", depInc);
    }

    for (int i = 0; i < numRegions; ++i) {
        printf("\n-----------------------------------------------------------------\n");
        printf("%20s|", tmp->region_name);
        for (int k = 0; k < 11; ++k) {
            printf("%4d", totals[i][k]);
        }
        tmp = tmp->next;
    }
    printf("\n-----------------------------------------------------------------\n");
    printf("*****************************************************************");

}


/*Arguments: header pointer to region headers, pointer to 2-D array, pointer to 2-D array, pointer to 2-D array
	Function takes header pointing to the region headers (input/output argument), pointer to daily totals array (input argument),
	pointer to magnitude totals array (input argument), pointer to depth totals array (input argument)
	All dynamic memory allocated must be freed. Care must be taken to free up any other member element in the linked list data structures
	before freeing up each node
*/
void cleanUp(region_header_t *head, int *dayTots, int *magTots, int *depTots) {
    region_header_t *tmp = head;
    region_header_t *buff = NULL;
    equake_data_t *tmpData = NULL;
    equake_data_t *dataBuff = NULL;

    while (tmp != NULL) {
        tmpData = tmp ->data;
        while (tmpData != NULL) {
           
            free(tmpData->location);
            free(tmpData->timeOfQuake);
            dataBuff = tmpData;
            tmpData = tmpData->next;
            free(dataBuff);
        }

        free(tmp->region_name);
        buff = tmp;
        tmp = tmp ->next;
        free(buff);
    }

    free(dayTots);
    free(magTots);
    free(depTots);
    


}
