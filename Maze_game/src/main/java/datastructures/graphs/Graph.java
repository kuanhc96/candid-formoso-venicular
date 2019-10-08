package datastructures.graphs;

import datastructures.dictionaries.ArrayDictionary;
import datastructures.dictionaries.ChainedHashDictionary;
import datastructures.dictionaries.IDictionary;
import datastructures.dictionaries.KVPair;
import datastructures.disjointsets.ArrayDisjointSets;
import datastructures.lists.DoubleLinkedList;
import datastructures.lists.IList;
import datastructures.priorityqueues.ArrayHeapPriorityQueue;
import datastructures.priorityqueues.IPriorityQueue;
import datastructures.sets.ChainedHashSet;
import datastructures.sets.ISet;



/**
 * Represents an undirected, weighted graph, possibly containing self-loops, parallel edges,
 * and unconnected components.
 *
 * @param <V> the type of the vertices
 * @param <E> the type of the additional data contained in edges
 *            <p>
 *            Note: This class is not meant to be a full-featured way of representing a graph.
 *            We stick with supporting just a few, core set of operations needed for the
 *            remainder of the project.
 */
public class Graph<V, E> {
    /*
    Feel free to add as many fields, private helper methods, and private inner classes as you want.

    And of course, as always, you may also use any of the data structures and algorithms we've
    implemented so far.

    Note: If you plan on adding a new class, please be sure to make it a private static inner class
    contained within this file. Our testing infrastructure works by copying specific files from your
    project to ours, and if you add new files, they won't be copied and your code will not compile.
    */

    private IDictionary<V, IList<Edge<V, E>>> adjacencyList;
    private IList<Edge<V, E>> edges;
    private int numVertices;
    private int numEdges;


    /**
     * Constructs a new empty graph.
     */
    public Graph() {
        adjacencyList = new ChainedHashDictionary<>();
        // vertices = null;
        edges = new DoubleLinkedList<>();
        numVertices = 0;
        numEdges = 0;
        //vertices = new ArrayDisjointSets();
        // MST = new ChainedHashSet<>();
    }

    /**
     * Adds a vertex to this graph. If the vertex is already in the graph, does nothing.
     */
    public void addVertex(V vertex) {
        if (!adjacencyList.containsKey(vertex)) {
            adjacencyList.put(vertex, new DoubleLinkedList<>());
            numVertices++;
        }
    }

    /**
     * Adds a new edge to the graph, with null data.
     * <p>
     * Every time this method is (successfully) called, a unique edge is added to the graph; even if
     * another edge between the same vertices and with the same weight and data already exists, a
     * new edge will be created and added (where `newEdge.equals(oldEdge)` is false).
     *
     * @throws IllegalArgumentException if `weight` is null
     * @throws IllegalArgumentException if either vertex is not contained in the graph
     */
    public void addEdge(V vertex1, V vertex2, double weight) {
        this.addEdge(vertex1, vertex2, weight, null);
    }

    /**
     * Adds a new edge to the graph with the given data.
     * <p>
     * Every time this method is (successfully) called, a unique edge is added to the graph; even if
     * another edge between the same vertices and with the same weight and data already exists, a
     * new edge will be created and added (where `newEdge.equals(oldEdge)` is false).
     *
     * @throws IllegalArgumentException if `weight` is null
     * @throws IllegalArgumentException if either vertex is not contained in the graph
     */
    public void addEdge(V vertex1, V vertex2, double weight, E data) {
        if (!adjacencyList.containsKey(vertex1) || !adjacencyList.containsKey(vertex2)) {
            throw new IllegalArgumentException();
        }

        if (weight < 0) {
            throw new IllegalArgumentException();
        }

        Edge<V, E> newEdge1 = new Edge<V, E>(vertex1, vertex2, weight, data);
        Edge<V, E> newEdge2 = new Edge<V, E>(vertex2, vertex1, weight, data);
        adjacencyList.get(vertex1).add(newEdge1);
        adjacencyList.get(vertex2).add(newEdge2);

        edges.add(newEdge1);
        edges.add(newEdge2);
        numEdges++;
    }

    /**
     * Returns the number of vertices contained within this graph.
     */
    public int numVertices() {
        return numVertices;
    }

    /**
     * Returns the number of edges contained within this graph.
     */
    public int numEdges() {
        return numEdges;
    }

