/**A program to store and manipulate a linked list.
 * @author Liam Warren
 * @version 10.24.23
 * @param <T> Type
 */
public class LinkedList<T> extends List<T> {

    Node head;
    Node tail;

    /**
     * Constructor for LinkedList, sets head and tail to null.
     */
    public LinkedList() {
        head = null;
        tail = null;
    }

    /**
     * getter for head.
     * @return head Node
     */
    public Node getHead() {
        return head;
    }

    /**
     * Getter for tail.
     * @return tail Node
     */
    public Node getTail() {
        return tail;
    }

    /**
    * Appends the specified value to the end of this list.
    *
    * @param value T The value to add
    * @return boolean True if the value is inserted, false otherwise
    */
    public boolean add(T value) {
        Node newNode = new Node(value);
    
        if (contains(value)) {
            return false;
        }
           
        
        if (head == null) {
            head = newNode;
            tail = head;
            return true;

        } else if (head.next == null) { 
            tail = newNode;
            head.next = tail;
            tail.prev = head;
            return true;

        } else {
            tail.next = newNode;
            newNode.prev = tail;
            tail = newNode;
            return true;
        }

    }

    /**
     * Inserts the specified value at the specified position in this list.
     *
     * @param index Integer The index at which to insert
     * @param value T The value to insert
     */
    public void add(int index, T value) {
        if (!contains(value) && index <= size()) {
            Node newNode = new Node(value);
            Node tmp = head;
            for (int i = 0; i < index; ++i) {
                tmp = tmp.next;
            }
            if (isEmpty()) {
                head = newNode;
                tail = newNode;
            } else if (tmp == head) {
                newNode.next = head;
                head.prev = newNode;
                head = newNode;
            } else if (tmp == null) {
                tail.next = newNode;
                newNode.prev = tail;
                tail = newNode;
            } else {
                newNode.prev = tmp.prev;
                newNode.next = tmp;
                tmp.prev.next = newNode;
                tmp.prev = newNode;
            }
            
        }
    }

    

    /**
     * Removes all of the elements from this list.
     */

    public void clear() {
        if (!isEmpty()) {
            head.next = null;
            tail.prev = null;
            head = null;
            tail = null;
        }
    }

    /**
     * Returns true if this list contains the specified element.
    *
    * @param o Object The element to check if present in the list
    * @return boolean
    */

    public boolean contains(Object o) {
        Node tmp = head;
        while (tmp != null) {
            if (tmp.value.equals(o)) {
                return true;
            }
            tmp = tmp.next;
        }

        return false;

    }

    /**
     * Returns the element at the specified position in this list.
    *
    * @param index Integer The index at which to insert
    * @return T
    */

    public T get(int index) {
        Node tmp = head;

        for (int i = 0; i < index; ++i) {
            if (isEmpty() || tmp.next == null) {
                return null;
            }
            tmp = tmp.next;
        }
        if (isEmpty()) {
            return null;
        }
        return tmp.value;
    }

    /**
     * Get the list entry corresponding to the value provided in the parameter.
    * @param o to search for
    * @return T matching data in the list
    */
    public T get(Object o) {

        Node tmp = head; 
        while (tmp != null) {
            if (tmp.value.equals(o)) {
                return tmp.value;
            }
            tmp = tmp.next;
        }
        return null;
        
    }

    /**
     * Removes the element at the specified position in this list.
    * Returns the element from the list or null if index is invalid.
    *
    * @param index the index of the element to be removed
    * @return the element previously at the specified position or null
    */

    public T remove(int index) {
        Node tmp = head;

        for (int i = 0; i < index; ++i) {
            if (isEmpty() || tmp.next == null) {
                return null;
            }
            tmp = tmp.next;
        }
        if (isEmpty()) {
            return null;
        }
        remove(tmp);
        return tmp.value;



    }

    /**
     * Removes the first occurrence of the specified element from this 
    * list, if it is present.
    * If this list does not contain the element, it is unchanged.
    * Returns true if this list contained the specified element
    * (or equivalently, if this list changed as a result of the call).
    *
    * @param o element to be removed from this list, if present
    * @return true if this list contained the specified element
    */

    public boolean remove(Object o) {

        Node tmp = head;
        while (tmp != null) {
            if (tmp.value.equals(o)) {
                remove(tmp);
                return true;
            }
            tmp = tmp.next;
        }

        return false;
    }

    /**
     * Returns true if this list contains no elements.
    *
    * @return true if this list contains no elements
    */

    public boolean isEmpty() {
        if (head == null) {
            return true;
        }
        return false;
    }


    /**
     * Returns the number of elements in this list.
    * @return int
    */

    public int size() {
        int i = 0;
        Node tmp = head;
        while (tmp != null) {
            ++i;
            tmp = tmp.next;
        }
        return i;
    }

    /**
     * Helper function for removing node from linked list.
     * @param tmp Node to remove
     */
    public void remove(Node tmp) {
        if (tmp == head) {
            head = tmp.next;

            if (head != null) {
                head.prev = null;
                tmp.next = null;
            }    
            
        } else if (tmp == tail) {
            tail = tmp.prev;
            tail.next = null;
            tmp.prev = null;
           
        } else {
            tmp.prev.next = tmp.next;
            tmp.next.prev = tmp.prev;
            tmp.next = null;
            tmp.prev = null;
        }

        
        
    }

    /**Overridded to string method.
     * @return String representing the Linked List
     */
    public String toString() {
        String ret = "[";
        Node tmp = head;
        while (tmp != null) {
            ret += tmp.value;
            if (tmp != tail) {
                ret += ", ";
            }
            tmp = tmp.next;
        }

        ret += "]";
        return ret;

    }    
}
