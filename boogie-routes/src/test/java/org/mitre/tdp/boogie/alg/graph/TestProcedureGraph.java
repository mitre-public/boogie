package org.mitre.tdp.boogie.alg.graph;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.data.CONNR5;
import org.mitre.tdp.boogie.data.HOBTT2;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.TF;
import static org.mitre.tdp.boogie.MockObjects.transition;

public class TestProcedureGraph {

  private boolean matchesSequence(List<Leg> legs, List<Pair<String, LegType>> path) {
    return IntStream.range(0, legs.size()).filter(i -> {
      Leg leg = legs.get(i);
      Pair<String, LegType> pair = path.get(i);
      return leg.type().equals(pair.second()) && leg.pathTerminator().identifier().equals(pair.first());
    }).count() == legs.size();
  }

  private Leg getLeg(ProcedureGraph graph, String id) {
    return graph.vertexSet().stream()
        .filter(leg -> leg.pathTerminator().identifier().equals(id))
        .findFirst()
        .orElse(null);
  }

  @Test
  public void testSingleLegSingleTransition() {
    Leg leg = IF("YYT", 0.0, 0.0);
    Transition transition = transition("A", TransitionType.COMMON, ProcedureType.STAR, singletonList(leg));

    ProcedureGraph graph = ProcedureGraph.from(singletonList(transition));

    assertTrue(graph.edgeSet().isEmpty());
    assertEquals(1, graph.vertexSet().size());
  }

  public static ProcedureGraph nominalGraph() {
    Leg l1_1 = IF("AAA", 0.0, 0.0);
    Leg l1_2 = TF("BBB", 0.0, 0.1);
    Transition ab = transition("ALPHA1", TransitionType.ENROUTE, ProcedureType.STAR, Arrays.asList(l1_1, l1_2));

    Leg l2_1 = IF("BBB", 0.0, 0.2);
    Leg l2_2 = TF("CCC", 0.0, 0.4);
    Transition bc = transition("ALPHA1", TransitionType.COMMON, ProcedureType.STAR, Arrays.asList(l2_1, l2_2));

    Leg l3_1 = IF("CCC", 0.0, 0.4);
    Leg l3_2 = TF("DDD", 0.0, 0.5);
    Transition cd = transition("ALPHA1", TransitionType.RUNWAY, ProcedureType.STAR, Arrays.asList(l3_1, l3_2));

    Leg l4_1 = IF("CCC", 0.0, 0.4);
    Leg l4_2 = TF("EEE", 0.0, 0.5);
    Transition ce = transition("ALPHA1", TransitionType.RUNWAY, ProcedureType.STAR, Arrays.asList(l4_1, l4_2));

    return ProcedureGraph.from(Arrays.asList(ab, bc, cd, ce));
  }

  @Test
  public void assertNominalGraphConnected() {
    assertTrue(new ConnectivityInspector<>(nominalGraph()).isConnected());
  }

  @Test
  public void testNominalPathAAA_EEE() {
    ProcedureGraph pg = nominalGraph();

    List<List<Leg>> paths = pg.pathsBetween(
        getLeg(pg, "AAA").pathTerminator(),
        getLeg(pg, "EEE").pathTerminator());

    assertEquals(1, paths.size());

    List<Leg> path = paths.get(0);
    matchesSequence(path, Arrays.asList(
        Pair.of("AAA", LegType.IF),
        Pair.of("BBB", LegType.TF),
        Pair.of("BBB", LegType.IF),
        Pair.of("CCC", LegType.TF),
        Pair.of("CCC", LegType.IF),
        Pair.of("EEE", LegType.TF)
    ));
  }

  @Test
  public void testNominalPathAAA_DDD() {
    ProcedureGraph pg = nominalGraph();

    List<List<Leg>> paths = pg.pathsBetween(
        getLeg(pg, "AAA").pathTerminator(),
        getLeg(pg, "DDD").pathTerminator());

    assertEquals(1, paths.size());

    List<Leg> path = paths.get(0);
    matchesSequence(path, Arrays.asList(
        Pair.of("AAA", LegType.IF),
        Pair.of("BBB", LegType.TF),
        Pair.of("BBB", LegType.IF),
        Pair.of("CCC", LegType.TF),
        Pair.of("CCC", LegType.IF),
        Pair.of("DDD", LegType.TF)
    ));
  }

  @Test
  public void testNoBackPath() {
    ProcedureGraph pg = nominalGraph();

    List<List<Leg>> paths = pg.pathsBetween(
        getLeg(pg, "DDD").pathTerminator(),
        getLeg(pg, "AAA").pathTerminator());

    assertEquals(0, paths.size());
  }

