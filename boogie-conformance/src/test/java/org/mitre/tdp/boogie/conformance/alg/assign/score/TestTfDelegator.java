package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

class TestTfDelegator {

  private final AfDelegator delegator = new AfDelegator();

  @Test
  void testTfScorerReturnsEmptyWithNoPreviousLeg() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.associatedFix()).thenReturn((Optional) Optional.of(pathTerminator));
    when(leg.pathTerminator()).thenReturn(PathTerminator.TF);

    FlyableLeg legs = new FlyableLeg(null, leg, null, dummyRoute());

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(delegator.test(point, legs));
  }

  @Test
  void testTfScorerReturnsEmptyWithNoPreviousFix() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.associatedFix()).thenReturn((Optional) Optional.of(pathTerminator));
    when(leg.pathTerminator()).thenReturn(PathTerminator.TF);

    Leg prev = mock(Leg.class);

    FlyableLeg legs = new FlyableLeg(prev, leg, null, dummyRoute());

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(delegator.test(point, legs));
  }

  @Test
  void testTfScorerReturnsEmptyWithNoPreviousFixLocation() {
    Fix pathTerminator = mock(Fix.class);
    Leg leg = mock(Leg.class);
    when(leg.associatedFix()).thenReturn((Optional) Optional.of(pathTerminator));
    when(leg.pathTerminator()).thenReturn(PathTerminator.TF);

    Leg prev = mock(Leg.class);
    Fix fix = mock(Fix.class);
    when(prev.associatedFix()).thenReturn((Optional) Optional.of(fix));

    FlyableLeg legs = new FlyableLeg(prev, leg, null, dummyRoute());

    ConformablePoint point = mock(ConformablePoint.class);
    assertFalse(delegator.test(point, legs));
  }

  private Route dummyRoute() {
    return Route.newRoute(new ArrayList<>(), new Object());
  }
}
