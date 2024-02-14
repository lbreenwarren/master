import java.util.Scanner;
public class BasicIOProgram {

    //read various variables from user and echo
    public static void main(String[] arg)  {

        //declare scanner class
        Scanner in = new Scanner(System.in);

        //prompt user, declare variables, echo user input
        System.out.println("Enter two whole numbers");
        System.out.println("separated by one or more spaces:");
        int num1 = in.nextInt();
        int num2 = in.nextInt();
        System.out.println("You entered " + num1 + " and " + num2 + "");

        //prompt user, declare variables, echo user input
        System.out.println("Next enter two numbers.");
        System.out.println("A decimal point is okay.");
        double num3 = in.nextDouble();
        double num4 = in.nextDouble();
        System.out.println("You entered " + num3 + " and " + num4 + "");

        //prompt user, declare variables, echo user input
        System.out.println("Next enter two words:");
        String word1 = in.next();
        String word2 = in.next();
        System.out.println("you entered '" + word1 + "' and '" + word2 + "'");

        //prompt user, declare variables, echo user input
        System.out.println("Next enter a line of text:");
        in.nextLine();
        String sentence = in.nextLine();
        System.out.println("you entered '" + sentence + "'");











    }
}
