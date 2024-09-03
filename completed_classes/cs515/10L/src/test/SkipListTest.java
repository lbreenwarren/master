import SkipList.SkipList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.extension.ExtendWith;

import org.junit.jupiter.api.Test;

public class SkipListTest {

    @Test
    public void skipTest0() {
        SkipList s = new SkipList(4, 0.5);

        System.out.println("Test insert");
        assertTrue(s.insert(100));
        assertTrue(s.insert(150));
        assertTrue(s.insert(200));
        assertTrue(s.insert(240));
        assertTrue(s.insert(250));
        System.out.println(s.toString());
        assertTrue(s.insert(255));
        assertTrue(s.insert(265));
        assertTrue(s.insert(275));
        assertTrue(s.insert(299));
        assertTrue(s.insert(520));

        System.out.println("Test toString");
        System.out.println(s.toString());
        assertEquals(
                     "100    \n150    \n200    \n240    \n250    250    250    250    \n255    \n265    \n275    \n299    \n520    520    520    520",
                     s.toString().trim());

        System.out.println("Test find");
        assertTrue(s.find(100));
        assertTrue(s.find(150));
        assertTrue(s.find(200));
        assertTrue(s.find(250));
        assertTrue(s.find(520));
        assertTrue(s.find(240));
        assertTrue(s.find(255));
        assertTrue(s.find(275));
        assertTrue(s.find(265));
        assertTrue(s.find(299));
        assertFalse(s.find(233));
        assertFalse(s.find(277));
        assertFalse(s.find(291));

        System.out.println("Test erase");

        assertTrue(s.erase(100));
        assert (s.find(100) == false);
        assertTrue(s.erase(265));
        assert (s.find(265) == false);
        assertEquals(
                     "150    \n200    \n240    \n250    250    250    250    \n255    \n275    \n299    \n520    520    520    520",
                     s.toString().trim());

        System.out.println("Test copy constructor");

        SkipList newList = new SkipList(s);

        assertEquals(
                     "150    \n200    \n240    \n250    250    250    250    \n255    \n275    \n299    \n520    520    520    520",
                     newList.toString().trim());
        assertEquals(
                     "150    \n200    \n240    \n250    250    250    250    \n255    \n275    \n299    \n520    520    520    520",
                     s.toString().trim());
        assertEquals(newList.toString(), s.toString());

        System.out.println("Modifying original list");

        s.insert(218);
        s.erase(250);
        s.erase(275);
        assertEquals(
                     "150    \n200    \n240    \n250    250    250    250    \n255    \n275    \n299    \n520    520    520    520",
                     newList.toString().trim());
        assertEquals("150    \n200    \n218    218    \n240    \n255    \n299    \n520    520    520    520",
                     s.toString().trim());
        assertNotEquals(newList.toString(), s.toString());
    }

    @Test
    public void skipTest1() {
        SkipList s = new SkipList(4, 0.5);

        System.out.println("Test insert");
        assertTrue(s.insert(100));
        assertTrue(s.insert(150));
        assertTrue(s.insert(200));
        assertTrue(s.insert(240));
        assertTrue(s.insert(250));
        assertTrue(s.insert(255));
        assertTrue(s.insert(265));
        assertTrue(s.insert(275));
        assertTrue(s.insert(299));
        assertTrue(s.insert(520));

        System.out.println("Test find");
        assertTrue(s.find(100));
        assertTrue(s.find(150));
        assertTrue(s.find(200));
        assertTrue(s.find(250));
        assertTrue(s.find(520));
        assertTrue(s.find(240));
        assertTrue(s.find(255));
        assertTrue(s.find(275));
        assertTrue(s.find(265));
        assertTrue(s.find(299));
        assertFalse(s.find(233));
        assertFalse(s.find(277));
        assertFalse(s.find(291));

        System.out.println("Test to string");
        assertEquals(
                     "100    \n150    \n200    \n240    \n250    250    250    250    \n255    \n265    \n275    \n299    \n520    520    520    520",
                     s.toString().trim());
    }

    @Test
    public void skipTest2() {
        SkipList s = new SkipList(4, 0.5);

        assertTrue(s.insert(10));
        assertTrue(s.insert(75));
        assertTrue(s.insert(20));
        assertTrue(s.insert(64));
        assertTrue(s.insert(25));
        assertTrue(s.insert(23));
        assertTrue(s.insert(46));
        assertTrue(s.insert(27));
        assertTrue(s.insert(35));
        assertTrue(s.insert(52));

        assertTrue(s.erase(25));
        assert (s.find(25) == false);

        assertTrue(s.erase(10));
        assert (s.find(10) == false);

        assertTrue(s.erase(64));
        assert (s.find(64) == false);

        assertTrue(s.erase(35));
        assert (s.find(35) == false);

        assertFalse(s.erase(35));
        assert (s.find(35) == false);

        assertFalse(s.erase(100));
        assert (s.find(100) == false);
    }

