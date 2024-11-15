//
// test of the simulated virtual memory system to show weakness of
//   round-robin replacement
//
// the idea is to allocate a page to hold the sum. this page is
// always needed, so round-robin replacement should occasionally
// force the page holding the sum out of memory. since we are
// writing to the sum, this will also cause extra disk writes
//
// LRU replacement will always keep the sum in memory
//

#include <stdio.h>
#include <stdlib.h>
#include "simVM.h"

#define VMEMSIZE 16
#define PMEMSIZE 4
#define PAGESIZE 4
#define TLBSIZE 0
#define MEMSIZE (VMEMSIZE*PAGESIZE)

#define LRU VM_LRU_REPLACEMENT
#define RR VM_ROUNDROBIN_REPLACEMENT

int main(void)
{
  // use round-robin
  
  int i, sum1;
  void *h = createVM(VMEMSIZE+1, PMEMSIZE, PAGESIZE, TLBSIZE, RR, RR);
  if (h == NULL)
  {
    fprintf(stderr, "createVM failed!\n");
    exit(-1);
  }

  // initialize the array
  for (i = 0; i < MEMSIZE; i++)
  {
    writeInt(h, i, i);
  }

  // initialize the sum
  writeInt(h, MEMSIZE, 0);

  // now sum it
  sum1 = 0;
  for (i = 0; i < MEMSIZE; i++)
  {
    int tmp = readInt(h, i);
    // printf("# %d %d\n", tmp, i);
    writeInt(h, MEMSIZE, tmp + readInt(h, MEMSIZE));
    sum1 += i;
  }
  printf("sum is %d (should be %d)\n", readInt(h, MEMSIZE), sum1);

  // print stats
  printf("\n");
  printf("Using Round-Robin:\n");
  printStatistics(h);
  printf("\n");

  // cleanup
  cleanupVM(h);

  return 0;
}

