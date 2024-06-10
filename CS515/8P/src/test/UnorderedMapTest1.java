import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.HashMap;
public class UnorderedMapTest1 {

    @Test
    public void testPutAndGet() {
        UnorderedMap<String, Integer> map = new UnorderedMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        map.put("key3", 3);

        assertEquals(Integer.valueOf(1), map.get("key1"));
        assertEquals(Integer.valueOf(2), map.get("key2"));
        assertEquals(Integer.valueOf(3), map.get("key3"));
    }

    @Test
    public void testContainsKey() {
        UnorderedMap<String, Integer> map = new UnorderedMap<>();
        map.put("key1", 1);
        map.put("key2", 2);

        assertTrue(map.containsKey("key1"));
        assertFalse(map.containsKey("key3"));
    }

    @Test
    public void testSize() {
        UnorderedMap<String, Integer> map = new UnorderedMap<>();
        map.put("key1", 1);
        map.put("key2", 2);

        assertEquals(2, map.size());
    }

    @Test
    public void testReplace() {
        UnorderedMap<String, Integer> map = new UnorderedMap<>();
        map.put("key1", 1);
        map.put("key2", 2);

        assertEquals(Integer.valueOf(1), map.replace("key1", 3));
        assertEquals(Integer.valueOf(3), map.get("key1"));
        assertNull(map.replace("key3", 3));
    }

    @Test
    public void testErase() {
        UnorderedMap<String, Integer> map = new UnorderedMap<>();
        map.put("key1", 1);
        map.put("key2", 2);

        assertTrue(map.erase("key1"));
        assertFalse(map.erase("key3"));
        assertNull(map.get("key1"));
        assertEquals(1, map.size());
    }

    @Test
    public void testForEach() {
        UnorderedMap<String, Integer> map = new UnorderedMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        map.put("key3", 3);

        StringBuilder result = new StringBuilder();
        map.forEach((key, value) -> result.append(key).append(value));

        assertEquals("key11key22key33", result.toString());
    }

    @Test
    public void testEquals() {
        UnorderedMap<String, Integer> map = new UnorderedMap<>();
        map.put("key1", 1);
        map.put("key2", 2);
        map.put("key3", 3);

        UnorderedMap<String, Integer>.Iterator it1 = map.iterator();
        UnorderedMap<String, Integer>.Iterator it2 = map.iterator();

        assertTrue(it1.equals(it2));

        // Advance one iterator
        it1.next();

        assertFalse(it1.equals(it2));

        // Reset iterator
        it1 = map.iterator();
        it2 = map.iterator();

        assertTrue(it1.equals(it2));

        // Advance both iterators
        it1.next();
        it2.next();

        assertTrue(it1.equals(it2));
    }
}
