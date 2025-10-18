CC = gcc
CFLAGS = -Wall


TARGET=binary_info
SRCS = binary_info.c
OBJS= binary_info.o 
TESTS=test1.o

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $@ $(OBJS)

# Compile .c to .o
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Clean up build artifacts
clean:
	rm -f $(OBJS) $(TARGET) $(TESTS)