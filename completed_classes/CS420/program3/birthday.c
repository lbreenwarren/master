//Liam Warren
//CS420
//Program 3
//3.4.23

#include <stdio.h>
#include <time.h>
#include <stdlib.h>

//struct to represent a persons birthday
typedef struct {
    int birthday;
    int birthmonth;
} person_t;

//function to assign a birthmonth
int birthmonth(){
   
    return (rand() % 12) +1;
}

//function to assign a birthday based on birthmonth
int birthday(int month){

    switch (month) {
        case 2:
        return (rand() % 28) + 1;
        break;

        case 9: 
        case 4:
        case 6:
        case 11:
        return (rand() % 30) + 1;
        break;

        default:
        return (rand() % 31) +1;
    }
    return -1;
}


double birthdayGame(int numberOfTrials, int numberOfPeople, double *monthProbability) {

//seed random function
    srand(time(NULL));
    
    //if input values not positive return -1
    if (numberOfTrials <= 0 || numberOfPeople <= 0) {
        return -1;
    }

    //initialize count of true trials
    double numTrueTrials = 0;
    double numMonthTrueTrials = 0;

    //declare array of people and count of shared birthdays
    for (int i = 0; i < numberOfTrials; ++i){
        person_t people[numberOfPeople];
        int same_month_count = 0;
        int same_birthday_count = 0;
        
        //populate array of people
        for (int k = 0; k < numberOfPeople; ++k){        
            people[k].birthmonth = birthmonth();
            people[k].birthday = birthday(people[k].birthmonth);
        }

        //for each person compare that persons birthday to each other persons birthday
        for (int k =0; k < numberOfPeople; ++k){

            for (int j = 0; j < numberOfPeople; ++j){

                if (j != k){

                    if (people[j].birthmonth == people[k].birthmonth){
                        ++same_month_count;

                        if (people[j].birthday == people[k].birthday){
                            ++same_birthday_count;
                        }
                    }
                }
            }

        }

        //if count was incremented trial was true trial
        if (same_birthday_count > 0){
            ++numTrueTrials;
        }
        if (same_month_count > 0){
            ++numMonthTrueTrials;
        }
        
    }
    
    //return propbabilities of there being a shared birthday
    *monthProbability = numMonthTrueTrials/numberOfTrials;
    return (numTrueTrials/numberOfTrials);
}