    @Test
    public void skipTest3() {
        SkipList s = new SkipList(5, 0.5);

        assertFalse(s.erase(25));
        assert (s.find(25) == false);

        assertTrue(s.insert(10));
        assertTrue(s.insert(75));
        assertTrue(s.insert(25));
        assertTrue(s.insert(23));
        assertTrue(s.insert(46));
        assertTrue(s.insert(27));
        assertTrue(s.insert(35));
        assertTrue(s.insert(52));
        assertTrue(s.insert(20));
        assertTrue(s.insert(64));
        assertTrue(s.insert(2));
        assertTrue(s.insert(3));
        assertTrue(s.insert(4));
        assertTrue(s.insert(7));
        assertTrue(s.insert(125));
        assertTrue(s.insert(123));
        assertTrue(s.insert(146));
        assertTrue(s.insert(127));
        assertTrue(s.insert(135));
        assertTrue(s.insert(152));
        assertTrue(s.insert(1));
        assertFalse(s.insert(7));
        assertTrue(s.insert(5));
        assertTrue(s.insert(6));

        s.erase(25);
        assertFalse(s.find(25));

        s.erase(10);
        assertFalse(s.find(10));

        s.erase(64);
        assertFalse(s.find(64));

        s.erase(35);
        assertFalse(s.find(35));

        s.erase(35);
        assertFalse(s.find(35));

        s.erase(100);
        assertFalse(s.find(100));

        s.erase(3);
        assertFalse(s.find(3));

        s.erase(125);
        assertFalse(s.find(125));

        s.erase(123);
        assertFalse(s.find(123));

        s.erase(7);
        assertFalse(s.find(7));

        s.erase(75);
        assertFalse(s.find(75));

        s.erase(25);
        assertFalse(s.find(25));

        s.erase(23);
        assertFalse(s.find(23));

        s.erase(46);
        assertFalse(s.find(46));

        s.erase(27);
        assertFalse(s.find(27));

        s.erase(35);
        assertFalse(s.find(35));

        s.erase(52);
        assertFalse(s.find(52));

        s.erase(20);
        assertFalse(s.find(20));

        s.erase(64);
        assertFalse(s.find(64));

        s.erase(2);
        assertFalse(s.find(2));

        s.erase(3);
        assertFalse(s.find(3));

        s.erase(4);
        assertFalse(s.find(4));

        s.erase(7);
        assertFalse(s.find(7));

        s.erase(146);
        assertFalse(s.find(146));

        s.erase(127);
        assertFalse(s.find(127));

        s.erase(135);
        assertFalse(s.find(135));

        s.erase(152);
        assertFalse(s.find(152));

        s.erase(1);
        assertFalse(s.find(1));

        s.erase(5);
        assertFalse(s.find(5));

        s.erase(6);
        assertFalse(s.find(6));

        s.erase(101);
        assertFalse(s.find(101));

        s.erase(Integer.MAX_VALUE - 1);
        assertFalse(s.find(Integer.MAX_VALUE - 1));

        s.erase(Integer.MIN_VALUE + 1);
        assertFalse(s.find(Integer.MIN_VALUE + 1));
    }

    @Test
    public void skipTest4() {
        SkipList s = new SkipList(4, 0.5);

        assertTrue(s.insert(10));
        assertTrue(s.insert(75));
        assertTrue(s.insert(20));
        assertTrue(s.insert(64));
        assertTrue(s.insert(25));
        assertTrue(s.insert(23));
        assertTrue(s.insert(46));
        assertTrue(s.insert(27));
        assertTrue(s.insert(35));
        assertTrue(s.insert(52));

        System.out.println("Test copy constructor");
        SkipList newList = new SkipList(s);
        assertEquals(newList.toString(), s.toString());

        s.insert(21);
        s.insert(70);
        s.erase(27);
        s.erase(75);
        assertEquals(
                     "10    \n20    \n23    \n25    25    25    25    \n27    \n35    \n46    \n52    52    52    52    \n64    \n75",
                     newList.toString().trim());
        assertNotEquals(s.toString(), newList.toString());

        System.out.println("Test assignment");
        SkipList another = new SkipList(6, 0.4);
        another.insert(100);
        another.insert(15);
        another.insert(112);
        assertEquals("15    \n100    \n112", another.toString().trim());
        another = newList;
        // Test assignment
        assertEquals(another.toString(), newList.toString());
    }

}
