package org.mitre.tdp.boogie.conformance.alg.assign.dp;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

/**
 * Quick and simple sample graph implementation from the linked video.
 *
 * https://www.youtube.com/watch?v=oNI0rf2P9gE
 */
public class SampleGraph extends SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge> {
    public SampleGraph() {
        super(DefaultWeightedEdge.class);
        this.addVertex(1);
        this.addVertex(2);
        this.addVertex(3);
        this.addVertex(4);

        this.addEdge(1, 2);
        this.setEdgeWeight(1, 2, 3);
        this.addEdge(2, 3);
        this.setEdgeWeight(2, 3, 2);
        this.addEdge(3, 4);
        this.setEdgeWeight(3, 4, 1);
        this.addEdge(4, 1);
        this.setEdgeWeight(4, 1, 2);
        this.addEdge(1, 4);
        this.setEdgeWeight(1, 4, 7);
        this.addEdge(3, 1);
        this.setEdgeWeight(3, 1, 5);
        this.addEdge(2, 1);
        this.setEdgeWeight(2, 1, 8);
    }
}
