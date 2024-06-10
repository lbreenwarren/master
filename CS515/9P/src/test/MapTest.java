import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import java.util.Iterator;
import java.util.Map.Entry;
import java.util.NoSuchElementException;

/**
 * New tests for Java set using Java iterator and set methods.
 */
@TestMethodOrder(MethodOrderer.MethodName.class)
public class MapTest {

    @Test
    void testMap00() throws IOException {

        StringBuilder actual = new StringBuilder();

        Map<String, String> m1 = new Map<>();

        actual.append("\n\nMap test 1\n\n");

        assertTrue( m1.insert("cute", "boy"));
        assertTrue( m1.insert("cool", "breeze"));
        assertTrue( m1.insert("warm", "fire"));
        assertTrue( m1.insert("red", "rose"));
        assertFalse( m1.insert("cool", "ice"));

        actual.append(m1.toString() + "\n");

        Iterator<Entry<String, String>> iter = m1.iterator();

        while (iter.hasNext()){
            actual.append(iter.next().getKey() + " ");
        }
        System.out.println(actual);
        actual.append("\n\nMap test 2\n\n");

        Map<Integer, Integer> m2 = new Map<>();
        
        m2.insert(29, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(34, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(39, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(23, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(87, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(45, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(83, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(22, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(12, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(57, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(21, 1);
        System.out.println("===================================================");
        System.out.println(m2);
        System.out.println("===================================================");
        m2.insert(30, 1);
        m2.insert(2, 1);
        m2.insert(26, 1);
        m2.insert(5, 1);
        m2.insert(11, 1);
        m2.insert(32, 1);
        m2.insert(86, 1);

        actual.append(m2.toString() +  "\n\n\n\n\n");

        m2.replace(29, 2);
        m2.replace(34, 2);
        m2.replace(39, 2);
        m2.replace(23, 2);
        m2.replace(87, 2);
        m2.replace(45, 2);
        m2.replace(83, 2);
        m2.replace(22, 2);
        m2.replace(12, 2);
        m2.replace(57, 2);
        m2.replace(21, 2);
        m2.replace(30, 2);
        m2.replace(2, 2);
        m2.replace(26, 2);
        m2.replace(5, 2);
        m2.replace(11, 2);
        m2.replace(32, 2);
        m2.replace(86, 2);

        actual.append(m2.toString() + "\n\n");

        Iterator<Entry<Integer, Integer>> iter2 = m2.iterator();


        while (iter2.hasNext()){
            actual.append(iter2.next().getKey() + " ");
        }
        actual.append("\n");

        System.out.println(actual.toString());

        InputStream is = MapTest.class.getResourceAsStream("/out0");
        String expected = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        assertEquals(expected, actual.toString());
    }
}
