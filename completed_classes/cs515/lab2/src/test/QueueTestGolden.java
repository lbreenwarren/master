import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class QueueTestGolden {

    private static void enqueueValues(Queue<Integer> q, int startValue,
                                      int howMany) {

        for(int i = 0; i < howMany; i++) {
            q.enqueue(startValue);
            startValue += 2;
        }
    }

    @Test
    void constructorCreatesEmptyQueue() {
        Queue<Integer> testQ = new Queue<>();
        assertEquals(0, testQ.size());
        assertTrue(testQ.isEmpty());
    }

    @Test
    void enqueueSixItemsHasRightSize() {
        Queue<Integer> testQ = new Queue<>();
        enqueueValues(testQ, 2, 6);
        assertEquals(6, testQ.size());
        assertFalse(testQ.isEmpty());
    }

    @Test
    void dequeueItemsHasRightValuesAndSize() {
        Queue<Integer> testQ = new Queue<>();
        int result;
        enqueueValues(testQ, 2, 6);
        result = testQ.dequeue();
        assertEquals(2, result);
        assertEquals(5, testQ.size());
        assertFalse(testQ.isEmpty());

        result = testQ.dequeue();
        assertEquals(4, result);
        assertEquals(4, testQ.size());
        assertFalse(testQ.isEmpty());
    }

    @Test
    void dequeueAfterMultipleEnqueueDequeue() {
        Queue<Integer> testQ = new Queue<Integer>();
        enqueueValues(testQ, 2, 6);
        testQ.dequeue();
        testQ.dequeue();
        enqueueValues(testQ, 14, 6);
        assertEquals(6, testQ.dequeue());
        assertEquals(8, testQ.dequeue());
        assertEquals(8, testQ.size());
    }

    @Test
    void dumpAfterMultipleEnqueueDequeue() {
        Queue<Integer> testQ = new Queue<>();
        enqueueValues(testQ, 2, 6);
        testQ.dequeue();
        testQ.dequeue();
        enqueueValues(testQ, 14, 6);
        testQ.dequeue();
        testQ.dequeue();
        System.out.println(testQ);
    }

    @Test
    void enqueueDequeuePatternDumpsCorrectly() {
        Queue<Integer> testQ = new Queue<>();
        enqueueValues(testQ, 2 ,6);
        for(int i = 0; i < 10000; i++) {
            testQ.dequeue();
            testQ.enqueue(i);
        }
        assertEquals(6, testQ.size());
        System.out.println(testQ);
    }

    @Test
    void dequeueAllLeavesEmpty() {
        Queue<Integer> testQ = new Queue<>();
        enqueueValues(testQ, 2, 6);
        testQ.dequeue();
        testQ.dequeue();
        enqueueValues(testQ, 14, 6);
        System.out.print("testQ values prior to emptying: ");
        for(int i = 0; i < 10; i++) {
            System.out.print(testQ.dequeue() + " ");
        }
        System.out.println();
        assertEquals(0, testQ.size());
        assertTrue(testQ.isEmpty());
        assertThrows(EmptyQueueException.class, () -> {
                testQ.dequeue();
            });
    }

    @Test
    void enqueueAfterDequeueToEmptyWorks() {
        Queue<Integer> testQ = new Queue<>();
        enqueueValues(testQ, 2, 6);
        testQ.dequeue();
        testQ.dequeue();
        enqueueValues(testQ, 14, 6);
        for (int i = 0; i < 10; i++)
            testQ.dequeue();
        testQ.enqueue(20);
        assertEquals(1, testQ.size());
    }

    @Test
    void copyConstructorMakesCorrectSize() {
        Queue<Integer> q1 = new Queue<>();
        enqueueValues(q1, 2, 6);

        Queue<Integer> q2 = new Queue<>(q1);
        assertEquals(6, q2.size());
    }

    @Test
    void copyConstructorMakesSeparateCopy() {
        Queue<Integer> q1 = new Queue<>();
        enqueueValues(q1, 2, 6);

        Queue<Integer> q2 = new Queue<>(q1);
        q1.dequeue();
        q2.enqueue(15);
        assertEquals(5, q1.size());
        assertEquals(7, q2.size());
    }

    @Test
    void assignmentWithManyItemsHasCorrectValues() {
        int i;
        Queue<Integer> q1 = new Queue<>();
        final int bigValue = 1050;
        for(i = 0; i < bigValue; i++) {
            q1.enqueue(i);
        }
        assertEquals(bigValue, q1.size());

        Queue<Integer> q2 = new Queue<>(q1);
        assertEquals(bigValue, q2.size());
        for(i = 0; i < bigValue - 1; i++) {
            assertEquals(i, q2.dequeue());
        }
        assertEquals(1, q2.size());
        assertEquals(bigValue, q1.size());
    }
}
