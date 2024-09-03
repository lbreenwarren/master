import java.util.ArrayList;

public class Map<K extends Comparable<K>, V> implements Cloneable {
    ArrayList<Entry> map;
    public Map() {
        map = new ArrayList<Entry>();
    }
    
    public class Entry {
        K key;
        V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }
    }

    public Map(Map<K, V> v) {
        map = new ArrayList<Entry>();
        for (int i = 0; i < v.size(); ++i) {
            Entry tmp = v.map.get(i);
            insert(tmp.getKey(), tmp.getValue());
        }
    }

    public boolean insert(K k, V v) {
        if (get(k) != null) {
            return false;
        }
        Entry newEntry = new Entry(k, v);
        map.add(newEntry);
        return true;
    }

    public boolean erase(K k) {
        if (get(k) == null) {
            return false;
        }
        for (int i = 0; i < size(); ++i) {
            if (map.get(i).getKey().equals(k)) {
                map.remove(i);
                return true;
            }
        }
        return false;
    }

    public int size() {
        if (map == null) {
            return 0;
        }
        return map.size();
    }

    public V get(Object k) {
        for (int i = 0; i < size(); ++i) {
            if (map.get(i).getKey().equals(k)) {
                return map.get(i).getValue();
            }
        }
        return null;
    }

    public Object clone() {
        Map<K, V> clone = new Map<K, V>();
        for (int i = 0; i < size(); ++i) {
            Entry tmp = map.get(i);
            clone.insert(tmp.getKey(), tmp.getValue());
        }
        return clone;
    }

    public V replace(K k, V v) {
        for (int i = 0; i < size(); ++i) {
            if (map.get(i).getKey().equals(k)) {
                V ret = map.get(i).getValue();
                map.get(i).setValue(v);
                return ret;
            }
        }
        return null;
    }

}
