import java.util.ArrayList;

/**
 * The LabStack class provides utility methods for working with stacks and arrays.
 * @author Liam Warren
 * @version 10.17.23
 */
public class LabStack {
    
    /**
     * Pops the top 'n' elements from the given stack and returns them as an ArrayList.
     *
     * @param st The stack from which elements are to be popped.
     * @param n  The number of elements to pop from the stack.
     * @return An ArrayList containing the popped elements.
     */
    public static  ArrayList<Integer> popN(Stack st, int n) {
        ArrayList<Integer> out = new ArrayList<Integer>();

        for (int i = 0; i < n; ++i) {
            out.add(st.pop());
        }
        return out;
    }

    /**
     * Pops all elements from the given stack and returns them as an ArrayList.
     *
     * @param st The stack from which all elements are to be popped.
     * @return An ArrayList containing all the popped elements.
     */
    public static ArrayList<Integer> popAll(Stack st) {
        ArrayList<Integer> out = new ArrayList<Integer>();
        while (!st.isEmpty()) {
            out.add(st.pop());
        }
        return out;
    }

    /**
     * pushes all elements of an array to a stack, then pops the stack to a new array, reversing it.
     *
     * @param arr the array to be reversed
     * @return An Array containing the reversed array
     */ 
    public static int[] reverse(int[] arr) {
        Stack st = new Stack();
        int[] out = new int[arr.length];
        for (int i = 0; i < arr.length; ++i) {
            st.push(arr[i]);
        }
        for (int i = 0; i < arr.length; ++i) {
            out[i] = st.pop();
        }
        return out;
    }
}
