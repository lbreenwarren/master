//++++++++++++++++++++++++ TreeApp +++++++++++++++++++++++++++++
import java.util.*;

/**
 * TreeApp - the main class for controlling the application code.
 *
 * The application semantics including the graphics generated
 * by the app are controlled from this class.
 *
 * @author rdb
 */

public class TreeApp
{
    //--------------------- class variables
    private static TreeApp   treeApp;   // the singleton TreeApp
    public static Random    rng = null;

    //--------------------- instance variables -------------------------
    public ArrayList<Data>  _list;
    public BinarySearchTree _bst = null;

    //--- data generation constants with package access
    int     dataSize        = 8;
    int     minDataSize     = 0;
    int     maxDataSize     = 20;
    int     defaultDataSize = 8;

    int     rngSeed          = 2;   // random seed
    int     maxSeed          = 16;   // arbitrary number

    //--- subset constants
    private int     subsetMin   = 30;   // arbitrary; all values are 0, 100
    private int     subsetMax   = 70;

    //---------------------- constructor --------------------------------
    /**
     *
     */
    public TreeApp( )
    {
        treeApp = this;
    }
    //----------------------- treeApp() --------------------------
    /**
     * Static application accessor.
     * @return TreeApp
     */
    public static TreeApp treeApp()
    {
        return treeApp;
    }
    //---------------------- makeData() -----------------------------
    /**
     * Update list size and generate a new list of that size.
     * @param sz int    size
     */
    public void makeData( int sz )
    {
        this.dataSize = sz;
        _list = generateData( this.dataSize, this.rngSeed );

        _bst = new BinarySearchTree();

        for ( Data d: _list ){
            _bst.add( d );
        }

    }

    /**
     * Show results to "user". Print to standard output.
     * @param msg String      string to print
     */
    public void showResults( String msg )
    {
        System.out.println( msg );
    }

    //------------------- printTree( String ) --------------------
    /**
     * Print the tree with given title.
     * @param title String
     */
    public void printTree( String title )
    {
        String dashes = " ------------------------ ";
        System.out.println( "\n" + dashes + title + dashes );
        if ( _bst == null || _bst.size() == 0 )
            System.out.println( "---Empty---" );
        else
        {
            System.out.println( _bst );
            System.out.println( "Tree has " + _bst.size() + " nodes." );
        }
    }

    //------------------- generateData ---------------------------------
    /**
     * Generate data for a new tree. Arguments are
     *    numItems -- number items to generate
     *    seed     -- random number seed; -1 means let system pick it.
     * @param numItems int
     * @param seed     int
     * @return ArrayList<Data>
     */
    static ArrayList<Data> generateData( int numItems, int seed )
    {
        ArrayList<Data>   dl = new ArrayList<Data>();
        ArrayList<String> keys = new ArrayList<String>();

        if ( rng == null )
            rng = new Random();

        rng.setSeed(seed);

        String        letters = "abcdefghijklmnopqrstuvwxyz";
        StringBuffer  keybuf  = new StringBuffer( "12" );

        while ( dl.size() < numItems )
        {
            // generate a key
            char letter1 = letters.charAt( rng.nextInt( letters.length() ) );
            char letter2 = letters.charAt( rng.nextInt( letters.length() ) );

            keybuf.setCharAt( 0, letter1 );
            keybuf.setCharAt( 1, letter2 );
            String key = keybuf.toString();

            if ( ! keys.contains( key ) )
            {

                // generate a value from 0 to 99
                int    val = rng.nextInt( 100 );
                dl.add( new Data( key, val ) );
                keys.add( key );
            }
        }
        return dl;
    }

    /**
     * Ensure that BST has all correct values.
     *
     */
    public String validateTreeData(){
        String ret = "";
        for( Data d: _list ){
            if(_bst.find( d.key )==null)
                ret += d.key + ", ";
        }
        if(ret.equals(""))
            return ret;
        else
            return ret.substring(0, ret.length()-2);
    }

    /**
     * Ensure that BST has all correct values.
     *
     */
    public String validateTreeOrder(){
        String ret = "";
        HashSet<String> outOfOrder = new HashSet<String>();
        validateSubtree(_bst.root(), outOfOrder);
        for(String s: outOfOrder)
            ret += s + ", ";
        if(ret.equals(""))
            return ret;
        else
            return ret.substring(0, ret.length()-2);
    }

