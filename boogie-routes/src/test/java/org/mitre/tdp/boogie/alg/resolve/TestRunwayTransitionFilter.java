package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Transition;

class TestRunwayTransitionFilter {

  @Test
  void testTrueWhenTransitionIdContainsRunwayId() {
    Transition transition = mock(Transition.class);
    when(transition.transitionIdentifier()).thenReturn(Optional.of("35R"));

    assertTrue(new RunwayTransitionFilter("RW35R").test(transition));
  }

  @Test
  void testTrueWhenRunwayIdContainsTransitionId() {
    Transition transition = mock(Transition.class);
    when(transition.transitionIdentifier()).thenReturn(Optional.of("RW35R"));

    assertTrue(new RunwayTransitionFilter("35R").test(transition));
  }

  @Test
  void testTrueForLRCWhenB() {
    Transition transition = mock(Transition.class);
    when(transition.transitionIdentifier()).thenReturn(Optional.of("RW35B"));

    assertAll(
        () -> assertTrue(new RunwayTransitionFilter("35R").test(transition)),
        () -> assertTrue(new RunwayTransitionFilter("35L").test(transition)),
        () -> assertTrue(new RunwayTransitionFilter("35C").test(transition))
    );
  }

  @Test
  void testFalseForMismatchedRunwayId() {
    Transition transition = mock(Transition.class);
    when(transition.transitionIdentifier()).thenReturn(Optional.of("RW25"));

    assertFalse(new RunwayTransitionFilter("5W").test(transition));
  }
}
