package org.mitre.tdp.boogie.alg.graph;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.junit.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mitre.tdp.boogie.ObjectMocks.IF;
import static org.mitre.tdp.boogie.ObjectMocks.TF;
import static org.mitre.tdp.boogie.ObjectMocks.transition;

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
    Transition cd = transition("ALPHA1", TransitionType.APPROACH, ProcedureType.STAR, Arrays.asList(l3_1, l3_2));

    Leg l4_1 = IF("CCC", 0.0, 0.4);
    Leg l4_2 = TF("EEE", 0.0, 0.5);
    Transition ce = transition("ALPHA1", TransitionType.APPROACH, ProcedureType.STAR, Arrays.asList(l4_1, l4_2));

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
    Transition cd = transition("ALPHA1", TransitionType.APPROACH, ProcedureType.STAR, Arrays.asList(l3_1, l3_2));

    Leg l4_1 = IF("EEE", 0.0, 0.4);
    Leg l4_2 = TF("FFF", 0.0, 0.5);
    Transition ef = transition("ALPHA1", TransitionType.APPROACH, ProcedureType.STAR, Arrays.asList(l4_1, l4_2));

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
}
