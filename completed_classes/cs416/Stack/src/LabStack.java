import java.util.ArrayList;
import java.util.Arrays;

/**Implementation of stack class.
 * @author Liam Warren
 * @version 3/21/23*/

public class LabStack {

    /**method to pop n variables from a stack and return them as a list.
     * @param st stack from which ints are popped
     * @param n number of ints to be popped
     * @return list of variables that were popped from stack*/
    public static ArrayList<Integer> popN(Stack st, int n) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < n; ++i) {
            list.add(st.pop());
        }
        return list;
    }

    /**method to pop all ints in a stack.
     * @param st stack from which to pop
     * @return list of ints popped from stack*/
    public static ArrayList<Integer> popAll(Stack st) {
        int n = st.getIndex();
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i <= n; ++i) {
            list.add(st.pop());
        }
        return list;
    }

    /**method to reverse an array using a stack.
     * @param arr array to be reversed
     * @return reversed array*/

    public static int[] reverse(int[] arr) {
        int n = arr.length;
        Stack st = new Stack();
        int[] newarr = new int[n];

        for (int i = 0; i < n; ++i) {
            st.push(arr[i]);
        }

        int i = 0;
        while (!st.isEmpty()) {
            newarr[i] = st.pop();
            ++i;
        }

        return newarr;
    }


}
