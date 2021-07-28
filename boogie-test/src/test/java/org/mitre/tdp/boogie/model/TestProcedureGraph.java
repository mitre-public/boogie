package org.mitre.tdp.boogie.model;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedure;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedureGraph;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.test.CONNR5;
import org.mitre.tdp.boogie.test.HOBTT2;
import org.mitre.tdp.boogie.test.MockObjects;

class TestProcedureGraph {

  private static final ProcedureGraph nominalGraph = nominalGraph();

  private static final QueryableProcedure queryNominal = new QueryableProcedure(nominalGraph);

  private static final ProcedureGraph splitGraph = splitGraph();

  private static final QueryableProcedure querySplit = new QueryableProcedure(splitGraph);

  @Test
  void testSingleLegSingleTransition() {
    Leg leg = MockObjects.IF("YYT", 0.0, 0.0);

    Transition transition = MockObjects.transition("A", TransitionType.COMMON, ProcedureType.STAR, singletonList(leg));
    ProcedureGraph graph = newProcedureGraph(newProcedure(singletonList(transition)));

    assertAll(
        () -> assertTrue(graph.edgeSet().isEmpty()),
        () -> assertEquals(1, graph.vertexSet().size())
    );
  }

  @Test
  void assertNominalGraphConnected() {
    assertTrue(new ConnectivityInspector<>(nominalGraph()).isConnected());
  }

  @Test
  void testNominalPathAAA_EEE() {
    Leg start = queryNominal.associatedLegsOf("AAA").iterator().next();
    Leg end = queryNominal.associatedLegsOf("EEE").iterator().next();

    List<List<Leg>> paths = nominalGraph.pathsBetween(start, end);

    assertEquals(1, paths.size());

    List<Leg> path = paths.get(0);

    matchesSequence(path, Arrays.asList(
        Pair.of("AAA", PathTerminator.IF),
        Pair.of("BBB", PathTerminator.TF),
        Pair.of("BBB", PathTerminator.IF),
        Pair.of("CCC", PathTerminator.TF),
        Pair.of("CCC", PathTerminator.IF),
        Pair.of("EEE", PathTerminator.TF)
    ));
  }

  @Test
  void testNominalPathAAA_DDD() {
    Leg start = queryNominal.associatedLegsOf("AAA").iterator().next();
    Leg end = queryNominal.associatedLegsOf("DDD").iterator().next();

    List<List<Leg>> paths = nominalGraph.pathsBetween(start, end);

    assertEquals(1, paths.size());

    List<Leg> path = paths.get(0);
    matchesSequence(path, Arrays.asList(
        Pair.of("AAA", PathTerminator.IF),
        Pair.of("BBB", PathTerminator.TF),
        Pair.of("BBB", PathTerminator.IF),
        Pair.of("CCC", PathTerminator.TF),
        Pair.of("CCC", PathTerminator.IF),
        Pair.of("DDD", PathTerminator.TF)
    ));
  }

  @Test
  void testNoBackPath() {
    Leg start = queryNominal.associatedLegsOf("DDD").iterator().next();
    Leg end = queryNominal.associatedLegsOf("AAA").iterator().next();

    List<List<Leg>> paths = nominalGraph.pathsBetween(start, end);

    assertEquals(0, paths.size());
  }

  @Test
  void assertSplitGraphDisconnected() {
    assertFalse(new ConnectivityInspector<>(splitGraph).isConnected());
  }

  @Test
  void testSplitGraphPathsWithinSplitGraph() {

    List<List<Leg>> inTransitionPath1 = splitGraph.pathsBetween(
        querySplit.associatedLegsOf("CCC").iterator().next(),
        querySplit.associatedLegsOf("DDD").iterator().next()
    );

    List<List<Leg>> inTransitionPath2 = splitGraph.pathsBetween(
        querySplit.associatedLegsOf("EEE").iterator().next(),
        querySplit.associatedLegsOf("FFF").iterator().next()
    );

    List<List<Leg>> betweenTransitionPath = splitGraph.pathsBetween(
        querySplit.associatedLegsOf("CCC").iterator().next(),
        querySplit.associatedLegsOf("FFF").iterator().next()
    );

    assertAll(
        "Paths within the transitions should both be non-empty - ones between transitions (which are split) should not.",
        () -> assertEquals(1, inTransitionPath1.size()),
        () -> assertEquals(1, inTransitionPath2.size()),
        () -> assertEquals(0, betweenTransitionPath.size())
    );
  }

