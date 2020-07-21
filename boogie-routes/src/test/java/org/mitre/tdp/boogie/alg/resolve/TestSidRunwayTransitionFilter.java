package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

public class TestSidRunwayTransitionFilter {

  @Test
  public void testFilterIgnoresNonRunwayTransitions() {
    Transition transition = testTransition();
    when(transition.transitionType()).thenReturn(TransitionType.COMMON);

    assertTrue(new SidRunwayTransitionFilter("25R").test(transition));
  }

  @Test
  public void testFilterIgnoresNonStarProcedures() {
    Transition transition = testTransition();
    when(transition.procedureType()).thenReturn(ProcedureType.STAR);

    assertTrue(new SidRunwayTransitionFilter("25R").test(transition));
  }

  @Test
  public void testFilterPassesTransitionsMatchingRunway() {
    assertTrue(new SidRunwayTransitionFilter("25R").test(testTransition()));
  }

  @Test
  public void testFilterSkipsTransitionsNotMatchingRunway() {
    assertFalse(new SidRunwayTransitionFilter("24R").test(testTransition()));
  }

  private Transition testTransition() {
    Transition transition = mock(Transition.class);
    when(transition.transitionType()).thenReturn(TransitionType.RUNWAY);
    when(transition.procedureType()).thenReturn(ProcedureType.SID);
    when(transition.identifier()).thenReturn("RW25R");
    return transition;
  }
}
