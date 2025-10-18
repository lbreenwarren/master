CC = gcc
CFLAGS = -Wall -g


TARGET=linkx20
SRCS = linkx20.c
OBJS= linkx20.o
OUT = out.exe out.obj
TESTS=

all: $(TARGET)

$(TARGET): $(OBJS)
	$(CC) $(CFLAGS) -o $@ $(OBJS)

# Compile .c to .o
%.o: %.c
	$(CC) $(CFLAGS) -c $< -o $@

# Clean up build artifacts
clean:
	rm -f $(OBJS) $(TARGET) $(TESTS) $(OUT)