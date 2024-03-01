package OrderedSet;

import java.util.NoSuchElementException;

/**
 * Set implemented with doubly linked list and sentinel head & tail.
 * Supports set operations and iterators.
 * Based on the Java SortedSet: https://docs.oracle.com/javase/7/docs/api/java/util/Set.html
 *                              https://docs.oracle.com/javase/7/docs/api/java/util/SortedSet.html
 * @param <E> the type being stored in the set
 */
public class OrderedSet<E extends Comparable<E>> {

    private Elem head, tail;
    private int size;

    /**
     * Constructs an empty set.
     */
    public OrderedSet() {
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
    public OrderedSet(OrderedSet<E> s) {
        OrderedIterator it = new OrderedIterator();
        while(it.hasNext()) {
            s.insert(it.next());
        }
    }

    /**
     * Inserts an element and keeps the list sorted.
     * @param value the element to be added
     */
    public void insert(E value) {
        OrderedIterator it = new OrderedIterator();
        if (!it.hasNext()) {
            Elem newElem = new Elem();
            newElem.info = value;
            it.nextElem.prev = newElem;
            it.prevElem.next = newElem;
            newElem.next = tail;
            newElem.prev = head;
            ++size;
        }else{
            while (it.hasNext()) {
                if (it.next().compareTo(value) > 0) {
                    Elem newElem = new Elem();
                    newElem.info = value;
                    newElem.next = it.prevElem;
                    newElem.prev = it.prevElem.prev;
                    it.prevElem.prev.next = newElem;
                    it.prevElem.prev = newElem;
                    ++size;
                    return;
                }
                if (it.prevElem.info.equals(value)) {
                    return;
                }
            }
            Elem newElem = new Elem();
            newElem.info = value;
            newElem.prev = tail.prev;
            newElem.next = tail;
            tail.prev.next = newElem;
            tail.prev = newElem;
            ++size;
        }
    }

    /**
     * Returns whether or not a given element is found in the set.
     * @param value the element being searched for
     * @return true if element found; false otherwise
     */
    public boolean contains(E value) {
        OrderedIterator it = new OrderedIterator();
        while (it.hasNext()) {
            if (it.nextElem.info.equals(value)) {
                return true;
            }
            it.next();
        }
        return false;

    }

    /**
     * Removes an element from the set if found.
     * @param value the element to be removed
     */
    public void erase(E value) {
        OrderedIterator it = new OrderedIterator();
        while (it.hasNext()) {
            if (it.next().equals(value)) {
                it.nextElem.prev = it.prevElem.prev;
                it.prevElem.prev.next = it.nextElem;
                --size;
            }
        }
    }
    
    /**
     * Removes all the elements from the set.
     */
    public void clear() {
        head.next = tail;
        tail.prev = head;
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
        if (!(o instanceof OrderedSet)) {
            return false;
        }
        OrderedSet<E> otherSet = (OrderedSet<E>) o;
        OrderedIterator other = otherSet.orderedIterator();
        OrderedIterator it = new OrderedIterator();
        while(it.hasNext()) {
            if (!it.next().equals(other.next())) {
                return false;
            }
        }

        if (other.hasNext()) {
            return false;
        }

        return true;
    }

    /**
     * toString method override for printing set contents.
     * @return a string representing the sets content
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
     * Perform set union on this set and the given set.
     * (Add elements from other set to this set, if not already present).
     * @param b the other set
     */
    public void addAll(OrderedSet<E> b) {
        OrderedIterator bIterator = b.orderedIterator();
        while(bIterator.hasNext()) {
            insert(bIterator.next());
        }
       
    }

    /**
     * Perform set intersection on this set and the given set.
     * (Retain only elements of this set that are also present in other set).
     * @param b the other set
     */
    public void retainAll(OrderedSet<E> b) {
        OrderedIterator thisIterator = this.orderedIterator();
        while (thisIterator.hasNext()) {
            E val = thisIterator.next();
            if (!b.contains(val)) {
                erase(val);
            }
        }
    }

    /**
     * Perform set difference on this set and the given set.
     * (Remove elements from this set that are contained in the other set).
     * @param b the other set
     */
    public void removeAll(OrderedSet<E> b) {
        OrderedIterator bIterator = b.orderedIterator();
        while (bIterator.hasNext()) {
            erase(bIterator.next());
        }
    }

    /**
     * Return an Iterator that starts at the first element of the set.
     * @return an Iterator for this set
     */
    public OrderedSet<E>.OrderedIterator orderedIterator() {
        return new OrderedIterator();
    }

    /**
     * Returns the first element in the set. 
     * @return the first element in the set
     */
    public E first() {
        OrderedIterator it = new OrderedIterator();
        return it.next();
    }

    /**
     * Returns the last element in the set.
     * @return the last element in the set
     */
    public E last() {
        OrderedIterator it = new OrderedIterator();
        return it.prev();
    }

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    /**
     * Inner class representing linked list node.
     */
    private class Elem {
        private E info;
        private Elem prev, next;

        public Elem() {
            this.prev = null;
            this.next = null;
        }
    }

    // * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *

    /**
     * Inner class representing OrderedIterator.
     * For more info see: https://docs.oracle.com/javase/7/docs/api/java/util/ListIterator.html
     */
    public class OrderedIterator {
        /* Iterator lives "in-between" elements */
        private Elem nextElem;
        private Elem prevElem;

        /**
         * Constructs a new Iterator for this set, starting from beginning. 
         */
        private OrderedIterator() {
            this.nextElem = head.next;
            this.prevElem = tail.prev;
        }

        /**
         * Returns the data from next element in the set.
         * @return the data from next element in the set
         */
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            E ret = nextElem.info;
            nextElem = nextElem.next;
            prevElem = nextElem.prev;
            return ret;
        }

        /**
         * Returns the data from previous element in the set.
         * @return the data from previous element in the set
         */
        public E prev() {
            if (!hasPrev()) {
                throw new NoSuchElementException();
            }
            E ret = prevElem.info;
            prevElem = prevElem.prev;
            nextElem = prevElem.next;
            return ret;
        }

        /**
         * Returns a boolean representing if another element after this one.
         * @return true if more elements, false otherwise.
         */
        public boolean hasNext() {
            if (nextElem.equals(tail)) {
                return false;
            }
            return true;
        }

        /**
         * Returns a boolean representing if another element before this one.
         * @return true if more elements, false otherwise
         */
        public boolean hasPrev() {
            if (prevElem.equals(head)) {
                return false;
            }
            return true;
        }

                /**
        * Equals method override.
        * @param o the object being compared to
        */
        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object o) {
            if (!(o instanceof OrderedSet<?>.OrderedIterator)) {
                return false;
            }
            OrderedIterator otherIterator = (OrderedIterator) o;
            if (nextElem.equals(otherIterator.nextElem) && prevElem.equals(otherIterator.prevElem)) {
                return true;
            }
            return false;
        }

    }
    
}
