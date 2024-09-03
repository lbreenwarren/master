public class Set {

    private final int ALPHABET_SIZE = 26;
    final static String alphabet = "abcdefghijklmnopqrstuvwxyz";

    class TrieNode {

        TrieNode[] child;
        boolean isWord;

        public TrieNode() {
            isWord = false;
            child = new TrieNode[ALPHABET_SIZE]; // very space inefficient
        }
    }

    TrieNode _root;
    int _size;
    char key;

    public Set() {
        _root = new TrieNode();
        _size = 0;
    }

    private int getLetterIndex(char c) {
        for (int i = 0; i < ALPHABET_SIZE; ++i) {
            if (c == alphabet.charAt(i)) {
                return i;
            }
        }
        return -1;
    }

    // insert a key into the set
    // return false if key already exists; otherwise insert new key and return true
    public boolean insert(String key) {
        TrieNode tmp = _root;
        char[] keychars = key.toCharArray();
        for (int i = 0; i < keychars.length; ++i) {
            int trieIndex = getLetterIndex(keychars[i]);
            if (trieIndex == -1) {
                return false;
            }
            if (tmp.child[trieIndex] == null) {
                tmp.child[trieIndex] = new TrieNode();
                tmp = tmp.child[trieIndex];
               
            } else {
                tmp = tmp.child[trieIndex];
            }
        }
        if (!tmp.isWord) {
            tmp.isWord = true;
            ++_size;
            return true;
        }
        return false;
    }

    // remove a key from the set
    // return false if the key doesn't exist; otherwise remove key and return true
    public boolean erase(String key) {
        TrieNode tmp = _root;
        char[] keychars = key.toCharArray();
        int lastIndex = keychars.length - 1;
        for (int i = 0; i < keychars.length; ++i) {
            int trieIndex = getLetterIndex(keychars[i]);
            if (trieIndex == -1) {
                return false;
            }
            if (tmp.child[trieIndex] == null) {
                return false;
            } else {
                tmp = tmp.child[trieIndex];
            }
        }
        if (tmp.isWord) {
            tmp.isWord = false;
            --_size;
            return true;
        }
        return false;
    }

    // search for a key
    // return true if an element is found; false otherwise
    public boolean find(String key) {
        TrieNode tmp = _root;
        char[] keychars = key.toCharArray();
        int lastIndex = keychars.length - 1;
        for (int i = 0; i < keychars.length; ++i) {
            int trieIndex = getLetterIndex(keychars[i]);
            if (trieIndex == -1) {
                return false;
            }
            if (tmp.child[trieIndex] == null) {
                return false;
            } else {
                tmp = tmp.child[trieIndex];
            }
        }
        if (tmp.isWord) {
            return true;
        }
        return false;
    }
    

     // return size of the set
    public int size() {
        return _size;
    }
}
