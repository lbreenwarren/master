import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import set.DisjointSet;

public class DisjointSetTest {

    @Test
    public void DSTest0() {
        DisjointSet<Integer> s = new DisjointSet<Integer>();

        //TO IMPLEMENT - DISJOINT SET USAGE

        assertEquals(s.findSet(1), s.findSet(6));
        assertNotEquals(s.findSet(3), s.findSet(6));
        assertEquals(s.findSet(1), s.findSet(1));
        assertEquals(s.findSet(3), s.findSet(5));
        assertNotEquals(s.findSet(3), s.findSet(9));
    }

    @Test
    public void DSTest1() {
        DisjointSet<Integer> s = new DisjointSet<Integer>();

        s.createSet(1);
        s.createSet(2);
        s.createSet(3);
        s.createSet(4);
        s.createSet(5);
        s.createSet(6);
        s.createSet(7);
        s.createSet(8);
        s.createSet(9);
        s.createSet(10);
        s.createSet(11);
        s.createSet(12);
        s.createSet(13);
        s.createSet(14);
        s.createSet(15);
        s.createSet(16);
        s.createSet(17);
        s.createSet(18);
        s.createSet(19);
        s.createSet(20);

        s.unionSets(3, 5);
        s.unionSets(1, 5);
        s.unionSets(20, 5);
        s.unionSets(1, 20);
        s.unionSets(3, 17);
        s.unionSets(19, 9);
        s.unionSets(19, 1);
        s.unionSets(17, 12);

        assertEquals(s.findSet(1), s.findSet(3));
        assertEquals(s.findSet(1), s.findSet(17));
        assertEquals(s.findSet(1), s.findSet(1));
        assertEquals(s.findSet(1), s.findSet(20));
        assertEquals(s.findSet(1), s.findSet(9));
        assertEquals(s.findSet(1), s.findSet(19));
        assertEquals(s.findSet(1), s.findSet(9));
    }

    @Test
    public void DSTest2() {
        DisjointSet<Integer> s = new DisjointSet<Integer>();

        s.createSet(1);
        s.createSet(2);
        s.createSet(3);
        s.createSet(4);
        s.createSet(5);
        s.createSet(6);
        s.createSet(7);
        s.createSet(8);
        s.createSet(9);
        s.createSet(10);
        s.createSet(11);
        s.createSet(12);
        s.createSet(13);
        s.createSet(14);
        s.createSet(15);
        s.createSet(16);
        s.createSet(17);
        s.createSet(18);
        s.createSet(19);
        s.createSet(20);

        s.unionSets(3, 5);
        s.unionSets(1, 5);
        s.unionSets(20, 5);
        s.unionSets(1, 20);
        s.unionSets(3, 17);
        s.unionSets(19, 9);
        s.unionSets(19, 1);
        s.unionSets(17, 12);
        s.unionSets(13, 11);
        s.unionSets(6, 11);
        s.unionSets(6, 13);
        s.unionSets(6, 18);
        s.unionSets(15, 18);

        assertNotEquals(s.findSet(1), s.findSet(6));
        assertNotEquals(s.findSet(3), s.findSet(6));
        assertNotEquals(s.findSet(9), s.findSet(11));
        assertNotEquals(s.findSet(17), s.findSet(13));
        assertNotEquals(s.findSet(5), s.findSet(18));
    }

    @Test
    public void DSTest3() {
        DisjointSet<Integer> s = new DisjointSet<Integer>();

        s.createSet(1);
        s.createSet(2);
        s.createSet(3);
        s.createSet(4);
        s.createSet(5);
        s.createSet(6);
        s.createSet(7);
        s.createSet(8);
        s.createSet(9);
        s.createSet(10);
        s.createSet(11);
        s.createSet(12);
        s.createSet(13);
        s.createSet(14);
        s.createSet(15);
        s.createSet(16);
        s.createSet(17);
        s.createSet(18);
        s.createSet(19);
        s.createSet(20);

        s.unionSets(3, 5);
        s.unionSets(1, 5);
        s.unionSets(20, 5);
        s.unionSets(1, 20);
        s.unionSets(3, 17);
        s.unionSets(19, 9);
        s.unionSets(19, 1);
        s.unionSets(17, 12);

        assertEquals(s.findSet(1), s.findSet(3));
        assertEquals(s.findSet(1), s.findSet(17));
        assertEquals(s.findSet(1), s.findSet(1));
        assertEquals(s.findSet(1), s.findSet(20));
        assertEquals(s.findSet(1), s.findSet(9));
        assertEquals(s.findSet(1), s.findSet(19));
        assertEquals(s.findSet(1), s.findSet(9));
    }

    @Test
    public void DSTest4() {
        DisjointSet<String> s = new DisjointSet<String>();

        s.createSet("1");
        s.createSet("2");
        s.createSet("3");
        s.createSet("4");
        s.createSet("5");
        s.createSet("6");
        s.createSet("7");
        s.createSet("8");
        s.createSet("9");
        s.createSet("10");
        s.createSet("11");
        s.createSet("12");
        s.createSet("13");
        s.createSet("14");
        s.createSet("15");
        s.createSet("16");
        s.createSet("17");
        s.createSet("18");
        s.createSet("19");
        s.createSet("20");

        s.unionSets("3", "5");
        s.unionSets("1", "5");
        s.unionSets("20", "5");
        s.unionSets("1", "20");
        s.unionSets("3", "17");
        s.unionSets("19", "9");
        s.unionSets("19", "1");
        s.unionSets("17", "12");

        assertEquals(s.findSet("1"), s.findSet("3"));
        assertEquals(s.findSet("1"), s.findSet("17"));
        assertEquals(s.findSet("1"), s.findSet("1"));
        assertEquals(s.findSet("1"), s.findSet("20"));
        assertEquals(s.findSet("1"), s.findSet("9"));
        assertEquals(s.findSet("1"), s.findSet("19"));
        assertEquals(s.findSet("1"), s.findSet("9"));
        assertNotEquals(s.findSet("1"), s.findSet("2"));
        assertNotEquals(s.findSet("1"), s.findSet("13"));
    }
}
