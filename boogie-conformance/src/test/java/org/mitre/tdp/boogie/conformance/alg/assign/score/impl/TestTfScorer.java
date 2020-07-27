package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

public class TestTfScorer {

  @Test
  public void testTfScorerReturnsEmptyWithNoPreviousLeg() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    when(leg.type()).thenReturn(PathTerm.TF);

    FlyableLeg legs = new FlyableLeg(null, leg, null);

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(legs.onLegScorer().score(point, legs).isPresent());
  }

  @Test
  public void testTfScorerReturnsEmptyWithNoPreviousFix() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    when(leg.type()).thenReturn(PathTerm.TF);

    Leg prev = mock(Leg.class);

    FlyableLeg legs = new FlyableLeg(prev, leg, null);

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(legs.onLegScorer().score(point, legs).isPresent());
  }

  @Test
  public void testTfScorerReturnsEmptyWithNoPreviousFixLocation() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    when(leg.type()).thenReturn(PathTerm.TF);

    Leg prev = mock(Leg.class);
    Fix fix = mock(Fix.class);
    when(prev.pathTerminator()).thenReturn(fix);

    FlyableLeg legs = new FlyableLeg(prev, leg, null);

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(legs.onLegScorer().score(point, legs).isPresent());
  }
}