  @Test
  void testHOBTT2Connections() {
    ProcedureGraph pg = newProcedureGraph(HOBTT2.INSTANCE);

    ConnectivityInspector<Leg, DefaultEdge> ci = new ConnectivityInspector<>(pg);

    assertAll(
        "Testing the connectivity of the components in the HOBTT2 STAR graph.",
        () -> assertTrue(ci.isConnected(), "Constructed graph is not connected."),

        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("DRSDN", "DRSDN"), HOBTT2.INSTANCE.get("KEAVY", "RW26B"))),
        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("DRSDN", "DRSDN"), HOBTT2.INSTANCE.get("YURII", "RW27B"))),
        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("DRSDN", "DRSDN"), HOBTT2.INSTANCE.get("YURII", "RW28"))),

        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("KHMYA", "KHMYA"), HOBTT2.INSTANCE.get("KEAVY", "RW26B"))),
        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("KHMYA", "KHMYA"), HOBTT2.INSTANCE.get("YURII", "RW27B"))),
        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("KHMYA", "KHMYA"), HOBTT2.INSTANCE.get("YURII", "RW28"))),

        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("COOUP", "COOUP"), HOBTT2.INSTANCE.get("KEAVY", "RW26B"))),
        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("COOUP", "COOUP"), HOBTT2.INSTANCE.get("YURII", "RW27B"))),
        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("COOUP", "COOUP"), HOBTT2.INSTANCE.get("YURII", "RW28"))),

        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("BEORN", "BEORN"), HOBTT2.INSTANCE.get("KEAVY", "RW26B"))),
        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("BEORN", "BEORN"), HOBTT2.INSTANCE.get("YURII", "RW27B"))),
        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("BEORN", "BEORN"), HOBTT2.INSTANCE.get("YURII", "RW28"))),

        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("FRDDO", "FRDDO"), HOBTT2.INSTANCE.get("KEAVY", "RW26B"))),
        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("FRDDO", "FRDDO"), HOBTT2.INSTANCE.get("YURII", "RW27B"))),
        () -> assertTrue(ci.pathExists(HOBTT2.INSTANCE.get("FRDDO", "FRDDO"), HOBTT2.INSTANCE.get("YURII", "RW28")))
    );
  }

  @Test
  void testCONNR5Connections() {
    ProcedureGraph pg = ProcedureFactory.newProcedureGraph(CONNR5.INSTANCE);

    ConnectivityInspector<Leg, DefaultEdge> ci = new ConnectivityInspector<>(pg);

    assertAll(
        "Testing the connectivity of the CONNR5 SID graph.",
        () -> assertTrue(ci.isConnected()),

        () -> assertTrue(ci.pathExists(CONNR5.INSTANCE.get("RW17L").legs().get(0), CONNR5.INSTANCE.get("DBL", "")), "Single VI leg reference as path start failed."),
        () -> assertTrue(ci.pathExists(CONNR5.INSTANCE.get("RW08").legs().get(0), CONNR5.INSTANCE.get("DBL", "")), "Multiple non-concrete leg as path start failed."),
        () -> assertTrue(ci.pathExists(CONNR5.INSTANCE.get("RW16L").legs().get(0), CONNR5.INSTANCE.get("DBL", "")), "VI into CF leg chain start failure.")
    );
  }

  private static ProcedureGraph nominalGraph() {

    Leg l1_1 = MockObjects.IF("AAA", 0.0, 0.0);
    Leg l1_2 = MockObjects.TF("BBB", 0.0, 0.1);
    Transition ab = MockObjects.transition("B", "ALPHA1", "APT", TransitionType.ENROUTE, ProcedureType.STAR, Arrays.asList(l1_1, l1_2));

    Leg l2_1 = MockObjects.IF("BBB", 0.0, 0.2);
    Leg l2_2 = MockObjects.TF("CCC", 0.0, 0.4);
    Transition bc = MockObjects.transition("C", "ALPHA1", "APT", TransitionType.COMMON, ProcedureType.STAR, Arrays.asList(l2_1, l2_2));

    Leg l3_1 = MockObjects.IF("CCC", 0.0, 0.4);
    Leg l3_2 = MockObjects.TF("DDD", 0.0, 0.5);
    Transition cd = MockObjects.transition("D", "ALPHA1", "APT", TransitionType.RUNWAY, ProcedureType.STAR, Arrays.asList(l3_1, l3_2));

    Leg l4_1 = MockObjects.IF("CCC", 0.0, 0.4);
    Leg l4_2 = MockObjects.TF("EEE", 0.0, 0.5);
    Transition ce = MockObjects.transition("E", "ALPHA1", "APT", TransitionType.RUNWAY, ProcedureType.STAR, Arrays.asList(l4_1, l4_2));

    return newProcedureGraph(newProcedure(Arrays.asList(ab, bc, cd, ce)));
  }

  private static ProcedureGraph splitGraph() {

    Leg l3_1 = MockObjects.IF("CCC", 0.0, 0.4);
    Leg l3_2 = MockObjects.TF("DDD", 0.0, 0.5);
    Transition cd = MockObjects.transition("D", "ALPHA1", "APT", TransitionType.RUNWAY, ProcedureType.STAR, Arrays.asList(l3_1, l3_2));

    Leg l4_1 = MockObjects.IF("EEE", 0.0, 0.4);
    Leg l4_2 = MockObjects.TF("FFF", 0.0, 0.5);
    Transition ef = MockObjects.transition("F", "ALPHA1", "APT", TransitionType.RUNWAY, ProcedureType.STAR, Arrays.asList(l4_1, l4_2));

    return newProcedureGraph(newProcedure(Arrays.asList(cd, ef)));
  }

  private static boolean matchesSequence(List<Leg> legs, List<Pair<String, PathTerminator>> path) {
    return IntStream.range(0, legs.size()).filter(i -> {
      Leg leg = legs.get(i);
      Pair<String, PathTerminator> pair = path.get(i);
      return leg.pathTerminator().equals(pair.second())
          && pair.first().equals(leg.associatedFix().map(Fix::fixIdentifier).orElse(null));
    }).count() == legs.size();
  }
}
