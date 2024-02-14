import java.util.Scanner;

/** A program to play a number guessing game.
 * @author Liam Warren
 * @version 9.20.23
 */
public class NGuesses {

    /**
     * Main method.
     * @param args 0: number of guesses.
     * args 1: correct number
     * args 2: prize
     */
    public static void main(String[] args) {
        int numGuesses = Integer.parseInt(args[0]);
        int correctNum = Integer.parseInt(args[1]);
        String prize = args[2];
        Scanner in = new Scanner(System.in);
        int i = 0;
        boolean win = false;

        while (i < numGuesses && !win) {
        
            System.out.println("Enter a guess:");
            int guess = in.nextInt();
        
            if (guess > correctNum) {
                System.out.println("Too high");
            }
        
            if (guess < correctNum) {
                System.out.println("Too low");
            }
        
            if (guess ==  correctNum) {
                System.out.println("That's the right number");
                System.out.println("Congratulations, you won a " + prize + "!");
                win = true;
            }

            ++i;
        
        }

        if (!win) {
            System.out.println("Sorry you didn't win, the number was " + correctNum);
        }

        in.close();
    }
}
