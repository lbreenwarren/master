/** CS515 Lab 1
 * File: Stack.java
 * Name: Liam Warren
 * Section: 2
 * Date: 1/31/23
 */
package stack;

import exceptions.*;

public class Stack {
    
    private final int INIT_CAP = 8; // Max number of elements in stack to start

    private int[] item;             // Dynamic integer array
    private int top;                // Index of stack top
    private int capacity;           // Current capacity of the stack

    /**
     * Constructor creates stack with default capacity.
     */
    public Stack() {
        this.capacity = INIT_CAP;
        this.top = 0;                   // Index to add to next
        this.item = new int[INIT_CAP];
    }

    /**
     * Constructor creates stack with the given capacity.
     */
    public Stack(int capacity) {
        this.capacity = capacity;
        this.top = 0;
        this.item = new int[capacity];
        return;
    }

    /**
     * Copy consructor creates a new stack which is a deep copy of given stack.
     * @param s the stack to copy
     */
    public Stack(Stack s) {
        this.capacity = s.capacity;
        this.top = s.size();                   // Index to add to next
        item = new int[capacity];
        for (int i = 0; i < size(); ++i) {
            item[i] = s.item[i];
        }
    }

    /**
     * Pushes an element on the stack. If stack full, double in size.
     * @param data the element to be pushed on stack
     */
    public void push(int data) {

        if (top == capacity) {
            capacity = capacity * 2;
            int[] newItem = new int[capacity];
            for (int i = 0; i < top; ++i) {
                newItem[i] = item[i];
            }
            this.item = newItem;
        }
        item[top++] = data;
        return;
    }

    /**
     * Pops the top element from the stack and returns it.
     * Throws EmptyStackException if no element to pop.
     * @return the top element in the stack
     */
    public int pop() throws EmptyStackException {
        if (empty()) {
            throw new EmptyStackException("Stack is empty");
        }
        return item[--top];
    }

    /**
     * Returns the top element from the stack (not removed).
     * Throws EmptyStackException if no element to return.
     * @return the integer at the top of the stack
     */
    public int top() throws EmptyStackException {
        if (empty()) {
            throw new EmptyStackException("Stack is empty");
        }
        return item[top - 1];
    }

    /**
     * Returns whether or not the stack is empty.
     * @return true if stack is empty, false otherwise
     */
    public boolean empty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    /**
     * Returns the number of elements currently on the top of the stack.
     * @return an integer repsenting how many elements are on stack
     */
    public int size() {
        return top;
    }

    /**
     * Returns the capacity of the stack.
     * @return an integer representing the capcity of the stack
     */
    public int capacity() {
        return capacity;
    }

    /**
     * Equality operator override compares two stacks for equality.
     * @param s "otherStack" the stack being compared to
     * @return true if stacks are equal, false otherwise
     */
    @Override
    public boolean equals(Object s) {
        if (!(s instanceof Stack)) {
            return false;
        }
        Stack otherStack = (Stack) s;

        for (int i = 0; i < size(); ++i) {
            if (otherStack.item[i] != item[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Builds a string based on the contents of the stack.
     * @return the string representing the stack
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        // Build string from stack top to bottom
        for (int i = this.top - 1; i >= 0; i--) {
            sb.append(this.item[i]);
            sb.append("\n");
        }
        
        return sb.toString();
    }

}