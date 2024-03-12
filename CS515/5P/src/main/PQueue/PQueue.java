package PQueue;

import java.lang.reflect.Array;

public class PQueue<T extends Comparable<T>> {
    int _size;
    T[] _array;

    // construct an empty binary heap.
    @SuppressWarnings("unchecked")
    public PQueue(Class<T> classRef, int capacity) { 
        _array = (T[]) Array.newInstance(classRef, capacity + 1);
        _size = 0;
    }

    // construct a binary heap from an array of element
    // assume the array size is smaller than capacity
    @SuppressWarnings("unchecked")
    public PQueue(Class<T> classRef, int capacity, T[] items){
        _array = (T[]) Array.newInstance(classRef, capacity + 1);
        for (int i = 0; i < items.length; ++i) {
            _array[i + 1] = items[i];
            ++_size;
        }
        buildHeap();
        
    }


    // insert item into the priority queue; duplicates are allowed.
    public void insert(T x){
        _array[_size + 1] = x;
        ++_size;
        moveUp();
    
    }

    // return the smallest item in the priority queue.
    public T findMin(){
        return _array[1];
    }

    // remove the smallest item from the priority queue
    // Would ideally throw UnderflowException if empty,
    // but not necessary for this lab.
    public void deleteMin(){
        _array[1] = _array[_size];
        --_size;
        moveDown(1);
    }

    // return queue size
    public int size() {
        return _size;
    }
    // test if the priority queue is logically empty.
    // return true if empty, false otherwise.
    public boolean isEmpty(){
        if (size() == 0) {
            return true;
        }
        return false; 
    }

    // private method to move up the last item in the heap.
    private void moveUp(){
        for (int i = _size; i > 1; --i) {
            if (_array[i].compareTo(_array[i / 2]) < 0) {
                T tmp = _array[i];
                _array[i] = _array[i / 2];
                _array[i / 2] = tmp;
            }
        } 
    }


    // Establish heap-order property from an arbitrary
    // arrangement of items. Runs in linear time.
    private void buildHeap(){
        int index = 1;
        while(!isLeafIndex(index)) {
            ++index;
        }
        --index;
        while (index != 0) {
            moveDown(index);
            --index;
        }
    }
    private boolean isLeafIndex(int index) {
        if (index > _size / 2) {
            return true;
        }
        return false;
    }
    // private method to move down in the heap.
    // curIndex is the index at which the move down begins.
    private void moveDown(int curIndex){
        while(!isLeafIndex(curIndex)) {
            int swapIndex = -1;
            if (_size >= (2 * curIndex) + 1 && _array[2*curIndex].compareTo(_array[(2*curIndex) + 1]) > 0) {
                swapIndex = (2*curIndex) + 1;
            } else {
                swapIndex = 2 * curIndex;
            }
            if (_array[curIndex].compareTo(_array[swapIndex]) > 0) {
                T tmp = _array[curIndex];
                _array[curIndex] = _array[swapIndex];
                _array[swapIndex] = tmp;
            } else {
                return;
            }
            curIndex = swapIndex;
        }
    }
}