    private void validateSubtree( BinarySearchTree.Node n,
                                  HashSet<String> result){
        if(n==null)
            return;
        validateSubtree(n.left, result);
        validateSubtree(n.right, result);

        validateSubtree(n.left, n.data.key, true, result);
        validateSubtree(n.right, n.data.key, false, result);
    }

    private void validateSubtree( BinarySearchTree.Node n,
                                  String rootKey, boolean isLeft,
                                  HashSet<String> result ){
        if(n==null)
            return;
        if(isLeft && rootKey.compareTo(n.data.key) <= 0)
            result.add(n.data.key);
        if(!isLeft && rootKey.compareTo(n.data.key) >= 0)
            result.add(n.data.key);

        validateSubtree(n.left, rootKey, isLeft, result);
        validateSubtree(n.right, rootKey, isLeft, result);
    }

    //------------------ checkParentLinks( BST ) ----------------------
    /**
     * For a complete check of parent links, the root parent should be
     *  checked to make sure it is null.
     */
    String checkParentLinks( )
    {
        BinarySearchTree.Node root = _bst.root();
        if ( root != null && root.parent != null )
        {
            System.err.print( "parent( root ) != null. -> "
                            + root.parent.data );
        }
        return checkParentLinks( root );
    }

    //--------------------- checkParentLinks -------------------------
    /**
     * Recursively check that the parent reference of both node's children
     *    reference the node. Print to System.err, if anything is bad.
     * @param n Node
     */
    String checkParentLinks( BinarySearchTree.Node n )
    {
        String ret = "";
        if ( n == null )
            return ret;
        if ( n.left != null )
        {
            if ( n.left.parent != n ){
                ret += n.left.data.key;
            }
            ret += checkParentLinks( n.left );
        }
        if ( n.right != null )
        {
            if ( n.right.parent != n ){
                ret += n.right.data.key;
            }
            ret += checkParentLinks( n.right );
        }

        return ret;
    }


    //----------------- main -------------------------------------------
    /**
     * Convenience method to execute BSTDeleteApp.main.
     * @param args String[]
     */
    public static void main( String[] args )
    {
        TreeApp t = new TreeApp( );

        for(int i = 1; i < 64; i*=2){
            t.makeData(i);
            t.printTree("current tree");

            //remove nodes
            boolean removedAll = true;
            while(!t._list.isEmpty()){
                int r = t.rng.nextInt( t._list.size() );
                String key = t._list.get(r).key;
                t._list.remove(r);
                String before = t._bst.toString();
                t._bst.delete( key );
                String missing = t.validateTreeData();
                String outOfOrder = t.validateTreeOrder();
                String badParents = t.checkParentLinks();
                boolean printTree = false;
                if( t._bst.size()!=t._list.size() ||
                    !missing.equals("") ||
                    !outOfOrder.equals("") ||
                    !badParents.equals("") ||
                    t._bst.find(key)!=null ){
                    removedAll = false;
                    printTree = true;
                    t.showResults("----------Invalid tree after deleting "
                                  + key);
                }
                if( t._bst.find(key)!=null ){
                    removedAll = false;
                    printTree = true;
                    t.showResults("----------Tree still contained "
                                  + key);
                }
                if( t._bst.size()!=t._list.size() ){
                    removedAll = false;
                    printTree = true;
                    t.showResults("----------Expected size: "
                                  + t._list.size() + "   BST size: " + t._bst.size());
                }
                if( !missing.equals("")){
                    removedAll = false;
                    printTree = true;
                    t.showResults("----------Cannot find: " + missing +
                                  " (possibly due to being out of order)");
                }
                if( !outOfOrder.equals("")){
                    removedAll = false;
                    printTree = true;
                    t.showResults("----------Out of order: "
                                  + outOfOrder);
                }
                if( !badParents.equals("")){
                    removedAll = false;
                    printTree = true;
                    t.showResults("----------Nodes with incorrect parents: "
                                  + badParents);
                }
                if(printTree){
                    System.out.println("********** Original tree **********\n" +
                                       before +
                                       "***********************************");
                    System.out.println("******* Tree after deletion *******\n" +
                                       t._bst.toString() +
                                       "***********************************");
                    System.out.println();
                    System.out.println();
                }
            }
            if(removedAll)
                t.showResults("----------Successfully removed all nodes.");
        }

    }
}
