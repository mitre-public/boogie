package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.airport;
import static org.mitre.tdp.boogie.MockObjects.airway;
import static org.mitre.tdp.boogie.MockObjects.fix;
import static org.mitre.tdp.boogie.MockObjects.newProcedure;
import static org.mitre.tdp.boogie.MockObjects.transition;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MockObjects;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.ResolvedLeg;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenMapper;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
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
  void testMixedResolvedTokensStillAddAirwayIntraLinks() {

    Leg leftEntry = TF("LEFT", 0., 0.);
    Leg leftBridge = TF("BRIDGE", 0., 1.);
    Leg rightBridge = TF("BRIDGE", 10., 10.);
    Leg rightExit = TF("RIGHT", 10., 11.);

    Airway leftAirway = airway("MIXED", List.of(leftEntry, leftBridge));
    Airway rightAirway = airway("MIXED", List.of(rightBridge, rightExit));
    Airport airport = airport("MIXED", 50., 50.);
    Fix fix = fix("MIXED", 60., 60.);

    ResolvedTokens resolvedTokens = new ResolvedTokens(
        RouteToken.standard("MIXED", 0.),
        List.of(
            ResolvedToken.standardAirway(leftAirway),
            ResolvedToken.standardAirway(rightAirway),
            ResolvedToken.standardFix(fix),
            ResolvedToken.standardAirport(airport)
        )
    );

    SimpleDirectedWeightedGraph<Leg, DefaultWeightedEdge> graph = routeChooser.constructRouteGraph(routeChooser.toLinkableTokens(List.of(resolvedTokens)));

    boolean hasBridge = graph.edgeSet().stream()
        .map(edge -> Pair.of(graph.getEdgeSource(edge), graph.getEdgeTarget(edge)))
        .anyMatch(edge -> edge.first().associatedFix().map(Fix::fixIdentifier).filter("BRIDGE"::equals).isPresent()
            && edge.second().associatedFix().map(Fix::fixIdentifier).filter("BRIDGE"::equals).isPresent()
            && !edge.first().equals(edge.second()));

    assertTrue(hasBridge, "Mixed co-resolved sections should still intra-link airway candidates sharing a fix identifier.");
  }

  @Test
  void testNoLegSidAlternativeBypassesSameNamedFix() {

    Procedure runwayOnlySid = runwayOnlyProcedure(
        "RW01", "SID1", "DEP", ProcedureType.SID,
        IF("SID_START", 0., 0., 10), TF("SID_EXIT", 0., 1., 20)
    );

    List<ResolvedLeg> legs = routeChooser.chooseRoute(List.of(
        resolvedTokens("DEP", 0., ResolvedToken.standardAirport(airport("DEP", 0., 0.))),
        resolvedTokens("SID1", .5, ResolvedToken.sidRunway(runwayOnlySid)),
        resolvedTokens(
            "SID1",
            1.,
            ResolvedToken.sidEnrouteCommon(runwayOnlySid),
            ResolvedToken.directToFix(fix("SID1", 50., 50.))
        ),
        resolvedTokens("LOCAL_EXIT", 2., ResolvedToken.directToFix(fix("LOCAL_EXIT", 0., 1.))),
        resolvedTokens("ARR", 3., ResolvedToken.directToAirport(airport("ARR", 0., 2.)))
    ));

    List<String> fixIdentifiers = associatedFixIdentifiers(legs);

    assertAll(
        () -> assertTrue(legs.stream().anyMatch(leg -> leg.resolvedToken() instanceof ResolvedToken.SidRunway)),
        () -> assertTrue(fixIdentifiers.contains("SID_EXIT")),
        () -> assertTrue(fixIdentifiers.contains("LOCAL_EXIT")),
        () -> assertFalse(fixIdentifiers.contains("SID1"), "The distant same-named fix must not replace the empty SID portion.")
    );
  }

  @Test
  void testNoLegStarAlternativeBypassesSameNamedFix() {

    Procedure runwayOnlyStar = runwayOnlyProcedure(
        "RW02", "STAR1", "ARR", ProcedureType.STAR,
        IF("STAR_ENTRY", 0., 2., 10), TF("STAR_EXIT", 0., 3., 20)
    );

    List<ResolvedLeg> legs = routeChooser.chooseRoute(List.of(
        resolvedTokens("DEP", 0., ResolvedToken.standardAirport(airport("DEP", 0., 0.))),
        resolvedTokens("LOCAL_ENTRY", 1., ResolvedToken.directToFix(fix("LOCAL_ENTRY", 0., 2.))),
        resolvedTokens(
            "STAR1",
            2.,
            ResolvedToken.starEnrouteCommon(runwayOnlyStar),
            ResolvedToken.directToFix(fix("STAR1", 50., 50.))
        ),
        resolvedTokens("STAR1", 2.5, ResolvedToken.starRunway(runwayOnlyStar)),
        resolvedTokens("ARR", 3., ResolvedToken.directToAirport(airport("ARR", 0., 3.)))
    ));

    List<String> fixIdentifiers = associatedFixIdentifiers(legs);

    assertAll(
        () -> assertTrue(legs.stream().anyMatch(leg -> leg.resolvedToken() instanceof ResolvedToken.StarRunway)),
        () -> assertTrue(fixIdentifiers.contains("LOCAL_ENTRY")),
        () -> assertTrue(fixIdentifiers.contains("STAR_ENTRY")),
        () -> assertFalse(fixIdentifiers.contains("STAR1"), "The distant same-named fix must not replace the empty STAR portion.")
    );
  }

  @Test
  void testLeadingNoLegAlternativeCanStartAtFollowingSection() {

    Procedure runwayOnlySid = runwayOnlyProcedure(
        "RW01", "SID1", "DEP", ProcedureType.SID,
        IF("SID_START", 0., 0., 10), TF("SID_EXIT", 0., 1., 20)
    );

    List<ResolvedLeg> legs = routeChooser.chooseRoute(List.of(
        resolvedTokens(
            "SID1",
            0.,
            ResolvedToken.sidEnrouteCommon(runwayOnlySid),
            ResolvedToken.directToFix(fix("SID1", 50., 50.))
        ),
        resolvedTokens("LOCAL_START", 1., ResolvedToken.directToFix(fix("LOCAL_START", 0., 1.))),
        resolvedTokens("ARR", 2., ResolvedToken.directToAirport(airport("ARR", 0., 2.)))
    ));

    List<String> fixIdentifiers = associatedFixIdentifiers(legs);

    assertAll(
        () -> assertTrue(fixIdentifiers.contains("LOCAL_START")),
        () -> assertTrue(fixIdentifiers.contains("ARR")),
        () -> assertFalse(fixIdentifiers.contains("SID1"), "The leading no-leg SID alternative should skip its distant fix.")
    );
  }

  @Test
  void testTrailingNoLegAlternativeCanEndAtPreviousSection() {

    Procedure runwayOnlyStar = runwayOnlyProcedure(
        "RW02", "STAR1", "ARR", ProcedureType.STAR,
        IF("STAR_ENTRY", 0., 2., 10), TF("STAR_EXIT", 0., 3., 20)
    );

    List<ResolvedLeg> legs = routeChooser.chooseRoute(List.of(
        resolvedTokens("DEP", 0., ResolvedToken.standardAirport(airport("DEP", 0., 0.))),
        resolvedTokens("LOCAL_END", 1., ResolvedToken.directToFix(fix("LOCAL_END", 0., 1.))),
        resolvedTokens(
            "STAR1",
            2.,
            ResolvedToken.starEnrouteCommon(runwayOnlyStar),
            ResolvedToken.directToFix(fix("STAR1", 50., 50.))
        )
    ));

    List<String> fixIdentifiers = associatedFixIdentifiers(legs);

    assertAll(
        () -> assertTrue(fixIdentifiers.contains("DEP")),
        () -> assertTrue(fixIdentifiers.contains("LOCAL_END")),
        () -> assertFalse(fixIdentifiers.contains("STAR1"), "The trailing no-leg STAR alternative should skip its distant fix.")
    );
  }

  @Test
  void testShortestPath() {
    List<ResolvedLeg> legs = split().andThen(i -> apfResolver().applyTo(i, (t) -> true)).andThen(routeChooser::chooseRoute)
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
    List<ResolvedTokens> resolvedTokens = split().andThen(i -> apfResolver().applyTo(i, (t) -> true)).apply(route);
    return routeChooser.constructRouteGraph(routeChooser.toLinkableTokens(resolvedTokens));
  }

  private static ResolvedTokens resolvedTokens(String identifier, double index, ResolvedToken... tokens) {
    return new ResolvedTokens(RouteToken.standard(identifier, index), Arrays.asList(tokens));
  }

  private static Procedure runwayOnlyProcedure(
      String transitionIdentifier,
      String procedureIdentifier,
      String airportIdentifier,
      ProcedureType procedureType,
      Leg... legs
  ) {
    return newProcedure(List.of(transition(
        transitionIdentifier,
        procedureIdentifier,
        airportIdentifier,
        TransitionType.RUNWAY,
        procedureType,
        Arrays.asList(legs)
    )));
  }

  private static List<String> associatedFixIdentifiers(List<ResolvedLeg> legs) {
    return legs.stream()
        .flatMap(leg -> leg.leg().associatedFix().stream())
        .map(Fix::fixIdentifier)
        .toList();
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
