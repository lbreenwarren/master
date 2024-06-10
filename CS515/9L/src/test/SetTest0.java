import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;


public class SetTest0 {

    @Test
    void testSet() {

        Set t = new Set();

        // test find and insert
        assertFalse(t.find("hello"));
        assertTrue(t.insert("hello"));
        assertTrue(t.find("hello"));
        assertEquals(1, t.size());

        assertFalse(t.insert("hello"));
        assertTrue(t.find("hello"));
        assertEquals(1, t.size());

        // test erase
        assertTrue(t.erase("hello"));
        assertFalse(t.find("hello"));
        assertEquals(0, t.size());

        assertFalse(t.erase("hell"));
        assertFalse(t.find("hell"));
        assertEquals(0, t.size());
    }
}
