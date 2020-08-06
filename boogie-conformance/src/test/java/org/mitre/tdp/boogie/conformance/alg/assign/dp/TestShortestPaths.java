package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import org.jgrapht.alg.shortestpath.BellmanFordShortestPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.io.DOTExporter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Shortest path main for the {@link SampleGraph}.
 */
public class TestShortestPaths {

    public static void main(String[] args) throws IOException {
        SampleGraph sampleGraph = new SampleGraph();
        writeGraphInDotFormat(sampleGraph);

        Integer start = 1;
        Integer end = 4;

        AllPairsShortestPath<Integer> allPairsShortestPath = new AllPairsShortestPath<>(sampleGraph);
        allPairsShortestPath.computeShortestPaths();
        System.out.println("Shortest path all pairs: " + allPairsShortestPath.shortestPathLength(start, end));

        DynamicProgrammer dp = allPairsShortestPath.dynamicProgrammer();
        System.out.println("Shortest path all pairs DP: " + allPairsShortestPath.shortestPathLengthDp(start, end));

        DijkstraShortestPath<Integer, DefaultWeightedEdge> dijk = new DijkstraShortestPath<>(sampleGraph);
        List<Integer> shortestPathDijk = dijk.getPath(start, end).getVertexList();

        System.out.println("Shortest path Dijkstra: " + dijk.getPath(start, end).getWeight() + " - " + shortestPathDijk.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(",")));

        BellmanFordShortestPath<Integer, DefaultWeightedEdge> bell = new BellmanFordShortestPath<>(sampleGraph);
        List<Integer> shortestPathBell = bell.getPath(start, end).getVertexList();

        System.out.println("Shortest path Bellman-Ford: " + bell.getPath(start, end).getWeight() + " - " + shortestPathBell.stream().map(i -> Integer.toString(i)).collect(Collectors.joining(",")));
    }

    /**
     * Running this will print the graph string to console which you can then copy into the web link below and it would visualize.
     *
     * http://www.webgraphviz.com/
     */
    public static void writeGraphInDotFormat(SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> graph) throws IOException {
        DOTExporter<Integer, DefaultWeightedEdge> exporter = new DOTExporter<>(
                i -> Integer.toString(i),
                i -> Integer.toString(i),
                edge -> Double.toString(graph.getEdgeWeight(edge)));
        String target = System.getProperty("user.dir").concat("/boogie-conformance/src/test/java/org/mitre/tdp/boogie/conformance/alg/assign/dp/sampleGraph.dot");
        FileWriter writer = new FileWriter(target);
        exporter.exportGraph(graph, writer);
    }
}
