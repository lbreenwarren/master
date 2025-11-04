#

CC = gcc

CFLAGS = -g -Werror

sum: simVM.o sum.o 
	$(CC) simVM.o sum.o  -o sum

simVM.o: simVM.c
	$(CC) $(CFLAGS) -c simVM.c

sum.o: sum.c
	$(CC) $(CFLAGS) -c sum.c

clean:
	-rm -f *.o sum 