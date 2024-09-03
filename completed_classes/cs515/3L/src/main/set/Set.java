/** CS515 Lab 3
 File: Set.java
 Name: Liam Warren
 Section: 1
 Date: 2.20.24
*/ 

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
        //init head and tail
        this.head = new Elem();
        this.tail = new Elem();

        this.head.prev = null;
        this.head.next = tail;

        this.tail.prev = head;
        this.tail.next = null;

        this.size = 0;

        //copy Set s
        Elem tmp = s.head.next;
        while (tmp!=s.tail) {
            insert(tmp.info);
            tmp = tmp.next;
        }
    }

    /**
     * Inserts an element and keeps the list sorted.
     * @param value the element to be added
     */
    public void insert(T value) {
        Elem tmp = head.next;
        while (tmp != tail && value.compareTo(tmp.info) > 0) {
            tmp = tmp.next;
        }
        if (tmp == tail || tmp.info.compareTo(value) != 0) {
            Elem newElem = new Elem();
            newElem.info = value;
            tmp.prev.next = newElem;
            newElem.prev = tmp.prev;
            newElem.next = tmp;
            tmp.prev = newElem;
            ++size;
        }
    }

    /**
     * Returns whether or not a given element is found in the set.
     * @param value the element being searched for
     * @return true if element found; false otherwise
     */
    public boolean find(T value) {
        Elem tmp = head.next;
        while (tmp != tail) {
            if (tmp.info.compareTo(value) == 0) {
                return true;
            }
            tmp = tmp.next;
        }
        return false;
    }

    /**
     * Removes an element from the set if found.
     * @param value the element to be removed
     */
    public void erase(T value) {
        Elem tmp = head.next;
        while (tmp != tail) {
            if (tmp.info.compareTo(value) == 0) {
                tmp.prev.next = tmp.next;
                tmp.next.prev = tmp.prev;
                --size;
            }
            tmp = tmp.next;
        }

    }
    
    /**
     * Removes all the elements from the set.
     */
    public void clear() {
        head.next = tail;
        tail.prev = head;
        size = 0;
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
        Elem tmp = head.next;
        Elem otherTmp = otherSet.head.next;
        while(tmp != tail) {
            if(!tmp.info.equals(otherTmp.info)) {
                return false;
            }
            tmp = tmp.next;
            otherTmp = otherTmp.next;
        }
        
        return true;
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
