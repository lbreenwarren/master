CXX=gcc -Wall -std=c99

All: bitTest ipAdress.o

bitTest: bitFunctions bitTest.c
	$(CXX) bitTest.c bitFunctions.o -o bitTest -lm

bitFunctions: bitFunctions.c bitFunctions.h
	$(CXX) -c bitFunctions.c 

ipAddress: ipAddress.c ipAddress.h
	$(CXX) ipAddress.c -lm
clean: 
	rm bitFunctions.o ipAdress.o bitTest output1.txt