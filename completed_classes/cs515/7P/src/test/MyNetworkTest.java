import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * New tests for Java set using Java iterator and set methods.
 */

public class MyNetworkTest {

    BufferedReader openInputFile(String filename) {
        BufferedReader file;
        try {
            String filePath = MyNetworkTest.class.getResource(filename).getPath();
            file = new BufferedReader(new FileReader(filePath));
        } catch (IOException e) {
            System.out.println("error opening " + filename + " for input.");
            return null;
        }
        return file;
    }

    @Test
    void readGraphReadsCorrectNumberOfVerticesEdgesAndHasRightRoot() throws IOException {
        MyNetwork n = new MyNetwork();
        BufferedReader file = openInputFile("data1");
        assertNotNull(file);
        n.readGraph(file);
        Graph<String> g = n.getFullNetwork();
        assertEquals(7, g.getVertices().size());
        assertEquals(10, g.getEdges().size());
        assertEquals("A", n.currentNode());
    }

    @Test

    void pingToStartingNodeReturnsCorrectPath() throws IOException {
        MyNetwork n = new MyNetwork();
        BufferedReader file = openInputFile("data1");
        assertNotNull(file);
        n.readGraph(file);
        String result = n.ping(n.currentNode());
        assertEquals("From: A\n  To: A", result);
    }

    @Test
    void pingToAdjacentNodeReturnsCorrectPath() throws IOException {
        MyNetwork n = new MyNetwork();
        BufferedReader file = openInputFile("data1");
        assertNotNull(file);
        n.readGraph(file);
        String result = n.ping("B");
        assertEquals("From: A\n  To: B", result);
    }

    @Test
    void pingToOtherNodesReturnCorrectPaths() throws IOException {
        MyNetwork n = new MyNetwork();
        BufferedReader file = openInputFile("data1");
        assertNotNull(file);
        n.readGraph(file);
        String result;
        result = n.ping("C");
        assertEquals("From: A\n  To: C", result);
        result = n.ping("D");
        assertEquals("From: A\n  To: C\n  To: F\n  To: D", result);
        result = n.ping("E");
        assertEquals("From: A\n  To: C\n  To: F\n  To: G\n  To: E", result);
        result = n.ping("F");
        assertEquals("From: A\n  To: C\n  To: F", result);
        result = n.ping("G");
        assertEquals("From: A\n  To: C\n  To: F\n  To: G", result);
    }

}
