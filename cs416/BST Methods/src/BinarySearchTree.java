import java.util.*;

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
        _size = 0;
    }
    
    //-------------------- find( String ) -------------------------
    /**
     * Given a key value, search the tree to find the node that has
     * that key value, if it exists.
     * 
     * Return the Data object from the node or null.
     * 
     * @param key String     string to search for as a key in the tree
     * @return Data          if id found, return Data object for id
     *                       else return null
     */
    public Data find( String key )
    {
        return find( _root, key );
    }
    
    //------------------ find( Node, String ) ----------------------
    /**
     * Recursion is done in this method.
     *   return the Data object with the specified key 
     *   if it is in the subtree rooted at Node n.
     * 
     * @param n Node         subtree to search for key
     * @param key String     key to search for
     * @return Data          Data object containing found key, or null
     */
    private Data find( Node n, String key )
    {
        if (n == null) {
            return null;
        }
        if (n.data.key.compareTo(key) == 0) {
            return n.data;
        }
        if (n.data.key.compareTo(key) < 0) {
            return(find(n.right, key));
        }
        if (n.data.key.compareTo(key) > 0) {
            return(find(n.left, key));
        }
        return null;  
    }

    
    
    //-------------------- maxKey() --------------------------------
    /**
     * Find the node in the tree that has the  maximum key field.
     * Uses the BST properties to recursively find the maximum key.
     * 
     * @return Data   returned Data containing max value,
     *                or null if tree is empty.
     */
    public Data maxKey()
    {
        return maxKey( _root );
    }
    //-------------------- maxKey( Node ) ----------------------------
    /**
     * Find the node in the tree that has the  maximum key field.
     * Uses the BST properties to recursively find the maximum key.
     * 
     * @param n Node       subtree to find max data for
     * @return Data        the Data object with the max data value
     */
    private Data maxKey( Node n )
    {
        if (n == null) {
            return null;
        }
        else if(n.right == null) {
            return n.data;
        }
        else {
            return (maxKey(n.right));
        }

    }   
    
    //-------------------- maxValue() --------------------------------
    /**
     * Find the node in the tree that has the  maximum value field.
     * Remember the tree is organized by "key", not "value", so 
     * you need to traverse the entire tree. It doesn't matter 
     * what traversal you do. However, it make sense to define 
     * a method that finds the max value in a subtree and 
     * recursively call that for each child subtree.
     * 
     * @return Data   returned Data containing max value,
     *                or null if tree is empty.
     */
    public Data maxValue()
    {
        return maxValue( _root );
    }
    //-------------------- maxValue( Node ) ----------------------------
    /**
     * Find maximum value in a subtree. Each invocation finds the 
     * max of the left subtree, the max of the right subtree, 
     * and returns the node with the max of those 2 values and 
     * the Data from that node.
     * 
     * @param n Node       subtree to find max data for
     * @return Data        the Data object with the max data value
     */
    private Data maxValue( Node n )
    {

        if (n == null) {
            return null;
        }

        Data maxN = n.data;
        Data maxR = n.data;
        Data maxL = n.data;

        if (n.right == null && n.left == null) {
            return n.data;
        }

        Data recursR = null;

        if (n.right != null) {
            recursR = maxValue(n.right);
        }

        if (n.right != null && maxN.compareTo(recursR) > 0) {
            maxR = recursR;
        }
        Data recursL = null;
        if (n.left != null) {
            recursL = maxValue(n.left);
        }

        if (n.left != null && maxN.compareTo(maxValue(n.left)) > 0) {
            maxL = recursL;
        }
        if (maxR.compareTo(maxN) >= 0 && maxL.compareTo(maxN) >= 0) {
            return maxN;
        }
        if (maxR.compareTo(maxL) > 0) {
            return maxL;
        }
        return maxR;


    }


    //////////////////////////////////////////////////////////////////
    // MAKE NO CHANGES BELOW HERE
    //////////////////////////////////////////////////////////////////
    
    
    //--------------------- add -----------------------------------
    /**
     * Add a node to the tree in its proper position determined by the
     * "key" field of the Data object. This method uses the addNode 
     * recursive utility method.
     * 
     * @param data Data         Data object to be added to the tree.
     */
    public void add( Data data )
    {
        boolean added = true;
        if ( _root == null )
            _root = new Node( data );
        else
            added = addNode( _root, data );
        if ( added )
            _size++;
    }
    
    //------------------ addNode( Node, Data ) -----------------------
    /**
     * A recursive method to add a new Data object to the subtree
     * rooted at the first argument.
     * 
     * @param parent Node       subtree to which Data is to be added
     * @param newOne Data       Data object to be added to the tree
     * @return boolean          true if add was successful
     */
    private boolean addNode( Node parent, Data newOne )
    {
        boolean added = true;
        int cmp = newOne.compareTo( parent.data );
        if ( cmp < 0 )
        {
            if ( parent.left != null )
                added = addNode( parent.left, newOne );
            else 
            {
                parent.left = new Node( newOne );
                parent.left.parent = parent;
            }
        }
        else if ( cmp == 0 )
        {
            System.err.println( "== key found: Not adding: " + newOne );
            added = false;
        }
        else
        {
            if ( parent.right != null )
                added = addNode( parent.right, newOne );
            else 
            {
                parent.right = new Node( newOne );
                parent.right.parent = parent;
            }
        }
        return added;
    }
    //-------------------- size(  ) -------------------------
    /**
     * Return the number of nodes in the tree.
     * @return int   size of tree
     */
    public int size()
    {
        return _size;
    }
    //-------------------------- toString() -------------------------
    /**
     * Generate a string representation of the tree.
     * 
     * @return String       a string representation of the tree.
     */
    public String toString()
    {
        return toString( _root, "  ", "=" ) ;        
    }
    
    /**
     * Recursively generate a string representation for a Node of a tree;
     *    indent is increased for increasing depth.
     * Branch is a short string that prefixes each node indicating
     *        whether the node is a left (L) or right (R) child.
     * @return String        string rep of subtree
     * @param n Node         subtree to convert to string
     * @param indent String  prefix string for indentation level
     * @param branch String  (L) or (R)
     */
    private String toString( Node n, String indent, String branch )
    {
        String s = "";
        if ( n != null )
        {
            String prefix = indent.substring( 0, indent.length() - 2 ) 
                                  + branch;
            s += prefix + n.data.toString() + "\n";
            if ( n.left != null )
                s += toString( n.left, indent + "  ", "L " );
            if ( n.right != null )
                s += toString( n.right, indent + "  ", "R " );
        }
        return s;
    }
    //+++++++++++++++++++++++ inner class Node ++++++++++++++++++++++
    /**
     * The Node class does not have to be seen outside this class, so
     * it is private.
     * @author rdb
     */
    public class Node
    {
        //-------------- instance variables ---------------------------
        Data data;
        Node left;
        Node right;
        Node parent;
        
        //--------------- constructor --------------------------------
        /**
         * Construct a node with Data.
         * 
         * @param  d Data    data for the node.
         */
        public Node( Data d )
        {
            data = d;
            left = null;
            right = null;
            parent = null;
        }
    }
}
