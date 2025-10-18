CC = gcc
CFLAGS = -Wall


TARGET=dsx20
SRCS = dsx20.c
OBJS= dsx20.o
TESTS=

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $@ $(OBJS)

# Compile .c to .o
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Clean up build artifacts
clean:
	rm -f $(OBJS) $(TARGET) $(TESTS)