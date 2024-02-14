
public class BST{

    private Node root;
    private int size;

    public String find(Node root, int key) {
        if (root.key == key) {
            return root.value;
        }

        if (key < root.key) {
            if (root.left == null) {
                return null;
            }
            return find(root.left, key);
        }

        if (key > root.key) {
            if (root.right == null) {
                return null;
            }
            return find(root.right, key);
        }

        return null;
    }

    public String[] findAll(int[] keys) {
        String[] ret = new String[keys.length];
        for (int i = 0; i < keys.length; ++i) {
            ret[i] = find(root, keys[i]);
        }

        return ret;
    }

    public void add( int key, String value ){
        Node node = new Node( key, value );

        if( root == null ){
            root = node;
            size++;
            return;
        }

        Node curr = root;
        while( curr != null ){
            if( key > curr.key ){
                if( curr.right == null ){
                    curr.right = node;
                    return;
                }
                curr = curr.right;
            }
            else if( key < curr.key ){
                if( curr.left == null ){
                    curr.left = node;
                    return;
                }
                curr = curr.left;
            }
            else{
                return;
            }
        }

    }

    public Node getRoot(){
        return root;
    }

    public int getSize(){
        return size;
    }

    public String toString(){
        String ret = toString( root, "", "" );
        if( !ret.equals("") ){
            ret = ret.substring( 0, ret.length()-1 );
        }
        return ret;
    }

    public String toString( Node node, String indent, String prefix ){
        String ret = "";

        if( node != null ){
            ret += toString( node.left, indent+"    ", "L " );
            ret += indent + prefix + "(" + node.key + ", " + node.value + ")" + "\n";
            ret += toString( node.right, indent+"    ", "R " );
        }

        return ret;
    }

    public class Node{
        int key;
        String value;
        Node left;
        Node right;
        Node parent;

        public Node( int key, String value ){
            this.key = key;
            this.value = value;
        }
    }

    public static void main( String[] args ){
        BST t = new BST();

        t.add(15, "F");
        t.add(21, "B");
        t.add(5, "M");
        t.add(18, "Z");
        t.add(10, "A");
        t.add(4, "V");

        int[] keys = {10, 4, 5};
        System.out.println(t.findAll(keys));

        System.out.println(t);
    }
}
