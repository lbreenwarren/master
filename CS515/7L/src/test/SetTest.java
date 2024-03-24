import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import set.Set;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicReference;

public class SetTest {
    @Test
    public void constructor_SetCreated_CreatesZeroSizeSet() {
        Set<Integer> testSet = new Set<Integer>();
        assertEquals(0, testSet.size());
    }

    @Test
    public void insert_OnEmptySet_HasCorrectSize() {
        Set<Integer> testSet = new Set<Integer>();
        testSet.insert(1);
        testSet.insert(2);
        testSet.insert(3);
        assertEquals(3, testSet.size());
    }

    @Test
    public void insert_SameKeyAgain_SizeRemainsSame() {
        Set<Integer> testSet = new Set<Integer>();
        testSet.insert(1);
        testSet.insert(2);
        testSet.insert(3);
        assertEquals(3, testSet.size());
        testSet.insert(3);
        assertEquals(3, testSet.size());
    }

    @Test 
    public void insert_OnMissingItem_ReturnsTrue() {
        Set<Integer> testSet =  new Set<Integer>();
        assertTrue(testSet.insert(1));
    }

    @Test 
    public void insert_OnExistingItem_ReturnsFalse() {
        Set<Integer> testSet =  new Set<Integer>();
        testSet.insert(1);
        testSet.insert(2);
        testSet.insert(3);
        assertFalse(testSet.insert(1));
    }

    @Test
    public void insert_manyItems_ReturnsCorrectSize() {
        Set<Integer> testSet =  new Set<Integer>();
        for (int i = 1; i <= 1000; i++) {
            testSet.insert(i);
            testSet.insert(-i);
        }
        assertEquals(2000, testSet.size());
    }

    @Test
    public void height_onEmptyTree_Returns0() {
        Set<Integer> testSet =  new Set<Integer>();
        assertEquals(0, testSet.height());
    }

    @Test
    public void height_onNonEmptyTree_ReturnsCorrectValue() {
        Set<Integer> testSet =  new Set<Integer>();
        testSet.insert(1);
        testSet.insert(2);
        testSet.insert(3);
        assertEquals(3, testSet.height());
    }

    @Test
    public void height_onQasiBalancedTree_ReturnsCorrectValue() {
        Set<Integer> testSet =  new Set<Integer>();
        testSet.insert(20);
        testSet.insert(17);
        testSet.insert(25);
        testSet.insert(13);
        testSet.insert(19);
        testSet.insert(23);
        testSet.insert(22);
        testSet.insert(33);
        testSet.insert(10);
        testSet.insert(14);
        testSet.insert(18);
        testSet.insert(21);
        
        testSet.insert(30);
        testSet.insert(45);
        testSet.dump();
        assertEquals(5, testSet.height());
    }

    @Test
    public void height_onLargeNonEmptyTree_ReturnsCorrectValue() {
        Set<Integer> testSet =  new Set<Integer>();
        for (int i = 0; i < 1000; i++) {
            testSet.insert(i);
            testSet.insert(-i);
        }
        testSet.insert(-1001);
        assertEquals(1001, testSet.height());
    }

    Set<Integer> createTestSet1() {
        Set<Integer> testSet =  new Set<Integer>();
        testSet.insert(14);
        testSet.insert(9);
        testSet.insert(3);
        testSet.insert(19);
        testSet.insert(21);
        testSet.insert(33);
        testSet.insert(35);
        testSet.insert(17);
        return testSet;
    }

    @Test
    public void setTest1DepthFirst() {
        Set<Integer> testSet =  createTestSet1();

        assert(testSet.size()==8);
        testSet.dump();

        System.out.println("Depth First In Order Traverse: ");
        System.out.println(testSet.depthFirstInOrder());
        assertEquals("3 9 14 17 19 21 33 35", testSet.depthFirstInOrder().trim());
    }

    @Test
    public void setTest1ForEach() {
        Set<Integer> testSet =  createTestSet1();

        System.out.println("Depth First In Order Traverse using forEach: ");
        StringBuilder sb = new StringBuilder();
        testSet.forEach(v -> sb.append(v + " "));
        String result = sb.toString();
        System.out.println(result);
        assertEquals("3 9 14 17 19 21 33 35", result.trim());
    }

    @Test
    public void setTest1BreadthFirst() {
        Set<Integer> testSet =  createTestSet1();

        assert(testSet.size()==8);
        testSet.dump();

        System.out.println("\nBreadth First Traverse: ");
        System.out.println(testSet.breadthFirst());
        assertEquals("14 9 19 3 17 21 33 35", testSet.breadthFirst().trim());
    }

    Set<Integer> createTestSet2() {
        Set<Integer> s1 =  new Set<Integer>();
        s1.insert(11);
        s1.insert(13);
        s1.insert(2);
        s1.insert(5);
        s1.insert(7);
        s1.insert(12);
        s1.insert(4);
        s1.insert(18);
        s1.insert(21);
        s1.insert(14);
        s1.insert(6);
        s1.insert(19);
        s1.insert(22);
        s1.insert(15);
        s1.insert(0);
        s1.insert(29);
        s1.insert(23);
        return s1;
    }

    Map<Integer, Integer> createTestMap2() {
        Map<Integer, Integer> s1 =  new TreeMap<>();
        int i = 0;
        s1.put(i++, 0);
        s1.put(i++, 2);
        s1.put(i++, 4);
        s1.put(i++, 5);
        s1.put(i++, 6);
        s1.put(i++, 7);
        s1.put(i++, 11);
        s1.put(i++, 12);
        s1.put(i++, 13);
        s1.put(i++, 14);
        s1.put(i++, 15);
        s1.put(i++, 18);
        s1.put(i++, 19);
        s1.put(i++, 21);
        s1.put(i++, 22);
        s1.put(i++, 23);
        s1.put(i, 29);
        return s1;
    }

    @Test
    public void setTest2DepthFirst() {
        Set<Integer> s1 =  createTestSet2();

        assertEquals(17, s1.size());
        s1.dump();

        System.out.println("Depth First In Order Traverse: ");
        System.out.println(s1.depthFirstInOrder());
        assertEquals("0 2 4 5 6 7 11 12 13 14 15 18 19 21 22 23 29", s1.depthFirstInOrder().trim());
    }

    @Test
    public void setTest2forEachSum() {
        Set<Integer> s1 =  createTestSet2();

        AtomicReference<Integer> accum = new AtomicReference<>(0);
        s1.forEach(val -> accum.updateAndGet(v -> v + val));
        System.out.println("Sum of values calculated using forEach: ");
        System.out.println(accum);
        assertEquals(221, accum.get());
    }

    @Test
    public void setTest2forEachMap() {
        Set<Integer> s1 =  createTestSet2();
        Map<Integer, Integer> m2 = createTestMap2();

        assertEquals(17, s1.size());

        Map<Integer, Integer> m = new TreeMap<>();
        AtomicReference<Integer> i = new AtomicReference<>(0);
        s1.forEach(val -> m.put(i.getAndSet(i.get() + 1), val));
        assertEquals(m2, m);
    }

    @Test
    public void setTest2BreadthFirst() {
        Set<Integer> s1 =  createTestSet2();

        assertEquals(17, s1.size());
        s1.dump();

        System.out.println("\nBreadth First Traverse: ");
        System.out.println(s1.breadthFirst());
        assertEquals("11 2 13 0 5 12 18 4 7 14 21 6 15 19 22 29 23", s1.breadthFirst().trim());
    }
}

