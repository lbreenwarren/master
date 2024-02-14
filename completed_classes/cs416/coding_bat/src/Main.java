import java.util.Scanner;

public class Main {

    public static boolean sleepIn(boolean weekday, boolean vacation) {

        //if its a weekday don't sleep in, if its not a weekday or were on vacation sleep in

        if((weekday == false)||(vacation == true)){
            return true;
        }
        else{
            return false;
        }
	}

    public static boolean nearHundred(int x){

        //check if 100 minus given number is between -10 and 10 and same for 200 minus given number

        if(((100 - x <= 10)&&(100 - x>=-10))||((200 - x <=10)&&(200 - x >= -10))){
            return true;
        }
        else{
            return false;
        }
    }


    public static boolean sameFirstLast(int[] nums) {

        //exception for case of empty array

            if (nums.length == 0){
                return false;
            }

        //compare first and last number of array

        int last = nums.length - 1;

            if (nums[0]==nums[last]){
                return true;
            }
            else{
                return false;
        }


    }

    public static boolean no23(int[] nums) {

        //walk array and check if any number in it is 2 or 3

        for (int i = 0; i<2; ++i){
            if ((nums[i] == 2)||(nums[i] == 3)){
                return false;
            }
        }
        return true;
    }

    public static String helloName(String name) {

        //combine given string with "hello" and "!"

        String greeting = "Hello " + name + "!";
        return greeting;

    }


}


