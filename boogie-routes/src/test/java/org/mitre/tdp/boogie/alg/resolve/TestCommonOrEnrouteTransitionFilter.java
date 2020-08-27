package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

public class TestCommonOrEnrouteTransitionFilter {

  @Test
  public void testPassesCommonTransition() {
    Transition transition = mock(Transition.class);
    when(transition.transitionType()).thenReturn(TransitionType.COMMON);

    assertTrue(new CommonOrEnrouteTransitionFilter().test(transition));
  }

  @Test
  public void testPassesEnrouteTransition() {
    Transition transition = mock(Transition.class);
    when(transition.transitionType()).thenReturn(TransitionType.ENROUTE);

    assertTrue(new CommonOrEnrouteTransitionFilter().test(transition));
  }

  @Test
  public void testFailsRunwayTransition() {
    Transition transition = mock(Transition.class);
    when(transition.transitionType()).thenReturn(TransitionType.RUNWAY);

    assertFalse(new CommonOrEnrouteTransitionFilter().test(transition));
  }

  @Test
  public void testFailsApproachTransition() {
    Transition transition = mock(Transition.class);
    when(transition.transitionType()).thenReturn(TransitionType.APPROACH);

    assertFalse(new CommonOrEnrouteTransitionFilter().test(transition));
  }
}
