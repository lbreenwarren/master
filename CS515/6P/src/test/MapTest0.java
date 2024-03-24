import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


import java.util.Iterator;
import java.util.NoSuchElementException;

public class MapTest0 {

    @Test
    void testMap0() {

        Map<String, String> m1 = new Map<>();
        System.out.println("\n\nMap test 1\n\n");

        assertTrue( m1.insert("cute", "boy"));
        assertTrue( m1.insert("cool", "breeze"));
        assertTrue( m1.insert("warm", "fire"));
        assertTrue( m1.insert("red", "rose"));
        assertFalse( m1.insert("cool", "ice"));

        System.out.println(m1);

        Iterator iter = m1.keyIterator();

        while (iter.hasNext()){
            System.out.print(iter.next() + " ");
        }
        System.out.println();

        assertThrows(NoSuchElementException.class, () -> { iter.next(); });

        System.out.println("\n\nMap test 2\n\n");
        Map<Integer, Integer> m2 = new Map<>();
        m2.insert(29, 1);
        m2.insert(34, 1);
        m2.insert(39, 1);
        m2.insert(23, 1);
        m2.insert(87, 1);
        m2.insert(45, 1);
        m2.insert(83, 1);
        m2.insert(22, 1);
        m2.insert(12, 1);
        m2.insert(57, 1);
        m2.insert(21, 1);
        m2.insert(30, 1);
        m2.insert(2, 1);
        m2.insert(26, 1);
        m2.insert(5, 1);
        m2.insert(11, 1);
        m2.insert(32, 1);
        m2.insert(86, 1);


        System.out.print(m2 + "\n\n\n\n\n");

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

        System.out.print(m2 + "\n\n");

        Iterator it2 = m2.keyIterator();

        while (it2.hasNext()){
            System.out.print(it2.next() + " ");
        }
        System.out.println();
    }
}
