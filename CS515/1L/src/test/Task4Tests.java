import stack.Stack;
import org.junit.jupiter.api.Test;
import exceptions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for a non-expandable stack.
 * For use with lab 1 task 4. 
 */
public class Task4Tests {
    
    @Test
    public void stackTestZero() throws EmptyStackException {
        Stack s = new Stack();

        assertTrue(s.size() == 0);
        assertTrue(s.capacity() == 8);
        assertTrue(s.empty());

        assertThrows(EmptyStackException.class, () -> {
            s.pop();
        });
        
        assertEquals(s.size(), 0);
        assertEquals(s.capacity(), 8);
        assertTrue(s.empty());

        s.push(2);
        s.push(4);
        s.push(6);
        s.push(8);
        s.push(10);
        s.push(12);

        assertEquals(s.size(), 6);
        assertEquals(s.capacity(), 8);
        assertFalse(s.empty());

        s.push(14);
        s.push(16);
        
        assertEquals(s.top(), 16);
        s.pop();
        assertEquals(s.top(), 14);
        s.pop();
        assertEquals(s.size(), 6);
        assertEquals(s.capacity(), 8);
        
        // Testing copy constructor
        Stack t = new Stack(s);
        assertEquals(t.size(), s.size());
        s.pop();
        assertEquals(t.top(), 12);
        assertEquals(s.top(), 10);
        
        s.pop();
        s.pop();
        s.pop();
        s.pop();
        s.pop();
        
        assertThrows(EmptyStackException.class, () -> {
            s.pop();
        });
    }

    @Test
    public void stackTestOne() throws EmptyStackException {
        // Testing default constructor
	    Stack s = new Stack();

	    // Testing pushing and popping
	    assertTrue(s.empty());
	    assertEquals(s.capacity(), 8);

	    for (int i = 0; i < 8; i++) {
	    	assertEquals(s.size(), i);
		    s.push(i);
		    assertEquals(s.top(), i);		
	    }

	    for (int i = 7; i >= 0; i--) {
		    s.pop();
		    assertEquals(s.size(), i);
	    }
	
	    assertTrue(s.empty());
    }

    @Test
    public void stackTestTwo() throws EmptyStackException {
        Stack s = new Stack(10);

        assertTrue(s.empty());
        assertEquals(s.capacity(), 10 );

        for(int i = 0; i < 1000; i++) {
            assertEquals(s.size(), 0);
            s.push(i);
            assertEquals(s.capacity(), 10);
            assertEquals(s.top(), i);
            s.pop();
        }

        for (int i = 0; i < 10; i++) {
            s.push(i);
            assertEquals(s.size(), i + 1);
        }

        assertEquals(s.capacity(), 10);
        assertEquals(s.size(), 10);

        for (int i = 0; i < 10; i++) {
            s.pop();
        }

        assertEquals(s.size(), 0);
        assertEquals(s.empty(), true);

        for (int i = 0; i < 1000; i++) {
            assertThrows(EmptyStackException.class, () -> {
                s.pop();
            });
        }
    }

    @Test
    public void stackTestThree() throws EmptyStackException {
        Stack s = new Stack(15); 
        assertEquals(s.size(), 0);
        assertTrue(s.empty());
        assertEquals(s.capacity(), 15);

        for (int i = 0; i < 15; i++ ) {
            s.push(i);
        }

        assertEquals(s.size(), 15);
        assertFalse(s.empty());
        assertEquals(s.capacity(), 15);

        Stack s2 = new Stack(s);

        assertEquals(s2.size(), 15);
        assertEquals(s2.capacity(), 15);
        assertFalse(s2.empty());

        for (int i = 0; i < 15; i++) {
            assert(s2.top() == s.top() );
            s2.pop();
            s.pop();
        }

        assertEquals(s2.size(), 0);
        assertTrue(s2.empty());
        assertEquals(s2.capacity(), 15);

        System.out.println("Test done");
    }
}
