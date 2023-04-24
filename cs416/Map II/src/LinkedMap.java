import java.util.Scanner;
/**Map program implemented using a linked list.
 * @author Liam Warren
 * @version 4/17/23
 * @param <K> generic Key parameter
 * @param <V> generic value parameter
 */

public class LinkedMap<K, V> implements Map<K, V> {
    LinkedList<Entry<K, V>> list;

    /** Entry class to define entries in the map.
     * @param <K> generic key parameter
     * @param <V> generic value parameter
     */
    public class Entry<K, V> implements Map.Entry<K, V> {
        K key;
        V value;

        /** constructor for class Entry.
         * @param key key of Entry instance
         * @param value value of Entry instance
         */
        public Entry(K key, V value) {
            this.value = value;
            this.key = key;
        }

        /**getter for key.
         * @return key
         */
        public K getKey() {
            return key;
        }

        /** getter for value.
         * @return value
         */
        public V getValue() {
            return value;
        }

        /**To string method for Entry class.
         * @return toString
         */
        public String toString() {
            return "(" + key + ", " + value + ")";
        }

        /**equals method for Entry class, checks if o is an instance of Entry and
         * if its equal to this instance of entry.
         * Otherwise checks if o is equal to the key of this instance of Entry.
         * @param o object which to compare
         * @return true if equal
         */
        public boolean equals(Object o) {
            if (o instanceof Map.Entry<?, ?>) {
                Entry<?, ?> check = (Entry<?, ?>) o;
                if ((check.getKey().equals(this.key)) && (check.getValue().equals(this.value))) {
                    return true;
                }

            } else {
                return this.key.equals(o);
            }


            return false;
        }

    }

    /**constructor for LinkedMap.
    */
    public LinkedMap() {
        list = new LinkedList<Entry<K, V>>();
    }

    /**getter for LinkedMap's associated linked list.
     * @return List
     */
    public LinkedList<Entry<K, V>> getList() {
        return list;
    }

    /**add entry to map
     * @param key key of entry to be added
     * @param value value of entry to be added
     * @return if key already exists, set value of entry with that key to value, otherwise make new entry
     * return value in both cas
     */

    public V put( K key, V value ) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).key.equals(key)) {
                list.get(i).value = value;
                return value;
            }
        }
        Entry<K,V> newEntry = new Entry<K,V>(key, value);
        list.add(newEntry);
        return value;
    }

    public V putIfAbsent( K key, V value ) {

        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).key.equals(key)) {
                return list.get(i).value;
            }
        }

        Entry<K,V> newEntry = new Entry<K,V>(key, value);
        list.add(newEntry);
        return value;
    }

    public String toString() {
        String toString = "[";
        for (int i = 0; i < list.size(); ++i) {
            toString += "(" + list.get(i).key + ", " + list.get(i).value + ")";
            if (i != list.size() - 1) {
                toString += ", ";
            }
        }
        return toString + "]";
    }

    public void clear() {
        list.clear();
    }

    public V get(K key) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).key.equals(key)) {
                return list.get(i).value;
            }
        }
        return null;
    }

    public boolean containsKey (K key) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {return list.isEmpty();}

    public V remove(K key) {
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i).key.equals(key)) {
                V out = list.get(i).value;
                list.remove(list.get(i));
                return out;
            }
        }
        return null;
    }

    public int size() {return list.size();}

    public static void main(String[] args) {
        LinkedMap<String, Integer> map = new LinkedMap<String, Integer>();
        Scanner in = new Scanner(System.in);
        String input = "";
        input = in.next();
        while (!input.equals("x")) {
            if (input.equals("p")) {
                String key = in.next();
                int value = in.nextInt();
                map.put(key, value);
                System.out.println(map.toString());
            }
            if (input.equals("P")) {
                String key = in.next();
                int value = in.nextInt();
                map.putIfAbsent(key, value);
                System.out.println(map.toString());
            }
            if (input.equals("c")) {
                String key = in.next();
                System.out.println(map.containsKey(key));
            }
            if (input.equals("C")) {
                map.clear();
                System.out.println(map.toString());
            }
            if (input.equals("g")) {
                String key = in.next();
                System.out.println(map.get(key));
            }
            if (input.equals("s")) {
                System.out.println(map.size());
            }
            if (input.equals("e")) {
                System.out.println(map.isEmpty());
            }
            if (input.equals("r")) {
                String key = in.next();
                map.remove(key);
                System.out.println(map.toString());
            }
            System.out.println("------------------");
            input = in.next();
        }
    }
}
