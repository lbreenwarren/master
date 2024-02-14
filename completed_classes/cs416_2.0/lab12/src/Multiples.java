import java.util.ArrayList;
import java.util.Arrays;

/**
 * A program to solve a puzzle of sorting an array into an order which every number is a multiple of its index.
 * @author  Liam Warren
 * @version 10.30.23
*/
public class Multiples {

    private int[] result;
    private ArrayList<Integer> numbers;

    /**
    * Constructor.
    * @param numbers Values to distribute into multiples location
    */
    public Multiples(ArrayList<Integer> numbers) {
        this.numbers = numbers;
        this.result = new int[numbers.size()];
    }

    //HELPER METHODS GO HERE

    /**
     * Helper function to check if a number is a multiple of a divisor.
     * @param num number to be checked
     * @param div divisor to be checked against
     * @return true if num is multiple of div
     */

    public boolean isMultiple(int num, int div) {
        if (num % div == 0) {
            return true;
        }

        return false;
    }

    /**
     * helper function to assign value to result array.
     * @param n index to assign to
     * @param i index of number to assign
     */
    public void assignValue(int n, int i) {
        int value = numbers.get(i);
        result[n] = value;
        numbers.remove(i);
    }

    /**
     * helper function to unassign number to result array.
     * @param n index of number to unassign
     */
    public void unassignValue(int n) {
        int value = result[n];
        numbers.add(0, value);
        result[n] = 0;
    }

    /**
     * DO NOT CHANGE!
     * Method to begin solving the problem for the first index in results.
     *
     * @return True if solution was found, false if none was possible.
     */
    public boolean solve() {
        return solve(0);
    }

    /**
     * Recursive method to solve the problem from a given index onward. Assumes
     * that any index before n was already assigned a valid value.
     *
     * @param n The current index in the results array to find a value for.
     * @return True if solution was found, false if none was possible from this index.
     */
    private boolean solve(int n) {

        if (n >= result.length) {
            return true;
        }

        for (int i = 0; i < numbers.size(); ++i) {
            int value = numbers.get(i);
            if (isMultiple(value, n + 1)) {
                assignValue(n, i);
                boolean recCall = solve(n + 1);
                if (recCall) {
                    return true;
                } else {
                    unassignValue(n);
                }
            }

        }

        return false; // REPLACE THIS WITH YOUR IMPLEMENTATION
    }

    /**
    * returns the retult array.
    * @return int[]
    */
    public int[] getResult() {
        return result;
    }

    /**
    * return the numbers ArrayList.
    * @return ArrayList
    */
    public ArrayList<Integer> getNumbers() {
        return numbers;
    }

    /**
    * main function.
    * @param args Command line args
    */
    public static void main(String[] args) {
        Integer[] numbers;
        Multiples m;

        //FIRST TEST (SOLUTION POSSIBLE WITHOUT BACKTRACKING)
        numbers = new Integer[]{1, 6, 21, 40, 25};
        m = new Multiples(new ArrayList<Integer>(Arrays.asList(numbers)));

        if (m.solve()) {
            System.out.println("Found solution for " + Arrays.toString(numbers));
        } else {
            System.out.println("No solution possible!");
        }
        System.out.println(Arrays.toString(m.getResult()));
        System.out.println();

        //SECOND TEST (SOLUTION POSSIBLE WITH BACKTRACKING)
        numbers = new Integer[]{15, 10, 6, 12, 16};
        m = new Multiples(new ArrayList<Integer>(Arrays.asList(numbers)));

        if (m.solve()) {
            System.out.println("Found solution for " + Arrays.toString(numbers));
        } else {
            System.out.println("No solution possible!");
        }
        System.out.println(Arrays.toString(m.getResult()));
        System.out.println();

        //THIRD TEST (NO SOLUTION)
        numbers = new Integer[]{15, 10, 6, 12, 17};
        m = new Multiples(new ArrayList<Integer>(Arrays.asList(numbers)));

        if (m.solve()) {
            System.out.println("Found solution for " + Arrays.toString(numbers));
        } else {
            System.out.println("No solution possible!");
        }
        System.out.println(Arrays.toString(m.getResult()));
    }

}
