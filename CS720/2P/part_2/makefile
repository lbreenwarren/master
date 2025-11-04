CC = gcc
CFLAGS = -Wall -g


TARGETS=testvm vmx20.a
SRCS = vmx20.c testvm.c
OBJS= testvm.o vmx20.o

all: $(TARGET)

testvm: testvm.o vmx20.o
	$(CC) $(CFLAGS) -o testvm testvm.o vmx20.o
vmx20:
	ar rcs vmx20.a vmx20.o
# Compile .c to .o
testvm.o: vmx20.c testvm.c
	$(CC) $(CFLAGS) -c testvm.c -o testvm.o
vmx20.o: vmx20.c
	$(CC) $(CFLAGS) -c vmx20.c -o vmx20.o
# Clean up build artifacts
clean:
	rm -f $(OBJS) $(TARGET) 