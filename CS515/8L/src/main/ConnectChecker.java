import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.Queue;
import java.util.Vector;
import java.util.List;

public class ConnectChecker {

    public StringBuilder printPath(List<Character> l, StringBuilder result) { // print in reverse order
        StringBuilder res = new StringBuilder();

        // TO IMPLEMENT

        return res;
    }

    public String check(BufferedReader is) throws IOException {
        // build the graph
        Map<Character, List<Character>> graph = new HashMap<Character,List<Character>>();
        char v1, v2, start, end;
        String line;

        while((line = is.readLine()) != null) {
            if (line.length() < 1)
                continue;
            String[] tokens = line.split(" ");

            //REPLACE WITH CODE TO STORE EDGE IN GRAPH


        }

        // display stored graph


        // PROCESS QUERIES

        return "";

    }
}
