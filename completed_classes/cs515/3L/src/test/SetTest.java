import set.Set;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests originally written for c++ 5L set.
 * Modified here for Java compatability (and removed c++ specific tests).
 */
public class SetTest {

    // * * * * * * stackTest.cpp tests (previously Google Tests) * * * * * * * * 
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
    public void clear_OnNonEmptySet_ReducesSizeToZero() {
        Set<Integer> testSet = new Set<Integer>();
        testSet.insert(1);
        testSet.insert(2);
        testSet.insert(3);
        testSet.clear();
        assertEquals(0, testSet.size());
    }

    @Test 
    public void find_OnMissingItem_ReturnsFalse() {
        Set<Integer> testSet =  new Set<Integer>();
        testSet.insert(1);
        testSet.insert(2);
        testSet.insert(3);
        assertFalse(testSet.find(4));
    }

    @Test
    public void find_OnExisitingItem_ReturnsTrue(){
        Set<Integer> testSet = new Set<Integer>();
        testSet.insert(1);
        testSet.insert(2);
        testSet.insert(3);
        assertTrue(testSet.find(3));
    }

    @Test
    public void erase_OfExistingItem_DecrementsSizeAndCannotBeFound() {
        Set<Integer> testSet = new Set<Integer>();
        testSet.insert(1);
        testSet.insert(2);
        testSet.insert(3);
        testSet.erase(3);
        assertEquals(2, testSet.size());
        assertFalse(testSet.find(3)); 
    }

    @Test
    public void eraseAndInsert_MultipleTimes_GivesProperSize() {
        Set<Integer> testSet = new Set<Integer>();
        testSet.insert(1);
        testSet.insert(2);
        testSet.insert(3);
        testSet.erase(3);
        testSet.insert(2);
        testSet.insert(4);
        testSet.insert(6);
        assertEquals(4, testSet.size());
        testSet.erase(6);
        assertEquals(3, testSet.size());
        testSet.erase(6);
        assertEquals(3, testSet.size());
        testSet.erase(1);
        assertEquals(2, testSet.size());
    }

    @Test
    public void toString_AfterMultipleInsertErasePrintsProperly() {
        Set<Integer> testSet =  new Set<Integer>();
        testSet.insert(1);
        testSet.insert(2);
        testSet.insert(3);
        testSet.erase(3);
        testSet.insert(2);
        testSet.insert(4);
        testSet.insert(6);
        testSet.erase(6);
        testSet.erase(6);
        testSet.erase(1);
        System.out.println("Should have 2 and 4 remaining.");
        System.out.println(testSet.toString());
        assertEquals("2 4 ", testSet.toString());
    }
    
    @Test
    public void copyConstructor_GivenNonEmptySet_MakesCorrectSizeAndPrintsProperly() {
        Set<Integer> testSet = new Set<Integer>();
        testSet.insert(2);
        testSet.insert(4);

        Set<Integer> s2 = new Set<Integer>(testSet);
        assertEquals(2, s2.size());
        
        System.out.println("Should have 2 and 4.");
        System.out.println(testSet.toString());
        assertEquals("2 4 ", testSet.toString());
    }

    @Test
    public void copyConstructor_GivenNonEmptySet_MakesSeparateCopy() {
        Set<Integer> testSet = new Set<Integer>();
        testSet.insert(2);
        testSet.insert(4);

        Set<Integer> s2 = new Set<Integer>(testSet);
        testSet.clear();
        assertEquals(2, s2.size());
        assertEquals(0, testSet.size());
        assertFalse(testSet.find(2));
        assertFalse(testSet.find(4));
        assertTrue(s2.find(2));
        assertTrue(s2.find(4));
    }

