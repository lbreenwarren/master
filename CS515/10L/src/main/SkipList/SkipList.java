package SkipList;

import java.util.Random;

public class SkipList {
    SkipNode _head;
    SkipNode _tail; // Sentinel head and tail
    int _curHeight;
    int _maxHeight;
    double _probability;
    Random randNum;

    // SkipList constructor: construct an empty skiplist of max height m and and
    // probability p with default value 0.5
    // current height initialize to 1
    public SkipList(int m, double p) {
        randNum = new Random(8675309); // DO NOT CHANGE THIS :)
        _curHeight = 1;
        _maxHeight = m;
        _probability = p;
        // Create head with intMin and tail with intMax;
        int intMin = Integer.MIN_VALUE; // minimum int value
        int intMax = Integer.MAX_VALUE; // maximum int value
        _head = new SkipNode(intMin, _maxHeight);
        _tail = new SkipNode(intMax, _maxHeight);
        // attach them to form an empty list
        for (int x = 1; x <= _maxHeight; x++) {
            _head.nextNodes[x] = _tail;
        }
    }

    // copy constructor
    public SkipList(SkipList v) {
        randNum = new Random(8675309); // DO NOT CHANGE THIS :)
        _curHeight = v._curHeight;
        _maxHeight = v._maxHeight;
        _probability = v._probability;
        copyCode(v);
    }

    // common copy code
    public void copyCode(SkipList v) {
        int intMin = Integer.MIN_VALUE; // minimum int value
        int intMax = Integer.MAX_VALUE; // maximum int value
        _head = new SkipNode(intMin, _maxHeight);
        _tail = new SkipNode(intMax, _maxHeight);

        for (int x = 1; x <= _maxHeight; x++) {
            _head.nextNodes[x] = _tail;
        }

        SkipNode[] lookBack = new SkipNode[_maxHeight + 1];
        for (int x = 1; x <= _maxHeight; x++) {
            lookBack[x] = _head;
        }

        // copy elements
        SkipNode cur = v._head.nextNodes[1];
        while (cur != v._tail) {
            // create a new node
            SkipNode add = new SkipNode(cur.info, cur.height);
            for (int x = 1; x <= cur.height; x++) {

                // set add's nextnodes to lookback's next
                add.nextNodes[x] = lookBack[x].nextNodes[x];

                // set lookback's nextnodes to add
                lookBack[x].nextNodes[x] = add;

                // update lookback
                lookBack[x] = add;


            }
            cur = cur.nextNodes[1];
        }
    }


    // insert a node key into the skiplist, return false if key already exists
    // otherwise insert key and return true.
    public boolean insert(int info) {
        // pointer array to keep track of the next nodes at the insertion point
        SkipNode[] lookBack = new SkipNode[_maxHeight + 1];
        SkipNode cur = _head;
        int curInfo;
        // find where new node goes
        for (int h = _curHeight; h >= 1; h--) {
            curInfo = cur.nextNodes[h].info;
            while (curInfo < info) {
                cur = cur.nextNodes[h];
                curInfo = cur.nextNodes[h].info;
            }
            lookBack[h] = cur; // keep track of previous node
        }
        cur = cur.nextNodes[1]; // move to next node at level 1
        curInfo = cur.info;

        // If dup, return false
        if (curInfo == info) {
            return false;
        } else {
            
            // get the random height for the new node (call randomLevel)
            int newHeight = randomLevel();
           
            // prepare insertion point
            // (check if node has higher level than _curHeight, fix lookBack & _curHeight as needed)
            
            if (newHeight > _curHeight) {
                for (int i = _curHeight + 1; i <= newHeight; i++) {
                    lookBack[i] = _head;
                }
                _curHeight = newHeight;
            }
            
            // Insert new element
            SkipNode newNode = new SkipNode(info, newHeight);
            for (int i = 1; i <= newHeight; i++) {
                newNode.nextNodes[i] = lookBack[i].nextNodes[i];
                lookBack[i].nextNodes[i] = newNode;
            }

            return true;
        }
    }

    // erase a node key from the skiplist, return false if key doesnot exists
    // otherwise erase key and return true.
    public boolean erase(int info) {
        // pointer array to keep track of the next nodes at the delete point
        SkipNode[] lookBack = new SkipNode[_maxHeight + 1];
        SkipNode cur = _head;
        int curInfo;
        // find node to erase
        for (int h = _curHeight; h >= 1; h--) {
            curInfo = cur.nextNodes[h].info;
            while (curInfo < info) {
                cur = cur.nextNodes[h];
                curInfo = cur.nextNodes[h].info;
            }
            lookBack[h] = cur; // keep track of previous node
        }
        cur = cur.nextNodes[1]; // move to next node at level 1
        curInfo = cur.info;
        if (curInfo != info) {
            return false;
        } else {
            for (int h = 1; h <= cur.height; ++h) {
                lookBack[h].nextNodes[h] = cur.nextNodes[h];
            }
            if (cur.height == _curHeight) {
                int height = _curHeight;
                while (_head.nextNodes[height] == _tail) {
                    --height;
                }
                _curHeight = height;
            }
            return true;
        }
    }

    // Returns a random height between 1 and maximum node height.
    private int randomLevel() {
        int level = 1;
        while (((double) randNum.nextFloat() < _probability) && (level < _maxHeight))
            level++;
        return level;
    }

    // return ture if the key is found, otherwise false
    public boolean find(int info) {
        int curInfo = 0;
        SkipNode cur = _head;
        // find starts at top level
        for (int h = _curHeight; h >= 1; h--) {
            curInfo = cur.nextNodes[h].info;
            while (curInfo < info) { // remain the current level before move down a level
                cur = cur.nextNodes[h];
                curInfo = cur.nextNodes[h].info;
            }
        }

        return (curInfo == info);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        SkipNode tmp = _head.nextNodes[1];
        while (tmp != _tail) {
            for (int h = 1; h <= tmp.height; h++)
                sb.append(tmp.info + "    ");
            sb.append('\n');
            tmp = tmp.nextNodes[1];
        }
        return sb.toString();
    }

    private class SkipNode {
        SkipNode[] nextNodes;
        int height;
        int info;

        // construct a new skipnode with given key and height
        public SkipNode(int in, int h) {
            info = in;
            height = h;
            nextNodes = new SkipNode[h + 1]; // height 0 un-used
            for (int x = 1; x <= height; x++) {
                nextNodes[x] = null;
            }
        }
    }
}
