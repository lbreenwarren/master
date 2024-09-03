    package funlist;

    import java.util.NoSuchElementException;

    import static funlist.List.cons;
    import static funlist.List.nil;

    public final class Cons<T> implements List<T> {

    private final T head;
    private List<T> tail; // non-final because of take and join

    Cons(T head, List<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public String toString() {
        List<T> tmp = this;
        StringBuilder ret = new StringBuilder("List(");
        while (!nil().equals(tmp)) {
            ret.append(tmp.head() + ",");
            tmp = tmp.tail();
        }
        ret.deleteCharAt(ret.length() - 1);
        return ret + ")";
    }

    @Override
    public boolean equals(Object any) {
        if (any.hashCode() == hashCode()) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        // To Implement
        List<T> tmp = this;
        int sum = 0;
        for (int i = 0; i < length(); ++i) {
            sum += tmp.head().hashCode() * 29;
            tmp = tmp.tail();
        }
        return sum;
    }

    public T head() {
        return head;
    }

    public List<T> tail() {
        return tail;
    }

    public boolean isEmpty() {
        if (nil().equals(tail)) {
            return true;
        }
        return false;
    }

    public boolean nonEmpty() {
        return !isEmpty();
    }

    public int length() {
        List<T> tmp = this;
        int count = 0;
        while (!nil().equals(tmp)) {
            ++count;
            tmp = tmp.tail();
        }
        return count;
    }

    public List<T> drop(int count) {
        if (count < 0) {
            throw new IllegalArgumentException();
        }
        if (count > length()) {
            return nil();
        }
        List<T> tmp = this;
        for (int i = 0; i < count; ++i) {
            tmp =  tmp.tail();
        }
        return tmp;
    }

    public List<T> reverse() {
        
        List<T> ret = null;
        List<T> prevTail = nil();
        for (int i = 0; i < length(); ++i) {
            ret = new Cons<T>(getAt(i), prevTail);
            prevTail = ret;
        }

        return ret;
    }

    public T getAt(int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        }
        List<T> tmp = this;
        for (int i = 0; i < index; ++i) {
            tmp = tmp.tail();
        }
        return tmp.head();
    }

    public List<T> take(int count) {
        if (count < 0) {
            throw new IllegalArgumentException();
        }
        List<T> prevTail = nil();
        List<T> tmp = nil();
        for (int i = count - 1; i >= 0; --i) {
            try {
                T head = getAt(i);
                tmp = new Cons<T>(head, prevTail);
                prevTail = tmp;
            } catch(NoSuchElementException e){}
        }
        return tmp;
    }

    public List<T> join(List<T> other) {
        List<T> prevTail = nil();
        List<T> tmp = null;
        for (int i = other.length() - 1; i >= 0; --i) {
            T head = other.getAt(i);
            tmp = new Cons<T>(head, prevTail);
            prevTail = tmp;
        }
        for (int i = length() - 1; i >= 0; --i) {
            T head = getAt(i);
            tmp = new Cons<T>(head, prevTail);
            prevTail = tmp;
        }
        return tmp;
    }

    public List<T> append(T value) {
        List<T> tmp = new Cons<T>(value, nil());
        List<T> ret = join(tmp);
        return ret;
    }

    public T last() {
        List<T> tmp = this;
        while(!nil().equals(tmp.tail())) {
            tmp = tmp.tail();
        }
        return tmp.head();
    }
    }
