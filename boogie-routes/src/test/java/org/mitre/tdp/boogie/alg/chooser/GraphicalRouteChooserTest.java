package org.mitre.tdp.boogie.alg.chooser;

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
import java.util.function.Function;
import java.util.stream.Stream;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MockObjects;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.ResolvedLeg;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkingStrategy;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenGrapher;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenMapper;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;
import org.mitre.tdp.boogie.alg.split.RouteToken;
import org.mitre.tdp.boogie.alg.split.RouteTokenizer;

class GraphicalRouteChooserTest {

  private static final RouteTokenizer sectionSplitter = RouteTokenizer.faaIfrFormat();

  private static final GraphicalRouteChooser routeChooser = new GraphicalRouteChooser(TokenMapper.standard());

  @Test
  void testMakeLinkableTokens() {

  }

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
    List<ResolvedLeg> legs = split().andThen(apfResolver()::applyTo).andThen(routeChooser::chooseRoute)
        .apply("KIND.BLSTR1.VNY");

    String message = "Check initiation point of leg graph shortest path or the comparator for subsequent paths.";

    assertAll(
        () -> assertEquals("KIND", legs.get(0).routeToken().infrastructureName(), "Incorrect initial section. " + message),
        () -> assertEquals("KIND", legs.get(0).leg().associatedFix().map(Fix::fixIdentifier).orElse(null), "Incorrect initial leg terminator. " + message),
        () -> assertEquals(PathTerminator.IF, legs.get(0).leg().pathTerminator(), "Incorrect initial leg type. " + message),

        () -> assertEquals("BLSTR1", legs.get(1).routeToken().infrastructureName()),
        () -> assertEquals("BNDRR", legs.get(1).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(1).leg().pathTerminator()),

        () -> assertEquals("BLSTR1", legs.get(2).routeToken().infrastructureName()),
        () -> assertEquals("HRRDR", legs.get(2).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(2).leg().pathTerminator()),

        () -> assertEquals("BLSTR1", legs.get(3).routeToken().infrastructureName()),
        () -> assertEquals("GRRDR", legs.get(3).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(3).leg().pathTerminator()),

        () -> assertEquals("BLSTR1", legs.get(4).routeToken().infrastructureName()),
        () -> assertEquals("VNY", legs.get(4).leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(4).leg().pathTerminator())
    );
  }

  private Function<String, List<RouteToken>> split() {
    return sectionSplitter::tokenize;
  }

  private SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> toGraph(String route) {
    List<ResolvedTokens> resolvedTokens = split().andThen(apfResolver()::applyTo).apply(route);
    return routeChooser.constructRouteGraph(routeChooser.toLinkableTokens(resolvedTokens));
  }

  private static RouteTokenResolver apfResolver() {
    Airport kind = airport("KIND", 1.0, 0.0);

    Leg l1 = IF("BNDRR", 0.0, 0.0);
    Leg l2 = TF("HRRDR", 0.0, 1.0);
    Leg l3 = TF("GRRDR", 0.0, 2.0);
    Leg l4 = TF("VNY", 0.0, 3.0);

    Transition t = transition("BLSTR1", TransitionType.COMMON, ProcedureType.SID, Arrays.asList(l1, l2, l3, l4));

    return RouteTokenResolver.standard(
        LookupService.inMemory(singletonList(kind), a -> Stream.of(a.airportIdentifier())),
        LookupService.inMemory(MockObjects.newProcedures(singletonList(t)), p -> Stream.of(p.procedureIdentifier())),
        LookupService.noop(),
        LookupService.inMemory(singletonList(l4.associatedFix().orElseThrow(IllegalStateException::new)), f -> Stream.of(f.fixIdentifier()))
    );
  }
}
