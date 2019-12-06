package org.mitre.tdp.boogie.alg.graph;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.mitre.tdp.boogie.Leg;

public class LegGraph extends SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> {
  public LegGraph() {
    super(DefaultWeightedEdge.class);
  }
}
