import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class SetTest {

    @Test
    void testSet1() {

        Set t = new Set();

        // test find and insert
        assertFalse(t.find("hello"));
        assertTrue(t.insert("hello"));
        assertTrue(t.find("hello"));
        assertEquals(1, t.size());

        assertFalse(t.insert("hello"));
        assertTrue(t.find("hello"));
        assertEquals(1, t.size());

        assertFalse(t.find("heo"));
        assertTrue(t.insert("heo"));
        assertTrue(t.find("heo"));
        assertEquals(2, t.size());

        assertFalse(t.find("helyo"));
        assertTrue(t.insert("helyo"));
        assertTrue(t.find("helyo"));
        assertEquals(3, t.size());

        assertFalse(t.find("ahe"));
        assertTrue(t.insert("ahe"));
        assertTrue(t.find("ahe"));
        assertEquals(4, t.size());

        assertFalse(t.find("helyooo"));
        assertTrue(t.insert("helyooo"));
        assertTrue(t.find("helyooo"));
        assertEquals(5, t.size());

        // test erase
        assertTrue(t.erase("hello"));
        assertFalse(t.find("hello"));
        assertEquals(4, t.size());

        assertFalse(t.erase("hell"));
        assertFalse(t.find("hell"));
        assertEquals(4, t.size());

        assertFalse(t.erase("abc"));
        assertFalse(t.find("abc"));
        assertEquals(4, t.size());

        assertTrue(t.erase("heo"));
        assertFalse(t.find("heo"));
        assertEquals(3, t.size());

        assertFalse(t.erase("he"));
        assertFalse(t.find("he"));
        assertEquals(3, t.size());

        assertTrue(t.erase("helyo"));
        assertFalse(t.find("helyo"));
        assertEquals(2, t.size());
        assertFalse(t.erase("helyo"));
        assertEquals(2, t.size());
    }

    @Test
    void testSet2() {

        Set t = new Set();

        // test find and insert
        assertFalse(t.find("pineapple"));
        assertTrue(t.insert("pineapple"));
        assertTrue(t.find("pineapple"));
        assertEquals(1, t.size());

        assertFalse(t.find("pineapples"));
        assertTrue(t.insert("pineapples"));
        assertTrue(t.find("pineapples"));
        assertEquals(2, t.size());

        assertFalse(t.find("pineapp"));
        assertTrue(t.insert("pineapp"));
        assertTrue(t.find("pineapp"));
        assertEquals(3, t.size());

        assertFalse(t.find("pine"));
        assertTrue(t.insert("pine"));
        assertTrue(t.find("pine"));
        assertEquals(4, t.size());

        assertFalse(t.find("p"));
        assertTrue(t.insert("p"));
        assertTrue(t.find("p"));
        assertEquals(5, t.size());

        assertFalse(t.find("pan"));
        assertTrue(t.insert("pan"));
        assertTrue(t.find("pan"));
        assertEquals(6, t.size());

        // test erase
        assertFalse(t.erase("pin"));
        assertFalse(t.find("pin"));
        assertEquals(6, t.size());

        assertFalse(t.erase("pi"));
        assertFalse(t.find("pi"));
        assertEquals(6, t.size());

        assertTrue(t.erase("p"));
        assertFalse(t.find("p"));
        assertEquals(5, t.size());

        assertTrue(t.find("pan"));
        assertTrue(t.erase("pan"));
        assertFalse(t.find("pan"));
        assertEquals(4, t.size());

        assertFalse(t.find("pan"));
        assertFalse(t.erase("pan"));
        assertFalse(t.find("pan"));
        assertEquals(4, t.size());

        assertTrue(t.find("pine"));
        assertTrue(t.erase("pine"));
        assertFalse(t.find("pine"));
        assertEquals(3, t.size());

        assertTrue(t.find("pineapp"));
        assertTrue(t.erase("pineapp"));
        assertFalse(t.find("pineapp"));
        assertEquals(2, t.size());

        assertTrue(t.find("pineapples"));
        assertTrue(t.erase("pineapples"));
        assertFalse(t.find("pineapples"));
        assertEquals(1, t.size());

        assertTrue(t.find("pineapple"));
        assertTrue(t.erase("pineapple"));
        assertFalse(t.find("pineapple"));
        assertEquals(0, t.size());

        assertFalse(t.find("pineapple"));
        assertFalse(t.find("pineapples"));
        assertFalse(t.find("p"));
        assertFalse(t.erase("pineapple"));
        assertFalse(t.erase("pineapples"));
        assertFalse(t.erase("p"));
        assertEquals(0, t.size());
    }
}
