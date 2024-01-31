import queue.Queue;
import org.junit.jupiter.api.Test;

import exceptions.*;

import static org.junit.jupiter.api.Assertions.*;

public class QueueTest {

    @Test
    public void queue_NoArgConstructor_HasDefaultCapacity10() {
        Queue q = new Queue();
        assertEquals(10, q.capacity());
    }

    @Test
    public void queue_WithCapacityArg8_HasCapacity8() {
        Queue q = new Queue(8);
        assertEquals(8, q.capacity());
    }

    @Test
    public void queue_NoArgConstructor_InEmptyState() {
        Queue q = new Queue();
        assertEquals(0, q.size());
        assertTrue(q.empty());
    }

    @Test
    public void queue_ArgConstructor_InEmptyState() {
        Queue q = new Queue(7);
        assertEquals(0, q.size());
        assertTrue(q.empty());

    }

    @Test
    public void queue_CopyConstructor_CreatesCorrectCopyQueue() throws EmptyQueueException {
        Queue origQueue = new Queue();
        origQueue.enqueue(1);
        origQueue.enqueue(2);
        origQueue.enqueue(3);
        origQueue.enqueue(4);
        origQueue.enqueue(5);
        origQueue.enqueue(6);

        Queue dupQueue = new Queue(origQueue); // Invoke copy constructor

        for (int i = 0; i < 6; i++) {
            assertEquals(origQueue.front(), dupQueue.front());
            origQueue.dequeue();
            dupQueue.dequeue();
        }
    }

    @Test
    public void queue_CopyConstructorWithLoopAround_CreatesCorrectCopyQueue() throws EmptyQueueException {
        Queue q = new Queue();

        // Enqueue 10 items 
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        q.enqueue(6);
        q.enqueue(7);
        q.enqueue(8);
        q.enqueue(9);
        q.enqueue(10);

        // Dequeue 3 items
        q.dequeue();
        q.dequeue();
        q.dequeue();

        // Enqueue 3 items (causes loop around)
        q.enqueue(11);
        q.enqueue(12);
        q.enqueue(13);

        Queue dupQ = new Queue(q); // Invoke the copy constructor

        int val = 4;
        for (int i = 0; i < 10; i++) {
            assertEquals(q.front(), dupQ.front());
            assertEquals(val, dupQ.dequeue());
            
            q.dequeue();
            val++;
        }
    }

    @Test
    public void enqueue_InEmptyState_UpdatesSize() {
        Queue q = new Queue();
        assertEquals(0, q.size());
        assertTrue(q.empty());

        q.enqueue(1);

        assertEquals(1, q.size());
        assertFalse(q.empty());
    }

    @Test
    public void enqueue_InHoldingState_UpdatesSize() {
        Queue q = new Queue();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertEquals(3, q.size());

        q.enqueue(4);
        assertEquals(4, q.size());
    }

    @Test
    public void enqueue_InFullState_UpdatesSizeCapacityAndCopiesValues() throws EmptyQueueException {
        Queue q = new Queue();

        // Enqueue 10 values
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        q.enqueue(6);
        q.enqueue(7);
        q.enqueue(8);
        q.enqueue(9);
        q.enqueue(10);

        // Enqueue 1 more value
        q.enqueue(11);

        // Capcity and size correct after expansion?
        assertEquals(20, q.capacity());
        assertEquals(11, q.size());

        // Check for right vals in queue
        for (int i = 1; i < 12; i++) {
            assertEquals(i, q.dequeue());
        }
    }

    @Test
    public void enqueue_InFullState_MultipleExpansionsUpdatesSizeAndCapacity() {
        Queue q = new Queue();

        // Add 80 items to the queue (should double capacity: 10, 20, 40, 80)
        for (int itemCount = 1; itemCount <= 80; itemCount++) {
            q.enqueue(itemCount);
            assertEquals(itemCount, q.size());

            if (itemCount <= 10) {                          // < 10 items
                assertEquals(10, q.capacity());
            } else if (itemCount > 10 && itemCount <= 20) { // 11-20 items
                assertEquals(20, q.capacity());
            } else if (itemCount > 20 && itemCount <= 40) { // 21-40 items
                assertEquals(40, q.capacity());
            } else if (itemCount > 40 && itemCount <= 80) { // 41-80 items
                assertEquals(80, q.capacity());
            }
        }
    }

