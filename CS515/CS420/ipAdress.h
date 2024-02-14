#include <stdio.h>

#define MAX_RECORDS 100
#define MAX_ADDR_SIZE 20

typedef struct {
    char ipAdressDot[MAX_ADDR_SIZE];
    char subnetMaskDot[MAX_ADDR_SIZE]; 
    unsigned int ipAdress;
    unsigned int subnetMask;
    char networkClass;
    int totalSubnets;
    int totalHosts;

} ipInfo_t;

void computeValues(ipInfo_t addr[], int size);

int readData(char filename[] , ipInfo_t addr[]);

void printIntasBin (unsigned int bin);

void printResults(char filename[], ipInfo_t addr[], int size);