  public static ProcedureGraph splitGraph() {
    Leg l3_1 = IF("CCC", 0.0, 0.4);
    Leg l3_2 = TF("DDD", 0.0, 0.5);
    Transition cd = transition("ALPHA1", TransitionType.RUNWAY, ProcedureType.STAR, Arrays.asList(l3_1, l3_2));

    Leg l4_1 = IF("EEE", 0.0, 0.4);
    Leg l4_2 = TF("FFF", 0.0, 0.5);
    Transition ef = transition("ALPHA1", TransitionType.RUNWAY, ProcedureType.STAR, Arrays.asList(l4_1, l4_2));

    return ProcedureGraph.from(Arrays.asList(cd, ef));
  }

  @Test
  public void assertSplitGraphDisconnected() {
    assertFalse(new ConnectivityInspector<>(splitGraph()).isConnected());
  }

  @Test
  public void testSplitGraphPathWithinTransitions() {
    ProcedureGraph pg = splitGraph();

    assertFalse(pg.pathsBetween(getLeg(pg, "CCC").pathTerminator(), getLeg(pg, "DDD").pathTerminator()).isEmpty());
    assertFalse(pg.pathsBetween(getLeg(pg, "EEE").pathTerminator(), getLeg(pg, "FFF").pathTerminator()).isEmpty());
  }

  @Test
  public void testSplitGraphPathBetweenTransitions() {
    ProcedureGraph pg = splitGraph();
    assertTrue(pg.pathsBetween(getLeg(pg, "CCC").pathTerminator(), getLeg(pg, "FFF").pathTerminator()).isEmpty());
  }

  @Test
  public void testHOBTT2Connections() {
    HOBTT2 hobtt2 = HOBTT2.build();
    ProcedureGraph pg = ProcedureGraph.from(hobtt2.transitions());

    ConnectivityInspector<Leg, DefaultEdge> ci = new ConnectivityInspector<>(pg);
    assertTrue(ci.isConnected(), "Constructed graph is not connected.");

    assertTrue(ci.pathExists(hobtt2.get("DRSDN", "DRSDN"), hobtt2.get("KEAVY", "RW26B")));
    assertTrue(ci.pathExists(hobtt2.get("DRSDN", "DRSDN"), hobtt2.get("YURII", "RW27B")));
    assertTrue(ci.pathExists(hobtt2.get("DRSDN", "DRSDN"), hobtt2.get("YURII", "RW28")));

    assertTrue(ci.pathExists(hobtt2.get("KHMYA", "KHMYA"), hobtt2.get("KEAVY", "RW26B")));
    assertTrue(ci.pathExists(hobtt2.get("KHMYA", "KHMYA"), hobtt2.get("YURII", "RW27B")));
    assertTrue(ci.pathExists(hobtt2.get("KHMYA", "KHMYA"), hobtt2.get("YURII", "RW28")));

    assertTrue(ci.pathExists(hobtt2.get("COOUP", "COOUP"), hobtt2.get("KEAVY", "RW26B")));
    assertTrue(ci.pathExists(hobtt2.get("COOUP", "COOUP"), hobtt2.get("YURII", "RW27B")));
    assertTrue(ci.pathExists(hobtt2.get("COOUP", "COOUP"), hobtt2.get("YURII", "RW28")));

    assertTrue(ci.pathExists(hobtt2.get("BEORN", "BEORN"), hobtt2.get("KEAVY", "RW26B")));
    assertTrue(ci.pathExists(hobtt2.get("BEORN", "BEORN"), hobtt2.get("YURII", "RW27B")));
    assertTrue(ci.pathExists(hobtt2.get("BEORN", "BEORN"), hobtt2.get("YURII", "RW28")));

    assertTrue(ci.pathExists(hobtt2.get("FRDDO", "FRDDO"), hobtt2.get("KEAVY", "RW26B")));
    assertTrue(ci.pathExists(hobtt2.get("FRDDO", "FRDDO"), hobtt2.get("YURII", "RW27B")));
    assertTrue(ci.pathExists(hobtt2.get("FRDDO", "FRDDO"), hobtt2.get("YURII", "RW28")));
  }

  @Test
  public void testCONNR5Connections() {
    CONNR5 connr5 = CONNR5.build();
    ProcedureGraph pg = ProcedureGraph.from(connr5.transitions());

    ConnectivityInspector<Leg, DefaultEdge> ci = new ConnectivityInspector<>(pg);
    assertTrue(ci.isConnected());

    assertTrue(ci.pathExists((Leg) connr5.get("RW17L").legs().get(0), connr5.get("DBL", "")), "Single VI leg reference as path start failed.");
    assertTrue(ci.pathExists((Leg) connr5.get("RW08").legs().get(0), connr5.get("DBL", "")), "Multiple non-concrete leg as path start failed.");
    assertTrue(ci.pathExists((Leg) connr5.get("RW16L").legs().get(0), connr5.get("DBL", "")), "VI into CF leg chain start failure.");
  }
}
