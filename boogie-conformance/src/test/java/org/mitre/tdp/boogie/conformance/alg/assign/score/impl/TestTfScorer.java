package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;

public class TestTfScorer {

  @Test
  public void testTfScorerReturnsEmptyWithNoPreviousFix() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    when(leg.type()).thenReturn(PathTerm.TF);

    FlyableLeg legs = mock(FlyableLeg.class);
    when(legs.previous()).thenReturn(Optional.empty());
    when(legs.current()).thenReturn(leg);
    when(legs.onLegScorer()).thenCallRealMethod();

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(legs.onLegScorer().score(point, legs).isPresent());
  }
}
