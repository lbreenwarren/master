#
# Sample makefile for creating threads library
#

CC = gcc
CFLAGS = -g -Wall
SRC = thread.c thread_asm.s
OBJ = thread.o thread_asm.o
LIB = -lthread

thread:
	$(CC) -c $(SRC) $(CFLAGS)
	ar -cr libthread.a $(OBJ)
