import java.util.Scanner;
/**A Program to prompt the user for an input and print that value to the command line.
 * @author Liam Warren
 * @version 8.31.23
 */

public class BasicIOProgram {
    
    /**Main Function.
     * @param args unused
     */
    public static void main(String[] args) {
        Scanner stdin = new Scanner(System.in);
        System.out.println("Enter two whole numbers");
        System.out.println("separated by one or more spaces:");
        int number1 = stdin.nextInt();
        int number2 = stdin.nextInt();
        System.out.println("You entered " + number1 + " and " + number2);
        System.out.println("Next enter two numbers.");
        System.out.println("A decimal point is OK.");
        float number3 = stdin.nextFloat();
        float number4 = stdin.nextFloat();
        System.out.println("You entered " + number3 + " and " + number4);
        System.out.println("Next enter two words:");
        String string1 = stdin.next();
        String string2 = stdin.next();
        System.out.println("You entered \"" + string1 + "\" and \"" + string2 + "\"");
        System.out.println("Next enter a line of text:");
        stdin.nextLine();
        String line = stdin.nextLine();
        System.out.println("You entered: \"" + line + "\"");
        stdin.close();
    }
}
