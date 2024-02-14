public class TreeNode{

    String data;
    TreeNode left;
    TreeNode right;

    public TreeNode( String data ){
        this.data = data;
    }

    public static TreeNode buildTree() {
        TreeNode A = new TreeNode("A");
        TreeNode B = new TreeNode("B");
        TreeNode C = new TreeNode("C");
        TreeNode D = new TreeNode("D");
        TreeNode E = new TreeNode("E");
        TreeNode F = new TreeNode("F");
        TreeNode G = new TreeNode("G");
        TreeNode H = new TreeNode("H");
        TreeNode I = new TreeNode("I");

        A.left = B;
        A.right = C;
        B.left = D;
        D.left = E;
        D.right = F;
        C.right = G;
        G.left = H;
        G.right = I;

        return A;
    }
}
