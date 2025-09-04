#
# Makefile for simple test of concurrent buffer
#

CC = gcc
CFLAGS = -g -Werror -pthread
EXES = countChars

all: $(EXES)

countChars: countChars.c concurrentBuffer.o
	$(CC) $(CFLAGS) countChars.c concurrentBuffer.o -o countChars

concurrentBuffer.o: concurrentBuffer.c
	$(CC) $(CFLAGS) -c concurrentBuffer.c

clean:
	-rm -f *.o $(EXES)

