import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
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

        int lastIndex = l.size() - 1;
        for (int i = lastIndex; i >= 0; --i) {
            res.append(l.get(i) + " ");
        }

        return res;
    }

    public String check(BufferedReader is) throws IOException {
        // build the graph
        Map<Character, List<Character>> graph = new HashMap<Character,List<Character>>();
        char start, end;
        String line;
        StringBuilder ret = new StringBuilder();
        

        while((line = is.readLine()) != null) {
            
            if (line.length() < 1){
                continue;
            }
            
            String[] tokens = line.split(" ");
            
            if (tokens[0].equals("?")) {
                Queue<List<Character>> q = new LinkedList<List<Character>>();
                start = tokens[1].charAt(0);
                end = tokens[2].charAt(0);
                ret.append("From " + start + " to " + end + " : ");
                List<Character> visited = new ArrayList<Character>();
                List<Character> adj = new ArrayList<Character>();
                adj.add(start);
                q.offer(adj);

                while (!q.isEmpty() && q.peek().get(0) != end) {
                    List<Character> path = q.poll();
                    Character vert = path.get(0);
                    
                    if (graph.containsKey(vert) && !visited.contains(vert)){    
                        
                        for(int i = 0; i < graph.get(vert).size(); ++i) {
                            List<Character> newPath = new ArrayList<Character>();
                            newPath.add(graph.get(vert).get(i));
                            
                            for (int k = 0; k < path.size(); ++k) {
                                newPath.add(path.get(k));
                            }
                            
                            q.offer(newPath);
                        }
                        visited.add(vert);
                    }
                }
                if (q.isEmpty()) {
                    ret.append("Path not found!\n");
                } else {
                    ret.append("path found.\n");
                    ret.append(printPath(q.poll(), ret).toString());
                    ret.append("\n");
                }
            
            } else {
                if (graph.containsKey(tokens[0].charAt(0))) {
                    graph.get(tokens[0].charAt(0)).add(tokens[1].charAt(0));
               
                } else {
                    List<Character> newEntry = new ArrayList<Character>();
                    newEntry.add(tokens[1].charAt(0));
                    graph.put(tokens[0].charAt(0), newEntry);
                }
            }

        }

        return ret.toString();

    }
}
