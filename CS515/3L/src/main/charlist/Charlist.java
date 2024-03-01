/** CS515 Lab 3
 File: Charlist.java
 Name: Liam Warren
 Section: 1
 Date: 2.20.24
*/ 
package charlist;

/**
 * LinkedList used to represent jumbled words.
 */
public class Charlist {
    
    private Elem head; // Elem that is list head

    /**
     * Default constructor for Charlist.
     */
    public Charlist() {
        head = null;
    }

    /**
     * Copy constructor for Charlist.
     * @param cl the Charlist to make a copy of
     */
    public Charlist(Charlist cl) {
        Elem e1, e2;

        if (cl.head == null) {
            this.head = null;
        } else {
            this.head = new Elem();
            this.head.info = cl.head.info;
            e1 = this.head;
            e2 = cl.head.next;

            while (e2 != null) {
                e1.next = new Elem();
                e1.info = e2.info;
                e2 = e2.next;
            }
            e1.next = null; 
        }
    }

    /**
     * toString method override for printing Charlist contents.
     * @return a string represetning the Charlist
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Elem e = this.head;
        while (e != null) {
            sb.append(e.info);
            e = e.next;
        }
        return sb.toString();
    }
    
    /**
     * Inserts a char node at list front.
     * @param c the char to insert 
     */
    public void insertFront(char value) {
        Elem e = new Elem();
        e.info = value;
        e.next = this.head;
        this.head = e;
    }

    /**
     * Inserts a char node at list rear.
     * @param c the char to insert
     */
    public void insertRear(char value) {
        Elem cur = this.head;
        if (head == null) { // Empty list
            Elem e = new Elem();
            e.info = value;
            e.next = this.head;
            this.head = e;
        } else {
            while (cur.next != null) {
                cur = cur.next;
            }
            Elem e = new Elem();
            e.info = value;
            e.next = null;
            cur.next = e;
        }
    }
    
    /**
     * Inner class representing linked list node.
     */
    public class Elem {
        private char info;
        private Elem next;

        /**
         * Default constructor for Elem object that creates blank Elem.
         */
        public Elem() {
            this.info = 0;
            this.next = null;
        }
        
        /**
         * Constructor for Elem objects.
         * @param info the char for this Elem
         * @param next the next Elem in linked list
         */
        public Elem(char info, Elem next) {
            this.info = info;
            this.next = next;
        }
    }

}