package org.mitre.tdp.boogie.alg.graph;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.RouteExpander;
import org.mitre.tdp.boogie.alg.resolve.ResolvedRoute;
import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.airport;
import static org.mitre.tdp.boogie.MockObjects.transition;

public class TestLegGraph {

  private static final Logger LOG = LoggerFactory.getLogger(TestLegGraph.class);

  private static RouteExpander apfExpander() {
    Airport kind = airport("KIND", 1.0, 0.0);

    Leg l1 = IF("BNDRR", 0.0, 0.0);
    Leg l2 = TF("HRRDR", 0.0, 1.0);
    Leg l3 = TF("GRRDR", 0.0, 2.0);
    Leg l4 = TF("VNY", 0.0, 3.0);

    Transition t = transition("BLSTR1", TransitionType.COMMON, ProcedureType.SID, Arrays.asList(l1, l2, l3, l4));
    return RouteExpander.with(singletonList(l4.pathTerminator()), emptyList(), singletonList(kind), singletonList(t));
  }

  private static LegGraph getGraph(String route, RouteExpander expander) {
    List<SectionSplit> splits = SectionSplitter.splits(route);

    SectionResolver resolver = SectionResolver.with(expander);
    ResolvedRoute resolved = resolver.resolve(splits);

    return LegGraphFactory.build(resolved);
  }

  @Test
  public void testConnectedSubsets() {
    String route = "KIND.BLSTR1.VNY";
    LegGraph graph = getGraph(route, apfExpander());

    ConnectivityInspector<SectionSplitLeg, DefaultWeightedEdge> conn = new ConnectivityInspector<>(graph);

    String msg = "Check hashing and object references for insert into graph.";
    assertTrue(conn.isConnected(), msg);
    assertEquals(1, conn.connectedSets().size(), msg);
    assertEquals(6, conn.connectedSets().get(0).size(), msg);
  }

  @Test
  public void testShortestPath() {
    String route = "KIND.BLSTR1.VNY";
    LegGraph graph = getGraph(route, apfExpander());

    GraphPath<SectionSplitLeg, DefaultWeightedEdge> path = graph.shortestPath();

    List<SectionSplitLeg> legs = path.getVertexList();

    String message = "Check initiation point of leg graph shortest path or the comparator for subsequent paths.";
    assertEquals("KIND", legs.get(0).sectionSplit().value(), "Incorrect initial section. " + message);
    assertEquals("KIND", legs.get(0).leg().pathTerminator().identifier(), "Incorrect initial leg terminator. " + message);
    assertEquals(LegType.IF, legs.get(0).leg().type(), "Incorrect initial leg type. " + message);

    assertEquals("BLSTR1", legs.get(1).sectionSplit().value());
    assertEquals("BNDRR", legs.get(1).leg().pathTerminator().identifier());

    assertEquals("BLSTR1", legs.get(2).sectionSplit().value());
    assertEquals("HRRDR", legs.get(2).leg().pathTerminator().identifier());

    assertEquals("BLSTR1", legs.get(3).sectionSplit().value());
    assertEquals("GRRDR", legs.get(3).leg().pathTerminator().identifier());

    assertEquals("BLSTR1", legs.get(4).sectionSplit().value());
    assertEquals("VNY", legs.get(4).leg().pathTerminator().identifier());

    assertEquals("VNY", legs.get(5).sectionSplit().value(), "Incorrect final section. " + message);
    assertEquals("VNY", legs.get(5).leg().pathTerminator().identifier(), "Incorrect final leg terminator. " + message);
    assertEquals(LegType.IF, legs.get(0).leg().type(), "Incorrect final leg type. " + message);

    assertEquals(60.007, path.getWeight(), 0.01, "Incorrect resolved shortest path weight. Check leg weight functions.");
  }
}
