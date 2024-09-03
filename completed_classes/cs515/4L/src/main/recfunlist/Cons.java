package recfunlist;

import static recfunlist.List.cons;
import static recfunlist.List.nil;

import java.util.NoSuchElementException;

public final class Cons<T> implements List<T> {

    private final T head;
    private final List<T> tail;

    Cons(T head, List<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public int hashCode() {
        return 19 * tail.hashCode() + head.hashCode();
    }

    public T head() {
        return head;
    }

    public List<T> tail() {
        return tail;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean nonEmpty() {
        return true;
    }

    public List<T> append(T value) {
        return join(cons(value, nil()));
    }

    @Override
    public boolean equals(Object any) {

        if( !(any instanceof Cons<?>)) {
            return false;
        }

        Cons<?> list = (Cons<?>)any;

        return this.head().equals(list.head()) && this.tail().equals(list.tail());
    }

    // STEP 1
    public int length() {
        if (equals(nil())) {
            return 0;
        } else {
            return 1 + tail.length();
        }
    }

    // STEP 2
    public T last() {
        if (tail.equals(nil())) {
            return head;
        } else {
            return tail.last();
        }
    }

     // STEP 3
    public T getAt(int index) {
        if (equals(nil())) {
            return null;
        } else if (index == 0) {
            return head;
        } else {
            return tail.getAt(index - 1);
        }
    }

    // STEP 4
    public List<T> join(List<T> other) {
        List<T> ret = new Cons<T>(head, other);
        
        if (equals(nil())){
            if (other.equals(nil())) {
                return nil();
            } else {
                return join(other.tail());
            }
        }
        ret = tail.join(other);
        return new Cons<T>(head, ret);
    }

    // STEP 5
    public List<T> take(int count) {
        if (count < 0) {
            throw new IllegalArgumentException();
        }
        return takeHelper(this, count);
    }

    // STEP 5 helper
    private List<T> takeHelper(List<T> l, int count) {
        if (count != 0  && l.equals(nil())) {
            return nil();
        } else if (count == 0) {
            return nil();
        } else {
            return new Cons<T>(l.head(), takeHelper(l.tail(), count - 1));
        }

    }

    // STEP 6
    public List<T> drop(int count) {
        if (count < 0) {
            throw new IllegalArgumentException();
        }
        if (count == 0) {
            return this;
        }
        if (count == 1) {
            return tail;
        }
        return dropHelper(tail, count - 1);
    }

    // STEP 6 helper
    private List<T> dropHelper(List<T> l, int count) {
        if (count != 0  && l.equals(nil())) {
            return nil();
        } else if (count == 0) {
            return l;
        } else {
            return dropHelper(l.tail(), count - 1);
        }
    }

    // STEP 7
    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("List(" + head + ",");
        toStringHelper(tail(), str);
        return str.toString();
    }

    // STEP 7 helper
    private StringBuilder toStringHelper(List<T> list, StringBuilder str) {
        if (list.equals(nil())) {
            str.append(")");
            return str;
        } else {
            str.append(list.head());
            if (!list.tail().equals(nil())) {
                str.append(",");
            }
            toStringHelper(list.tail(), str);
            return str;
        }
    }

    // STEP 8
    public List<T> reverse() {
        // TO IMPLEMENT
        return reverseHelper(this, nil());
    }

    // STEP 8 helper
    private List<T> reverseHelper(List<T> list, List<T> rev) {
        if (list.equals(nil())) {
            return rev;
        } else {
            rev = new Cons<T>(list.head(), rev);
            return reverseHelper(list.tail(), rev);
        }
    }

}
