import java.util.*;
import java.io.*;

public class BinarySearchTree 
{
    //-------------------- instance variables ---------------------
    private Node   _root;
    private int    _size;
    
    //-------------------- constructors --------------------------
    /**
     * Construct an empty tree with no nodes.
     */
    public BinarySearchTree()
    {
        _root = null;
    }
    /**
     * Construct a tree with a root .
     * @param rootData Data
     */
    public BinarySearchTree( Data rootData )
    {
        this();
        _root = new Node( rootData );
    }
    //+++++++++++++++++++++++ inner class Node ++++++++++++++++++++++
    /**
     * The Node class does not have to be seen outside this class, so
     * it is private.
     */
    static public class Node
    {
        //-------------- instance variables ---------------------------
        Data data;
        Node left;
        Node right;
        Node parent;
        
        //--------------- constructor --------------------------------
        /**
         * Constructor.
         * @param d Data
         */
        public Node( Data d )
        {
            data = d;
            left = null;
            right = null;
            parent = null;
        }
    }

    /////////////////////////////////////////////////////////////////
    // DO NOT CHANGE ANY CODE BEFORE THIS POINT
    /////////////////////////////////////////////////////////////////
    
    //---------------------- addToFarRight( node, node ) ----------------
    /**
     * Add subtree Node as the right most descendant of the 1st argument.
     * @param n Node        root of tree to which subtree must be added
     * @param subtree Node  subtree to be added to far right of subtree
     */
    public void addToFarRight( Node n, Node subtree )
    {
        Node tmp = n;
        while (tmp.right != null){
            tmp = tmp.right;
        }
        tmp.right = subtree;
        subtree.parent = tmp;
    
    
    }
    //----------------------- addToFarLeft( Node, Node ) --------------
    /**
     * Add subtree Node as the left most descendant of the 1st argument.
     * @param n Node        root of tree to which subtree must be added
     * @param subtree Node  subtree to be added to far left of subtree
     */
    public void addToFarLeft( Node n, Node subtree )
    {

        Node tmp = n;
        while (tmp.left != null){
            tmp = tmp.left;
        }
        tmp.left = subtree;
        subtree.parent = tmp;


    }

    //-------------------- removeRight( Node, Node ) -------------------
    /**
     * Remove a node that is the right child of its parent.
     * @param parent Node
     * @param n      Node
     */
    private void removeRight( Node parent, Node n )
    {
        if (n.right != null && n.left != null) {
            addToFarLeft(n.right, n.left);
            parent.right = n.right;
            n.right.parent = parent;
            n.parent = null;
        }
        if (n.left == null && n.right != null) {
            parent.right = n.right;
            n.right.parent = parent;
            n.parent = null;
        }
        if (n.right == null && n.left != null) {
            parent.right = n.left;
            n.left.parent = parent;
            n.parent = null;
        }
        if (n.right == null && n.left == null) {
            parent.right = null;
            n.parent = null;
        }
        
        
    }
    //-------------------- removeLeft( Node, Node ) --------------------
    /**
     * Remove a node that is the left child of its parent.
     * @param parent Node
     * @param n      Node
     */
    private void removeLeft( Node parent, Node n )
    {
        if (n.right != null && n.left != null) {
            addToFarRight(n.left, n.right);
            parent.left = n.left;
            n.left.parent = parent;
            n.parent = null;
        }
        if (n.left == null && n.right != null) {
            parent.left = n.right;
            n.right.parent = parent;
            n.parent = null;
        }
        if (n.right == null && n.left != null) {
            parent.left = n.left;
            n.left.parent = n.parent;
            n.parent = null;
        }
        if (n.right == null && n.left == null) {
            parent.left = null;
            n.parent = null;
        }
    
    
    }
    //-------------------- removeRoot( ) ------------------------------
    /**
     * Remove the root node.
     */
    private void removeRoot()
    {
        if (_root.left != null && _root.right != null) {
            addToFarRight(_root.left, _root.right);
            _root = _root.left;
            _root.parent = null;
        }
        else if (_root.left == null && _root.right != null) {
            _root = _root.right;
            _root.parent = null;
        }
        else if (_root.left != null && _root.right == null) {
            _root = _root.left;
            _root.parent = null;
        }
        else if (_root.left == null && _root.right == null) {
            _root = null;
        }
    
    } 
    //-------------------- removeNode( Node ) ------------------------
    /**
     * Remove a specific node from the tree. Decrease the size.
     * @param n Node        node to be deleted
     */
    void removeNode( Node n ) {
        if (n.equals(_root)) {
            removeRoot();
        }
        else if (n.equals(n.parent.left)) {
            removeLeft(n.parent, n);
        }
        else if (n.equals(n.parent.right)) {
            removeRight(n.parent, n);
        }
        --_size;
    }   
    
