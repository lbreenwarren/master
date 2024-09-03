import queue.Queue;
import org.junit.jupiter.api.Test;
import exceptions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for a non-expandable queue implemented with a circular array.
 * For use with lab 1 task 3. 
 */
public class Task3Tests {

    @Test
    public void queueTestZero() throws EmptyQueueException {
        Queue myQueue = new Queue();

        // Test an empty queue
        System.out.println("Testing empty queue");
        assertEquals(0, myQueue.size());
        assertTrue(myQueue.empty());

        System.out.println("Testing queue operations");
        // Enqueue 6 items
        myQueue.enqueue(2);
        myQueue.enqueue(4);
        myQueue.enqueue(6);
        myQueue.enqueue(8);
        myQueue.enqueue(10);
        myQueue.enqueue(12);
        assertEquals(6, myQueue.size());
        assertFalse(myQueue.empty());

        // Dequeue 2 items
        assertEquals(myQueue.dequeue(), 2);
        assertFalse(myQueue.empty());

        assertEquals(myQueue.dequeue(), 4);
        assertEquals(myQueue.size(), 4);
        assertFalse(myQueue.empty());

        // Enqueue 6 more items
        myQueue.enqueue(14);
        myQueue.enqueue(16);
        myQueue.enqueue(18);
        myQueue.enqueue(20);
        myQueue.enqueue(22);
        myQueue.enqueue(24);
        assertFalse(myQueue.empty());

        // Dequeue 2 more items
        assertEquals(myQueue.dequeue(), 6);
        assertEquals(myQueue.dequeue(), 8);
        assertEquals(myQueue.size(), 8);
        System.out.println("myQueue values:\n" + myQueue);
        System.out.println(myQueue);

        System.out.println("Testing multiple enqueues and dequeues");
        // Add and remove an item 100 times
        for (int i = 0; i < 100; i++) {
            myQueue.dequeue();
            myQueue.enqueue(1);
        }
        assertEquals(myQueue.size(), 8);
        System.out.println("myQueue values:\n" + myQueue);

        System.out.println("Testing multiple enqueues and dequeues");
        // Remove and add an item 10,000 times
        for (int i = 0; i < 10000; i++) {
            myQueue.dequeue();
            myQueue.enqueue(2);
        }
        assertEquals(myQueue.size(), 8);
        System.out.println("myQueue values:\n" + myQueue);
    }

    @Test
    public void queueTestOne() throws EmptyQueueException {
        Queue q = new Queue();
        
        System.out.println("Testing empty queue");
        assertEquals(q.size(), 0);
        assertTrue(q.empty());
        System.out.println("Correct");
        
        System.out.println("Testing enqueues");
        for (int i = 0; i < 10; i++) {
            assertEquals(q.size(), i);
            q.enqueue(i * 10);
        }
        assertEquals(q.size(), 10);
        assertFalse(q.empty());

        System.out.println("Looking at output. Should be 0, 10, 20, 30, 40, 50, 60, 70, 80, 90");
        System.out.println(q);
        
        System.out.println("Testing dequeues");
        for (int i = 0; i < 10; i++) {
            assertEquals(q.size(), 10 - i);
            assertFalse(q.empty());
            assertEquals(q.dequeue(), i * 10);
        }

        System.out.println("Printing empty queue");
        System.out.println(q);
    }

    @Test
    public void queueTestTwo() throws EmptyQueueException {
        Queue q = new Queue();

        System.out.println("Testing enqueues and dequeues.\n There should be no outputs");

        for (int i = 0; i < 1000; i++) {
            assertTrue(q.empty());
            
            q.enqueue(i);
            assertEquals(q.size(), 1);
            assertEquals(q.dequeue(), i);

            System.out.println(q);

            assertTrue(q.empty());
        }

    }

    @Test
    public void queueTestThree() throws EmptyQueueException {
        Queue q = new Queue();
        System.out.println("Testing queueing and dequeueing on almost full queue");

        for (int i = 0; i < 10; i++) {
            q.enqueue(0);
            assertEquals(q.size(), i + 1);
        }

        System.out.println("Should be all zeros");
        System.out.println(q);

        for (int i = 0; i < 10000; i++) {
            q.dequeue();
            q.enqueue(i);
        }

        System.out.println("output should be 9990, 9991, 9992, 9993, 9994, 9995, 9996, 9997, 9998, 9999");
        System.out.println(q);
    }

    @Test
    public void queueTestFour() throws EmptyQueueException {
        Queue q = new Queue();
        System.out.println("Testing a mildly filled queue");

        for (int i = 0; i < 4; i++) {
            q.enqueue(i);
            assertEquals(q.size(), i + 1);
        }

        for (int i = 4; i < 11111; i++) {
            assertEquals(4, q.size());
            q.enqueue(i);
            assertEquals(q.size(), 5);
            assertEquals(q.dequeue(), i - 4);
            assertEquals(q.size(), 4);
        }

        System.out.println("Should output 11107 to 11110");
        System.out.println(q);

        for (int i = 0; i < 4; i++) {
            assertEquals(q.size(), 4 - i);
            assertEquals( q.dequeue(), 11110 - (3 - i) );
        }

        System.out.println("Nothing should output after here");
        System.out.println(q);
    }
}
