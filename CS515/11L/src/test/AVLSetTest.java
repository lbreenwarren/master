import Set.AVLSet;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

public class AVLSetTest {

    private final InputStream systemIn = System.in;
    private final PrintStream systemOut = System.out;

    private ByteArrayInputStream testIn;
    private ByteArrayOutputStream testOut;

    @BeforeEach
    public void setup() {
        testOut = new ByteArrayOutputStream();
        System.setOut(new PrintStream(testOut));
    }

    @AfterEach
    public void cleanUpStreams() {
        System.setOut(systemOut);
        System.setIn(systemIn);
    }

    private List<String> split(String content) {
        return Arrays.asList(content.split("\n"));
    }

    private void print(List<DiffRow> diffRows) {

        for (int r = 0; r < diffRows.size(); r++) {
            DiffRow row = diffRows.get(r);

            if (!row.getTag().equals(DiffRow.Tag.EQUAL)) {

                String expected = row.getOldLine();
                String actual = row.getNewLine();

                if (r != 0) {
                    String prevLine = diffRows.get(r - 1).getNewLine();
                    System.err.println(prevLine);
                }

                System.err.printf("[MISMATCH] %d: Expected: \"%s\", Actual: \"%s\"%n",
                        r, expected, actual);

                if (r + 1 < diffRows.size()) {
                    String nextLine = diffRows.get(r + 1).getNewLine();
                    System.err.println(nextLine);
                }
                System.err.println();
            }
        }
    }

    @Test
    public void setTest0() throws IOException {
        AVLSet t = new AVLSet();
        int numbers[] = { 50, 25, 75, 10, 30, 60, 80, 5, 15, 27, 55, 1 };

        int size = 12;
        // try to insert a sorted set of numbers
        for (int i = 0; i < size; i++) {
            t.insert(numbers[i]);
            assertEquals(i + 1, t.size());
            t.printTree();
            System.out.println("************************************");
        }

        t.insert(80); // re-insert 80
        assertEquals(12, t.size());
        t.printTree();
        System.out.println("************************************");
        File file = new File("src/resources/setTest0.out");
        InputStream is = new FileInputStream(file);
        String expected = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        System.err.println("Test Case input: setTest0");

        String actual = testOut.toString();
        System.err.println("Observed output for setTest0");
        System.err.println(actual);

        DiffRowGenerator generator = DiffRowGenerator.create()
                .ignoreWhiteSpaces(true)
                .columnWidth(Integer.MAX_VALUE) // do not wrap
                .build();
        List<DiffRow> rows = generator.generateDiffRows(split(expected),
                split(actual));

        List<DiffRow> errors = rows.stream()
                .filter(r -> !r.getTag().equals(DiffRow.Tag.EQUAL))
                .collect(Collectors.toList());

        if (errors.size() != 0) {
            print(rows);
        }
        assertTrue(errors.size() == 0);
    }

    @Test
    public void setTest1() throws IOException {
        AVLSet t = new AVLSet();
        int numbers[] = { 20, 25, 75, 29, 30, 60, 80, 12, 15, 27, 55, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11 };

        int size = 22;
        // try to insert a sorted set of numbers
        for (int i = 0; i < size; i++) {
            t.insert(numbers[i]);
            assertEquals(i + 1, t.size());
            t.printTree();
            System.out.println("************************************");
        }

        t.insert(80); // re-insert 80
        assertEquals(size, t.size());
        t.printTree();
        System.out.println("************************************");

        File file = new File("src/resources/setTest1.out");
        InputStream is = new FileInputStream(file);
        String expected = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        System.err.println("Test Case input: setTest1");

        String actual = testOut.toString();
        System.err.println("Observed output for setTest1");
        System.err.println(actual);

        DiffRowGenerator generator = DiffRowGenerator.create()
                .ignoreWhiteSpaces(true)
                .columnWidth(Integer.MAX_VALUE) // do not wrap
                .build();
        List<DiffRow> rows = generator.generateDiffRows(split(expected),
                split(actual));

        List<DiffRow> errors = rows.stream()
                .filter(r -> !r.getTag().equals(DiffRow.Tag.EQUAL))
                .collect(Collectors.toList());

        if (errors.size() != 0) {
            print(rows);
        }
        assertTrue(errors.size() == 0);
    }

    @Test
    public void setTest3() throws IOException {
        AVLSet t = new AVLSet();
        int numbers[] = { 50, 25, 75, 10, 30, 60, 80, 5, 15, 27, 55, 1 };

        int size = 12;
        // try to insert a sorted set of numbers
        for (int i = 0; i < size; i++) {
            t.insert(numbers[i]);
            assertEquals(i + 1, t.size());
        }

        t.printTree();

        System.out.println("************************************");

        t.erase(80);
        assertEquals(11, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(80); // re-erase 80
        assertEquals(11, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(15);
        assertEquals(10, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(1);
        assertEquals(9, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(5);
        assertEquals(8, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(10);
        assertEquals(7, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(30);
        assertEquals(6, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(75);
        assertEquals(5, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(25);
        assertEquals(4, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(50);
        assertEquals(3, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(27);
        assertEquals(2, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(55);
        assertEquals(1, t.size());
        t.printTree();
        System.out.println("************************************");

        t.erase(60);
        assertEquals(0, t.size());
        t.printTree();
        System.out.println("************************************");

        File file = new File("src/resources/setTest2.out");
        InputStream is = new FileInputStream(file);
        String expected = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        System.err.println("Test Case input: setTest2");

        String actual = testOut.toString();
        System.err.println("Observed output for setTest2");
        System.err.println(actual);

        DiffRowGenerator generator = DiffRowGenerator.create()
                .ignoreWhiteSpaces(true)
                .columnWidth(Integer.MAX_VALUE) // do not wrap
                .build();
        List<DiffRow> rows = generator.generateDiffRows(split(expected),
                split(actual));

        List<DiffRow> errors = rows.stream()
                .filter(r -> !r.getTag().equals(DiffRow.Tag.EQUAL))
                .collect(Collectors.toList());

        if (errors.size() != 0) {
            print(rows);
        }
        assertTrue(errors.size() == 0);
    }

    @Test
    public void setTest4() throws IOException {
        AVLSet s1 = new AVLSet();

        for (int i = 1; i < 100; i++) {
            s1.insert(i);
        }
        s1.printTree();
        File file = new File("src/resources/setTest3.out");
        InputStream is = new FileInputStream(file);
        String expected = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        System.err.println("Test Case input: setTest3");

        String actual = testOut.toString();
        System.err.println("Observed output for setTest3");
        System.err.println(actual);

        DiffRowGenerator generator = DiffRowGenerator.create()
                .ignoreWhiteSpaces(true)
                .columnWidth(Integer.MAX_VALUE) // do not wrap
                .build();
        List<DiffRow> rows = generator.generateDiffRows(split(expected),
                split(actual));

        List<DiffRow> errors = rows.stream()
                .filter(r -> !r.getTag().equals(DiffRow.Tag.EQUAL))
                .collect(Collectors.toList());

        if (errors.size() != 0) {
            print(rows);
        }
        assertTrue(errors.size() == 0);

    }
}
