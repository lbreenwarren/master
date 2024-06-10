package Tree;

import org.javatuples.Pair;

public class BInternal extends BTreeNode {
    BTreeNode[] child = new BTreeNode[MAX + 1];
    int size;

    public BInternal() {
        super();
    }

    public Pair insert(int newKey, String item) {
        // TODO : Implement

        // find the child pointer for further insertion

        // recursively insert into child node

        // if returns a non-zero value, it indicates child node was split
        // include new added-key and child reference to current node

        // check if this node overflows. If so, split and return a pointer
        // to the new node (remember to update the size)

        // Return a pair (tuple) of the sibling and newkey
        return new Pair(null, 0);
    }

    public void dump(int depth) {
        indent(depth);
        System.out.print("[");
        int i;
        for (i = 1; i < size - 1; i++) // keys[0] not displayed
            System.out.print(keys[i] + ",");
        System.out.println(keys[i] + "]");

        // call dump on all children
        for (i = 0; i < size; i++)
            child[i].dump(depth + 1);
    }
}
