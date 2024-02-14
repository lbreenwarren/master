package funlist;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;

import static funlist.List.nil;
import static org.junit.jupiter.api.Assertions.*;

class NilTests {

    @Test
    void testToString() {
        assertEquals("List()", nil().toString());
    }

    @Test
    void testEquals() {
        var list = List.make(1, 2, 3, 4, 5);
        var list2 = List.make(1, 2, 3, 4, 5);
        assertEquals(list, list2);
        assertNotEquals(list, nil());
    }

    @Test
    void head() {
        assertThrows(NoSuchElementException.class, nil()::head);
    }

    @Test
    void tail() {
        assertThrows(NoSuchElementException.class, nil()::tail);
    }

    @Test
    void isEmpty() {
        assertTrue(nil().isEmpty());
        assertFalse(nil().nonEmpty());

    }

    @Test
    void length() {
        assertEquals(0, nil().length());
    }

    @Test
    void reverse() {
        assertEquals(nil(), nil().reverse());
    }

    @Test
    void getAt() {
        assertThrows(IllegalArgumentException.class, () -> nil().getAt(-1));
        assertThrows(NoSuchElementException.class, () -> nil().getAt(0));
    }

    @Test
    void take() {
        assertThrows(IllegalArgumentException.class, () -> nil().take(-1));
    }

    @Test
    void last() {
        assertThrows(NoSuchElementException.class, () -> nil().last());
    }
}
