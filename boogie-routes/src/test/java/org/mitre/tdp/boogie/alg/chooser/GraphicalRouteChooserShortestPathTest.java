package org.mitre.tdp.boogie.alg.chooser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.fix;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.ResolvedLeg;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkableToken;
import org.mitre.tdp.boogie.alg.chooser.graph.LinkedLegs;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenMapper;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;
import org.mitre.tdp.boogie.alg.resolve.ResolvedTokens;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class GraphicalRouteChooserShortestPathTest {

  @Test
  void choosesGlobalMinimumAcrossMultipleEntriesAndExits() {
    Leg entry1 = TF("ENTRY1", 0., 0.);
    Leg entry2 = TF("ENTRY2", 0., 1.);
    Leg exit1 = TF("EXIT1", 0., 2.);
    Leg exit2 = TF("EXIT2", 0., 3.);

    List<ResolvedLeg> route = chooseRoute(List.of(entry1, entry2), List.of(exit1, exit2),
        new LinkedLegs(entry1, exit1, 8.),
        new LinkedLegs(entry1, exit2, 3.),
        new LinkedLegs(entry2, exit1, 7.),
        new LinkedLegs(entry2, exit2, 1.));

    assertEquals(List.of(entry2, exit2), route.stream().map(ResolvedLeg::leg).toList());
    assertEquals(List.of("ENTRIES", "EXITS"), route.stream().map(leg -> leg.routeToken().infrastructureName()).toList());
  }

  @Test
  void equalWeightsKeepTheFirstEntryAndExitInEncounterOrder() {
    Leg firstEntry = TF("ENTRY_Z", 0., 0.);
    Leg secondEntry = TF("ENTRY_A", 0., 1.);
    Leg firstExit = TF("EXIT_Z", 0., 2.);
    Leg secondExit = TF("EXIT_A", 0., 3.);

    List<ResolvedLeg> route = chooseRoute(List.of(firstEntry, secondEntry), List.of(firstExit, secondExit),
        new LinkedLegs(secondEntry, secondExit, 1.),
        new LinkedLegs(secondEntry, firstExit, 1.),
        new LinkedLegs(firstEntry, secondExit, 1.),
        new LinkedLegs(firstEntry, firstExit, 1.));

    assertEquals(List.of(firstEntry, firstExit), route.stream().map(ResolvedLeg::leg).toList());
  }

  @Test
  void skipsUnreachablePairsWhenAnotherPairHasAPath() {
    Leg unreachableEntry = TF("ISOLATED_ENTRY", 0., 0.);
    Leg entry = TF("ENTRY", 0., 1.);
    Leg unreachableExit = TF("ISOLATED_EXIT", 0., 2.);
    Leg exit = TF("EXIT", 0., 3.);

    List<ResolvedLeg> route = chooseRoute(List.of(unreachableEntry, entry), List.of(unreachableExit, exit),
        new LinkedLegs(entry, exit, 2.));

    assertEquals(List.of(entry, exit), route.stream().map(ResolvedLeg::leg).toList());
  }

  @Test
  void returnsEmptyWhenNoEndpointPairConnects() {
    Leg entry1 = TF("ENTRY1", 0., 0.);
    Leg entry2 = TF("ENTRY2", 0., 1.);
    Leg exit1 = TF("EXIT1", 0., 2.);
    Leg exit2 = TF("EXIT2", 0., 3.);

    assertTrue(chooseRoute(List.of(entry1, entry2), List.of(exit1, exit2)).isEmpty());
    assertTrue(chooseRoute(List.of(entry1, entry2), List.of(exit1)).isEmpty());
  }

  @Test
  void choosesBestEntryWithOneExit() {
    Leg entry1 = TF("ENTRY1", 0., 0.);
    Leg entry2 = TF("ENTRY2", 0., 1.);
    Leg exit = TF("EXIT", 0., 2.);

    List<ResolvedLeg> route = chooseRoute(List.of(entry1, entry2), List.of(exit),
        new LinkedLegs(entry1, exit, 5.),
        new LinkedLegs(entry2, exit, 2.));

    assertEquals(List.of(entry2, exit), route.stream().map(ResolvedLeg::leg).toList());
  }

  @Test
  void duplicateEdgesKeepTheFirstWeight() {
    Leg entry1 = TF("ENTRY1", 0., 0.);
    Leg entry2 = TF("ENTRY2", 0., 1.);
    Leg exit = TF("EXIT", 0., 2.);

    List<ResolvedLeg> route = chooseRoute(List.of(entry1, entry2), List.of(exit),
        new LinkedLegs(entry1, exit, 5.),
        new LinkedLegs(entry1, exit, 1.),
        new LinkedLegs(entry2, exit, 3.));

    assertEquals(List.of(entry2, exit), route.stream().map(ResolvedLeg::leg).toList(),
        "A later duplicate must not replace the first edge weight, even when it is lower.");
  }

  @Test
  void returnsEmptyForNoResolvedSections() {
    assertTrue(new GraphicalRouteChooser(TokenMapper.standard()).chooseRoute(List.of()).isEmpty());
  }

  private static List<ResolvedLeg> chooseRoute(List<Leg> entries, List<Leg> exits, LinkedLegs... links) {
    LinkableToken left = tokenWithVertices(entries);
    LinkableToken right = tokenWithVertices(exits);
    when(left.accept(right)).thenReturn(() -> List.of(links));

    ResolvedToken leftResolved = ResolvedToken.standardFix(fix("LEFT", 0., 0.));
    ResolvedToken rightResolved = ResolvedToken.standardFix(fix("RIGHT", 0., 1.));
    TokenMapper mapper = mock(TokenMapper.class);
    when(mapper.map(leftResolved)).thenReturn(left);
    when(mapper.map(rightResolved)).thenReturn(right);

    return new GraphicalRouteChooser(mapper).chooseRoute(List.of(
        new ResolvedTokens(RouteToken.standard("ENTRIES", 0.), List.of(leftResolved)),
        new ResolvedTokens(RouteToken.standard("EXITS", 1.), List.of(rightResolved))
    ));
  }

  private static LinkableToken tokenWithVertices(List<Leg> legs) {
    LinkableToken token = mock(LinkableToken.class);
    when(token.graphRepresentation()).thenReturn(legs.stream()
        .map(leg -> new LinkedLegs(leg, leg, LinkedLegs.SAME_ELEMENT_MATCH_WEIGHT))
        .toList());
    return token;
  }
}
