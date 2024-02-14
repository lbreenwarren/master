/**
* Implementation of a linked map using a linked list to store key-value pairs.
* @author Liam Warren 
* @version 11.16.23
* @param <K> The type of keys.
* @param <V> The type of values.
*/
public class LinkedMap<K, V> implements Map<K, V> {

    LinkedList<Entry<K, V>> list;
    
    /** Inner class to store key and value pair. 
     * @param <K> key
     * @param <V> value
    */
    public class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;
        
        /** Constructor for Entry class.
         * @param key key
         * @param value value
         */
        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
        
        /** Getter for key.
         * @return key
         */
        public K getKey() {
            return key;
        }
        
        /** Getter for value.
         * @return Value
         */
        public V getValue() {
            return value;
        }

        /**To string method.
         * @return toString
        */
        public String toString() {
            String ret = "(" + key + ", " + value + ")";
            return ret;
        }

        /**method for comparing Entry objects, if object is an entry checks 
         * both key and value, if its a key checks if keys are equal.
         * @param o Entry or Key object
         * @return true if Entry or key matches
         */
        public boolean equals(Object o) {
            if (o instanceof Entry) {
                o = (Entry) o;

                if (((Entry) o).getKey() == key && ((Entry) o).getValue() == value) {
                    return true;
                } else {
                    return false;
                }
    
            } else {
                if (((K) o).equals(key)) {
                    return true;
                } else {
                    return false;
                }
            }
        }

    }

    /**Constructor for Linked Map.*/
    public LinkedMap() {
        list = new LinkedList<Entry<K, V>>();
    }

    /**
     * Getter for Linked List instance.
     * @return list
     */
    public LinkedList<Entry<K, V>> getList() {
        return list;
    }

    /**Adds entry to map.
     * @param key key
     * @param value value
     * @return value of entry if key is already present or value of entry added to map
     */
    public V put(K key, V value) {
        
        if (list.contains(key)) {
            Entry<K, V> tmp = list.get(key);
            tmp.value = value;
            return value;
        } else {
            Entry<K, V> newEntry = new Entry<K, V>(key, value);
            list.add(newEntry);
            return newEntry.getValue();
        }
    }

    /**
     * adds entry to map if key is not alread present.
     * @param key key
     * @param value value
     * @return value of entry if key is already present
     */
    public V putIfAbsent(K key, V value) {

        if (list.contains(key)) {
            Entry<K, V> tmp = list.get(key);
            return tmp.value;
        } else {
            Entry<K, V> newEntry = new Entry<K, V>(key, value);
            list.add(newEntry);
            return null;
        }

    }

    /**
     * To String method.
     * @return toString
     */
    public String toString() {
        return list.toString();
    }
    
    /**Clears map of all entries.*/
    public void clear() {
        list.clear();
    }
    
    /**gets value of provided key.
     * @param key key of entry
     * @return value  value of entry
     */
    public V get(K key) {
        if (list.contains(key)) {
            return list.get(key).getValue();
        } else {
            return null;
        }
    }

    /** revoves entry with matching key from map.
     * @param key key of entry to remove
     * @return value of removed entry
     */
    public V remove(K key) {
        if (list.contains(key)) {
            V value = list.get(key).getValue();
            list.remove(key);
            return value;
        } else {
            return null;
        }
    }
    
    /**checks if map contains an entry with given key.
     * @param key key to check for
     * @return true if map contains key
     */
    public boolean containsKey(K key) {
        return list.contains(key);
    }

    /**checks if map is empty.
     * @return true if map is empty
     */
    public boolean isEmpty() {
        return list.isEmpty();
    }

    /**checks map size.
     * @return size of map
     */
    public int size() {
        return list.size();
    }

}