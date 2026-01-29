CC = gcc
CFLAGS = -g -Wall -fPIE
SRC = alloc.c mem_parse.s
OBJ = alloc.o mem_parse.o
TEST = test.c
LIB = -lalloc

alloc: 
	$(CC) -c $(SRC) $(CFLAGS)
	ar -cr liballoc.a $(OBJ)

test: alloc
	$(CC) $(CFLAGS) $(TEST) -L. $(LIB) -o test
clean:
	rm -f $(OBJ) test liballoc.a