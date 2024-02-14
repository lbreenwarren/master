#include <stdio.h>
#include <stdlib.h>
#include "ipAdress.h"
#include <math.h>

void printIntasBin (unsigned int bin) {
    
    //helper function to print visual binary representation of integer for debugging

    int out = 0;
    for (int i = 0; i < 32; ++i) {

        out =  (bin & 0x80000000 ? 1 : 0);
        printf("\n%d", out);

       if (i % 4 == 3){
            printf(" ");   
        }

        bin = bin << 0x1;
    }
    printf("\n");

}

int readData(char filename[] , ipInfo_t addr[]){

    //input arguments: filename, array of IP adress structures

    FILE *infilep;

    infilep = fopen (filename, "r");

    if (infilep == NULL) {
        printf("file doesnt exits");
        return -1;
    }

    int i = 0;
    char c;

    while((fscanf(infilep, "%s %s", addr[i].ipAdressDot, addr[i].subnetMaskDot)) != EOF){
        printf("%s\n", addr[i].ipAdressDot);
        ++i;
    }

    int k = 0;
    int h = 31;

    for (int j = 0; j < i; ++j) {
        unsigned int IPadress = 0;
        k = 0;

        for (int l = 0; l < 4; ++l){
      
            int q = 0;
            char octstr[4] = {'\0'};
            c = addr[j].ipAdressDot[k];
            
            do {
                octstr[q] = c;
                ++k;
                ++q;
                 c = addr[j].ipAdressDot[k];
            }

            while (c != '.' && c != '\0');
            
            int octint = 0;

            octint = atoi(octstr);
            printf("\n%d\n", octint);           
            for (int n = 0; n < 8; ++n) {
               
                IPadress += (octint & 0x80 ? 1 : 0) * pow(2,h);
                octint = octint << 1;
                --h;
                printIntasBin(IPadress);
            }
            addr[j].ipAdress = IPadress;
            ++k;
        }
        printf("\n%x\n", IPadress);
    }

    unsigned int subNetMask = 0;
    k = 0;
    h = 31;

    for (int j = 0; j < i; ++j) {
        
        k = 0;

        for (int l = 0; l < 4; ++l){
      
            int q = 0;
            char octstr[4] = {'\0'};
            c = addr[j].subnetMaskDot[k];
            
            do {
                octstr[q] = c;
                ++k;
                ++q;
                 c = addr[j].subnetMaskDot[k];
            }

            while (c != '.' && c != '\0');
            
            int octint = 0;
            octint = atoi(octstr);
            for (int n = 0; n < 8; ++n) {
               
                subNetMask += (octint & 0x80 ? 1 : 0) * pow(2,h);
                octint = octint << 1;
                --h;

            }
            addr[j].subnetMask = subNetMask;
            ++k;
        }
        printf("%x\n", subNetMask);
    }
    
    return i;
}

void computeValues(ipInfo_t addr[], int size){
    for (int i = 0; i < size; ++i) {
        if ((addr[i].ipAdress & 0x80000000) == 0) {
            addr[i].networkClass = 'A';
        }
        else if ((addr[i].ipAdress & 0xc0000000) == 0) {
            addr[i].networkClass = 'B';
        }
        else if ((addr[i].ipAdress & 0xe0000000) == 0) {
            addr[i].networkClass = 'C';
        }

    }

    for (int i = 0; i < size; ++i) {
        
        int count1s = 0;
        int count0s = 0;
        int tmp = addr[i].subnetMask;

        if (addr[i].networkClass == 'A') {

            for (int k = 0; k < 24; ++k) {
                tmp & 0x1 ? ++count1s : ++count0s;
                tmp = tmp >> 1;  
            }
            
            addr[i].totalSubnets = pow(2, count1s);
            addr[i].totalHosts = pow(2,count0s) - 2;
        }

        else if (addr[i].networkClass == 'B') {

            for (int k = 0; k < 16; ++k) {
                tmp & 0x1 ? ++count1s : ++count0s;
                tmp = tmp >> 1;  
            }
            
            addr[i].totalSubnets = pow(2, count1s);
            addr[i].totalHosts = pow(2,count0s) - 2;
        }

        else  if (addr[i].networkClass == 'C') {

            for (int k = 0; k < 8; ++k) {
                tmp & 0x1 ? ++count1s : ++count0s;
                tmp = tmp >> 1;  
            }
            
            addr[i].totalSubnets = pow(2, count1s);
            addr[i].totalHosts = pow(2,count0s) - 2;
        }
    }

}


void printResults(char filename[], ipInfo_t addr[], int size) {
    FILE *infilep = fopen(filename, "w");
    

    fprintf(infilep, "    Octet IP Address   IP Address   Octet Subnet Mask  Subnet Mask   Class Subnets   Hosts\n");

    for (int i = 0; i < size; ++i) {
        fprintf(infilep, "%20s%13x%20s%13x%8c%8d%8d\n", addr[i].ipAdressDot, addr[i].ipAdress, addr[i].subnetMaskDot, addr[i].subnetMask, addr[i].networkClass, addr[i].totalHosts, addr[i].totalSubnets);  
    }
}


int main() {
        char filename[] = "address.txt";
        ipInfo_t addr[MAX_RECORDS];
        int size = readData(filename, addr);
        computeValues(addr, size);
        printResults("output1.txt", addr, size) ;
        for (int i = 0; i < size; ++i) {
            printf("%c", addr[i].networkClass);
        }
	return 0;
    }
