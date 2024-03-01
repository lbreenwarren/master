    package recfunlist;

    import java.util.NoSuchElementException;
    import static recfunlist.List.cons;

    public final class Nil<T> implements List<T> {

    private Nil() {
        // make Nil a singleton; equals and hashCode not redefined
        // Please don't change me :)
    }

    /* Must be defined here and not in List to avoid class initialization deadlocks. */
    final static Object TheNil = new Nil<Void>();

    @Override
    public String toString() {
        return "List()";
    }

    public T head() {
        throw new NoSuchElementException("Nil Has No Head");
    }

    public List<T> tail() {
        throw new NoSuchElementException("Nil Has No Tail");
    }

    public T last() {
        throw new NoSuchElementException("Nil Has No Last Element");

    }

    public boolean isEmpty() {
        return true;
    }

    public boolean nonEmpty() {
       return false;
    }

    public int length() {
        return 0;
    }

    public List<T> take(int count) {
        throw new IllegalArgumentException();
    }

    public List<T> drop(int count) {
        throw new IllegalArgumentException();
    }
    

    public List<T> reverse() {
        return (List<T>) TheNil;
    }
    

    public T getAt(int index) {
        if (index < 0) {
            throw new IllegalArgumentException();
        } else {
            throw new NoSuchElementException("Nil has no elements");
        }
    }

    public List<T> join(List<T> other) {
        return other;
    }

    public List<T> append(T value) {
        return new Cons<T>(value, (List<T>)TheNil);
    }
}
