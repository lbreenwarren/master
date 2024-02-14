/** program to represent and manipulate a Binary Search Tree.
 * @author Liam Warren
 * @version 12.13.23
 * @param <T> abstract comparable type 
 */
public class BST <T extends Comparable> implements Tree<T> {
    
    Node root;

    /** Constructor for BST. */
    public BST() {
        root = null;
    }

    /** Getter for root.
     * @return root
     */
    public Node getRoot() {
        return root;
    }

    /**adds a node to the tree in the correct location.
     * @param value value of node to add to tree
     * @return true if node was added
     */
    public boolean add(T value) {
        Node tmp = new Node(value);
        return addNode(root, tmp);
        
    }

    /**
     * recursively traverse tree to find correct spot for node to be added.
     * @param curr current node to compare node being added to
     * @param newNode node to be added to tree
     * @return true if node was added
     */
    public boolean addNode(Node curr, Node newNode) {
        if (root == null) {
            
            root = newNode;
            return true;

        } else if (newNode.getValue().compareTo(curr.getValue()) > 0) {

            if (curr.right == null) {
                curr.right = newNode;
                newNode.parent = curr;
                return true;
            } else {
                return addNode(curr.right, newNode);
            }

        } else if (newNode.getValue().compareTo(curr.getValue()) < 0) {
            
            if (curr.left == null) {
                curr.left = newNode;
                newNode.parent = curr;
                return true;
            } else {
                return addNode(curr.left, newNode);
            }

        } else {
            return false;
        }
    }

    /**toString method for BST.
     * @return toString
     */
    public String toString() {
        String ret = "";
        return printTree(root, "", "");
    }

    /**
     * Helper method for to string, recursively traverses and fomats each node of the tree.
     * @param curr current node being formatted
     * @param indent number of indents preceding node
     * @param branch leter preceding node indicating whether its a left or right child
     * @return formatted portion of tree
     */
    public String printTree(Node curr, String indent, String branch) {
        String ret = "";
        if (curr != null) {
            if (curr.left != null) {
                ret += printTree(curr.left, indent + "      ", "L ") + "\n";
            }
            String prefix = indent + branch;
            ret += prefix + curr.getValue();
            if (curr.right != null) {
                ret += "\n" + printTree(curr.right, indent + "      ", "R ");
            }
        }
        return ret;
    }

    /**clears tree. */
    public void clear() {
        root.left = null;
        root.right = null;
        root = null;
    }

    /**gets value of a node matching witha matching value.
     * @param o object to search for
     * @return value of node with matching value
    */
    public T get(Object o) {
        Node tmp = root;
        o = (T) o;
        while (tmp != null) {
            if (tmp.getValue().compareTo(o) > 0) {
                tmp = tmp.left;
            } else if (tmp.getValue().compareTo(o) < 0) {
                tmp = tmp.right;
            } else {
                return tmp.getValue();
            }
        }

        return null;
    }

    /**
     * gets node with matching value.
     * @param o value to match node to
     * @return node with matching value
     */
    public Node getNode(Object o) {
        Node tmp = root;
        o = (T) o;
        while (tmp != null) {
            if (tmp.getValue().compareTo(o) > 0) {
                tmp = tmp.left;
            } else if (tmp.getValue().compareTo(o) < 0) {
                tmp = tmp.right;
            } else {
                return tmp;
            }
        }

        return null;
    }

    /**checks if a node with matching value is in tree.
     * @param o value to search for
     * @return true if tree contains node with matching value
     */
    public boolean contains(Object o) {
        Node tmp = root;
        o = (T) o;
        while (tmp != null) {
            if (tmp.getValue().compareTo(o) > 0) {
                tmp = tmp.left;
            } else if (tmp.getValue().compareTo(o) < 0) {
                tmp = tmp.right;
            } else {
                return true;
            }
        }

        return false;
    }
    
    /**
     * checks if tree is emtpy.
     * @return true if tree is empty
     */
    public boolean isEmpty() {
        if (root == null) {
            return true;
        }

        return false;
    }

