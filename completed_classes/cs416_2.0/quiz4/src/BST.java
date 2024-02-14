public class BST{

    Node root;

    public BST(){ }

    public void add( String data ){
        Node n = new Node( data );
        if( root == null ){
            root = n;
            return;
        }

        Node curr = root;
        boolean added = false;
        while( !added ){
            int c = curr.data.compareTo( data );
            if( c > 0 ){
                if( curr.left == null ){
                    curr.left = n;
                    added = true;
                    n.parent = curr;
                }
                else{
                    curr = curr.left;
                }
            }
            else{
                if( curr.right == null ){
                    curr.right = n;
                    added = true;
                    n.parent = curr;
                }
                else{
                    curr = curr.right;
                }
           }
       }
    }

    public class Node{
        String data;
        Node left;
        Node right;
        Node parent;

        public Node( String data ){
            this.data = data;
        }
    }

    public String min() {
        Node tmp = root;
        while (tmp.left != null) {
            tmp = tmp.left;
        }

        return tmp.data;
    }
}
