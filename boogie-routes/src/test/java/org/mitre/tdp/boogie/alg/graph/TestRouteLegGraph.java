package org.mitre.tdp.boogie.alg.graph;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.test.MockObjects.IF;
import static org.mitre.tdp.boogie.test.MockObjects.TF;
import static org.mitre.tdp.boogie.test.MockObjects.airport;
import static org.mitre.tdp.boogie.test.MockObjects.transition;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.alg.resolve.resolver.RouteResolver;
import org.mitre.tdp.boogie.alg.resolve.resolver.RouteResolverFactory;
import org.mitre.tdp.boogie.alg.split.IfrFormatSectionSplitter;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

class TestRouteLegGraph {

  @Test
  void testConnectedSubsets() {
    String route = "KIND.BLSTR1.VNY";
    RouteLegGraph graph = getGraph(route, apfResolver());

    ConnectivityInspector<GraphableLeg, DefaultWeightedEdge> conn = new ConnectivityInspector<>(graph);

    String msg = "Check hashing and object references for insert into graph.";

    assertAll(
        () -> assertTrue(conn.isConnected(), msg),
        () -> assertEquals(1, conn.connectedSets().size(), msg),
        () -> assertEquals(6, conn.connectedSets().get(0).size(), msg)
    );
  }

  @Test
  void testShortestPath() {
    String route = "KIND.BLSTR1.VNY";
    RouteLegGraph graph = getGraph(route, apfResolver());

    GraphPath<GraphableLeg, DefaultWeightedEdge> path = graph.shortestPath();

    List<GraphableLeg> legs = path.getVertexList();

    String message = "Check initiation point of leg graph shortest path or the comparator for subsequent paths.";

    assertAll(
        () -> assertEquals("KIND", legs.get(0).split().value(), "Incorrect initial section. " + message),
        () -> assertEquals("KIND", legs.get(0).leg().associatedFix().map(Fix::fixIdentifier).orElse(null), "Incorrect initial leg terminator. " + message),
        () -> assertEquals(PathTerminator.IF, legs.get(0).leg().pathTerminator(), "Incorrect initial leg type. " + message),

        () -> assertEquals("BLSTR1", legs.get(1).split().value()),
        () -> assertEquals("BNDRR", legs.get(1).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("BLSTR1", legs.get(2).split().value()),
        () -> assertEquals("HRRDR", legs.get(2).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("BLSTR1", legs.get(3).split().value()),
        () -> assertEquals("GRRDR", legs.get(3).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("BLSTR1", legs.get(4).split().value()),
        () -> assertEquals("VNY", legs.get(4).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("VNY", legs.get(5).split().value(), "Incorrect final section. " + message),
        () -> assertEquals("VNY", legs.get(5).leg().associatedFix().map(Fix::fixIdentifier).orElse(null), "Incorrect final leg terminator. " + message),
        () -> assertEquals(PathTerminator.IF, legs.get(0).leg().pathTerminator(), "Incorrect final leg type. " + message),

        () -> assertEquals(60.007, path.getWeight(), 0.01, "Incorrect resolved shortest path weight. Check leg weight functions.")
    );
  }

  private static RouteResolver apfResolver() {
    Airport kind = airport("KIND", 1.0, 0.0);

    Leg l1 = IF("BNDRR", 0.0, 0.0);
    Leg l2 = TF("HRRDR", 0.0, 1.0);
    Leg l3 = TF("GRRDR", 0.0, 2.0);
    Leg l4 = TF("VNY", 0.0, 3.0);

    Transition t = transition("BLSTR1", TransitionType.COMMON, ProcedureType.SID, Arrays.asList(l1, l2, l3, l4));

    return new RouteResolverFactory(
        singletonList(l4.associatedFix().orElseThrow(IllegalStateException::new)),
        emptyList(),
        singletonList(kind),
        singletonList(t)
    ).newResolver();
  }

  private static RouteLegGraph getGraph(String route, RouteResolver resolver) {
    List<SectionSplit> splits = new IfrFormatSectionSplitter().splits(route);
    return graphFactory.newLegGraphFor(resolver.apply(splits));
  }

  private static final LegGraphFactory graphFactory = new LegGraphFactory();
}
