import java.util.Iterator;
import java.util.TreeMap;
import java.util.NavigableMap;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.NoSuchElementException;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Map<K extends Comparable<K>, V> implements Cloneable {

    private class Elem {
        K key;
        V value;
        Elem left;
        Elem right;
        boolean rightThread;
    }

    private Elem _root;
    private int _size;

    public Map() {
        _root = new Elem();
        _root.left = _root;
        _root.right = null;
        _root.rightThread = false;
        _size = 0;
    }

    public Map(Map<K, V> v) {
        if (v._root == v._root.left) {
            _root = new Elem();
            _root.left = _root;
            _root.right = null;
            _size = 0;
        } else {
            _root = new Elem();
            _root.left = _root;
            _root.right = null;
            _root.left = copyTree(v._root.left);
            copyThread(_root, v._root);
            _size = v._size;
        }
    }


    private void addToMap(Elem root, TreeMap<K, Elem> keyElemMap) {
        if(root != null) {
            keyElemMap.put(root.key, root);
            addToMap(root.left, keyElemMap);
            if(!root.rightThread) {
                addToMap(root.right, keyElemMap);
            }
        }
    }

    private void copyThread(Elem newRoot, Elem origRoot) {

        TreeMap<K, Elem> newKeyElemMap = new TreeMap<>();
        TreeMap<K, Elem> origKeyElemMap = new TreeMap<>();

        addToMap(newRoot.left, newKeyElemMap);
        addToMap(origRoot.left, origKeyElemMap);

        NavigableMap<K, Elem> revMap = origKeyElemMap.descendingMap();

        Iterator<NavigableMap.Entry<K,Elem>> it = revMap.entrySet().iterator();
        Entry<K, Elem> e = it.next();
        Elem temp = newKeyElemMap.get(e.getKey());
        temp.rightThread = true;
        temp.right = newRoot;

        while(it.hasNext()) {
            e = it.next();
            Elem o = origKeyElemMap.get(e.getKey());
            if( o.rightThread) {
                temp = newKeyElemMap.get(e.getKey());
                temp.rightThread = true;
                temp.right = newKeyElemMap.get(origKeyElemMap.get(e.getKey()).right.key);
            }
        }
    }

    private Elem copyTree(Elem origRoot) {
        if (origRoot == null) {
            return null;
        } else {
            Elem newRoot = new Elem();
            newRoot.key = origRoot.key;
            newRoot.value = origRoot.value;
            newRoot.left = copyTree(origRoot.left);
            if (!origRoot.rightThread)
                newRoot.right = copyTree(origRoot.right);
            return newRoot;
        }
    }

    public boolean insert(K k, V v) {
        //To Be Implemented
        return false;
    }

    public boolean erase(K k) {
        //TO BE IMPLEMENTED
        return false;
    }


	public void clear() {
        // TO BE IMPLEMENTED
    }

    boolean isEmpty() {
        // TO BE IMPLEMENTED
        return false;
    }

    public int size() {
        // TO BE IMPLEMENTED
        return -1;
    }

    @SuppressWarnings("unchecked")
    public V get(Object k) {
        // TO BE IMPLEMENTED
        return null;
    }

    public V replace(K key, V val) {
        // TO BE IMPLEMENTED
        return null;
    }

    boolean containsKey(K k) {
        // TO BE IMPLEMENTED
        return false;
    }

    V getorDefault(K key, V defaultValue) {
        // TO BE IMPLEMENTED
        return null;
    }

    V insertIfAbsent(K key, V value) {
        // TO BE IMPLEMENTED
        return null;
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        //TODO: Implement
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        //TODO: Implement
    }

    @Override
    public Object clone() {
        //TO BE IMPLEMENTED
        return null;
    }

    private void printTree(int level, Elem p, StringBuilder res) {
        int i;
        if (p != null ) {
            if (p.right != null  && !p.rightThread)
                printTree(level+1, p.right, res);
            for(i=0;i<level;i++) {
                res.append("\t");;
            }
            res.append(p.key).append(" ").append(p.value).append("\n");
            printTree(level+1, p.left, res);
        }
    }

    public String toString() {
        if (_root == _root.left) {
            return "";
        }
        StringBuilder res = new StringBuilder();
        printTree(0, _root.left, res);
        return res.toString();
    }

    public Map<K,V>.KeyIterator keyIterator() {
        return new KeyIterator();
    }

    public class KeyIterator implements Iterator {

        private Elem ptr;

        private KeyIterator() {
            // TO BE IMPLEMENTED
        }

        public K next() {
            // TO BE IMPLEMENTED
            return null;
        }

        public boolean hasNext() {
            // TO BE IMPLEMENTED
            return false;
        }
    }
}
