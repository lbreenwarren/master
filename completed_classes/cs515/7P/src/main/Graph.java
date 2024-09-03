import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;
import java.util.TreeMap;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import set.DisjointSet;

public class Graph<T extends Comparable<T>> {

    class Edge implements Comparable<Edge> {
        public final T first;
        public final T second;
        public final int weight;

        public Edge( T v1, T v2, int w) {
            first = v1;
            second = v2;
            weight = w;
        }

        @Override
        public int compareTo(Edge rhs) {
            if (rhs.weight < this.weight) {
                return 1;
            } else if (rhs.weight > this.weight) {
                return -1;
            } else {
                return 0;
            }
        }

        @Override
        @SuppressWarnings("unchecked")
        public boolean equals(Object any) {
            Edge cmp = (Edge)any;
            if (cmp.first.equals(this.first) && cmp.second.equals(this.second) && cmp.weight == this.weight) {
                return true;
            }
            return false;
        }

    };

    T _root;
    Collection<T> vertices;
    Collection<Edge> edges;


    public Graph() {
        _root = null;
        vertices = new LinkedList<T>();
        edges = new LinkedList<Edge>();
    }

    public Graph(T _root, Collection<T> verts, Collection<Edge> edges) {
        this.edges = edges;
        this._root = _root;
        this.vertices = verts;
    }

    public T getRoot() {
        if (_root != null) {
            return _root;
        }
        return null;
    }

    public Collection<T> getVertices() {
        return vertices;
    }

    public Collection<Edge> getEdges() {
        return edges;
    }

    Iterator<T> iterator() {
        return vertices.iterator();
    }

    // does nothing if vertex already in graph
    void addVertex(T v) {
        if (vertices.contains(v)) {
            return;
        } else {
            if (_root == null) {
                _root = v;
            }
            vertices.add(v);
        }
    }

    boolean containsVertex(T v) {
        if (vertices.contains(v)) {
            return true;
        }
        return false;
    }

    void addEdge( T v1, T v2, int weight) {
        Edge newEdge1 = new Edge(v1, v2, weight);
        edges.add(newEdge1);
    }

    // creates a minimum spanning tree from the graph
    // use graph = graph.generateMST(); for easy conversion
    Graph<T> generateMST( ) {
        DisjointSet<T> set = new DisjointSet<T>();
        LinkedList<Edge> newEdges = new LinkedList<Edge>();
	    Iterator<T> it = vertices.iterator();
        while (it.hasNext()) {
            set.createSet(it.next());
        }
        PriorityQueue<Edge> q = new PriorityQueue<Edge>(edges);
        
        while (!q.isEmpty() && set.size() != 1) {
            Edge edge = q.poll();
            if (set.findSet(edge.first) != set.findSet(edge.second)) {
                set.unionSets(edge.first, edge.second);
                newEdges.add(new Edge(edge.first, edge.second, edge.weight));
            }
        }
        return new Graph<T>(_root, vertices, newEdges);
    }

    // returned lists, including start and target, the vertices along path
    // breadth-first traversal; best if called on MST
    List<T> findPath( T start, T target) {
        Graph<T> graph = generateMST();
        Queue<LinkedList<T>> q = new LinkedList<LinkedList<T>>();
        Collection<T> checked = new LinkedList<T>();
        checked.add(start);
        LinkedList<T> init = new LinkedList<T>();
        init.add(start);
        q.add(init);

        while (!q.isEmpty() && !q.peek().getLast().equals(target)){  
            LinkedList<T> path = q.poll();
            T vert = path.getLast();
            Iterator<Edge> it = graph.getEdges().iterator();
            while (it.hasNext()) {
                Edge edge = it.next();
                if (edge.first.equals(vert)) {
                    if (!checked.contains(edge.second)) {
                        LinkedList<T> newPath = new LinkedList<T>(path);
                        newPath.add(edge.second);
                        q.offer(newPath);
                    }
                }
                if (edge.second.equals(vert)) {
                    if (!checked.contains(edge.first)) {
                        LinkedList<T> newPath = new LinkedList<T>(path);
                        newPath.add(edge.first);
                        q.offer(newPath);
                    }
                }
                
            }
            checked.add(vert);
        }

        if (q.isEmpty()) {
            return null;
        } else {
            return q.poll();
        }

    }
}
