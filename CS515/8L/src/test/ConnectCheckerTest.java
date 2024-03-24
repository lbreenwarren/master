import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.Path;

public class ConnectCheckerTest {

    @Test
    public void testConnectCheckerData0() throws IOException {

        String expected =
            "From a to e : path found.\n" +
            "a b c e \n" +
            "From b to f : path found.\n" +
            "b c f \n" +
            "From d to a : Path not found!\n" +
            "From d to f : Path not found!\n";



        ConnectChecker checker = new ConnectChecker();
        Path path = Paths.get("src", "main", "input_files", "data0");
        BufferedReader file = new BufferedReader(new FileReader(path.toFile()));
        String actual = checker.check(file);

        assertEquals(expected, actual);
    }

    @Test
    public void testConnectCheckerData1() throws IOException {

        String expected =
            "From a to g : path found.\n" +
            "a b c f g \n" +
            "From a to c : path found.\n" +
            "a b c \n" +
            "From g to a : Path not found!\n";

        ConnectChecker checker = new ConnectChecker();
        Path path = Paths.get("src", "main", "input_files", "data1");
        BufferedReader file = new BufferedReader(new FileReader(path.toFile()));
        String actual = checker.check(file);

        assertEquals(expected, actual);
    }

    @Test
    public void testConnectCheckerData2() throws IOException {

        String expected =
            "From a to m : Path not found!\n" +
            "From a to l : Path not found!\n" +
            "From g to b : path found.\n" +
            "g b \n" +
            "From c to m : Path not found!\n";

        ConnectChecker checker = new ConnectChecker();
        Path path = Paths.get("src", "main", "input_files", "data2");
        BufferedReader file = new BufferedReader(new FileReader(path.toFile()));
        String actual = checker.check(file);

        assertEquals(expected, actual);

    }

    @Test
    public void testConnectCheckerData3() throws IOException {

        String expected =
            "From a to m : path found.\n" +
            "a b f h l m \n" +
            "From a to l : path found.\n" +
            "a b f h l \n" +
            "From g to b : path found.\n" +
            "g b \n" +
            "From c to m : path found.\n" +
            "c f h l m \n";

        ConnectChecker checker = new ConnectChecker();
        Path path = Paths.get("src", "main", "input_files", "data3");
        BufferedReader file = new BufferedReader(new FileReader(path.toFile()));
        String actual = checker.check(file);

        assertEquals(expected, actual);

    }
}
