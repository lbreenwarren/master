import java.util.Scanner;
import java.util.Stack;

/**
 * The Postfix class provides methods for evaluating postfix expressions.
 * @author Liam Warren
 * @version 10.23.23
 */
public class Postfix {

    /**
     * Checks if a given string represents a valid operator and returns the operator character.
     *
     * @param opString The operator as a string.
     * @return The operator character ('+', '-', '*', '/', or '?').
     */
    public static char checkValidOperator(String opString) {
        switch (opString) {
            case "+":
                return '+';
            case "-":
                return '-';
            case "/":
                return '/';
            case "*":
                return '*';
            default:
                return '?';
        }
    }

    /**
     * Executes a mathematical operation based on the operator and updates the stack.
     *
     * @param st       The stack containing operands.
     * @param operator The operator character ('+', '-', '*', '/').
     * @return The result of the operation or Integer.MIN_VALUE if the operator is invalid.
     */
    public static int execute(Stack<Integer> st, char operator) {
        int leftop = st.pop();
        int rightop = st.pop();
        int out = 0;

        switch (operator) {
            case '+':
                out = rightop + leftop;
                st.push(out);
                return out;
            case '-':
                out = rightop - leftop;
                st.push(out);
                return out;
            case '/':
                out = rightop / leftop;
                st.push(out);
                return out;
            case '*':
                out = rightop * leftop;
                st.push(out);
                return out;
        }
        return Integer.MIN_VALUE;
    }

    /**
     * Evaluates a postfix expression and returns the result.
     *
     * @param expression The postfix expression as a string.
     * @return The result of the evaluation, or null if an error occurred.
     */
    public static Number evaluateExpression(String expression) {
        // Error messages
        String tooFewOperands = "Too few operands";
        String unknownOperator = "Unknown operator: ";
        String tooManyOperands = "Too many operands.";

        Stack<Integer> stack = new Stack<Integer>();
        String errMsg = null;
        Scanner expr = new Scanner(expression);

        while (expr.hasNext() && errMsg == null) {
            if (expr.hasNextInt()) {
                int operand = expr.nextInt();
                System.out.println("Operand read: " + operand);
                stack.push(operand);
            } else {
                String operatorString = expr.next();
                char operator = checkValidOperator(operatorString);
                if (operator != '?') {
                    System.out.println("Operator read: " + operator);

                    if (stack.size() >= 2) {
                        execute(stack, operator);
                    } else {
                        errMsg = tooFewOperands;
                    }
                } else {
                    errMsg = unknownOperator + operatorString;
                }
            }
            System.out.println("------ Stack state -----");
            System.out.println(stack);
        }

        expr.close();

        if (errMsg != null) {
            System.out.println("Failed evaluation of |" + expression + "|\n" + errMsg);
            return null;
        } else if (stack.size() > 1) {
            errMsg = tooManyOperands;
            System.out.println("Failed evaluation of |" + expression + "|\n" + tooManyOperands + stack);
            return null;
        } else {
            return stack.pop();
        }
    }

    /**
     * The main method for running the postfix expression evaluation.
     *
     * @param args Command-line arguments (not used in this program).
     */
    public static void main(String[] args) {
        String input = "";
        Scanner in = new Scanner(System.in);
        System.out.println("Enter a postfix expression");
        if (in.hasNextLine()) {
            input = in.nextLine();
            Number result = evaluateExpression(input);
            if (result != null) {
                System.out.println("Expression: " + input + " --> " + result);
            }
        }
        in.close();
    }
}