    @Test
    public void insert_AfterCopy_MakesCorrectSizeChanges() {
        Set<Integer> testSet = new Set<Integer>();
        testSet.insert(2);
        testSet.insert(4);
    
        Set<Integer> s2 = new Set<Integer>(testSet);
        testSet.clear();
        testSet.insert(1);
        testSet.insert(9);
        testSet.insert(3);
        testSet.insert(10);
        testSet.insert(21);
        testSet.insert(3);
        testSet.insert(4);
        testSet.insert(21);
        testSet.insert(33);
        testSet.insert(21);
        testSet.insert(14);
        testSet.insert(9);
        testSet.insert(1);
        assertEquals(8, testSet.size());
        System.out.println("Should be 1 3 4 9 10 14 21 33.");
        System.out.println(testSet.toString());
        assertEquals("1 3 4 9 10 14 21 33 ", testSet.toString());
        assertEquals(2, s2.size());
    }

    // * * * * * * * * * * * * * Gauntlet-style tests * * * * * * * * * * * * * * * *
    @Test
    public void setTest1() {
        System.out.println("Start of set test 1.");

        Set<Integer> s1 = new Set<Integer>();
	    s1.insert(1);
	    s1.insert(2);
        s1.insert(3);
        assertEquals(3, s1.size());
        s1.insert(3);
        assertEquals(3, s1.size());

        assertFalse(s1.find(4));
        assertTrue(s1.find(3));
        s1.erase(3);
        assertEquals(2, s1.size());
        assertFalse(s1.find(3));

        s1.insert(2);
        s1.insert(4);
        s1.insert(6);
        assertEquals(4, s1.size());
        s1.erase(6);
        assertEquals(3, s1.size());
        s1.erase(6);
        assertEquals(3, s1.size());
        s1.erase(1);
        assertEquals(2, s1.size());
        assertEquals("2 4 ", s1.toString());
        System.out.println(s1);

        s1.clear();
        assertEquals(0, s1.size());
        assertFalse(s1.find(2));
        assertFalse(s1.find(4));

        s1.insert(1);
        s1.insert(9);
        s1.insert(3);
        s1.insert(10);
        s1.insert(21);
        s1.insert(3);
        s1.insert(4);
        s1.insert(21);
        s1.insert(33);
        s1.insert(21);
        s1.insert(14);
        s1.insert(9);
        s1.insert(1);
        assertEquals(8, s1.size());
        assertEquals("1 3 4 9 10 14 21 33 ", s1.toString());
        System.out.println(s1);

        s1.erase(1);
        s1.erase(9);
        s1.erase(3);
        s1.erase(10);
        s1.erase(21);
        s1.erase(3);
        s1.erase(4);
        s1.erase(21);
        s1.erase(33);
        s1.erase(21);
	    s1.erase(14);
	    s1.erase(9);
	    s1.erase(1);

	    assertEquals(0, s1.size());

        System.out.println(s1);
        assertEquals("", s1.toString());
    }

    @Test
    public void setTest2() {
        Set<Integer> s1 = new Set<Integer>();
        for (int i = 1; i < 10000; i++){
            s1.insert(i);
            s1.insert(i - 1);
        }
        assertEquals(10000, s1.size());

        for (int i = 1; i < 10000; i++){
            s1.erase(i);
            s1.erase(i - 1);
        }
        assertEquals(0, s1.size());
        System.out.println("done.");
    }

    @Test
    public void setTest3() {
        Set<Integer> s1 = new Set<Integer>();
        s1.insert(1);
        s1.insert(2);
        s1.insert(3);
        s1.insert(2);
        s1.insert(4);
        s1.insert(6);

        assertEquals(5, s1.size());

        // test copy constructor
        Set<Integer> s2 = new Set<Integer>(s1);
        assertEquals(5, s2.size());
        System.out.println(s1);
        System.out.println(s2);

        assert(s1.equals(s2));

        s1.clear();
        assertEquals(5, s2.size());

        s1.insert(1);
        s1.insert(9);
        s1.insert(3);
        s1.insert(10);
        s1.insert(21);
        s1.insert(3);
        s1.insert(4);
        s1.insert(21);
        s1.insert(33);
        s1.insert(21);
        s1.insert(14);
        s1.insert(9);
        s1.insert(1);
        assertEquals(8, s1.size());
        System.out.println(s1);
        assertEquals(5, s2.size());
    }
}

