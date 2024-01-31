/** CS515 Lab 1
 * File: Queue.java
 * Name: Liam Warren
 * Section: 2
 * Date: 1/31/23
 */

package queue;

import exceptions.EmptyQueueException;

/*
 * Circular array implementation of int queue.
 */
public class Queue {

    final int INIT_CAP = 10; // Max number of elements in queue to start
    
    private int[] item;      // Array of queue elements 
    private int front;       // Index of queue front
    private int rear;        // How many elements are in queue
    private int capacity;    // Current capacity of the queue
    
    /**
     * Constructor creates stack with default capacity.
     */ 
    public Queue() {
        this.front = -1;
        this.rear = 0;
        this.capacity = INIT_CAP;
        this.item = new int[INIT_CAP];
    }

    /**
     * Constructor creates stack with the given capacity.
     */
    public Queue(int capacity) {
        this.front = -1;
        this.rear = 0;
        this.capacity = capacity;
        this.item = new int[capacity];
    }
    
    /**
     * Creates a queue identical to the one given.
     * @param q the queue to make a copy of
     */
    public Queue(Queue q) {
        this.front = q.front;
        this.rear = q.rear;
        this.capacity = q.capacity;
        this.item = new int[capacity];
        for (int i = 0; i < capacity; ++i) {
            this.item[i] = q.item[i];
        }
    }

    /**
     * Adds an element to the end of qeueue.
     * Precondidtion: queue is not full.
     * @param data an integer to add to the end of the queue
     */
    public void enqueue(int data) {
        if (empty()) {
            this.front = 0;
        }
        if (rear == capacity) {
            int newFront = front + capacity;
            capacity = capacity * 2;
            int[] newItem = new int[capacity];
            for (int i = 0; i < rear; ++i) {
                newItem[(newFront + i) % capacity] = item[(front + i) % 10];
            }
            front = newFront;
            item = newItem;
        }
        this.item[(this.front + this.rear) % capacity] = data;
        ++this.rear;
        return;
    }

    /**
     * Removes an element from the front of the queue.
     * Throws EmptyQueueException if no elems in queue.
     * @return the integer removed from the front of the queue
     */
    public int dequeue() throws EmptyQueueException {
        if (empty()) {
            throw new EmptyQueueException("Queue is Empty");
        }
        int ret = this.item[this.front];
        this.front = (this.front + 1) % capacity;
        --this.rear;
        return ret;
    }

    /**
     * Builds a String based off the state of the queue.
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = this.front; i < this.rear; i++) {
             sb.append(this.item[i]);
             sb.append(" ");
        }

        return sb.toString();
    }

    /**
     * Returns the number of elements currently in the queue.
     * @return an integer representing the size of the queue
     */
    public int size() {
        return (this.rear);
    }

    /**
     * Returns whether or not the queue is empty.
     * @return true if queue is empty, false otherwise
     */
    public boolean empty() {
        return (this.rear == 0);
    }

    /**
     * Returns whether or not the queue is full.
     * @return true if queue is full, false otherwise
     */ 
    public boolean full() {
        return (this.size() >= this.capacity);
    }

    /**
     * Returns the current capacity of the queue.
     * @return an int representing the current queue capacity
     */
    public int capacity() {
        return this.capacity;
    }

    /**
     * Returns the element at the front of the queue (does not remove it).
     * Throws EmptyStackException if no elements in queue.
     * @return the int at the front of the queue
     */
    public int front() throws EmptyQueueException {
        if (!this.empty()) {
            return this.item[front];
        } else {
            throw new EmptyQueueException("Tried to front on an empty queue.");
        }
    }

}