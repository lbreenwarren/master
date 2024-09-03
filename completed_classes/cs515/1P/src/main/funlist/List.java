package funlist;

sealed public interface List<T> permits Cons, Nil {

  // List construction
  @SuppressWarnings("unchecked")
  static <T> Nil<T> nil() {
    return (Nil<T>)Nil.TheNil;
  }

  static <T> Cons<T> cons(T head, List<T> tail) {
    return new Cons<>(head, tail);
  }

  @SafeVarargs
  static <T> List<T> make(T... values) {
    List<T> list = nil();
    for (int i = values.length - 1; i >= 0; i--) {
      list = cons(values[i], list);
    }
    return list;
  }

  // List methods
  T head();

  List<T> tail();

  boolean isEmpty();

  boolean nonEmpty();

  int length();

  List<T> take(int count);

  List<T> drop(int count);

  List<T> reverse();

  T getAt(int index);

  List<T> join(List<T> other);

  List<T> append(T value);

  T last();
}
