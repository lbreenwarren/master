/** A program to implement a binary seach tree using key value pairs.
 * @author Liam Warren
 * @version 12.13.23
 * @param <K> comparable key parameter
 * @param <V> value parameter
 */
public class BSTMap<K extends Comparable, V> implements Map<K, V> {

    BST<Entry<K, V>> tree;

    /**Class to represent entries in a binary search tree.
     * @param <K> key parameter
     * @param <V> value parameter
     */
    public class Entry<K extends Comparable, V> implements Map.Entry, Comparable {
        
        K key;
        V value;

        /**
         * Constructor for Entry class.
         * @param key key param
         * @param value val param
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Getter for key.
         * @return key
         */
        public K getKey() {
            return key;
        }

        /**
         * Getter for value.
         * @return value
         */
        public V getValue() {
            return value;
        }

        /**To string method.
         * @return toString
         */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }

        /**equals method for Entry class, checks if both key and value are equal, else checks if keys are equal.
         * @param o object to compare to 
         * @return true if equal
         */
        public boolean equals(Object o) {
            if (o instanceof Entry) {
                Entry comparable = (Entry<K, V>) o;

                if (comparable.getKey().equals(this.key) && comparable.getValue() == this.value) {
                    return true;
                } else {
                    return false;
                }
            } else if (o.equals(this.key)) {
                return true;
            }
            return false;
        }

        /**
         * compareTo method for Entry class, compares key and value if comparing to an entry, 
         * compares keys if comparing to a key.
         * @param o object to compare to
         * @return 1 if greater than, -1 if less than, 0 if equal
         */
        public int compareTo(Object o) {
            if (o instanceof Entry) {
                Entry comparable = (Entry<K, V>) o;
                return this.key.compareTo(comparable.getKey());
            } else {
                return this.key.compareTo(o);
            }
        }
    
    }

    /**Constructor for BSTMap.*/
    public BSTMap() {
        tree = new BST<Entry<K, V>>();
    }

    /**
     * getter for BST being used in map.
     * @return BST
     */
    public BST<Entry<K, V>> getTree() {
        return tree;
    }

    /**adds node to tree in correct position or changes value of existing key.
     * @param key key of entry
     * @param value value of entry
     * @return value of added node
     */
    public V put(K key, V value) {
        Entry<K, V> newEntry = new Entry<K, V>(key, value);
        if (!tree.add(newEntry)) {
            Entry<K, V> tmp = tree.get(newEntry);
            tmp.value = value;
            return value;
        }

        return value;

    }

    /** adds node to tree in correct position only if key doesnt already exist.
     * @param key key
     * @param value value
     * @return value of added node, null if node not added
     */
    public V putIfAbsent(K key, V value) {
        Entry<K, V> newEntry = new Entry<K, V>(key, value);
        if (!tree.add(newEntry)) {
            return tree.get(key).getValue();
        }

        return null;
    }

    /** toString method for BSTMap.
     * @return toString
     */
    public String toString() {
        return tree.toString();
    }

    /** Clears tree.
     */
    public void clear() {
        tree.clear();
    }

    /** gets value of node with matching key.
     * @param key of node to search for
     * @return value of node with matching key
     */
    public V get(K key) {
        Entry<K, V> tmp = tree.get(key);
        if (tmp != null) {
            return tmp.getValue();
        } else {
            return null;
        }
    } 

    /** checks if tree contains an entry with a given key.
     * @param key key to search for
     * @return true if key in tree
     */
    public boolean containsKey(K key) {
        return tree.contains(key);
    }

    /** checks if tree is empty.
     * @return true if tree is empty
     */
    public boolean isEmpty() {
        return tree.isEmpty();
    }

    /**removes an entry from the tree with a given key.
     * @param key key of entry to remove
     * @return value of removed entry or null if key not in tree
     */
    public V remove(K key) {
        if (containsKey(key)) {
            Entry<K, V> tmp = tree.get(key);
            tree.remove(key);
            return tmp.getValue();
        }

        return null;
    }

    /** counts entries in tree.
     * @return number of entries in tree
     */
    public int size() {
        return tree.size();
    }

}
