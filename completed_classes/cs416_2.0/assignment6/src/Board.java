import java.util.Scanner;

/**
 * Represents a Sudoku board.
 * @author Liam Warren
 * @version 11.30.23
 */
public class Board {
    
    /** The Sudoku board represented as a 2D array. */
    int[][] board;

    /**
     * Constructs a Sudoku board by reading input from a Scanner.
     *
     * @param sc The Scanner used to read input for the Sudoku board.
     */
    public Board(Scanner sc) {
        board = readBoard(sc);
    }

    /**
     * Reads a Sudoku board from the provided Scanner.
     *
     * @param sc The Scanner used to read input for the Sudoku board.
     * @return The 2D array representing the Sudoku board.
     */
    public static int[][] readBoard(Scanner sc) {

        int[][] board = new int[9][9];
        for (int i = 0; i < 9; ++i) {

            if (!sc.hasNext()) {
                return null;
            }

            String nextRow = sc.next();

            if (nextRow.length() > 9 || nextRow.length() < 9) {
                return null;
            }

            for (int k = 0; k < 9; ++k) {
                
                char next = nextRow.charAt(k);
                
                if (next == '-') {
                    board[i][k] = 0;
                } else {
                    int nextInt = Character.getNumericValue(next);
                    if (nextInt < 1 || nextInt > 9) {
                        return null;
                    }
                    board[i][k] = nextInt;
                }
            }
        } 

        if (sc.hasNext()) {
            return null;
        }
        
        return board;
        
    }
 
    /**
     * Gets the value at the specified row and column on the Sudoku board.
     *
     * @param row The row index.
     * @param col The column index.
     * @return The value at the specified row and column.
     */
    public int get(int row, int col) {
        return board[row][col];
    }

    /**
     * Sets the value at the specified row and column on the Sudoku board.
     *
     * @param row The row index.
     * @param col The column index.
     * @param value The value to be set.
     */
    public void set(int row, int col, int value) {
        board[row][col] = value;
    }

    /**
     * Checks if the specified number is present in the specified row of the Sudoku board.
     *
     * @param row The row index.
     * @param number The number to check for.
     * @return True if the number is present in the row, false otherwise.
     */
    public boolean containsInRow(int row, int number) {
        for (int i = 0; i < 9; ++i) {
            if (board[row][i] == number) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the specified number is present in the specified column of the Sudoku board.
     *
     * @param col The column index.
     * @param number The number to check for.
     * @return True if the number is present in the column, false otherwise.
     */
    public boolean containsInCol(int col, int number) {
        for (int i = 0; i < 9; ++i) {
            if (board[i][col] == number) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks if the specified number is present in the 3x3 box containing the specified row and column.
     *
     * @param row The row index.
     * @param col The column index.
     * @param number The number to check for.
     * @return True if the number is present in the box, false otherwise.
     */
    public boolean containsInBox(int row, int col, int number) {

        int threesRow = Math.floorDiv(row, 3); 
        int threesCol = Math.floorDiv(col, 3);
        int stRow = threesRow * 3;
        int stCol = threesCol * 3;
        for (int i = stRow; i < stRow + 3; ++i) {
            for (int k = stCol; k < stCol + 3; ++k) {
                if (board[i][k] == number) {
                    return true;
                }
            
            }
        }
        return false;

    }

    /**
     * Checks if placing the specified number at the specified row and column is allowed in the Sudoku board.
     *
     * @param row The row index.
     * @param col The column index.
     * @param number The number to check for.
     * @return True if placing the number is allowed, false otherwise.
     */
    public boolean isAllowed(int row, int col, int number) {
        if (containsInRow(row, number) || containsInCol(col, number) || containsInBox(row, col, number)) {
            return false;
        }
        return true;
    }

    /**
     * Returns a string representation of the Sudoku board.
     *
     * @return The string representation of the Sudoku board.
     */
    public String toString() {
        String ret = "+-------+-------+-------+\n";
        for (int i = 0; i < 9; ++i) {
            for (int k = 0; k < 9; ++k) {
                if (k % 3 == 0) {
                    ret += "| ";
                }
                if (board[i][k] == 0) {
                    ret += "- ";
                } else {
                    ret += board[i][k] + " ";
                }
            }
            ret += "|\n";
            if (i % 3 == 2) {
                ret += "+-------+-------+-------+";
                if (i != 8) {
                    ret += "\n";
                }
            }
        }
        return ret;
    }
}
