package org.mitre.tdp.boogie.conformance.scorers.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;

public class TestTfScorer {

  @Test
  public void testTfScorerReturnsEmptyWithNoPreviousFix() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    when(leg.type()).thenReturn(PathTerm.TF);

    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
    when(legs.previous()).thenReturn(Optional.empty());
    when(legs.current()).thenReturn(leg);
    when(legs.scorer()).thenCallRealMethod();

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(legs.scorer().score(point).isPresent());
  }
}
