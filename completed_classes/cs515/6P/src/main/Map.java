import java.util.Iterator;
import java.util.TreeMap;
import java.util.NavigableMap;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.NoSuchElementException;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import org.apache.commons.lang3.tuple.Pair;

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

    private Elem createLeaf(K key, V value, Elem left, Elem right) {
        Elem cur = new Elem();
        cur.key = key;
        cur.value = value;
        cur.left = left;
        cur.right = right;
        cur.rightThread = true;
        return cur;
    }

    public boolean insert(K k, V v) {
        if (_root.left == _root) {
            _root.left = createLeaf(k, v, null, _root);
            _size++;
            return true;
        }
        if (insert(_root.left, k, v, _root)){
            _size++;
            return true;
        }
        return false;
    }

    private boolean insert(Elem cur, K key, V value, Elem lastLeft) {
        if (key.equals(cur.key)){
            if (cur.value == null) {
                cur.value = value;
                return true;
            }
            return false;
        }
        // insert at left
        if (key.compareTo(cur.key) < 0) {
            if (cur.left == null) {
                cur.left = createLeaf(key, value, null, cur);
                return true;
            }
            // Else continue down the tree
            return insert(cur.left, key, value, cur);
        }
        // insert at right
        if (!cur.rightThread){
            // Continue down the tree
            return insert(cur.right, key, value, lastLeft);
        } else {  // current's right is a thread; add a new node
            if (cur.right == _root) {
                cur.rightThread = false;
                Elem newElem = new Elem();
                newElem.value = value;
                newElem.key = key;
                cur.right = newElem;
                newElem.right = _root;
                newElem.rightThread = true;
                return true;

            }
            cur.rightThread = false;
            cur.right = createLeaf(key, value, null, lastLeft);
            return true;
        }
    }

    public boolean erase(K k) {
       
        if (isEmpty()) {
            return false;
        }
       
        Elem tmp = _root.left;
        Elem parent = null;
        boolean found = false;
        boolean rightChild = false;
        boolean leftChild = false;

        while (tmp != _root && !found) {
            int compare = tmp.key.compareTo(k);
            // if tmp.key > k
            if (compare > 0) {
                //if no left, key does not exist
                if (tmp.left == null) {
                    return false;
                } else {
                    // if tmp.left is the key to replace, store tmp as parent and move to tmp.left
                    if (tmp.left.key.compareTo(k) == 0) {
                        parent = tmp;
                        tmp = tmp.left;
                        found = true;
                        leftChild = true;
                    // else search left
                    } else {
                        tmp = tmp.left;
                    }
                }
            //if tmp.key < k
            } else if (compare < 0) {
                //if no right, key does not exist
                if (tmp.rightThread) {
                   return false;
                } else {
                    // if tmp.right is key to replace, store tmp as parent and move to tmp.right
                    if (tmp.right.key.compareTo(k) == 0) {
                        parent = tmp;
                        tmp = tmp.right;
                        rightChild = true;
                        found = true;
                    //else search right
                    } else {
                        tmp = tmp.right;
                    }
                }
            //remove root
            } else {
                parent = _root;
                leftChild = true;
                found = true;
            }
        }
        
        //remove leaf
        if (tmp.rightThread && tmp.left == null) {
            // if only root, clear tree
            if (parent == _root) {
                _root.left = _root;
                --_size;
                return true;
            }
            //leaf is left child, just remove
            else if (leftChild) {
                parent.left = null;
                --_size;
                return true;

            //leaf is right child, set parents right to tmps right thread
            } else {
                parent.right = tmp.right;
                parent.rightThread = true;
                --_size;
                return true;
            }

        // remove node with only right child, will never be threaded or threaded to
        } else if (tmp.left == null) {
            if (rightChild) {
                parent.right = tmp.right;
            }else {
                parent.left = tmp.right;
            }
            --_size;
            return true;

        //remove node with only left child
        } else if (tmp.rightThread) {
            Elem pred = tmp.left;
            while (pred.right != tmp) {
                pred = pred.right;
            }

            // set thread of predecessor to tmps successor
            pred.right = tmp.right;

            //remove tmp
            if (leftChild) {
                parent.left = tmp.left;
            } else {
                parent.right = tmp.left;
            }
            --_size;
            return true;

        //remove node with both children
        } else {
            Elem pred = tmp.left;
            while (pred.right != tmp) {
                pred = pred.right;
            }

            Elem successor = tmp.right;
            while (successor.left != null) {
                successor = successor.left;
            }

            //move tmps left subtree to far left of tmps right subtree
            Elem farLeft = getFarLeft(tmp.right);
            farLeft.left = tmp.left;

            // thread tmps predecessor to tmps successor
            pred.right = successor;

            //remove tmp
            if (rightChild) {
                parent.right = tmp.right;
            } else {
                parent.left = tmp.right;
            }
            --_size;
            return true;
        }


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

    // private void printTree(int level, Elem p, StringBuilder res) {
    //     int i;
    //     if (p != null ) {
    //         if (p.right != null  && !p.rightThread)
    //             printTree(level+1, p.right, res);
    //         for(i=0;i<level;i++) {
    //             res.append("\t\t");;
    //         }
    //         res.append(p.key).append(" ").append(p.value);
    //         if (p.rightThread) {
    //             res.append(" (" + p.right.key + ")");
    //         }
    //         res.append("\n");
    //         printTree(level+1, p.left, res);
    //     }
    // }

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
            if (_root.left == null) {
                throw new NoSuchElementException();
            }
            ptr = getFarLeft(_root.left);
        }

        public K next() {
      
            if (hasNext()) {
                K ret = ptr.key;
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
