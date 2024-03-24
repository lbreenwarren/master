package set;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.function.Consumer;

import javax.swing.tree.TreeNode;

public class Set<T extends Comparable<T>> {
    private Elem _root;
    private int _size; 

    public Set() {
        _size = 0;
        // create a dummy root node
        _root = new Elem();
        _root.left = _root;  // empty tree
        _root.right = null;
        _root.rightThread = false;
    }

    public boolean insert(T v) {
        if (_root.left == _root) {
            _root.left = createLeaf(v, null, _root);
            _size++;
            return true;
        }
        if (insert(_root.left, v, _root)){
            _size++;
            return true;
        }
        return false;
    }

    private boolean insert(Elem cur, T value, Elem lastLeft) {
        if (value.equals(cur.info)){
            return false;
        }
        // insert at left
        if (value.compareTo(cur.info) < 0) {
            if (cur.left == null) {
                cur.left = createLeaf(value, null, cur);
                return true;
            }
            // Else continue down the tree
            return insert(cur.left, value, cur);
        }
        // insert at right
        if (!cur.rightThread){
            // Continue down the tree
            return insert(cur.right, value, lastLeft);
        } else {  // current's right is a thread; add a new node
            cur.rightThread = false;
            cur.right = createLeaf(value, null, lastLeft);
            return true;
        }
    }

    private Elem createLeaf(T value, Elem left, Elem right) {
        Elem cur = new Elem();
        cur.info = value;
        cur.left = left;
        cur.right = right;
        cur.rightThread = true;
        return cur;
    }

    public String breadthFirst() {
        if (_root.left == _root)
            return "";
        String ret = "";
        Queue<Elem> queue = new LinkedList<>();
        queue.offer(_root.left);

        while (!queue.isEmpty()) {
            int levelSize = queue.size();

            for (int i = 0; i < levelSize; i++) {
                Elem current = queue.poll();
                ret += current.info + " ";

                if (current.left != null)
                    queue.offer(current.left);

                if (current.right != null && !current.rightThread)
                    queue.offer(current.right);
            }
        }
        return ret;
    }

    private boolean isDummy(Elem tmp) {
        if (tmp.right == null) {
            return true;
        }
        return false;
    }

    public String depthFirstInOrder() {
        Elem tmp = _root.left;
        String ret = "";

        while (!isDummy(tmp)) {
            while (tmp.left != null) {
                tmp = tmp.left;
            } 
            ret += tmp.info + " ";
            while (tmp.rightThread && !isDummy(tmp.right)) {
                tmp = tmp.right;
                ret += tmp.info + " ";
            }
            tmp = tmp.right;
        
        }
        return ret;
    }

    public void forEach(Consumer<? super T> action) {
        Elem tmp = _root.left;

        while (!isDummy(tmp)) {
            while (tmp.left != null) {
                tmp = tmp.left;
            } 
            action.accept(tmp.info);
            while (tmp.rightThread && !isDummy(tmp.right)) {
                tmp = tmp.right;
                action.accept(tmp.info);
            }
            tmp = tmp.right;
        
        }
    }

    public int size() {
        return this._size;
    }

    public int height() {
        if (_root.left == _root) {
            return 0;
        }
        return height(_root);
    }

    private boolean isLeaf(Elem tmp) {
        if ((tmp.right == null || tmp.rightThread) && tmp.left == null) {
            return true;
        }
        return false;
    }

    // Post order traversal height of the tree
    public int height(Elem p) {
        if (isLeaf(p)) {
            return 0;
        }

        int rightHeight = 0;
        int leftHeight = 0;

        if(p.right != null && !p.rightThread) {
            rightHeight = height(p.right);
        }
        if (p.left != null) {
            leftHeight = height(p.left);
        }
        if (rightHeight > leftHeight) {
            return 1 + rightHeight;
        }

        return 1 + leftHeight;
    }


    // output the structure of tree. The tree is output as "lying down"
    // in which _root is the LEFT most Elem.
    public void printTree(int level, Elem p){
        int i;
        if (p != null) {
            if (p.right != null && !p.rightThread) {
                printTree(level+1, p.right);
            }
            for(i=0;i<level;i++) {
                System.out.print("\t");
            }
            System.out.println(p.info);
            printTree(level+1, p.left);
        }
    }

    // outputs information in tree in in order traversal order
    public void dump() {
        if (this._size != 0) { // tree non-empty
            printTree(0, _root.left);   // print tree structure
        }
    }

    private class Elem {
        T info;
        Elem left;
        Elem right;
        boolean rightThread;
        public Elem() {
            left = null;
            right = null;
            rightThread = false;
        }
    }
}
