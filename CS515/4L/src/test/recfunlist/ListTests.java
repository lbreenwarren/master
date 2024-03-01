package recfunlist;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;
import static recfunlist.List.cons;
import static recfunlist.List.nil;

class ListTests {

  @Test
  void testToString() {
    assertEquals("List(1,2,3,4,5)", List.make(1, 2, 3, 4, 5).toString());
  }

  @Test
  void testEquals() {
    var list = List.make(1, 2, 3, 4, 5);
    assertEquals(list, cons(1, cons(2, cons(3, cons(4, cons(5, nil()))))));
    assertNotEquals(list, cons(0, cons(2, cons(3, cons(4, cons(5, nil()))))));
    assertNotEquals(list, cons(1, cons(2, cons(3, cons(4, nil())))));
    assertNotEquals(list, nil());
  }

  @Test
  void testHashCode() {
    var list = List.make(1, 2, 3, 4, 5);
    int hash = list.hashCode();
    assertEquals(hash, list.hashCode());
    assertEquals(hash, List.make(1, 2, 3, 4, 5).hashCode());
    assertNotEquals(hash, List.make(1, 2, 3, 4).hashCode());
  }

  @Test
  void head() {
    assertEquals(1, List.make(1, 2, 3, 4, 5).head());
  }

  @Test
  void tail() {
    assertEquals(List.make(2, 3, 4, 5), List.make(1, 2, 3, 4, 5).tail());
  }

  @Test
  void isEmpty() {
    assertTrue(List.make(1, 2, 3, 4, 5).nonEmpty());
    assertFalse(List.make(1, 2, 3, 4, 5).isEmpty());
  }

  @Test
  void length() {
    assertEquals(5, List.make(1, 2, 3, 4, 5).length());
  }

    @Test
    void drop1() {
        var list = List.make(1, 2, 3, 4, 5);
        assertEquals(list, list.drop(0));
        assertEquals(List.make(2, 3, 4, 5), list.drop(1));
        assertEquals(List.make(3, 4, 5), list.drop(2));
        assertEquals(List.make(4, 5), list.drop(3));
        assertEquals(List.make(5), list.drop(4));
    }

    @Test
    void drop2() {
        var list = List.make(1, 2, 3, 4, 5);
        assertEquals(nil(), list.drop(5));
        assertEquals(nil(), list.drop(50));
        assertThrows(IllegalArgumentException.class, () -> list.drop(-1));
        assertThrows(IllegalArgumentException.class, () -> nil().drop(-1));
    }


  @Test
  void reverse() {
    assertEquals(List.make(5), List.make(5).reverse());
    assertEquals(List.make(5, 4, 3, 2, 1), List.make(1, 2, 3, 4, 5).reverse());
  }

  @Test
  void getAt() {
    var list = List.make(1, 2, 3, 4, 5);
    for (int i = 0; i < 5; i++) assertEquals(i + 1, list.getAt(i));
    assertThrows(IllegalArgumentException.class, () -> list.getAt(-1));
    assertThrows(NoSuchElementException.class, () -> list.getAt(50));
  }

    @Test
    void take1() {
        var list = List.make(1, 2, 3, 4, 5);
        assertEquals(nil(), list.take(0));
        assertEquals(List.make(1), list.take(1));
        assertEquals(List.make(1, 2), list.take(2));
        assertEquals(List.make(1, 2, 3), list.take(3));
        assertEquals(List.make(1, 2, 3, 4), list.take(4));
    }

    @Test
    void take2() {
        var list = List.make(1, 2, 3, 4, 5);
        assertEquals(list, list.take(5));
        assertEquals(list, list.take(50));
        assertThrows(IllegalArgumentException.class, () -> list.take(-1));
    }



  @Test
  void join() {
    var list = List.make(1, 2, 3, 4, 5);
    assertEquals(List.make(1, 2, 3, 4, 5, 6, 7), list.join(List.make(6, 7)));
    assertEquals(list, list.join(nil()));
    assertEquals(list, List.<Integer>nil().join(list));
  }

  @Test
  void append() {
    assertEquals(List.make(1, 2, 3), List.make(1, 2).append(3));
    assertEquals(List.make(1), nil().append(1));
  }

  @Test
  void last() {
    assertEquals(3, List.make(1, 2, 3).last());
    assertEquals(42, List.make(42).last());
  }
}
