import org.junit.jupiter.api.Test;
import OrderedSet.OrderedSet;
import static org.junit.jupiter.api.Assertions.*;
import java.util.NoSuchElementException;

/**
 * New tests for Java set using Java iterator and set methods.
 */
public class SetTest {

    @Test
    public void removeAll_OnNonEmptySets_UpdatesSizeCorrectly() {
        OrderedSet<String> testSet = new OrderedSet<String>();
        OrderedSet<String> otherSet = new OrderedSet<String>();

        testSet.insert("red");
        testSet.insert("orange");
        testSet.insert("green");
        testSet.insert("blue");
        testSet.insert("purple");
        testSet.insert("brown");
        testSet.insert("yellow");
        assertEquals(7, testSet.size());

        otherSet.insert("red");
        otherSet.insert("blue");
        otherSet.insert("yellow");
        otherSet.insert("black");
        otherSet.insert("white");
        assertEquals(5, otherSet.size());

        testSet.removeAll(otherSet); // Difference

        assertEquals(4, testSet.size());
    }

    @Test
    public void retainAll_OnNonEmptySets_UpdatesSetContentsCorrectly() {
        OrderedSet<Character> alphabet = new OrderedSet<Character>();
        OrderedSet<Character> vowels = new OrderedSet<Character>();

        // Fill one set with the entire alphabet
        for (char c = 'a'; c <= 'z'; c++) {
            alphabet.insert(c);
        }

        // Fill one set with just vowels
        vowels.insert('a');
        vowels.insert('e');
        vowels.insert('i');
        vowels.insert('o');
        vowels.insert('u');
        vowels.insert('y');

        alphabet.retainAll(vowels); // Perform set intersection

        // Make sure all vowels are found
        assertTrue(alphabet.contains('a'));
        assertTrue(alphabet.contains('e'));
        assertTrue(alphabet.contains('i'));
        assertTrue(alphabet.contains('o'));
        assertTrue(alphabet.contains('u'));
        assertTrue(alphabet.contains('y'));

        // Make sure all consonants are NOT found
        Character consonants[] = {'b', 'c', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'm', 'n', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'z'};
        for (int i = 0; i < consonants.length; i++) {
            assertFalse(alphabet.contains(consonants[i]));
        }

    }

    @Test
    public void retainAll_OnNonEmptySets_MaintainsOrder() {
        OrderedSet<Character> alphabet = new OrderedSet<Character>();
        OrderedSet<Character> vowels = new OrderedSet<Character>();

        // Fill one set with the entire alphabet
        for (char c = 'a'; c <= 'z'; c++) {
            alphabet.insert(c);
        }

        // Fill one set with just vowels
        vowels.insert('a');
        vowels.insert('e');
        vowels.insert('i');
        vowels.insert('o');
        vowels.insert('u');
        vowels.insert('y');
        String test1 = vowels.toString();
        alphabet.retainAll(vowels); // Perform set intersection

        // Make sure that all the vowels appear in order
        char vowelArr[] = {'a', 'e', 'i', 'o', 'u'};
        OrderedSet<Character>.OrderedIterator it = alphabet.orderedIterator(); // Get an iterator
        String test = vowels.toString();
        for (int i = 0; i < vowelArr.length; i++) {
            assertEquals(vowelArr[i], it.next()); 
        }
    }

    @Test
    public void hasNext_OnNonEmptySet_ReturnsCorrectBoolean() {
        OrderedSet<Character> alphabet = new OrderedSet<Character>();

        // Add the alphabet to the set (reverse order so set must sort)
        for (char c = 'z'; c >= 'a'; c--) {
            alphabet.insert(c);
        }

        // Get an iterator
        OrderedSet<Character>.OrderedIterator it = alphabet.orderedIterator();

        // Iterate through set and check hasNext returns true until on last elem
        for (int i = 0; i < 26; i++) {
            assertTrue(it.hasNext());
            it.next();                  // Move forward
        }
        assertFalse(it.hasNext()); // Should be nothing left
    }

    @Test
    public void hasPrev_OnNonEmptySet_ReturnsCorrectBoolean() { 
        OrderedSet<Character> alphabet = new OrderedSet<Character>();

        // Add the alphabet to the set (reverse order so set must sort)
        for (char c = 'z'; c >= 'a'; c--) {
            alphabet.insert(c);
        }

        // Get an iterator
        OrderedSet<Character>.OrderedIterator it = alphabet.orderedIterator();

        // Iterate to the last elem so we can use hasPrev
        for (int i = 0; i < 26; i++) {
            it.next();
        }

        // Iterate though set and check hasPrev returns true until on first elem
        for (int i = 0; i < 26; i++) { 
            assertTrue(it.hasPrev());
            it.prev();                  // Move backwards
        }

        assertFalse(it.hasPrev()); // Should be nothing left
    }


    @Test
    public void next_OnNonEmptySet_ReadsAllElementsAndInOrder() {
        OrderedSet<Character> alphabet = new OrderedSet<Character>();
        
        // Fill set with entire alphabet (reverse order, so set must sort it)
        for (char c = 'z'; c >= 'a'; c--) {
            alphabet.insert(c);
        }

        // Get an iterator
        OrderedSet<Character>.OrderedIterator iterator = alphabet.orderedIterator();

        // Make sure we get the alphabet in order
        Character result = '-';
        for (char c = 'a'; c <= 'z'; c++) {
            result = iterator.next();
            assertEquals(c, result);
        }
    }
    
    @Test
    public void prev_BeyondBeginningOfSet__ThrowsException() {
        OrderedSet<String> mySet = new OrderedSet<String>();

        mySet.insert("apple");
        mySet.insert("bannana");
        mySet.insert("cherry");

        OrderedSet<String>.OrderedIterator iterator = mySet.orderedIterator();

        // Iterate to beginning of set
        for (int i = 0; i < 3; i++) { 
            String test = iterator.prev();
            System.out.println(test);
        }

        // Further calls to prev should throw exception
        for (int i = 0; i < 10; i++) {
            assertThrows(NoSuchElementException.class, () -> {
                iterator.prev();
            });
        }
    }

    @Test
    public void first_OnNonEmptySet_ReturnsLeastElement() {
        OrderedSet<String> mySet = new OrderedSet<String>();

        mySet.insert("bannana");
        mySet.insert("zebra");
        mySet.insert("apple");
        mySet.insert("cherry");
        assertEquals("apple", mySet.first());        
    }

    // [4] Simple stress test (idea from c++ tests)
    @Test
    public void size_WithManyElements_ReturnsCorrectValue() {
        OrderedSet<String> testSet = new OrderedSet<String>();

        for (Integer i = 1; i < 1000; i++) {
            testSet.insert(i.toString());
            assertEquals(i, testSet.size());
        }

    }

        // [5] Test equality of Iterators (relies on hasNext)
    @Test
    public void iteratorEquals_OnSameFromOppositeDirections_ReturnsTrue() {
        OrderedSet<Integer> testSet = new OrderedSet<Integer>();
        for (int i = 0; i < 10; i++) {
            testSet.insert(i);
        }

        OrderedSet<Integer>.OrderedIterator it1 = testSet.orderedIterator();
        OrderedSet<Integer>.OrderedIterator it2 = testSet.orderedIterator();
        
        // Bring both iterators to center
        for (int i = 0; i < 5; i++) {
            it1.next();
            it2.prev();
        }

        assertTrue(it1.equals(it2));
    }

    
}
