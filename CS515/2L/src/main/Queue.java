
public class Queue<T> {

    private Node _front;
    private Node _rear;
    private int _size;

    private class Node {
        T data;
        Node next;

        public Node(T e) {
            this.data = e;
        }

        public Node(T e, Node n) {
            this(e);
            this.next = n;
        }
    }

    public Queue() {
        // To Implement
    }

    public Queue(Queue<T> q) {
        // To Implement
    }

    public void enqueue(T e) {
        // To Implement
    }

    public T dequeue() {
        // To Implement
        return null;
    }

    public int size() {
        // To Implement
        return 1;
    }

    public boolean isEmpty() {
        // To Implement
        return true;
    }

    public String toString() {
        // To Implement
        return "";
    }
}
