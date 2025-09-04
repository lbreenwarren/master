all: frames.o main.o asm.o
	gcc -g -Werror  frames.o asm.o main.o -o main

frames.o: frames.c
	gcc -g -Werror  -c frames.c

main.o: main.c
	gcc -g -Werror  -c main.c

asm.o: asm.s
	gcc -g -c asm.s