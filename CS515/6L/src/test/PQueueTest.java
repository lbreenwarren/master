import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import PQueue.PQueue;

public class PQueueTest {

    //----From PQueueTest1----//
    @Test
    public void constructor_QueueCreated_CreatesZeroSizeQueue() {
        PQueue<Integer> q = new PQueue<Integer>(Integer.class, 100);
        assertEquals(0, q.size());
        assertTrue(q.isEmpty());
    }

    @Test
    public void insert_Repeated_ProperlyIncreasesSize() {
        PQueue<Integer> q = new PQueue<Integer>(Integer.class, 100);
        q.insert(3);
        q.insert(10);
        q.insert(9);
        assertEquals(3, q.size());
    }

    @Test
    public void findMin_OnThreeItems_ReturnsMin() {
        PQueue<Integer> q = new PQueue<Integer>(Integer.class, 100);
        q.insert(3);
        q.insert(10);
        q.insert(9);
        assertEquals(3, q.findMin());
    }

    @Test
    public void findMin_AfterThreeInsertsAndOneDelete_ReturnsMin() {
        PQueue<Integer> q = new PQueue<Integer>(Integer.class, 100);
        q.insert(3);
        q.insert(10);
        q.insert(9);
        q.deleteMin();
        assertEquals(9, q.findMin());
    }

    @Test
    public void findMin_AfterThreeInsertsAndTwoDeletes_ReturnsMin() {
        PQueue<Integer> q = new PQueue<Integer>(Integer.class, 100);
        q.insert(3);
        q.insert(10);
        q.insert(9);
        q.deleteMin();
        q.deleteMin();
        assertEquals(10, q.findMin());
    }

    @Test
    public void size_AfterThreeInsertsAndThreeDeletes_Returns0() {
        PQueue<Integer> q = new PQueue<Integer>(Integer.class, 100);
        q.insert(3);
        q.insert(10);
        q.insert(9);
        q.deleteMin();
        q.deleteMin();
        q.deleteMin();
        assertEquals(0, q.size());
    }

    @Test
    public void findMinAndSize_AfterDeepInsertAndDeleteCycles_ReturnsProperResults() {
        PQueue<Integer> q = new PQueue<Integer>(Integer.class, 100);
        Integer x;

        //fill and empty
        q.insert(3);
        q.insert(10);
        q.insert(9);
        q.deleteMin();
        q.deleteMin();
        q.deleteMin();

        //fill again and test
        q.insert(8);
        q.insert(2);
        q.insert(2);
        q.insert(1);
        q.insert(3);
        q.insert(9);
        q.insert(10);
        
        x = q.findMin();
        assertEquals(7, q.size());
        assertEquals(1, x);
        q.deleteMin();
        assertEquals(6, q.size());
    
        x = q.findMin();
        assertEquals(6, q.size());
        assertEquals(2, x);
        q.deleteMin();
        assertEquals(5, q.size());
        
        x = q.findMin();
        assertEquals(5, q.size());
        assertEquals(2, x);
        q.deleteMin();
        assertEquals(4, q.size());
    }

    //----From PQueueTest2----//
    @Test
    public void findMinAndSize_AfterInsertDeleteInsertMin_ReturnsProperResults() {
        PQueue<Integer> q = new PQueue<Integer>(Integer.class, 100);
        Integer x;

        q.insert(3);
        q.insert(10);
        q.insert(9);
        q.insert(8);
        q.insert(2);
        q.insert(2);
        q.insert(1);
        q.insert(3);
        q.insert(9);
        q.insert(10);
        
        x = q.findMin();
        assertEquals(10, q.size());
        assertEquals(1, x);
        q.deleteMin();
        assertEquals(9, q.size());
        
        x = q.findMin();
        assertEquals(9, q.size());
        assertEquals(2, x);
        q.deleteMin();
        assertEquals(8, q.size());
        
        q.insert(1);
        assertEquals(9, q.size());
        
        x = q.findMin();
        assertEquals(9, q.size());
        assertEquals(1, x);
        q.deleteMin();
        assertEquals(8, q.size());
    }

