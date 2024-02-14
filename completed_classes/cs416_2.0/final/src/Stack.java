public class Stack<T> {

    LinkedList<T> list; 

    public void push(T value) {
        list.addHead(value);
    }

    public T pop(){
        T ret = list.remHead();
        return ret;
    }

    public LinkedList<T> getList() {
        return list;
    }
}