import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Represents a Sudoku solving game.
 * @author Liam Warren
 * @version 11.30.23
 */
public class Sudoku {

    /** The Sudoku board. */
    Board board;

    /** Count of recursive calls made during the solving process. */
    int recursiveCalls;

    /** Count of backup operations made during the solving process. */
    int backups;

    /**
     * Constructs a Sudoku game using input from a Scanner.
     *
     * @param sc The Scanner used to read input for the Sudoku board.
     */
    public Sudoku(Scanner sc) {
        board = new Board(sc);
        recursiveCalls = 0;
        backups = 0;
    }

    /**
     * Solves the Sudoku puzzle starting from the specified location.
     *
     * @param loc The starting location for solving.
     * @return True if the puzzle is solved, false otherwise.
     */
    public boolean solve(Location loc) {
        ++recursiveCalls;

        // Base Case 1: If you are past the end of the board, then all previous locations must have been 
        // filled by previous recursive calls, and the puzzle is solved.
        if (loc == null) {
            return true;
        }

        // Recursion case 1 : If this location already has a value (from the board file), 
        // it should not be changed. Recursively call for the next location. 
        if (board.get(loc.getRow(), loc.getColumn()) != 0) {
            return solve(loc.next());
        }

        // Recursion case 2 : If this location did not already have a value, try the next legal value, 
        // then recursively attempt to find a solution from the next cell. 
        //If the recursive attempt returns no solution, try the next legal value and call recursively again.
        int col = loc.getColumn();
        int row = loc.getRow();
        for (int i = 0; i < 10; ++i) {
            if (board.isAllowed(row, col, i)) {
                board.set(row, col, i);
                if (solve(loc.next())) {
                    return true;
                }
            }
        }

        ++backups;
        board.set(row, col, 0);
        return false;
    }

    /**
     * Gets the count of recursive calls made during the solving process.
     *
     * @return The count of recursive calls.
     */
    public int getRecursionCount() {
        return recursiveCalls;
    }

    /**
     * Gets the count of backup operations made during the solving process.
     *
     * @return The count of backup operations.
     */
    public int getBackupCount() {
        return backups;
    }

    /**
     * Gets the Sudoku board being solved.
     *
     * @return The Sudoku board.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Entry point for the Sudoku game.
     *
     * @param args Command line arguments *NOT USED*.
     */
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.println("Enter the path to the sudoku file:");
        String filename = in.next();
        File file = new File(filename);

        try {
            Scanner sc = new Scanner(file);
            Sudoku game = new Sudoku(sc);
            System.out.println("Initial configuration of the sudoku");
            System.out.println(game.getBoard());
            Location start = new Location(0, 0);
            if (game.solve(start)) {
                System.out.println("Successful!");
                System.out.println("Final configuration of the sudoku");
                System.out.println(game.getBoard());
                System.out.println("Recursion count = " + game.getRecursionCount());
                System.out.println("Backup count = " + game.getBackupCount());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File was not found.");
        }

        in.close();
    }
}