    //----From PQueueTest3----//
    @Test
    public void findMinAndSize_AfterArrayInitializationAndOps_ReturnsProperResults() {
        Integer[] a = {3, 10, 9, 8, 2, 2, 1, 3, 9, 10};

        PQueue<Integer> q = new PQueue<Integer>(Integer.class, 10, a);
        Integer x;
  
        x = q.findMin();
        assertEquals(10, q.size());
        assertEquals(1, x);
        q.deleteMin();
        assertEquals(9, q.size());
        
        x = q.findMin();
        assertEquals(9, q.size());
        assertEquals(2, x);
        q.deleteMin();
        assertEquals(8, q.size());
        
        q.insert(1);
        assertEquals(9, q.size());
        
        x = q.findMin();
        assertEquals(9, q.size());
        assertEquals(1, x);
        q.deleteMin();
        assertEquals(8, q.size());
    }

    //-----Tests involving TextSegment-----//
    @Test
    public void findMinAndIsEmpty_AfterTextSegmentArrayInitAndOps_ReturnsProperResults() {
        TextSegment[] ts = {
            new TextSegment(3, "a"),
            new TextSegment(1, "This"),
            new TextSegment(4, "test."),
            new TextSegment(2, "is")
        };

        PQueue<TextSegment> q = new PQueue<TextSegment>(TextSegment.class, 100, ts);

        String result = q.findMin().toString();
        assertEquals(ts.length, q.size());
        q.deleteMin();
        assertEquals(ts.length - 1, q.size());

        while (!q.isEmpty()) {
            result += " " + q.findMin();
            q.deleteMin();
        }
        assertEquals("This is a test.", result);
    }

    @Test
    public void findMinAndIsEmpty_AfterLongTextSegmentArrayInitAndOps_ReturnsProperResults() {
        TextSegment[] ts = {
            new TextSegment(35, "Liberty,"),
            new TextSegment(8, "fathers"),
            new TextSegment(600, "mothers"),
            new TextSegment(9, "brought"),
            new TextSegment(150, "men"),
            new TextSegment(900, "words"),
            new TextSegment(3, "and"),
            new TextSegment(500, "equal."),
            new TextSegment(40, "and"),
            new TextSegment(5, "years"),
            new TextSegment(300, "created"),
            new TextSegment(13, "continent,"),
            new TextSegment(4, "seven"),
            new TextSegment(60, "to"),
            new TextSegment(80, "proposition"),
            new TextSegment(11, "on"),
            new TextSegment(200, "are"),
            new TextSegment(90, "that"),
            new TextSegment(14, "a"),
            new TextSegment(1, "Four"),
            new TextSegment(15, "new"),
            new TextSegment(600, "Yankee"),
            new TextSegment(20, "nation,"),
            new TextSegment(2, "score"),
            new TextSegment(25, "conceived"),
            new TextSegment(30, "in"),
            new TextSegment(100, "all"),
            new TextSegment(10, "forth"),
            new TextSegment(50, "dedicated"),
            new TextSegment(7, "our"),
            new TextSegment(700, "Doodle"),
            new TextSegment(12, "this"),
            new TextSegment(6, "ago"),
            new TextSegment(70, "the"),
            new TextSegment(800, "extra"),
        };

        PQueue<TextSegment> q = new PQueue<TextSegment>(TextSegment.class, 100, ts);

        String result = q.findMin().toString();
        assertEquals(ts.length, q.size());
        q.deleteMin();
        assertEquals(ts.length - 1, q.size());

        TextSegment word = q.findMin();
        TextSegment end = new TextSegment(500, "Whatever");
        while (!q.isEmpty() && word.compareTo(end) <= 0) {
            result += " " + word;
            q.deleteMin();
            word = q.findMin(); //this should be ok because we should be stopping before we reach the last few!
        }
        assertEquals("Four score and seven years ago our fathers "+
                     "brought forth on this continent, a new nation, "+
                     "conceived in Liberty, and dedicated to the "+
                     "proposition that all men are created equal.", result);
    }

}