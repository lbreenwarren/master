public class Queue<T> {
    
    LinkedList<T> list;

    public void add(T value) {
        list.addHead(value);
    }

    public T remove() {
        T ret = list.remTail();
        return ret;
    }

    public LinkedList<T> getList() {
        return list;
    }
}
