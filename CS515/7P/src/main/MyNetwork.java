import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

public class MyNetwork {

    private Graph<String> g;
    private Graph<String> mst;
    LinkedList<String> sshStack = new LinkedList<>();

    public String currentNode() {
        return sshStack.peek();
    }
    
    public MyNetwork() {
        g = new Graph<String>();
        mst = g.generateMST();
    }

    public void readGraph(BufferedReader file) throws IOException{
        String line;
        
        while ((line = file.readLine()) != null) {
            if (line.length() < 1) {
                continue;
            }
            String[] tokens = line.split(" ");
            String first = tokens[0];
            String second = tokens[1];
            int weight = Integer.parseInt(tokens[2]);
            g.addVertex(first);
            g.addVertex(second);
            g.addEdge(first, second, weight);
        }
        sshStack.push(g.getRoot());
    }

    public Graph<String> getFullNetwork() {
        return g;
    }

    public Graph<String> getMST() {
        return mst;
    }

    public String ping(String target) {
        List<String> list = g.findPath(currentNode(), target);
        String ret = "From: " + currentNode();
        if (list.size() == 1) {
            ret += "\n  To: " + list.get(0);
        } else {
            for (int i = 1; i < list.size(); ++i) {
                ret += "\n  To: ";
                ret += list.get(i);
            }
        }
        return ret;
    }

    public void ssh(String target) {
        sshStack.push(target);
    }

    public void exit() {
        sshStack.pop();
    }

    public static void main(String[] args) throws IOException {
        if( args.length != 1) {
            System.out.println("usage: java MyNetwork <filename>");
            return;
        }

        BufferedReader file;
        try {
            Path filePath = Paths.get(args[0]);
            file = new BufferedReader(new FileReader(filePath.toFile()));
        } catch (IOException e) {
            System.out.println("error opening " + args[0] + " for input.");
            return;
        }

        MyNetwork n = new MyNetwork();
        n.readGraph(file);

        String commandLine, currentNode, target;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        while((currentNode = n.currentNode()) != null) {
            System.out.println("<" + currentNode + ">#");

            try {
               commandLine = in.readLine();
            } catch (IOException e) {
               System.out.println("Error reading command");
               e.printStackTrace();
               continue;
            }

            String[] tokens = commandLine.split(" ");
            if (tokens[0].equals("ping")) {
                if (n.g.containsVertex(tokens[1])) {
                    System.out.println(n.ping(tokens[1]));
                } else {
                    System.out.println("Cannot find node. Available nodes are:");
                    System.out.println(n.getFullNetwork());
                }
            } else if (tokens[0].equals("ssh")) {
                if (n.g.containsVertex(tokens[1])) {
                    n.ssh(tokens[1]);
                } else {
                    System.out.println("Cannot find node. Available nodes are:");
                    System.out.println(n.getFullNetwork());
                }
            } else if (tokens[0].equals("exit")) {
                n.exit();
            } else {
                System.out.println("unknown command");
            }
        }
    }
}
