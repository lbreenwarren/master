
/**A program to create and manipulate an array stack.
 * @author Liam Warren
 * @version 3/21/23*/
public class Stack {
    int[] stack = new int[10];
    int index;

    /**Constructor for class Stack.*/
    public Stack() {
        index = -1;
    }

    /**getter method for index.
     * @return index*/
    public int getIndex() {
        return index;
    }

    /**Method to check if stack is empty.
     * @return true if stack is empty*/
    public boolean isEmpty() {
        return index == -1;
    }

    /**A push method to add an int to top of stack.
     * @param n value to be pushed onto stack
     * @return true if successfully added*/
    public boolean push(int n) {
        if (index < 9) {
            ++index;
            stack[index] = n;
            return true;
        }
        return false;
    }

    /**Pop method to return integer from top of the stack and remove it from the stack.
     *@return integer from top of stack, or min int value if stack is empty */
    public int pop() {
        if (!isEmpty()) {
            int n = stack[index];
            --index;
            return n;

        }
        return Integer.MIN_VALUE;
    }

    /**peek method to return int at top of stack without removing it.
     * @return int at the top of stack, or min int value if stack is empty*/
    public int peek() {
        if (!isEmpty()) {
            return stack[index];
        }
        return Integer.MIN_VALUE;
    }

    /**Override toString method to return list of ints in stack.
     * @return list of ints in stack or null if stack is empty*/
    public String toString() {
        String stackList = "";
        if (!isEmpty()) {

            for (int i = index; i >= 0; --i) {
                stackList += stack[i];
                if (i != 0) {
                    stackList += ", ";
                }
            }
        }

        return stackList;
    }
}