    /////////////////////////////////////////////////////////////////
    // DO NOT CHANGE ANY CODE AFTER TO THIS POINT
    /////////////////////////////////////////////////////////////////

    //-------------------- delete( Data ) ------------------------------
    /**
     * Find the node in the tree whose data field contains a key that 
     *  matches the key contained in the Data parameter.
     * @param d Data
     * @return Data
     */
    public Data delete( Data d )
    {
        return delete( d.key );
    }
    
    //-------------------- delete( String ) ----------------------------
    /**
     * Find the node in the tree whose data field contains a key that 
     *  matches the string passed as an argument.
     * @param k String
     * @return Data
     */
    public Data delete( String k )
    {
        Data foundData = null;
        Node found = findNode( k );
        if ( found != null )
        {
            foundData = found.data;
            removeNode( found );
        }
        return foundData;
    }
    //--------------------- root() ----------------------------------
    /**
     * Return root of the tree; this is package access so that DrawPanel
     * can do a prefix walk of the tree. Would be better to have multiple
     * iterators.
     * @return BinarySearchTree.Node
     */
    BinarySearchTree.Node root()
    {
        return _root;
    }
    
    //-------------------- find( String ) -------------------------
    /**
     * Given a key value, search the tree to find the node that has
     * that key value, if it exists.
     * 
     * Return the Data object from the node or null
     * @param key String
     * @return Data
     */
    public Data find( String key )
    {
        Data found = null;
        Node cur = _root;
        while ( cur != null && found == null )
        {
            int cmp = key.compareTo( cur.data.key );
            if ( cmp == 0 )
                found = cur.data;
            else if ( cmp < 0 )
                cur = cur.left;
            else 
                cur = cur.right;
        }
        return found;
    }
    //-------------------- findNode( String ) -------------------------
    /**
     * Given a key value, search the tree to find the node that has
     * that key value, if it exists.
     * 
     * Return the Data object from the node or null.
     * @param key String
     * @return Node
     */
    public Node findNode( String key )
    {
        Data found = null;
        Node cur = _root;
        while ( cur != null && found == null )
        {
            int cmp = key.compareTo( cur.data.key );
            if ( cmp == 0 )
                found = cur.data;
            else if ( cmp < 0 )
                cur = cur.left;
            else 
                cur = cur.right;
        }
        return cur;
    }
    
    //--------------------- add -----------------------------------
    /**
     * Add a node to the tree in its proper position determined by the
     * "key" field of the Data object. This method uses the addNode 
     * recursive utility method.
     * @param data Data
     */
    public void add( Data data )
    {
        Node newNode = new Node( data );
        if ( _root == null )
            _root = newNode;
        else
            addNode( _root, newNode );
        _size++;
    }
    
    //------------------ addNode( Node, Node ) -----------------------
    /**
     * A recursive method to add a new Node (2nd argument) to the subtree
     * rooted at the first argument.
     * @param parent Node   root of tree into which the new Node must go.
     * @param newOne Node   Node to be added
     */
    private void addNode( Node parent, Node newOne )
    {
        if ( newOne.data.compareTo( parent.data ) < 0 )
        {
            if ( parent.left != null )
                addNode( parent.left, newOne );
            else 
            {
                parent.left = newOne;
                newOne.parent = parent;
            }
        }
        else
        {
            if ( parent.right != null )
                addNode( parent.right, newOne );
            else 
            {
                parent.right = newOne;
                newOne.parent = parent;
            }
        }
    }
    
    //-------------------------- size() -------------------------
    /**
     * Return tree size.
     * @return int
     */
    public int size()
    {
        return _size;
    }
    //-------------------------- toString() -------------------------
    /**
     * Generate a string representation of the tree.
     * @return String
     */
    public String toString()
    {
        return toString( _root, "  ", "  " ) ;        
    }
    
    /**
     * Recursively generate a string representation for a Node of a tree.
     * indent is increased for increasing depth.
     * branch is a short character string that prefixes each node indicating
     *     whether this node is a left (L) or right (R) child of its parent.
     * @param n Node  subtree root
     * @param indent String
     * @param branch String
     * @return String
     */
    private String toString( Node n, String indent, String branch )
    {
        String s = "";
        if ( n != null )
        {
            String prefix = indent.substring( 0, indent.length() - 2 ) + branch;
            s += prefix + n.data.toString() + "\n";
            if ( n.left != null )
                s += toString( n.left, indent + "  ", "L " );
            if ( n.right != null )
                s += toString( n.right, indent + "  ", "R " );
        }
        return s;
    }
}
