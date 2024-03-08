import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;


public class MapTest0 {

    @Test
    void testMap0() {

        Map m1 = new Map();
        assertTrue(m1.insert("a", "boy"));
        System.out.println(m1);
        System.out.println("+++++++++++++++++++++++++++++\n");

        assertTrue( m1.insert("b", "breeze"));
        System.out.println(m1);
        System.out.println("+++++++++++++++++++++++++++++\n");

        assertTrue( m1.insert("r", "fire"));
        System.out.println(m1);
        System.out.println("+++++++++++++++++++++++++++++\n");

        assertTrue( m1.insert("d", "rose"));
        System.out.println(m1);
        System.out.println("+++++++++++++++++++++++++++++\n");

        assertTrue( m1.insert("c", "ice"));
        System.out.println(m1);
        assertFalse( m1.insert("c", "car"));


        assertTrue(m1.insert("new", "element"));
        System.out.println(m1);
        System.out.println("+++++++++++++++++++++++++++++\n");

        assertTrue(m1.insert("cool", "ice"));
        System.out.println(m1);
        System.out.println("+++++++++++++++++++++++++++++\n");

        m1.erase("cool");
        System.out.println(m1);
        System.out.println("+++++++++++++++++++++++++++++\n");

        Map m2 = new Map(m1);
        m2.erase("a");
        System.out.println(m1);
        System.out.println("+++++++++++++++++++++++++++++\n");
        System.out.println(m2);;

    }
}





/*

 */
