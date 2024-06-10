import java.util.Iterator;
import java.util.TreeMap;
import java.util.NavigableMap;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.NoSuchElementException;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class Map<K extends Comparable<K>, V> implements Cloneable {

    private class Elem implements Entry<K,V> {
        K key;
        V value;
        Elem left;
        Elem right;
        int height;
        boolean rightThread;

        public Elem(K key, V value, Elem left, Elem right, int height, boolean rightThread) {
            this.key = key;
            this.value = value;
            this.left = left;
            this.right = right;
            this.height = height;
            this.rightThread = rightThread;
        }
        
        public Elem(){}

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V v) {
            V old = value;
            value = v;
            return old;
        }

        public String toString() {
            return String.format("%s %s(%d)", key, value, height);
        }
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

    private Elem createLeaf(K key, V value, Elem left, Elem right) {
        Elem cur = new Elem();
        cur.key = key;
        cur.value = value;
        cur.left = left;
        cur.right = right;
        cur.rightThread = true;
        return cur;
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


    // public boolean insert(K k, V v) {
    //     //insert in tree
    //     if (_root.left == _root) {
    //         _root.left = createLeaf(k, v, null, _root);
    //         _size++;
    //         return true;
    //     }
    //     if (insert(_root.left, k, v, _root)){
    //         _size++;
    //         return true;
    //     }
    //     return false;
    // }

    // private boolean insert(Elem cur, K key, V value, Elem lastLeft) {
    //     if (key.equals(cur.key)){
    //         if (cur.value == null) {
    //             cur.value = value;
    //             return true;
    //         }
    //         return false;
    //     }
    //     // insert at left
    //     if (key.compareTo(cur.key) < 0) {
    //         if (cur.left == null) {
    //             cur.left = createLeaf(key, value, null, cur);
    //             return true;
    //         }
    //         // Else continue down the tree
    //         return insert(cur.left, key, value, cur);
    //     }
    //     // insert at right
    //     if (!cur.rightThread){
    //         // Continue down the tree
    //         return insert(cur.right, key, value, lastLeft);
    //     } else {  // current's right is a thread; add a new node
    //         if (cur.right == _root) {
    //             cur.rightThread = false;
    //             Elem newElem = new Elem();
    //             newElem.value = value;
    //             newElem.key = key;
    //             cur.right = newElem;
    //             newElem.right = _root;
    //             newElem.rightThread = true;
    //             return true;

    //         }
    //         cur.rightThread = false;
    //         cur.right = createLeaf(key, value, null, lastLeft);
    //         return true;
    //     }
    // }

    private int height(Elem node) {
        return node == null ? -1 : node.height;
    }

    // insert an element
    public boolean insert(K key, V value) {
        if (get(key) != null) {
            return false;
        }
        if (_root.left == _root) {
            _root.left = new Elem(key, value, null, _root, 0, true);
            _size++;
            return true;
        }
        _root.left = insert(key, value, _root.left);
        return true;
    }

    // helper method for recusively insertion
    public Elem insert(K key, V value, Elem node) {
        if (node == null) {
            node = new Elem(key, value, null, null, 0, false);
            _size++;
        }
        // move to left
        if (key.compareTo(node.key) < 0) {

            boolean needsThread = false;
            if (node.left == null) {
                needsThread = true;
            }

            node.left = insert(key, value, node.left);

            if (needsThread) {
                node.left.right = node;
                node.left.rightThread = true;
            }

            int rightHeight = -1;
            if (!node.rightThread) {
                rightHeight = height(node.right);
            }
            if (height(node.left) - rightHeight == 2) {
                if (key.compareTo(node.left.key) < 0) {
                    node = rotateRight(node);
                }
                else {
                    node = doubleRotateRight(node);
                }
            }    
        }
        // move to right
        else if (key.compareTo(node.key) > 0) {
            Elem nextNode = node.right;
            Elem threadedTo = null;

            if (node.rightThread) {
                threadedTo = node.right;
                nextNode = null;
            }

            node.right = insert(key, value, nextNode);

            if (node.rightThread) {
                node.rightThread = false;
                node.right.right = threadedTo;
                node.right.rightThread = true;
            }
            int rightHeight = -1;
            if (!node.rightThread) {
                rightHeight = height(node.right);
            }
            if (rightHeight - height(node.left) == 2) {
                if (key.compareTo(node.right.key) > 0) {
                    node = rotateLeft(node);
                }
                else {
                    node = doubleRotateLeft(node);
                }
            }
        }
        int height = -1;
        if (!node.rightThread) {
            height = height(node.right);
        }
        node.height = Math.max(height(node.left), height) + 1; // update height
        return node;
    }

    public Elem rotateLeft(Elem node) {
        // Node cant be right threaded because if it was we would have just inserted at its right
        Elem ret = node.right;
        node.right = ret.left;
        ret.left = node;
        if (node.right == null) {
            node.right = ret;
            node.rightThread = true;
        }
        ++ret.height;
        --node.height;
        return ret;
        

    }

    public Elem rotateRight(Elem node) {
        Elem ret = node.left;
        if (!ret.rightThread) {
            node.left = ret.right;
        } else {
            node.left = null;
        }
        ret.right = node;
        ret.rightThread = false;
        ++ret.height;
        --node.height;
        return ret;
    }

    public Elem doubleRotateRight(Elem node) {
        node.left = rotateLeft(node.left);
        Elem ret = rotateRight(node);
        return ret;
    }

    public Elem doubleRotateLeft(Elem node) {
        node.right = rotateRight(node.right);
        Elem ret = rotateLeft(node);
        return ret;
    }

	public void clear() {
        _root.left = _root;
    }

    boolean isEmpty() {
        if (_root.left == _root) {
            return true;
        }
        return false;
    }

    public int size() {
        return _size;
    }

    @SuppressWarnings("unchecked")
    public V get(Object k) {
        Elem tmp = _root.left;
        K key = (K)k;
        while (tmp != _root) {
            int compare = tmp.key.compareTo(key);
            if (compare == 0) {
                return tmp.value;
            }
            if (compare > 0) {
                if (tmp.left == null) {
                    return null;
                } else {
                    tmp = tmp.left;
                }
            } else if (compare < 0) {
                if (tmp.rightThread) {
                   return null;
                } else {
                    tmp = tmp.right;
                }
            }
        }
        return null;
    }

    public V replace(K key, V val) {
        Elem tmp = _root.left;
        V ret = null;
        while (tmp != _root) {
            int compare = tmp.key.compareTo(key);
            if (compare == 0) {
                ret = tmp.value;
                tmp.value = val;
                return ret;
            }
            if (compare > 0) {
                if (tmp.left == null) {
                    return null;
                } else {
                    tmp = tmp.left;
                }
            } else if (compare < 0) {
                if (tmp.rightThread) {
                    return null;
                } else {
                    tmp = tmp.right;
                }
            }
        }
        return null;
    }

    boolean containsKey(K k) {
        if (get(k) == null) {
            return false;
        }
        return true;
    }

    V getorDefault(K key, V defaultValue) {
        V ret = get(key);
        if (ret == null) {
            insert(key, defaultValue);
            return defaultValue;
        } else {return ret;}
    }

    V insertIfAbsent(K key, V value) {
        V ret = get(key);
        if (ret == null) {
            insert(key, value);
            return null;
        } else {
            return ret;
        }
    }

    private void forEachHelper(BiConsumer<? super K, ? super V> action, Elem nextElem) {
        if (isEmpty()) {
            return;
        }
        if (nextElem.left == null && nextElem.rightThread) {
            action.accept(nextElem.key, nextElem.value);
        } else if (nextElem.left != null) {
            forEachHelper(action, nextElem.left);
            action.accept(nextElem.key, nextElem.value);
        } else if (!nextElem.rightThread){
            forEachHelper(action, nextElem.right);
            action.accept(nextElem.key, nextElem.value);
        }
    }

    private void replaceAllHelper(BiFunction<? super K, ? super V, ? extends V> function, Elem nextElem) {
        if (isEmpty()) {
            return;
        }
        if (nextElem.left == null && nextElem.rightThread) {
            nextElem.value = function.apply(nextElem.key, nextElem.value);
        } 
        if (nextElem.left != null) {
            replaceAllHelper(function, nextElem.left);
            nextElem.value = function.apply(nextElem.key, nextElem.value);
        } 
        if (!nextElem.rightThread){
            replaceAllHelper(function, nextElem.right);
            if (nextElem.left == null) {
                nextElem.value = function.apply(nextElem.key, nextElem.value);
            }
        }
    }
    public void forEach(BiConsumer<? super K, ? super V> action) {
        Elem tmp = _root.left;
        forEachHelper(action, tmp);
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        Elem tmp = _root.left;
        replaceAllHelper(function, tmp);
    }


    @Override
    public Object clone() {
        return new Map<K, V>(this);
    }

    private void printTree(int level, Elem p, StringBuilder res) {
        int i;
        if (p != null ) {
            if (p.right != null  && !p.rightThread)
                printTree(level+1, p.right, res);
            for(i=0;i<level;i++) {
                res.append("\t");;
            }
            res.append(p.toString()).append("\n");

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

    public Iterator<Entry<K,V>> iterator() {
        return new EntryIterator();
    }

    private Elem getFarLeft(Elem subRoot) {
        Elem tmp = subRoot;
        if (_root.left == _root) {
            return _root;
        }
        while (tmp.left != null) {
            tmp = tmp.left;
        }
        return tmp;
    }

    public class EntryIterator implements Iterator<Entry<K,V>> {

        private Elem ptr;

        private EntryIterator() {
            if (_root.left == null) {
                throw new NoSuchElementException();
            }
            ptr = getFarLeft(_root.left);
        }

        public Entry<K,V> next() {
            if (hasNext()) {
                Entry<K,V> ret = ptr;
                if (ptr.rightThread) {
                    ptr = ptr.right;
                } else {
                    ptr = ptr.right;
                    while (ptr.left != null) {
                        ptr = ptr.left;
                    }
                }
                return ret;
            } else {
                throw new NoSuchElementException();
            }
        }

        public boolean hasNext() {
            if (ptr.right != null) {
                return true;
            }
            return false;
        }
    }
}
