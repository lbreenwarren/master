import PQueue.PQueue;
import java.lang.StringBuilder;

public class HuffTree {
    HuffNode _root;

    public void buildTree(char[] chs, int[] freqs, int size){
        // TO IMPLEMENT
        
        HuffNode nodes[] = new HuffNode[size];
        for(int i = 0; i < chs.length; ++i) {
            HuffNode tmp = new HuffNode(null, null, freqs[i], chs[i]);
            nodes[i] = tmp;
        }
        PQueue<HuffNode> q = new PQueue<HuffTree.HuffNode>(HuffNode.class, size, nodes);
        while (q.size() != 1) {
            HuffNode left = q.findMin();
            q.deleteMin();
            HuffNode right = q.findMin();
            q.deleteMin();
            int newFreq = left.freq + right.freq;
            HuffNode parent = new HuffNode(left, right, newFreq, '0');
            q.insert(parent);
        }
        _root = q.findMin();
    }

    public String getCode(char ch){
        StringBuilder ret = new StringBuilder();
        getCodeHelper(ch, ret, _root);
        return ret.toString();
    }

    private StringBuilder getCodeHelper(char ch, StringBuilder code, HuffNode huff) {
        if (huff.data == ch) {
            return code;
        } 
        if (huff.left != null) {
            code.append("0");
            StringBuilder nextCall = getCodeHelper(ch, code, huff.left);
            if (nextCall != null) {
                return code; 
            } else {
                code.setLength(code.length() - 1);
            }
        }
        if (huff.right != null) {
            code.append("1");
            StringBuilder nextCall = getCodeHelper(ch, code, huff.right);
            if (nextCall != null) {
                return code; 
            } else {
                code.setLength(code.length() - 1);
            }
        }
        return null;
    }

    private class HuffNode implements Comparable<HuffNode> {
        HuffNode left;
        HuffNode right;
        int freq;
        char data;
        String code;
        public HuffNode(HuffNode left, HuffNode right, int freq, char data) {
            this.left = left;
            this.right = right;
            this.freq = freq;
            this.data = data;
        }

        @Override
        public int compareTo(HuffNode o) {
            if (freq > o.freq) {
                return 1;
            } else if (freq < o.freq) {
                return -1;
            }
            return 0;
        }
    }
}
