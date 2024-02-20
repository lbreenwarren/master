package set;

/**
 * Set implemented with doubly linked list and sentinel head & tail.
 * @param <T> the type being stored in the set
 */
public class Set<T extends Comparable<T>> {

    private Elem head, tail;
    private int size;

    /**
     * Constructs an empty set.
     */
    public Set() {
        this.head = new Elem();
        this.tail = new Elem();

        this.head.prev = null;
        this.head.next = tail;

        this.tail.prev = head;
        this.tail.next = null;

        this.size = 0;
    }

    /**
     * Copy constructor.
     * @param s the set to be copied
     */
    public Set(Set<T> s) {
        // TODO : Implement
    }

    /**
     * Inserts an element and keeps the list sorted.
     * @param value the element to be added
     */
    public void insert(T value) {
        // TODO : Implement
    }

    /**
     * Returns whether or not a given element is found in the set.
     * @param value the element being searched for
     * @return true if element found; false otherwise
     */
    public boolean find(T value) {
        // TODO : Implement
        return false;
    }

    /**
     * Removes an element from the set if found.
     * @param value the element to be removed
     */
    public void erase(T value) {
        // TODO : Implement
    }
    
    /**
     * Removes all the elements from the set.
     */
    public void clear() {
        // TODO : Implement
    }

    /**
     * Returns the size of the set.
     * @return the size of the set
     */
    public int size() {
        return this.size;
    }

    /**
     * Equals method override.
     * @param o the object being compared to
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (!(o instanceof Set)) {
            return false;
        }  
        Set<T> otherSet = (Set<T>) o;

        // TODO : Implement
        
        return false;
    }

    /**
     * toString method override for printing set contents.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Elem curr = this.head.next;
        while (curr != this.tail) {
            sb.append(curr.info);
            sb.append(" ");
            curr = curr.next;
        }
        return sb.toString();
    }

    /**
     * Inner class representing linked list node
     */
    private class Elem {
        private T info;
        private Elem prev, next;

        public Elem() {
            this.prev = null;
            this.next = null;
        }

    }
    
}
