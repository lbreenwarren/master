public class TreeNode<K extends Comparable, V> {
    TreeNode<K, V> left;
    TreeNode<K, V> right;
    TreeNode<K, V> parent;
    K key;
    V value;

    public TreeNode(K key, V value) {
        key = this.key;
        value = this.value;
        left = null;
        right = null;
        parent = null;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    } 

    public String toString() {
        return key + "-> " + value;
    }




}