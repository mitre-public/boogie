package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class TraversalOrderSorterTest {

  @Test
  void testStarTransitionOrdering() {
    List<Transition> transitions = Arrays.asList(
        transition(TransitionType.RUNWAY),
        transition(TransitionType.COMMON),
        transition(TransitionType.ENROUTE)
    );

    List<List<Transition>> grouped = TraversalOrderSorter.star().sort(transitions);

    assertAll(
        () -> assertEquals(3, grouped.size()),

        () -> assertFalse(grouped.get(0).isEmpty()),
        () -> assertFalse(grouped.get(1).isEmpty()),
        () -> assertFalse(grouped.get(2).isEmpty()),

        () -> assertTrue(grouped.get(0).stream().allMatch(t -> t.transitionType().equals(TransitionType.ENROUTE))),
        () -> assertTrue(grouped.get(1).stream().allMatch(t -> t.transitionType().equals(TransitionType.COMMON))),
        () -> assertTrue(grouped.get(2).stream().allMatch(t -> t.transitionType().equals(TransitionType.RUNWAY)))
    );
  }

  @Test
  void testSidTransitionOrdering() {
    List<Transition> transitions = Arrays.asList(
        transition(TransitionType.ENROUTE),
        transition(TransitionType.COMMON),
        transition(TransitionType.RUNWAY)
    );

    List<List<Transition>> grouped = TraversalOrderSorter.sid().sort(transitions);

    assertAll(
        () -> assertEquals(3, grouped.size()),

        () -> assertFalse(grouped.get(0).isEmpty()),
        () -> assertFalse(grouped.get(1).isEmpty()),
        () -> assertFalse(grouped.get(2).isEmpty()),

        () -> assertTrue(grouped.get(0).stream().allMatch(t -> t.transitionType().equals(TransitionType.RUNWAY))),
        () -> assertTrue(grouped.get(1).stream().allMatch(t -> t.transitionType().equals(TransitionType.COMMON))),
        () -> assertTrue(grouped.get(2).stream().allMatch(t -> t.transitionType().equals(TransitionType.ENROUTE)))
    );
  }

  @Test
  void testApproachTransitionOrdering() {
    List<Transition> transitions = Arrays.asList(
        transition(TransitionType.MISSED),
        transition(TransitionType.COMMON),
        transition(TransitionType.RUNWAY),
        transition(TransitionType.APPROACH)
    );

    List<List<Transition>> grouped = TraversalOrderSorter.approach().sort(transitions);

    assertAll(
        () -> assertEquals(4, grouped.size()),

        () -> assertFalse(grouped.get(0).isEmpty()),
        () -> assertFalse(grouped.get(1).isEmpty()),
        () -> assertFalse(grouped.get(2).isEmpty()),
        () -> assertFalse(grouped.get(3).isEmpty()),

        () -> assertTrue(grouped.get(0).stream().allMatch(t -> t.transitionType().equals(TransitionType.APPROACH))),
        () -> assertTrue(grouped.get(1).stream().allMatch(t -> t.transitionType().equals(TransitionType.COMMON))),
        () -> assertTrue(grouped.get(2).stream().allMatch(t -> t.transitionType().equals(TransitionType.RUNWAY))),
        () -> assertTrue(grouped.get(3).stream().allMatch(t -> t.transitionType().equals(TransitionType.MISSED)))
    );
  }

  private Transition transition(TransitionType ttype) {
    Transition transition = mock(Transition.class);
    when(transition.transitionType()).thenReturn(ttype);
    return transition;
  }
}
