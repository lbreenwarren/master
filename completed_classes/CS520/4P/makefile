#

CC = gcc

CFLAGS = -g -Werror

sum: simVM.o sum.o 
	$(CC) simVM.o sum.o  -o sum
sum.o: sum.c
	$(CC) $(CFLAGS) -c sum.c

sum1: simVM.o sum1.o 
	$(CC) simVM.o sum1.o  -o sum1
sum1.o: sum1.c
	$(CC) $(CFLAGS) -c sum1.c
	
simVM.o: simVM.c
	$(CC) $(CFLAGS) -c simVM.c
clean:
	-rm -f *.o sum 