
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
        _front = null;
        _rear = null;
        _size = 0;
    }

    public Queue(Queue<T> q) {
        this._front = q._front;
        this._rear = q._rear;
        Node qtmp = q._front;
        while(qtmp != null) {
            enqueue(qtmp.data);
            qtmp = qtmp.next;
        }
    }

    public void enqueue(T e) {
        Node newNode = new Node(e);

        if (isEmpty()) {
            _front = newNode;
            _rear = newNode;
            ++_size;
        } else {
            _rear.next = newNode;
            _rear = newNode;
            newNode.next = null;
            ++_size;
        }

        
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new EmptyQueueException("Queue is Empty");
        }
        else if (size() == 1) {
            T ret = _front.data;
            _front = null;
            _rear = null;
            --_size;
            return ret;
        } else {
            T ret = _front.data;
            _front = _front.next;
            --_size;
            return ret;
        }
    }

    public int size() {
        
        return _size;
    }

    public boolean isEmpty() {
        if (size() == 0) {
            return true;
        }
        return false;
    }

    public String toString() {
        String ret = "";
        Node tmp = _front;
        while(tmp != null) {
            ret += tmp.data + " ";
            tmp = tmp.next;
        }
        return ret;
    }
}
