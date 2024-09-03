package Tree;

import org.javatuples.Pair;

public class BTree {
    BTreeNode root;

    public BTree() {
        root = new BLeaf();
    }

    public void insert(int newKey, String item) {
        Pair insertCall = root.insert(newKey, item);

        if (insertCall != null) {
            BTreeNode split = (BTreeNode) insertCall.getValue0();
            newKey = (int) insertCall.getValue1();
            // splitting root; new root now has two children
            BInternal newRoot = new BInternal();
            newRoot.child[0] = root;
            newRoot.child[1] = split;
            newRoot.keys[1] = newKey;
            newRoot.size = 2;
            root = newRoot;
        }
    }

    public void dump() {
        root.dump(0);
    }

}
