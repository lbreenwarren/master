CXX=gcc -Wall -std=c99

ALL: gol

gol: gol.o golFunctions.o
	$(CXX) gol.o golFunctions.o -o gol

gol.o: gol.c
	$(CXX) gol.c -c

golfunctions.o: golFunctions.c gol.h
	$(CXX) golFunctions.c -c