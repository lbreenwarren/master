
/**
* LinkedList lab.
* @author Liam Warren  
* @version 10.3.23
*/
public class LinkedList {

    /**
    * Internal Node class used for the linked list.
    */
    public class Node {
        String key;
        int value;
        Node next;

        /**
        * Node Constructor.
        * @param key The Key
        * @param value The Value
        */
        public Node(String key, int value) {
            this.key = key;
            this.value = value;
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
    * Add the key, value pair to the head of the linkedlist.
    * @param key The Key
    * @param val The Value
    *
    */
    public void addHead(String key, int val) {
        Node n = new Node(key, val);

        if (head == null) {
            head = n;
            tail = n;
        } else {
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
    private Node find(String key) {
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
     * Calculates the average value of all elements in a linked list.
     *
     * @param list The linked list containing numeric values.
     * @return The average value of the elements in the linked list.
     */
    public static double average(LinkedList list) {
        Node tmp = list.head;
        int total = 0;
        int count = 0;
        while (tmp != null) {
            total += tmp.value;
            ++count;
            tmp = tmp.next;
        }

        return (double) total / (double) count;
    }

    /**
     * Checks if the elements in a linked list are in ascending order.
     *
     * @param list The linked list to check.
     * @return True if the list is ordered in ascending order, false otherwise.
     */
    public static boolean ordered(LinkedList list) {
        Node tmp = list.head;
        while (tmp.next != null) {
            if (tmp.key.compareTo(tmp.next.key) <= 0) {
                tmp = tmp.next;
            } else {
                return false;
            }
        }
        return true;
    }

    /**
     * Creates a new linked list with elements in reversed order.
     *
     * @param list The original linked list.
     * @return A new linked list with elements in reversed order.
     */
    public static LinkedList reversed(LinkedList list) {
        LinkedList revList = new LinkedList();
        Node tmp = list.head;
        
        while (tmp != null) {
            revList.addHead(tmp.key, tmp.value);
            tmp = tmp.next;
        }

        return revList;
    }

    /**
     * Compresses a linked list by combining nodes with the same key.
     *
     * @param list The original linked list.
     * @return A new linked list with combined nodes for the same keys.
     */
    public static LinkedList compressList(LinkedList list) {
        Node tmp = list.head;
        LinkedList comprList = new LinkedList();
        while (tmp != null) {
            if (comprList.find(tmp.key) == null) {
                comprList.addTail(tmp.key, tmp.value);
            } else {
                comprList.find(tmp.key).value += tmp.value;
            }
            tmp = tmp.next;
        }

        return comprList;
    }


    ///////////////////////////////////////////////////////////////

}
