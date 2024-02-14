/**
* LinkedList lab.
* @author cs416
* @version 1
*/
public class LinkedList {

    /**
    * Internal Node class used for the linked list.
    */
    public static class Node {
        String key;
        int value;
        Node next;
        Node prev;

        /**
        * Node Constructor.
        * @param key The Key
        * @param value The Value
        */
        public Node(String key, int value) {
            this.key = key;
            this.value = value;
        }

        /**
        * Gets the next Node in the List.
        * @return Node
        */
        public Node getNext() {
            return next;
        }

        /**
        * Gets the previous Node in the List.
        * @return Node
        */
        public Node getPrev() {
            return prev;
        }

        /**
        * Gets the String Key.
        * @return String
        */
        public String getKey() {
            return key;
        }

        /**
        * Gets the Value.
        * @return int
        */
        public int getValue() {
            return value;
        }
    }

    private Node head;
    private Node tail;

    /**
    *  The default constructor.
    */
    public LinkedList() {
        head = null;
        tail = null;
    }

    /**
    * Gets the head of the List.
    * @return Node Head Node
    */
    public Node getHead() {
        return head;
    }

    /**
    * Gets the tail of the List.
    * @return Node Tail Node
    */
    public Node getTail() {
        return tail;
    }

    /**
    * Add the key, value pair to the head of the linkedlist.
    * @param key The Key
    * @param val The Value
    */
    public void addHead(String key, int val) {
        Node n = new Node(key, val);

        if (head == null) {
            head = n;
            tail = n;
        } else {
            head.prev = n;
            n.next = head;
            head = n;
        }
    }

    /**
    * Add the key, val pair to the tail of the linkedlist.
    * @param key The Key
    * @param val The Value
    */
    public void addTail(String key, int val) {
        Node n = new Node(key, val);

        if (tail == null) {
            head = n;
            tail = n;
        } else {
            tail.next = n;
            n.prev = tail;
            tail = n;
        }
    }

    /**
    * Returns the String format of the linkedlist.
    * @return String The String format.
    */
    public String toString() {
        String ret = "";

        Node curr = head;

        while (curr != null) {
            if (curr.next != null) {
                ret += curr.key + ":" + curr.value + ", ";
            } else {
                ret += curr.key + ":" + curr.value;
            }

            curr = curr.next;
        }

        return ret;
    }

    /**
    * Locate the Node in the linkedlist with the given key.
    * @param key  The key to find in the LinkedList
    * @return Node Returns the Node with the given key or null if non-existent.
    */
    public Node find(String key) {
        Node curr = head;

        while (curr != null) {
            if (curr.key.equals(key)) {
                return curr;
            }

            curr = curr.next;
        }

        return null;
    }


    //////////////////////// YOUR CODE HERE ////////////////////////

    /**
     * Unlinks the specified node from the linked list. If the node is the head or tail, 
     * updates the head or tail accordingly.
     * @param n The node to unlink.
     */
    public void unlinkNode(Node n) {
        if (n.prev == null) {
            head = n.next;
            head.prev = null;
        } else if (n.next == null) {
            tail = n.prev;
            tail.next = null;
        } else {
            n.next.prev = n.prev;
            n.prev.next = n.next;
        }
    }

    /**
     * Adds a new node (n) after the specified node (before) in the linked list.
     *
     * @param n      The node to be added.
     * @param before The node after which the new node will be added.
     */
    public void addAfter(Node n, Node before) {

        if (before.next == null) {
            tail = n;
            before.next = n;
            n.next = null;
        } else {
            before.next.prev = n;
            Node tmp = before.next;
            n.prev = before;
            n.next = tmp;
            before.next = n;
        }

    }

    /**
     * Checks if the given node needs to be swapped with the next node based on their keys. 
     * If swapping occurs, updates the linked list.
     * @param n The node to be checked and potentially swapped.
     * @return true if a swap occurred, false otherwise.
     */
    public boolean swapIfNeeded(Node n) {

        if (n.next == null) {
            return false;
        }

        if (n.key.compareTo(n.next.key) > 0) {
            Node tmp = n.next;
            unlinkNode(n);
            addAfter(n, tmp);
            return true;
        }

        return false;
    }

    /**
     * Sorts the linked list in ascending order based on the keys of the nodes.
     */
    public void sort() {
        boolean sorted = false;

        while (!sorted) {
            Node tmp = head;
            sorted = true;

            while (tmp != null) {

                if (swapIfNeeded(tmp)) {
                    sorted = false;
                }

                tmp = tmp.next;
            }
        }
    }

    ///////////////////////////////////////////////////////////////

}
