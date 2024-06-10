import Tree.BTree;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import com.github.difflib.text.DiffRow;
import com.github.difflib.text.DiffRowGenerator;

import static org.junit.jupiter.api.Assertions.*;

import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.ByteArrayInputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.io.IOException;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

public class BTreeTest {
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
    public void BTreeTest0() throws IOException {
        BTree B = new BTree();

        B.insert(1, "apple");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(2, "baby");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(3, "carrot");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(4, "dog");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(5, "eat");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(6, "fly");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(7, "giggle");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(8, "hello");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(9, "island");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(10, "joy");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(11, "kind");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(12, "light");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(13, "merry");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(14, "nice");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(15, "orange");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(16, "puppy");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(17, "quite");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(18, "rose");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(19, "sandy");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(20, "tree");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(21, "uncle");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(22, "viola");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(23, "wonderful");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(24, "xxoo");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(25, "young");
        System.out.println("----printing tree----");
        B.dump();

        B.insert(26, "zebra");
        System.out.println("----printing tree----");
        B.dump();

        File file = new File("src/resources/btreetest0.out");
        InputStream is = new FileInputStream(file);
        String expected = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        System.err.println("Test Case input: btreetest0");

        String actual = testOut.toString();
        System.err.println("Observed output for btreetest0");
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
    public void BTreeTest1() throws IOException {
        BTree B = new BTree();

        B.insert(1, "date");

        B.insert(3, "watermelon");

        B.insert(5, "starfruit");

        B.insert(8, "pear");

        B.insert(2, "apple");

        B.insert(11, "pineapple");

        B.insert(12, "banana");

        B.insert(18, "orange");

        B.insert(20, "kiwi");

        B.insert(31, "cranberry");

        B.insert(9, "grape");

        B.insert(14, "persimmon");

        B.insert(21, "grapefruit");

        B.insert(19, "papaya");

        B.insert(24, "pomegranates");

        B.insert(7, "cherry");

        B.insert(22, "blueberry");

        B.insert(13, "lychee");

        B.insert(25, "mango");

        B.insert(30, "coconut");

        B.insert(4, "clementine");

        B.insert(35, "peach");

        B.insert(17, "plum");

        B.insert(37, "raspberry");

        B.insert(23, "strawberry");
        B.dump();

        File file = new File("src/resources/btreetest1.out");
        InputStream is = new FileInputStream(file);
        String expected = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        System.err.println("Test Case input: btreetest1");

        String actual = testOut.toString();
        System.err.println("Observed output for btreetest1");
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
    public void BTreeTest2() throws IOException {
        BTree B = new BTree();

        for (int i = 10; i >= 0; i--) {
            B.insert(i, "positive");
            if (i > 0)
                B.insert(-1 * i, "negative");
        }

        B.dump();
        File file = new File("src/resources/btreetest2.out");
        InputStream is = new FileInputStream(file);
        String expected = new String(is.readAllBytes(), StandardCharsets.UTF_8);

        System.err.println("Test Case input: btreetest2");

        String actual = testOut.toString();
        System.err.println("Observed output for btreetest2");
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
