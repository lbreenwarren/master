#
# This is a simple makefile for building the lab4 test programs.
#

test1: test1.o symtab.o
	gcc -g test1.o symtab.o -o test1

test2: test2.o symtab.o
	gcc -g test2.o symtab.o -o test2

test6: test6.o symtab.o
	gcc -g test6.o symtab.o -o test6

lab4: main.o symtab.o
	gcc -g main.o symtab.o -o lab4

test1.o: test1.c symtab.h
	gcc -Werror -std=c99 -c -g test1.c

test2.o: test2.c symtab.h
	gcc -Werror -c -g test2.c

test6.o: test6.c symtab.h
	gcc -Werror -c -g test6.c
	
main.o: main.c symtab.h
	gcc -Werror -c -g main.c

symtab.o: symtab.c symtab.h
	gcc -Werror -c -g symtab.c

clean:
	rm -f main.o symtab.o lab4 test1.o test2.o test1 test2

