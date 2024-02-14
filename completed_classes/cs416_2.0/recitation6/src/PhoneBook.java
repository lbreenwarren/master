public class PhoneBook {
    
    TreeNode<String, String> root;

    public PhoneBook() {
        root = null;
    }

    public void put(String name, String number) {
        if (root == null) {
            root = new TreeNode<String,String>(name, number);
        } else {
            addHelper(root, name, number);
        }

    }

    private void addHelper(TreeNode<String, String> n, String key, String value) {
    
        int cmp = n.getKey().compareTo(key);
    
        if (cmp == 0) {
            n.setValue(value);
        } else if (cmp < 0) {
            if (n.right == null) {
                n.right = new TreeNode<String,String>(key, value);
            } else {
                addHelper(n.right, key, value);
            }
        } else {
           if (n.right == null) {
                n.left = new TreeNode<String,String>(key, value);
            } else {
                addHelper(n.left, key, value);
            }
        }

    }

    public String get(String k) {
        return getHelper(root, k);
    }

    private String getHelper(TreeNode<String, String> n, String k) {
        if (n == null) {
            return null;
        }

        int cmp = n.getKey().compareTo(k);
        
        if (cmp == 0) {
            return n.getValue();
        } else if (cmp < 0) {
            return getHelper(n.right, k);
        } else {
            return getHelper(n.left, k);
        }
    }

    public String toString() {
        return toStringHelper(root);
    }

    private String toStringHelper(TreeNode<String, String> n) {
        if (n == null) {
            return "";
        } else {

            String retLeft = toStringHelper(n.left);
            String retRight = toStringHelper(n.right);
            return retLeft + "\n" + n + "\n" + retRight;
        }
    }
}