    /**
     * Helper method to add a subtree to the far right of a given nodes subtree.
     * @param root node to add move to
     * @param move subtree to add to root
     */
    public void addToFarRight(Node root, Node move) {
        Node tmp = root;
        while (tmp.right != null) {
            tmp = tmp.right;
        }
        tmp.right = move;
        move.parent = tmp;
    }

    
    /**
     * Helper method to add a subtree to the far left of a given nodes subtree.
     * @param root node to add move to
     * @param move subtree to add to root
     */
    public void addToFarLeft(Node root, Node move) {
        Node tmp = root;
        while (tmp.left != null) {
            tmp = tmp.left;
        }
        tmp.left = move;
        move.parent = tmp;
    }

    /** 
     * removes a node witha matching value from the tree.
     * @param o value of node to remove
     * @return true if node was removed
     */
    public boolean remove(Object o) {
        if (!contains(o)) {
            return false;
        }

        Node tmp = getNode(o);
        if (tmp == root) {
            if (tmp.right == null && tmp.left == null) {
                clear();
                return true;
            }
            if (tmp.right != null && tmp.left != null) {
                addToFarRight(tmp.left, tmp.right);
                tmp.right = null;
                root = tmp.left;
                tmp.left.parent = null;
                return true;
            }

            if (tmp.right == null) {
                root = tmp.left;
                root.parent = null;
            }

            if (tmp.left == null) {
                root = tmp.right;
                root.parent = null;
            }

            return true;

        }

        if (tmp.parent.left == tmp && tmp.left != null && tmp.right != null) {
        
            addToFarRight(tmp.left, tmp.right);
            tmp.right = null;
            tmp.parent.left = tmp.left;
            tmp.left.parent = tmp.parent;
            tmp.left = null;
            
        }

        if (tmp.parent.right == tmp && tmp.right != null && tmp.left != null) {
            addToFarLeft(tmp.right, tmp.left);
            tmp.left = null;
            tmp.parent.right = tmp.right;
            tmp.right.parent = tmp.parent;
            tmp.right = null;
        }

        if (tmp.parent.left == tmp) {
            if (tmp.left != null) {
                tmp.parent.left = tmp.left;
                tmp.left.parent = tmp.parent;
            }
            if (tmp.right != null) {
                tmp.parent.left = tmp.right;
                tmp.right.parent = tmp.parent;
            }
        }

        if (tmp.parent.right == tmp) {
            if (tmp.left != null) {
                tmp.parent.right = tmp.left;
                tmp.left.parent = tmp.parent;
            }
            if (tmp.right != null) {
                tmp.parent.right = tmp.right;
                tmp.right.parent = tmp.parent;
            }
        }
        
        if (tmp.left == null && tmp.right == null) {
            if (tmp.parent.left == tmp) {
                tmp.parent.left = null;
            }
            if (tmp.parent.right == tmp) {
                tmp.parent.right = null;
            }
        }
        
        return true;

    }

    /**
     * Size helper function, recursively traverses tree and increments for each node.
     * @param tmp current node in traversal
     * @return size of tree
     */
    public int sizeHelper(Node tmp) {
            
        if (tmp == null) {
            return 0;
        } else {
            int leftSize = sizeHelper(tmp.left);
            int rightSize = sizeHelper(tmp.right);
            return leftSize + rightSize + 1;
        }

     
    }

    /** 
     * gets size of tree.
     * @return size of tree
     */
    public int size() {
        return sizeHelper(root);
    }

    /** 
     * inner class to represent nodes of the tree.
     */
    public class Node implements Tree.Node<T> {
        Node left;
        Node right;
        Node parent;
        T value;

        /**
         * Constructor for Node.
         * @param value value of node
         */
        public Node(T value) {
            left = null;
            right = null;
            parent = null;
            this.value = value;
        }

        /**setter for node value.
         * @param value value for node to be set to
         */
        public void setValue(T value) {
            this.value = value;
        }

        /** 
         * getter for value of node.
         * @return nodes value
         */
        public T getValue() {
            return value;
        }

        /**
         * getter for a nodes left child.
         * @return nodes left child
         */
        public Node getLeft() {
            return left;
        }

        /**
         * getter for nodes right child.
         * @return nodes right child
         */
        public Node getRight() {
            return right;
        }   

        /**
         * getter for nodes parent.
         * @return nodes parent
         */
        public Node getParent() {
            return parent;
        }
    }
}
