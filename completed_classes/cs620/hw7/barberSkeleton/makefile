# CS620
# Author: Bo-Cheng Cui
# Name: makefile
# Description: makefile for barber, you should not modify this file.

all: barber

barber: driver.o barber.c
	gcc -g -Wall -lrt -lpthread -lm driver.o barber.c -o barber

driver.o: driver.c barber.h
	gcc -g -Wall -lrt -lpthread -lm -c driver.c

clean:
	rm -f driver.o barber
