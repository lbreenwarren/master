/**
 * The Stack class represents a simple integer stack with a fixed size.
 * @author Liam Warren
 * @version 10.17.23
 */
public class Stack {

    int[] stack = new int[10];
    int index;

    /**
     * Constructs an empty stack with a default index of -1.
     */
    public Stack() {
        index = -1;
    }

    /**
     * Checks if the stack is empty.
     *
     * @return true if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        if (index < 0) {
            return true;
        }
        return false;
    }

    /**
     * Pushes an integer value onto the stack.
     *
     * @param val The value to be pushed onto the stack.
     * @return true if the push operation is successful, false if the stack is full.
     */
    public boolean push(int val) {
        if (index >= 9) {
            return false;
        }
        ++index;
        stack[index] = val;
        return true;
    }

    /**
     * Pops and returns the top integer value from the stack.
     *
     * @return The top integer value that was removed from the stack.
     */
    public int pop() {
        int val = stack[index];
        --index;
        return val;
    }

    /**
     * Peeks at the top integer value in the stack without removing it.
     *
     * @return The top integer value in the stack.
     */
    public int peek() {
        return stack[index];
    }

    /**
     * Returns a string representation of the stack elements.
     *
     * @return A string representation of the stack in the format "[element1, element2, ...]".
     */
    public String toString() {
        String out = "";
        for (int i = index; i >= 0; --i) {
            out += stack[i];
            if (i > 0) {
                out += ", ";
            }
        }
        return out;
    }
}