    /**
     * Returns the set of all edges that make up the minimum spanning tree of this graph.
     * <p>
     * If there exists multiple valid MSTs, returns any one of them.
     * <p>
     * Precondition: the graph does not contain any unconnected components.
     */
    public ISet<Edge<V, E>> findMinimumSpanningTree() {
        ArrayDisjointSets<V> vertices = new ArrayDisjointSets();
        for (KVPair<V, IList<Edge<V, E>>> vertex : adjacencyList) {
            vertices.makeSet(vertex.getKey());
        }
        IPriorityQueue<Edge<V, E>> sortedEdges = new ArrayHeapPriorityQueue<>();
        for (Edge<V, E> edge : edges) {
            sortedEdges.add(edge);
        }
        ISet<Edge<V, E>> mst = new ChainedHashSet<>();
        for (Edge<V, E> edge : sortedEdges) {
            if (vertices.union(edge.getVertex1(), edge.getVertex2())) {
                mst.add(edge);


            }
        }
        return mst;
    }

    /**
     * Returns the edges that make up the shortest path from `start` to `end`.
     * <p>
     * The first edge in the output list will be the edge leading out of the `start` node; the last
     * edge in the output list will be the edge connecting to the `end` node.
     * <p>
     * Returns an empty list if the start and end vertices are the same.
     *
     * @throws NoPathExistsException    if there does not exist a path from `start` to `end`
     * @throws IllegalArgumentException if `start` or `end` is null or not in the graph
     */
    public IList<Edge<V, E>> findShortestPathBetween(V start, V end) {
        if (start == null || end == null ||
                !adjacencyList.containsKey(start) || !adjacencyList.containsKey(end)) {
            throw new IllegalArgumentException();
        }

        IList<Edge<V, E>> output = new DoubleLinkedList<>();

        IPriorityQueue<Vertex<V, E>> vertices = new ArrayHeapPriorityQueue<>();
        IDictionary<V, Vertex<V, E>> findVertices = new ArrayDictionary<>();
        IDictionary<V, Vertex<V, E>> allVertices = new ArrayDictionary<>();

        if (start.equals(end)) {
            return output;
        }

        for (KVPair<V, IList<Edge<V, E>>> vertex : adjacencyList) {
            if (vertex.getKey().equals(start)) {
                Vertex<V, E>  input = new Vertex(vertex.getKey(), vertex.getValue(), 0, null);
                vertices.add(input);
                allVertices.put(vertex.getKey(), input);
            } else {
                Vertex<V, E> input = new Vertex(vertex.getKey(), vertex.getValue(), Double.POSITIVE_INFINITY, null);
                vertices.add(input);
                allVertices.put(vertex.getKey(), input);
            }
        }

        while (findVertices.size() < allVertices.size() && !findVertices.containsKey(end)) {
            Vertex<V, E> closest = vertices.removeMin();
            V current = closest.getVertex();
            if (!findVertices.containsKey(current)) {
                if (!current.equals(end)) {
                    for (Edge<V, E> edge : adjacencyList.get(current)) {
                        V otherVertex = edge.getOtherVertex(current);
                        double newWeight = closest.getWeight() + edge.getWeight();
                        double oldWeight = allVertices.get(otherVertex).getWeight();
                        if (oldWeight > newWeight) {
                            Vertex<V, E> update =
                                    new Vertex<>(otherVertex, adjacencyList.get(otherVertex), newWeight, edge);
                            allVertices.put(otherVertex, update);
                            vertices.add(update);
                        }
                    }
                }
                findVertices.put(current, closest);

            }
        }
        if (!findVertices.containsKey(end) || Double.POSITIVE_INFINITY <= findVertices.get(end).getWeight()) {
            throw new NoPathExistsException();
        }

        Vertex<V, E> last = findVertices.get(end);
        Edge<V, E> predecessor = last.getPredecessor();
        while (last.getPredecessor() != null) {
            Edge<V, E> temp = last.getPredecessor();
            output.insert(0, temp);
            last = findVertices.get(temp.getOtherVertex(last.getVertex()));
        }
        return output;
    }

private static class Vertex<V, E>
        implements Comparable<Vertex<V, E>> {
    private final IList<Edge<V, E>> data;
    private final V vertex;
    private double weight;
    private Edge<V, E> predecessor;

    public Vertex(V vertex, IList<Edge<V, E>> data, double weight, Edge<V, E> predecessor) {
        this.data = data;
        this.vertex = vertex;
        this.weight = weight;
        this.predecessor = predecessor;
    }

    public double getWeight() {
        return this.weight;
    }

    public Edge<V, E> getPredecessor() {
        return this.predecessor;
    }

    public V getVertex() {
        return this.vertex;
    }

    public int compareTo(Vertex<V, E> other) {
        return Double.compare(this.weight, other.weight);
    }
}
}