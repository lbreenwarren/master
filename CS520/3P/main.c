//
//
// main.c - main routine for cs520 assembler
//
//          Usage: asx20 file.asm
//
//          Output: file.obj
//
//

#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <unistd.h>
#include "defs.h"

// parser generated by bison
void yyparse(void);

// forward reference
static void nameOutFile(char *, char *);

// file pointer to be used by message functions 
FILE *yyerrfp;

// count of errors detected by the parser
unsigned int parseErrorCount = 0;

// count of errors detected by the scanner
unsigned int scanErrorCount = 0;

//
//      main
//
//
int main(int argc, char *argv[])
{
  char *outn;
  FILE *outf;
  extern FILE *yyin;
  extern int yylineno;
 
  yyerrfp = stderr;

  // initialize assembler
  initAssemble();

  // check that single argument is all that is there
  if ((argc != 2))
  {
    fprintf(stderr,"usage: asx20 file.asm\n");
    exit(1);
  }

  // tell yacc to start on line 1
  yylineno = 1;

  // open the input file
  if (!(yyin = fopen(argv[1],"r")))
  {
    fprintf(stderr, "can't open %s\n", argv[1]);
    exit(1);
  }

  // invoke parser to drive the first pass
  yyparse();

  // close input file
  fclose(yyin);

  // allocate space for output filename (+1 for null; +4 for ".obj")
  outn = malloc(strlen(argv[1]) + 1 + 4);
  if (outn == 0)
  {
    fprintf(stderr, "malloc failed for output filename\n");
    exit(1);
  }

  // name the output file
  nameOutFile(argv[1], outn);

  // open the output file
  if (!(outf = fopen(outn,"w")))
  {
    fprintf(stderr, "can't open %s\n", outn);
    exit(1);
  }

  // let the assembler know that the first pass is done
  //   it will tell us how many errors were detected and therefore
  //   whether to continue with the second pass
  int errorCount = betweenPasses(outf);
  if (errorCount + scanErrorCount + parseErrorCount)
  {
    // close output file that was not used
    fclose(outf);

    // remove the output file
    if (unlink(outn))
    {
      bug("can't remove output file?");
    }

    error("assembler terminating after first pass with %d error(s)",
      errorCount + scanErrorCount + parseErrorCount);
    return errorCount + scanErrorCount + parseErrorCount;
  }

  // tell yacc again to start on line 1
  yylineno = 1;

  // re-open the file
  if (!(yyin = fopen(argv[1],"r")))
  {
    fprintf(stderr, "can't open input file for second pass\n");
    exit(1);
  }

  // invoke parser to drive the second pass
  yyparse();


  // close the files
  fclose(outf);
  fclose(yyin);

  return 0;
}

//
//      nameOutFile
//
//      if input filename is "x.asm" then output filename is "x.obj"
//      else if input filename is "xyz" then output filename is "xyz.obj"
//
//      memory is assumed to be allocated already for outName
//
static
void nameOutFile(char *inName, char *outName)
{
  int i;

  strcpy(outName, inName);
  i = strlen(inName);
  while (i >= 0 && (inName[i] != '.')) 
  {
    i--;
  }
  if (i > 0 &&
     (!strcmp(inName+i,".asm")))
  {
    strcpy(outName+i,".obj");
  }
  else
  {
    strcat(outName,".obj");
  }
}