    @Test
    public void enqueue_InFullStateWithLoopAround_UpdatesSizeCapacityAndCopiesValues() throws EmptyQueueException {
        Queue q = new Queue();

        // Enqueue 10 values
        q.enqueue(-1);
        q.enqueue(-2);
        q.enqueue(-3);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        q.enqueue(6);
        q.enqueue(7);

        // Dequeue 3 values
        q.dequeue();
        q.dequeue();
        q.dequeue();

        // Enqueue 3 values (create loop around)
        q.enqueue(8);
        q.enqueue(9);
        q.enqueue(10);
        
        // Enqueue 1 more value (force expansion)
        q.enqueue(11);

        // Capcity and size correct after expansion?
        assertEquals(20, q.capacity());
        assertEquals(11, q.size());

        // Check for right vals in queue
        for (int i = 1; i < 12; i++) {
            assertEquals(i, q.dequeue());
        }
    }

    @Test
    public void dequeue_InHoldingState_UpdatesSize() throws EmptyQueueException {
        Queue q = new Queue();
        q.enqueue(500);
        q.enqueue(800);
        q.enqueue(400);
        assertEquals(3, q.size());

        q.dequeue();
        assertEquals(2, q.size());

        q.dequeue();
        q.dequeue();
        assertEquals(0, q.size());
        assertTrue(q.empty());
    }

    @Test
    public void front_InHoldingState_DoesNotChangeSize() throws EmptyQueueException {
        Queue q = new Queue();
        q.enqueue(500);
        q.enqueue(800);
        q.enqueue(400);
        assertEquals(3, q.size());

        q.front();
        assertEquals(3, q.size());
    }

    @Test
    public void dequeue_InEmptyState_ThrowsEmptyQueueException() {
        Queue q = new Queue();
        assertThrows(EmptyQueueException.class, () -> {
            q.dequeue();
        });
    }

    @Test
    public void front_InEmptyState_ThrowsEmptyQueueException() {
        Queue q = new Queue();
        assertThrows(EmptyQueueException.class, () -> {
            q.front();
        });
    }

    @Test
    public void empty_OnNewlyConstructedQueue_ReturnsTrue() {
        Queue q = new Queue();
        assertTrue(q.empty());
    }

    @Test
    public void empty_InEmptyState_ReturnsTrue() throws EmptyQueueException {
        Queue q = new Queue();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);

        q.dequeue();
        q.dequeue();
        q.dequeue();

