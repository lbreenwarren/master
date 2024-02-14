import java.util.Scanner;

/**Map 1 starter program to be expanded on.
 * @author Liam Warren
 * @version 3/23/23
 ** @param <T> value parameter for linked list*/

public class LinkedList<T> extends List<T> {

    Node head;
    Node tail;

    /**getter for Head.
     * @return head.*/
    public Node getHead() {
        return head;
    }

    /**getter for tail.
     * @return tail*/
    public Node getTail() {
        return tail;
    }

    /**
     * Appends the specified value to the end of this list.
     *
     * @param value T The value to add
     * @return boolean True if the value is inserted, false otherwise
     */

    boolean add(T value) {
        Node tmp = head;
        while (tmp != tail) {
            if (value.equals(tmp.value)) {
                return false;
            }
            tmp = tmp.next;
        }
        if (head != null && head == tail && head.value.equals(value)) {
            return false;
        }
        Node newNode = new Node(value);
        if (head == null && tail == null) {
            head = newNode;
            tail = newNode;
            return true;
        }
        tail.next = newNode;
        newNode.prev = tail;
        tail = newNode;
        return true;

    }

    /**
     * Inserts the specified value at the specified position in this list.
     *
     * @param index Integer The index at which to insert
     * @param value T The value to insert
     */
    void add(int index, T value) {
        Node tmp = head;
        int i = 0;
        while (tmp != null && i < index) {
            if (value.equals(tmp.value)) {
                return;
            }
            tmp = tmp.next;
            ++i;
        }
        if (tmp == null) {
            add(value);
        } else {
            Node add = new Node(value);
            if (index == 0) {
                add.next = head;
                head.prev = add;
                head = add;
            } else {
                tmp.prev.next = add;
                add.prev = tmp.prev;
                tmp.prev = add;
                add.next = tmp;

            }
        }
    }

    /**
     * Removes all of the elements from this list.
     */
    void clear() {
        head.next = null;
        head = null;
        tail = null;
    }

    /**
     * Returns true if this list contains the specified element.
     *
     * @param o Object The element to check if present in the list
     * @return boolean
     */
    boolean contains(Object o) {
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
    T get(int index) {
        Node tmp = head;
        int i = 0;
        while (tmp != null && i < index) {
            tmp = tmp.next;
            ++i;
        }
        if (tmp == null) {
            return null;
        }
        return tmp.value;

    }

    /**
     * Get the list entry corresponding to the value provided in the parameter.
     *
     * @param o to search for
     * @return T matching data in the list
     */
    T get(Object o) {
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

    T remove(int index) {
        Node tmp = head;
        int i = 0;
        while (tmp != null && i < index) {
            tmp = tmp.next;
            ++i;
        }
        if (tmp == null) {
            return null;
        }
        if (index == 0) {
            if (tmp == tail) {
                clear();
                return null;
            }
            head = tmp.next;
            head.prev = null;
            return head.value;
        } else if (tmp == tail) {
            tail = tmp.prev;
            tail.next = null;
            return tail.value;
        } else {
            tmp.next.prev = tmp.prev;
            tmp.prev.next = tmp.next;
            return tmp.value;
        }
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

    boolean remove(Object o) {
        Node tmp = head;
        while (tmp != null && !tmp.value.equals(o)) {
            tmp = tmp.next;
        }
        if (tmp == null) {
            return false;
        }
        if (tmp.next == null && tmp.prev == null) {
            clear();
            return true;
        }
        if (tmp == head) {
            head = tmp.next;
            tmp.next.prev = null;
            return true;
        } else if (tmp == tail) {
            tail = tmp.prev;
            tmp.prev.next = null;
            return true;
        } else {
            tmp.next.prev = tmp.prev;
            tmp.prev.next = tmp.next;
            return true;
        }
    }

    /**
     * Returns true if this list contains no elements.
     *
     * @return true if this list contains no elements
     */
    boolean isEmpty() {
        if (head == null) {
            return true;
        }
        return false;
    }

    /**
     * Returns the number of elements in this list.
     *
     * @return int*/
    @Override
    int size() {
        Node tmp = head;
        int i = 0;
        while (tmp != null) {
            ++i;
            tmp = tmp.next;
        }
        return i;
    }

    /**toString method for LinkedList class.
     * @return list info*/

    public String toString() {
        String list = "";

        Node ptr = head;
        while (ptr != null) {
            list += ptr.value;
            if (ptr != tail) {
                list += ", ";
            }
            ptr = ptr.next;
        }
        return "[" + list + "]";
    }

    /**main method.
     * @param args command line arguments (not used)*/
    public static void main(String[] args) {
        LinkedList list;
        list = new LinkedList();
        Scanner in = new Scanner(System.in);
        String input = "";
        input = in.next();
        while (!input.equals("x")) {
            if (input.equals("a")) {
                String add = in.next();
                list.add(add);
                System.out.println(list);
            }
            if (input.equals("A")) {
                int addnum = in.nextInt();
                String add = in.next();
                list.add(addnum, add);
                System.out.println(list);
            }
            if (input.equals("c")) {
                String cont = in.next();
                System.out.println(list.contains(cont));
            }
            if (input.equals("C")) {
                list.clear();
                System.out.println(list);
            }
            if (input.equals("g")) {
                int get = in.nextInt();
                System.out.println(list.get(get));
            }
            if (input.equals("s")) {
                System.out.println(list.size());
            }
            if (input.equals("e")) {
                System.out.println(list.isEmpty());
            }
            if (input.equals("r")) {
                String rem = in.next();
                list.remove(rem);
                System.out.println(list);
            }
            if (input.equals("R")) {
                int rem = in.nextInt();
                list.remove(rem);
                System.out.println(list);
            }

            System.out.println("------------------");
            input = in.next();

        }
    }

}
