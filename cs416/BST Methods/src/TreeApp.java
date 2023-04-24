import java.util.*; 

public class TreeApp
{

    ArrayList<Data>  _list;
    BinarySearchTree _bst = null;
    private Random           _rng = null;
    int     dataSize        = 8;
    int     minDataSize     = 0;
    int     maxDataSize     = 20;
    int     defaultDataSize = 8;
    int     maxVal           = 9;
    Integer expectedMaxVal   = null;
    String  expectedMaxKey   = null;
    int     expectedHeight   = -1;
    
    int     rngSeed          = 2;   // random seed

    /**
     * This is the constructor for batch execution.
     */
    public TreeApp( )
    {
    }
    
    /**
     * Update list size and generate a new list of that size.
     * @param sz int    size
     */
    public void makeData( int sz )
    {
        expectedMaxVal=null;
        expectedMaxKey=null;
        this.dataSize = sz;
        _list = generateData( this.dataSize, this.rngSeed );
        
        _bst = new BinarySearchTree();
        
        for ( Data d: _list ){
            if(expectedMaxVal==null || expectedMaxVal < d.value )
                expectedMaxVal = d.value;
            if(expectedMaxKey==null || expectedMaxKey.compareTo(d.key) < 0 )
                expectedMaxKey = d.key;
            _bst.add( d );
        }
        
    }

    /**
     * Generate data for the tree.
     * @param numItems int      number items to generate
     * @param seed     int      random number seed; -1: system picks
     * @return ArrayList<Data>  the data
     */
    private ArrayList<Data> generateData( int numItems, int seed )
    {
        _bst = new BinarySearchTree();
        ArrayList<Data> dl = new ArrayList<Data>();
        ArrayList<String> keys = new ArrayList<String>();
        
        if ( _rng == null )
            _rng = new Random( seed );
        
        String        letters = "abcdefghijklmnopqrstuvwxyz";
        StringBuffer  keybuf  = new StringBuffer( "12" );
        
        while ( dl.size() < numItems )
        {
            // generate a key
            char letter1 = letters.charAt( _rng.nextInt( letters.length() ) );
            char letter2 = letters.charAt( _rng.nextInt( letters.length() ) );
            
            keybuf.setCharAt( 0, letter1 );
            keybuf.setCharAt( 1, letter2 );         
            String key = keybuf.toString();
            
            int found = keys.indexOf( key ); 
            if ( found < 0 )   // keys must be unique so only add if not there
            {            
                keys.add( key );   // add key to key list
                // generate a value from 0 to maxVal
                int    val = _rng.nextInt( maxVal );
                dl.add( new Data( key, val ) );
                
            }
        }
        return dl;
    }
    
    /**
     * Show results to "user". Print to standard output.
     * @param msg String      string to print
     */
    public void showResults( String msg )
    {
        System.out.println( msg );
    }
    
    /**
     * Print the current tree.
     * @param title String             title
     */
    public void printTree( String title )
    {
        String dashes = "------------------------ ";
        System.out.println( "\n" + dashes + title + " " + dashes );
        if ( _bst == null || _bst.size() == 0 )
            System.out.println( "---Empty---" );
        else
        {
            System.out.println( _bst );
            System.out.println( "Tree has " + _bst.size() + " nodes." );
        }
    }
    /**
     * Find and report the max key Data item in the list.
     */
    public void maxKey( )
    { 
        String msg;
        Data max = _bst.maxKey();
        if ( max == null )
            msg = "----------Expected max key: " + expectedMaxKey +
                "    Actual max key: null";
        else
            msg = "----------Expected max key: " + expectedMaxKey +
                "    Actual max key: " + max.key;
        showResults( msg );
    }
    /**
     * Find and report the max value Data item in the list.
     */
    public void maxValue( )
    { 
        String msg;
        Data max = _bst.maxValue();
        if ( max == null )
            msg = "----------Expected max value: " + expectedMaxVal +
                "   Actual max value: null";
        else
            msg = "----------Expected max value: " + expectedMaxVal +
                "   Actual max value: " + max.value;
        showResults( msg );
    }
    /**
     * Batch version specifies id on command input line.
     * @param key String       key to be search for
     */
    public boolean find( String key )
    { 
        if ( key != null && key.length() > 0 )
        {         
            Data found = _bst.find( key );
            return found != null;
        }

        return false;
    }

    public static void main( String[] args ){
        TreeApp t = new TreeApp( );

        t.makeData(0);
        t.printTree("current tree");
        t.maxKey();
        t.maxValue();
            
        for(int i = 1; i < 32; i*=2){
            t.makeData(i);
            t.printTree("current tree");
            t.maxKey();
            t.maxValue();
            boolean foundAll = true;
            for( Data d: t._list ){
                if(!t.find( d.key )){
                    foundAll = false;
                    t.showResults("----------Did not find key: " + d.key);
                }
            }
            if( foundAll )
                t.showResults("----------Found all valid keys in tree.");
            String key = "fake";
            if(t.find(key))
                t.showResults("----------Found result for key not in tree. Key was " + key + " and found " + t._bst.find(key));
        }
    }
    
}
