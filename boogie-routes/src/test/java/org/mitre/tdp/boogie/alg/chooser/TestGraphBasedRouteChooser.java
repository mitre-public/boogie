package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.airport;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.Arrays;
import java.util.List;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.RouteExpanderFactory;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.alg.split.IfrFormatSectionSplitter;
import org.mitre.tdp.boogie.model.ProcedureFactory;

class TestGraphBasedRouteChooser {

  private static final GraphBasedRouteChooser routeChooser = new GraphBasedRouteChooser();

  @Test
  void testConnectedSubsets() {
    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph = toGraph("KIND.BLSTR1.VNY");

    ConnectivityInspector<Leg, DefaultWeightedEdge> conn = new ConnectivityInspector<>(graph);

    String msg = "Check hashing and object references for insert into graph.";

    assertAll(
        () -> assertTrue(conn.isConnected(), msg),
        () -> assertEquals(1, conn.connectedSets().size(), msg),
        () -> assertEquals(6, conn.connectedSets().get(0).size(), msg)
    );
  }

  @Test
  void testShortestPath() {
    List<ExpandedRouteLeg> legs = IfrFormatSectionSplitter.INSTANCE.andThen(apfResolver()::applyTo).andThen(routeChooser).apply("KIND.BLSTR1.VNY").legs();

    String message = "Check initiation point of leg graph shortest path or the comparator for subsequent paths.";

    assertAll(
        () -> assertEquals("KIND", legs.get(0).section(), "Incorrect initial section. " + message),
        () -> assertEquals("KIND", legs.get(0).leg().associatedFix().map(Fix::fixIdentifier).orElse(null), "Incorrect initial leg terminator. " + message),
        () -> assertEquals(PathTerminator.IF, legs.get(0).leg().pathTerminator(), "Incorrect initial leg type. " + message),

        () -> assertEquals("BLSTR1", legs.get(1).section()),
        () -> assertEquals("BNDRR", legs.get(1).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(1).pathTerminator()),

        () -> assertEquals("BLSTR1", legs.get(2).section()),
        () -> assertEquals("HRRDR", legs.get(2).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(2).pathTerminator()),

        () -> assertEquals("BLSTR1", legs.get(3).section()),
        () -> assertEquals("GRRDR", legs.get(3).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(3).pathTerminator()),

        () -> assertEquals("BLSTR1", legs.get(4).section()),
        () -> assertEquals("VNY", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(4).pathTerminator())
    );
  }

  private SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> toGraph(String route) {
    List<ResolvedSection> resolvedSections = IfrFormatSectionSplitter.INSTANCE.andThen(apfResolver()::applyTo).apply(route);
    return routeChooser.constructRouteGraph(resolvedSections);
  }

  private static SectionResolver apfResolver() {
    Airport kind = airport("KIND", 1.0, 0.0);

    Leg l1 = IF("BNDRR", 0.0, 0.0);
    Leg l2 = TF("HRRDR", 0.0, 1.0);
    Leg l3 = TF("GRRDR", 0.0, 2.0);
    Leg l4 = TF("VNY", 0.0, 3.0);

    Transition t = transition("BLSTR1", TransitionType.COMMON, ProcedureType.SID, Arrays.asList(l1, l2, l3, l4));

    return RouteExpanderFactory.newStandardSectionResolver(
        singletonList(l4.associatedFix().orElseThrow(IllegalStateException::new)),
        emptyList(),
        singletonList(kind),
        ProcedureFactory.newProcedures(singletonList(t))
    );
  }
}
