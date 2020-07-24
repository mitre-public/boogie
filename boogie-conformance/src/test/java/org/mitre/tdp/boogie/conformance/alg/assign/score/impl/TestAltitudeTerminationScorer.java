package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;

public class TestAltitudeTerminationScorer {

  @Test
  public void testScoreAgainstLeg() {
    AltitudeLimit altitudeLimit = mock(AltitudeLimit.class);
    when(altitudeLimit.altitudeLimit()).thenReturn(Optional.of(8000.0));

    Leg va = mock(Leg.class);
    when(va.outboundMagneticCourse()).thenReturn(Optional.of(100.0));
    when(va.altitudeConstraint()).thenReturn((Optional) Optional.of(altitudeLimit));

    FlyableLeg consecutiveLegs = mock(FlyableLeg.class);
    when(consecutiveLegs.current()).thenReturn(va);

    AltitudeTerminationScorer altitudeTerminationScorer = mock(AltitudeTerminationScorer.class);
    when(altitudeTerminationScorer.score(any(), any())).thenCallRealMethod();
    when(altitudeTerminationScorer.scoreAgainstLeg(any(), any())).thenCallRealMethod();
    when(altitudeTerminationScorer.offCourseWeight(any())).thenAnswer(invocation -> simpleLogistic(10.0, 20.0).apply(invocation.getArgument(0)));
    when(altitudeTerminationScorer.feetToTargetAltitudeWeight(any())).thenAnswer(invocation -> simpleLogistic(100.0, 250.0).apply(invocation.getArgument(0)));

    ConformablePoint point = mock(ConformablePoint.class);
    when(point.trueCourse()).thenReturn(Optional.of(100.0));
    when(point.pressureAltitude()).thenReturn(Optional.of(7500.0));

    assertEquals(0.9d, altitudeTerminationScorer.scoreAgainstLeg(point, consecutiveLegs), 0.05d);

    when(point.trueCourse()).thenReturn(Optional.of(110.0));
    assertEquals(0.5, altitudeTerminationScorer.scoreAgainstLeg(point, consecutiveLegs), 0.05d);

    when(point.trueCourse()).thenReturn(Optional.of(100.0));
    when(point.pressureAltitude()).thenReturn(Optional.of(8100.0));
    assertEquals(0.5, altitudeTerminationScorer.scoreAgainstLeg(point, consecutiveLegs), 0.05d);
  }

  @Test
  public void testScoreReturnsEmptyIfPointMissingAltitude() {
    ConformablePoint point = mock(ConformablePoint.class);
    when(point.pressureAltitude()).thenReturn(Optional.empty());
    when(point.trueCourse()).thenReturn(Optional.of(120.0));

    AltitudeTerminationScorer scorer = mock(AltitudeTerminationScorer.class);
    when(scorer.score(any(), any())).thenCallRealMethod();

    assertFalse(scorer.score(point, null).isPresent());
  }

  @Test
  public void testScoreReturnsEmptyIfPointMissingTrueCourse() {
    ConformablePoint point = mock(ConformablePoint.class);
    when(point.pressureAltitude()).thenReturn(Optional.of(10000.0));
    when(point.trueCourse()).thenReturn(Optional.empty());

    AltitudeTerminationScorer scorer = mock(AltitudeTerminationScorer.class);
    when(scorer.score(any(), any())).thenCallRealMethod();

    assertFalse(scorer.score(point, null).isPresent());
  }
}
