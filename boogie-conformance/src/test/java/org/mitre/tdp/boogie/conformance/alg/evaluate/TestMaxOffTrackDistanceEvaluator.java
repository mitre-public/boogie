package org.mitre.tdp.boogie.conformance.alg.evaluate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.TurnDirection;
import org.mockito.stubbing.Answer;

class TestMaxOffTrackDistanceEvaluator {
  @Test
  void LegDistance_OnRfLeg_IsRadialDistance() {
    ConformablePoint pt = mock(ConformablePoint.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(pt.latLong()).thenReturn(LatLong.of(0.0, 2.0));
    Distance dist = MaxOffTrackDistanceEvaluator.offTrackDistance(pt, mockRfLegPair()).orElseThrow(() -> new IllegalStateException());
    assertEquals(60., dist.inNauticalMiles(), 0.01);
  }

  @Test
  void LegDistance_OffRfLeg_IsEndpointDistance() {
    ConformablePoint pt = mock(ConformablePoint.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(pt.latLong()).thenReturn(LatLong.of(0.0, -1.0));
    Distance dist = MaxOffTrackDistanceEvaluator.offTrackDistance(pt, mockRfLegPair()).orElseThrow(() -> new IllegalStateException());
    assertEquals(60. * Math.sqrt(2), dist.inNauticalMiles(), 0.01);
  }

  @Test
  void LegDistance_OnAfLeg_IsRadialDistance() {
    ConformablePoint pt = mock(ConformablePoint.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(pt.latLong()).thenReturn(LatLong.of(0.0, 2.0));
    Distance dist = MaxOffTrackDistanceEvaluator.offTrackDistance(pt, mockAfLegPair()).orElseThrow(() -> new IllegalStateException());
    assertEquals(60., dist.inNauticalMiles(), 0.01);
  }

  @Test
  void LegDistance_OffAfLeg_IsEndpointDistance() {
    ConformablePoint pt = mock(ConformablePoint.class, withSettings().defaultAnswer(CALLS_REAL_METHODS));
    when(pt.latLong()).thenReturn(LatLong.of(0.0, -1.0));
    Distance dist = MaxOffTrackDistanceEvaluator.offTrackDistance(pt, mockAfLegPair()).orElseThrow(() -> new IllegalStateException());
    assertEquals(60. * Math.sqrt(2), dist.inNauticalMiles(), 0.01);
  }

  static LegPair mockRfLegPair() {
    Fix previousPathTerminator = mock(Fix.class, "PREVIOUS");
    Fix currentPathTerminator = mock(Fix.class, "CURRENT");
    Fix centerFix = mock(Fix.class, "CENTER");
    Leg previousLeg = mock(Leg.class);
    Leg currentLeg = mock(Leg.class);

    when(previousLeg.type()).thenReturn(PathTerm.TF);
    when(previousLeg.pathTerminator()).thenReturn(previousPathTerminator);
    when(currentLeg.pathTerminator()).thenReturn(currentPathTerminator);
    when(currentLeg.centerFix()).then((Answer<Optional<Fix>>) (x -> Optional.of(centerFix)));

    when(previousPathTerminator.distanceInNmTo(any())).thenCallRealMethod();
    when(currentPathTerminator.distanceInNmTo(any())).thenCallRealMethod();
    when(centerFix.distanceInNmTo(any())).thenCallRealMethod();
    when(centerFix.courseInDegrees(any())).thenCallRealMethod();

    when(currentLeg.type()).thenReturn(PathTerm.RF);
    when(currentLeg.turnDirection()).then((Answer<Optional<TurnDirection>>) (x -> Optional.of(TurnDirection.left())));
    when(centerFix.latLong()).thenReturn(LatLong.of(0., 0.));
    when(previousPathTerminator.latLong()).thenReturn(LatLong.of(0.0, 1.0));
    when(currentPathTerminator.latLong()).thenReturn(LatLong.of(1.0, 0.0));

    return new LegPair(previousLeg, currentLeg);
  }

  static LegPair mockAfLegPair() {
    Fix previousPathTerminator = mock(Fix.class, "PREVIOUS");
    Fix currentPathTerminator = mock(Fix.class, "CURRENT");
    Fix recommendedNavaid = mock(Fix.class, "CENTER");
    Leg previousLeg = mock(Leg.class);
    Leg currentLeg = mock(Leg.class);

    when(previousLeg.type()).thenReturn(PathTerm.TF);
    when(previousLeg.pathTerminator()).thenReturn(previousPathTerminator);
    when(currentLeg.pathTerminator()).thenReturn(currentPathTerminator);
    when(currentLeg.recommendedNavaid()).then((Answer<Optional<Fix>>) (x -> Optional.of(recommendedNavaid)));

    when(previousPathTerminator.distanceInNmTo(any())).thenCallRealMethod();
    when(currentPathTerminator.distanceInNmTo(any())).thenCallRealMethod();
    when(recommendedNavaid.distanceInNmTo(any())).thenCallRealMethod();
    when(recommendedNavaid.courseInDegrees(any())).thenCallRealMethod();

    when(currentLeg.type()).thenReturn(PathTerm.AF);
    when(currentLeg.turnDirection()).then((Answer<Optional<TurnDirection>>) (x -> Optional.of(TurnDirection.left())));
    when(recommendedNavaid.latLong()).thenReturn(LatLong.of(0., 0.));
    when(previousPathTerminator.latLong()).thenReturn(LatLong.of(0.0, 1.0));
    when(currentPathTerminator.latLong()).thenReturn(LatLong.of(1.0, 0.0));

    return new LegPair(previousLeg, currentLeg);
  }
}