package Tree;

import org.javatuples.Pair;

// The BTreeNode class is an Abstract Base Class.  This means that no
// instance of the BTreeNode class can exist.  Only classes which
// inherit from the BTreeNode class may exist.  Furthermore, any class
// which inherits from the BTreeNode class MUST define the methods
// insert() and dump().  The indent() method does not have to
// be defined as it will be inherited in all subclasss of BTreeNode

public abstract class BTreeNode {
    int MAX = 3;
    int[] keys = new int[MAX + 1];
    int size;

    public BTreeNode() {
        size = 0;
    }

    public abstract Pair insert(int newKey, String item);

    public abstract void dump(int depth);

    // helper method for tree display
    public void indent(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print('\t');
        }
    }

}