        assertTrue(q.empty());
    }

    @Test
    public void empty_InEmptyStateAfterLoopAround_ReturnsTrue() throws EmptyQueueException {
        Queue q = new Queue();
        
        // Enqueue 10 values
        q.enqueue(-3);
        q.enqueue(-2);
        q.enqueue(-1);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        q.enqueue(6);
        q.enqueue(7);

        // Dequeue 3 values
        q.dequeue();
        q.dequeue();
        q.dequeue();

        // Enqueue 3 values (force loop around)
        q.enqueue(8);
        q.enqueue(9);
        q.enqueue(10);

        // Dequeue all 10 items
        for (int i = 0; i < 10; i++) {
            q.dequeue();
        }

        assertTrue(q.empty());
    }

    @Test
    public void empty_InHoldingState_ReturnsFalse() {
        Queue q = new Queue();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);

        assertFalse(q.empty());
    }

    @Test
    public void empty_InHoldingStateWithLoopAround_ReturnsFalse() throws EmptyQueueException {
        Queue q = new Queue();
        // Enqueue 10 values
        q.enqueue(-3);
        q.enqueue(-2);
        q.enqueue(-1);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        q.enqueue(6);
        q.enqueue(7);

        // Dequeue 5 values
        for (int i = 0; i < 5; i++) {
            q.dequeue();
        }

        // Enqueue 3 values (force loop around)
        q.enqueue(8);
        q.enqueue(9);
        q.enqueue(10);

        assertFalse(q.empty());
    }

    @Test
    public void empty_InFullState_ReturnsFalse() {
        Queue q = new Queue();
        for (int i = 0; i < 10; i++) { // Queue is full with 10 items
            q.enqueue(i);
        }
        assertFalse(q.empty());
    }

    @Test
    public void full_OnNewlyConstructedQueue_ReturnsFalse() {
        Queue q = new Queue();
        assertFalse(q.full());
    }
    
    @Test
    public void full_InEmptyState_ReturnsFalse() throws EmptyQueueException{
        Queue q = new Queue();
        for (int i = 0; i < 5; i++) { // Queue is half full with 5 items
            q.enqueue(i);
        } 
        for (int i = 0; i < 5; i++) { // Queue is empty again
            q.dequeue();
        }
        assertFalse(q.full()); 
    }

    @Test
    public void full_InHoldingState_ReturnsFalse() {
        Queue q = new Queue();
        for (int i = 0; i < 5; i++) { // Queue is half full with 5 items
            q.enqueue(i);
        }
        assertFalse(q.full());
    }

    @Test
    public void full_InFullState_ReturnsTrue() {
        Queue q = new Queue();
        for (int i = 0; i < 10; i++) { // Queue is full with 10 items
            q.enqueue(i);
        }
        assertTrue(q.full());
    }

    @Test
    public void full_InFullState_MultipleExpansionsReturnsCorrectBoolean() {
        Queue q = new Queue();

        // Add 80 items to the queue (should double capacity: 10, 20, 40, 80)
        for (int itemCount = 1; itemCount <= 80; itemCount++) {
            q.enqueue(itemCount);
            assertEquals(itemCount, q.size());

            if (itemCount == 10 || itemCount == 20 || itemCount == 40 || itemCount == 80) { // Full queue
                assertTrue(q.full());
            } else {
                assertFalse(q.full());
            }
        }

    }

    @Test
    public void full_InFullStateWithLoopAround_ReturnsTrue() throws EmptyQueueException {
        Queue q = new Queue();
        // Enqueue 10 values
        for (int i = 0; i < 10; i++) {
            q.enqueue(i);
        }
       
        // Dequeue 5 values
        for (int i = 0; i < 5; i++) {
            q.dequeue();
        }

        // Enqueue 5 values (force loop around and refill queue)
        for (int i = 0; i < 5; i++) {
            q.enqueue(i);
        }
    }

    @Test
    public void full_InHoldingStateWithLoopAround_ReturnsFalse() throws EmptyQueueException {
        Queue q = new Queue();
        // Enqueue 10 values
        for (int i = 0; i < 10; i++) {
            q.enqueue(i);
        }
       
        // Dequeue 5 values
        for (int i = 0; i < 5; i++) {
            q.dequeue();
        }

        // Enqueue 4 values (force loop around but not full queue)
        for (int i = 0; i < 4; i++) {
            q.enqueue(i);
        }
    }

    @Test
    public void dequeue_InHoldingState_ReturnsCorrectValues() throws EmptyQueueException {
        Queue q = new Queue();
        
        for (int i = 1; i <= 10; i++) {
            q.enqueue(i);
        }

        for (int i = 1; i <= 10; i++) {
            assertEquals(i, q.dequeue());
        }
    }

    @Test
    public void front_InHoldingState_ReturnsCorrectValues() throws EmptyQueueException {
        Queue q = new Queue();
        
        for (int i = 1; i <= 10; i++) {
            q.enqueue(i);
        }

        for (int i = 1; i <= 10; i++) {
            assertEquals(i, q.front());
            q.dequeue();
        }
    }

    @Test
    public void dequeue_InHodlingStateWithLoopAround_ReturnsCorrectValues() throws EmptyQueueException {
        Queue q = new Queue();
        
        // Enqueue 10 values
        q.enqueue(-3);
        q.enqueue(-2);
        q.enqueue(-1);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        q.enqueue(6);
        q.enqueue(7);

        // Dequeue 3 values
        q.dequeue();
        q.dequeue();
        q.dequeue();

        // Enqueue 3 values (force loop around)
        q.enqueue(8);
        q.enqueue(9);
        q.enqueue(10);

        for (int i = 1; i <= 10; i++) {
            assertEquals(i, q.dequeue());
        }
    }

    @Test
    public void front_InHoldingStateWithLoopAround_ReturnsCorrectValues() throws EmptyQueueException {
        Queue q = new Queue();

        // Enqueue 10 values
        q.enqueue(-3);
        q.enqueue(-2);
        q.enqueue(-1);
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        q.enqueue(4);
        q.enqueue(5);
        q.enqueue(6);
        q.enqueue(7);

        // Dequeue 3 values
        q.dequeue();
        q.dequeue();
        q.dequeue();

        // Enqueue 3 values (force loop around)
        q.enqueue(8);
        q.enqueue(9);
        q.enqueue(10);

        for (int i = 1; i <= 10; i++) {
            assertEquals(i, q.front());
            q.dequeue();
        }
    }

}