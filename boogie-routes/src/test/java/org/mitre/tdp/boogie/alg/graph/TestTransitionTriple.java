package org.mitre.tdp.boogie.alg.graph;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestTransitionTriple {

  private Transition transition(TransitionType ttype, ProcedureType ptype) {
    Transition transition = mock(Transition.class);
    when(transition.transitionType()).thenReturn(ttype);
    when(transition.procedureType()).thenReturn(ptype);
    return transition;
  }

  @Test
  public void testTripleStarOrdering() {
    List<Transition> transitions = Arrays.asList(
        transition(TransitionType.RUNWAY, ProcedureType.STAR),
        transition(TransitionType.COMMON, ProcedureType.STAR),
        transition(TransitionType.ENROUTE, ProcedureType.STAR));

    TransitionTriple triple = TransitionTriple.from(transitions);
    List<List<Transition>> grouped = triple.listOrdered();

    assertEquals(3, grouped.size());

    assertFalse(grouped.get(0).isEmpty());
    assertFalse(grouped.get(1).isEmpty());
    assertFalse(grouped.get(2).isEmpty());

    assertTrue(grouped.get(0).stream().allMatch(t -> t.transitionType().equals(TransitionType.ENROUTE)));
    assertTrue(grouped.get(1).stream().allMatch(t -> t.transitionType().equals(TransitionType.COMMON)));
    assertTrue(grouped.get(2).stream().allMatch(t -> t.transitionType().equals(TransitionType.RUNWAY)));
  }

  @Test
  public void testTripleSidOrdering() {
    List<Transition> transitions = Arrays.asList(
        transition(TransitionType.RUNWAY, ProcedureType.SID),
        transition(TransitionType.COMMON, ProcedureType.SID),
        transition(TransitionType.ENROUTE, ProcedureType.SID));

    TransitionTriple triple = TransitionTriple.from(transitions);
    List<List<Transition>> grouped = triple.listOrdered();

    assertEquals(3, grouped.size());

    grouped = triple.listOrdered();

    assertTrue(grouped.get(2).stream().allMatch(t -> t.transitionType().equals(TransitionType.ENROUTE)));
    assertTrue(grouped.get(1).stream().allMatch(t -> t.transitionType().equals(TransitionType.COMMON)));
    assertTrue(grouped.get(0).stream().allMatch(t -> t.transitionType().equals(TransitionType.RUNWAY)));
  }

  @Test
  public void testFailOnRunwayTransitions() {
    assertThrows(
        IllegalArgumentException.class,
        () -> TransitionTriple.from(Collections.singletonList(transition(TransitionType.APPROACH, ProcedureType.SID))));
  }

  @Test
  public void testFailOnApproachProcedures() {
    assertThrows(
        IllegalArgumentException.class,
        () -> TransitionTriple.from(Collections.singletonList(transition(TransitionType.ENROUTE, ProcedureType.APPROACH))));
  }
}
