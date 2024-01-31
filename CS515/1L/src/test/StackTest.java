import stack.Stack;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import exceptions.*;

public class StackTest {

    @Test
    public void stack_NoArgConstructor_HasDefaultCapacity8() {
        Stack s = new Stack();
        assertEquals(8, s.capacity());
    }

    @Test
    public void stack_WithCapacityArg12_HasCapacity12() {
        Stack s = new Stack(12);
        assertEquals(12, s.capacity());
    }    
    
    @Test
    public void stack_NoArgConstructor_InEmptyState() {
        Stack s = new Stack();
        assertEquals(0, s.size());
        assertTrue(s.empty());
    }

    @Test
    public void stack_ArgConstructor_InEmptyState() {
        Stack s = new Stack(12);
        assertEquals(0, s.size());
        assertTrue(s.empty());
    }

    @Test
    public void stack_CopyConstructor_CreatesCorrectCopyStack() throws EmptyStackException{
        Stack origStack = new Stack();
        origStack.push(1);
        origStack.push(2);
        origStack.push(3);
        origStack.push(4);

        Stack newStack = new Stack(origStack); // Invoke copy constructor

        assertEquals(origStack.size(), newStack.size());
        assertEquals(origStack.capacity(), newStack.capacity());

        for (int i = 0; i < 4; i++) {
            assertEquals(origStack.top(), newStack.top());
            origStack.pop();
            newStack.pop();
        }
    }

    @Test
    public void push_InEmptyState_UpdatesSize() {
        Stack s = new Stack();
        assertEquals(0, s.size());
        assertTrue(s.empty());
        
        s.push(1);

        assertEquals(1, s.size());
        assertFalse(s.empty());
    }

    @Test
    public void push_InHoldingState_UpdatesSize() {
        Stack s = new Stack();
        s.push(1);
        s.push(2);
        s.push(3);
        assertEquals(3, s.size());

        s.push(4);
        assertEquals(4, s.size());
    }

    @Test
    public void push_InFullState_UpdatesSize() {
        Stack s = new Stack(2);
        s.push(700);
        s.push(400);
        assertEquals(2, s.size());

        s.push(900);
        assertEquals(3, s.size());
    }

    @Test
    public void push_InFullState_MultipleExpansionsUpdatesSizeAndCapacity() {
        Stack s = new Stack();
        
        // Add 64 items to the stack (should double capacity: 8, 16, 32, 64)
        for (int itemCount = 1; itemCount <= 64; itemCount++) {
            s.push(itemCount);
            assertEquals(itemCount, s.size());

            if (itemCount <= 8) {                           // < 8 items
                assertEquals(8, s.capacity());
            } else if (itemCount > 8 && itemCount <= 16) {  // 9-16 items
                assertEquals(16, s.capacity());
            } else if (itemCount > 17 && itemCount <= 32) { // 17-32 items
                assertEquals(32, s.capacity());
            } else if (itemCount > 32 && itemCount <= 64) { // 33-64 items
                assertEquals(64, s.capacity());
            }
        }
    }

    @Test
    public void top_InHoldingState_ReturnsCorrectValue() throws EmptyStackException{
        Stack s = new Stack();
        s.push(1);
        s.push(2);
        s.push(3);

        assertEquals(3, s.top());
    }
    
    @Test
    public void top_InEmptyState_ThrowsEmptyStackException() {
        Stack s = new Stack();
        assertThrows(EmptyStackException.class, () -> {
            s.top();
        });
    }

    @Test
    public void top_inHoldingState_ReturnsCorrectValue() throws EmptyStackException {
        Stack s = new Stack();
        s.push(123);
        assertEquals(123, s.top());
    }
    
    @Test
    public void pop_InHoldingState_ReturnsCorrectValues() throws EmptyStackException {
        Stack s = new Stack();
        for (int i = 0; i <= 6; i++) {
            s.push(i);
        }
        for (int i = 6; i >= 0; i--) {
            assertEquals(i, s.pop());
        }
    }

    @Test
    public void top_InHoldingState_ReturnsCorrectValues() throws EmptyStackException {
        Stack s = new Stack();
        for (int i = 0; i <= 6; i++) {
            s.push(i);
        }
        for (int i = 6; i >= 0; i--) {
            assertEquals(i, s.top());
            s.pop();
        }
    }
    
    @Test
    public void pop_InHoldingState_UpdatesSize() throws EmptyStackException {
        Stack s = new Stack();
        s.push(500);
        s.push(800);
        s.push(400);
        assertEquals(3, s.size());

        s.pop();
        assertEquals(2, s.size());

        s.pop();
        s.pop();
        assertEquals(0, s.size());
        assertTrue(s.empty());
    }

    @Test
    public void pop_InEmptyState_ThrowsEmptyStackException() {
        Stack s = new Stack();
        assertThrows(EmptyStackException.class, () -> {
            s.pop();
        });
    }

    @Test
    public void equals_OnUnequalStacks_ReturnsFalse() {
        Stack a = new Stack();
        a.push(100);
        a.push(101);
        
        Stack b = new Stack();
        b.push(1);

        assertFalse(a.equals(b));
        // - - - - - - - - - - - - - - - - - - - - - 
        Stack aa = new Stack();
        aa.push(1);
        aa.push(2);
        aa.push(3);

        Stack bb = new Stack();
        bb.push(1);
        bb.push(2);
        bb.push(999);

        assertFalse(aa.equals(bb));
    }

    @Test
    public void equals_onEqualStacks_ReturnsTrue() {
        Stack a = new Stack();
        Stack b = new Stack();

        for (int i = 0; i < 7; i++) {
            a.push(i);
            b.push(i);
        }

        assertTrue(a.equals(b));
    }

    @Test
    public void empty_InEmptyState_ReturnsTrue() throws EmptyStackException {
        Stack s = new Stack();
        s.push(1);
        s.push(2);
        s.push(3);

        s.pop();
        s.pop();
        s.pop();

        assertTrue(s.empty());
    }

    @Test
    public void empty_InHoldingState_ReturnsFalse() {
        Stack s = new Stack();
        s.push(1);
        s.push(2);
        s.push(3);

        assertFalse(s.empty());
    }

    @Test
    public void push_InFullState_DoublesCapacityAndCopiesValues() throws EmptyStackException{
        Stack s = new Stack(4);
        s.push(0);
        s.push(1);
        s.push(2);
        s.push(3);
        assertEquals(4, s.capacity());

        s.push(4); // Expand the stack
        assertEquals(s.capacity(), 8);

        for (int i = 4; i >= 0; i--) {
            assertEquals(i, s.top());
            s.pop();
        }
    }
}