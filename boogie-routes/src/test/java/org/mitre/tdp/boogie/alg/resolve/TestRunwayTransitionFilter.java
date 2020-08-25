package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Transition;

public class TestRunwayTransitionFilter {

  @Test
  public void testTrueWhenTransitionIdContainsRunwayId() {
    Transition transition = mock(Transition.class);
    when(transition.identifier()).thenReturn("35R");

    assertTrue(new RunwayTransitionFilter("RW35R").test(transition));
  }

  @Test
  public void testTrueWhenRunwayIdContainsTransitionId() {
    Transition transition = mock(Transition.class);
    when(transition.identifier()).thenReturn("RW35R");

    assertTrue(new RunwayTransitionFilter("35R").test(transition));
  }

  @Test
  public void testTrueForLRCWhenB() {
    Transition transition = mock(Transition.class);
    when(transition.identifier()).thenReturn("RW35B");

    assertTrue(new RunwayTransitionFilter("35R").test(transition));
    assertTrue(new RunwayTransitionFilter("35L").test(transition));
    assertTrue(new RunwayTransitionFilter("35C").test(transition));
  }

  @Test
  public void testFalseForMismatchedRunwayId() {
    Transition transition = mock(Transition.class);
    when(transition.identifier()).thenReturn("RW25");

    assertFalse(new RunwayTransitionFilter("5W").test(transition));
  }
}
