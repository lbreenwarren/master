// simple unit test for symtabCreate, symtabInstall, symtabDelete
// with multiple symbol names, and repeating some with different values

#include <stdio.h>
#include <string.h>

#include "symtab.h"

int main()
{
  void *symtab = symtabCreate(5);
  long data = 0;
  if (symtab == NULL)
  {
    fprintf(stderr, "symtabCreate failed!\n");
  }

  if (!symtabInstall(symtab, "MAX", (void *) (long) 42))
  {
    fprintf(stderr, "symtabInstall failed for MAX(1)!\n");
  }

  if (((long) symtabLookup(symtab, "MAXI")) != 44)
  {
    fprintf(stderr, "symtabLookup failed for MAXI!\n");
  }

  if (!symtabInstall(symtab, "MAX", (void *) (long) 92))
  {
    fprintf(stderr, "symtabInstall failed for MAX(2)!\n");
  }
 
   if ((data = (long) symtabLookup(symtab, "MAX")) != 92)
  {
    fprintf(stderr, "symtabLookup failed for MAX(2)!\n");
  }
  else
	fprintf(stderr, "MAX = %ld\n", data);
  
   if (((long) symtabLookup(symtab, "MINI")) == (long) NULL)
  {
    fprintf(stderr, "symtabLookup failed for MINI!\n");
  }
  
  void *iter = symtabCreateIterator(symtab);
  if (iter == NULL)
  {
    fprintf(stderr, "symtabCreateIterator failed!\n");
  }

  void *ret;
  const char *sym = symtabNext(iter, &ret);
  if (strcmp(sym, "MAX") || (((long) ret) != ((long) 92)))
  {
    printf("%s, %ld", sym, (long)ret);
    fprintf(stderr, "symtabNext failed!\n");
  }
  else {
	fprintf(stderr, "Iterator returned MAX = %ld\n", (long) ret);	  
  }

  symtabDeleteIterator(iter);
  symtabDelete(symtab);

  return 0;
}

