package Tree;

import org.javatuples.Pair;

// BTree leaf node
// BLeaf is a subclass of BTreeNode. This means it inherit all public methods
// of BTreeNode and it can access all protected memebers of BTreeNode
// BLeaf provides the concrete implementation of insert() and dump() methods.
// A BLeaf node
//       - has NO children nodes; does not have reference to child nodes. 
//       - DOES store data
//       - maintains a key array for locating the data;

public class BLeaf extends BTreeNode {
    String[] data = new String[MAX + 1];
    int size;

    public BLeaf() {
        super();
    }

    public Pair insert(int newKey, String item) {
        // find position for insert into current node
        int pos = size - 1;
        while (pos >= 0 && newKey < keys[pos]) {
            keys[pos + 1] = keys[pos];
            data[pos + 1] = data[pos];
            pos--;
        }
        keys[pos + 1] = newKey;
        data[pos + 1] = item;
        size++;

        if (size <= MAX) // if curent leaf node does not overflow
            return null;

        else { // need split
               // create new leaf
            BLeaf sibling = new BLeaf();

            // copy upper half of the current node's elements over
            for (int i = 0; i < (MAX + 1) / 2; i++) {
                sibling.data[i] = data[i + (MAX + 2) / 2];
                sibling.keys[i] = keys[i + (MAX + 2) / 2];
            }

            // update size for both nodes
            sibling.size = (MAX + 1) / 2;
            size -= (MAX + 1) / 2;

            // send back new key by reference
            newKey = sibling.keys[0]; // sibling.keys[0] same as keys[(MAX+2)/2]
            Pair returnValue = new Pair<BLeaf, Integer>(sibling, newKey);
            // and return pointer to new node
            return returnValue;
        }
    }

    public void dump(int depth) {
        indent(depth);
        int i;
        for (i = 0; i < size - 1; i++) {
            if (i != 0) {
                System.out.print(" ");
            }
            System.out.print(keys[i] + ":" + data[i]);
        }
        System.out.println(" " + keys[i] + ":" + data[i]);
    }
}
