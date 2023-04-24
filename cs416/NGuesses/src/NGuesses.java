import java.util.Scanner;
/**Program to play a guessing game with user.
 * @author Liam Warren
 * @version 2/16/22
 * */

public class NGuesses {

    /**main method.
     * @param args command line arguments for number of guesses allowed, number being guessed at, and prize*/
    public static void main(String[] args) {
        int numguesses = Integer.parseInt(args[0]);
        int correctnum = Integer.parseInt(args[1]);
        int guess = 0;
        String prize = args[2];
        Scanner in = new Scanner(System.in);
        int i = 0;
        while ((i < numguesses) && (guess != correctnum)) {
            System.out.println("Enter a guess:");
            guess = in.nextInt();

            if (guess > correctnum) {
                System.out.println("Too high");
            }
            if (guess < correctnum) {
                System.out.println("Too low");
            }
            ++i;
        }
        if (guess == correctnum) {
            System.out.println("That's the right number");
            System.out.println("Congratulations, you won a " + prize + "!");
        }
        if (guess != correctnum) {
            System.out.println("Sorry you didn't win, the number was " + correctnum);
        }
    }
}
