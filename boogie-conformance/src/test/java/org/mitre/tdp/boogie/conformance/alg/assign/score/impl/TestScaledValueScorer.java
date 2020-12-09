package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

public class TestScaledValueScorer {

  @Test
  public void testFailsWhenMaxValueLessThan0() {
    assertThrows(IllegalArgumentException.class, () -> ScaledValueScorer.wrap(null, -0.5));
  }

  @Test
  public void testFailsWhenMaxValueGreaterThan1() {
    assertThrows(IllegalArgumentException.class, () -> ScaledValueScorer.wrap(null, 1.3));
  }

  @Test
  public void testIsFractionOfMaxValue() {
    OnLegScorer scorer = mock(OnLegScorer.class);
    when(scorer.scoreAgainstLeg(any(), any())).thenReturn(0.5);

    ScaledValueScorer cappedScorer = ScaledValueScorer.wrap(scorer, 0.5);
    assertEquals(0.25, cappedScorer.scoreAgainstLeg(null, null));
  }
}
