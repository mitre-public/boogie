package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

public class TestStarRunwayTransitionFilter {

  @Test
  public void testFilterFailsNonRunwayTransitions() {
    Transition transition = testTransition();
    when(transition.transitionType()).thenReturn(TransitionType.COMMON);

    assertFalse(new StarRunwayTransitionFilter("25R").test(transition));
  }

  @Test
  public void testFilterFailsSidProcedures() {
    Transition transition = testTransition();
    when(transition.procedureType()).thenReturn(ProcedureType.SID);

    assertFalse(new StarRunwayTransitionFilter("25R").test(transition));
  }

  @Test
  public void testFilterPassesTransitionsMatchingRunway() {
    assertTrue(new StarRunwayTransitionFilter("25R").test(testTransition()));
  }

  @Test
  public void testFilterSkipsTransitionsNotMatchingRunway() {
    assertFalse(new StarRunwayTransitionFilter("24R").test(testTransition()));
  }

  private Transition testTransition() {
    Transition transition = mock(Transition.class);
    when(transition.transitionType()).thenReturn(TransitionType.RUNWAY);
    when(transition.procedureType()).thenReturn(ProcedureType.STAR);
    when(transition.identifier()).thenReturn("RW25R");
    return transition;
  }
}
