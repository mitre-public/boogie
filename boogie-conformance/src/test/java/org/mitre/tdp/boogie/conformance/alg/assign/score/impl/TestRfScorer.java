package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

public class TestRfScorer {

  @Test
  public void testRfScorerLeftNo0Crossing() {
    FlyableLeg consecutiveLegs = new FlyableLeg(TF(), RF(), null);

    Fix pathTerminator = consecutiveLegs.current().pathTerminator();
    Fix centerFix = consecutiveLegs.current().centerFix().get();

    double radiusNm = centerFix.distanceInNmTo(pathTerminator);
    RfScorer scorer = new RfScorer();

    LatLong pointLocation = centerFix.projectOut(75.0, radiusNm).latLong();
    ConformablePoint point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);

    pointLocation = centerFix.projectOut(135.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(0.0, scorer.score(point, consecutiveLegs).get(), 0.1);

    pointLocation = centerFix.projectOut(20.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(0.0, scorer.score(point, consecutiveLegs).get(), 0.1);
  }

  @Test
  public void testRfScorerLeftWith0Crossing() {
    Leg rf = RF();
    when(rf.outboundMagneticCourse()).thenReturn(Optional.of(265.0));

    FlyableLeg consecutiveLegs = new FlyableLeg(TF(), rf, null);

    Fix pathTerminator = consecutiveLegs.current().pathTerminator();
    Fix centerFix = consecutiveLegs.current().centerFix().get();

    double radiusNm = centerFix.distanceInNmTo(pathTerminator);
    RfScorer scorer = new RfScorer();

    LatLong pointLocation = centerFix.projectOut(357.0, radiusNm).latLong();
    ConformablePoint point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);

    pointLocation = centerFix.projectOut(10.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);

    pointLocation = centerFix.projectOut(135.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(0.0, scorer.score(point, consecutiveLegs).get(), 0.1);

    pointLocation = centerFix.projectOut(320.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(0.0, scorer.score(point, consecutiveLegs).get(), 0.1);
  }

  @Test
  public void testRfScorerRightNo0Crossing() {
    Leg rf = RF();
    when(rf.turnDirection()).thenReturn((Optional) Optional.of(TurnDirection.right()));
    when(rf.outboundMagneticCourse()).thenReturn(Optional.of(50.0));

    FlyableLeg consecutiveLegs = new FlyableLeg(TF(), rf, null);

    Fix pathTerminator = consecutiveLegs.current().pathTerminator();
    Fix centerFix = consecutiveLegs.current().centerFix().get();

    double radiusNm = centerFix.distanceInNmTo(pathTerminator);
    RfScorer scorer = new RfScorer();

    LatLong pointLocation = centerFix.projectOut(280.0, radiusNm).latLong();
    ConformablePoint point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);

    pointLocation = centerFix.projectOut(170.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(0.0, scorer.score(point, consecutiveLegs).get(), 0.1);

    pointLocation = centerFix.projectOut(75.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(0.0, scorer.score(point, consecutiveLegs).get(), 0.1);
  }

  @Test
  public void testRfScorerRightWith0Crossing() {
    Leg rf = RF();
    when(rf.turnDirection()).thenReturn((Optional) Optional.of(TurnDirection.right()));
    when(rf.theta()).thenReturn(Optional.of(275.0));
    when(rf.outboundMagneticCourse()).thenReturn(Optional.of(140.0));

    FlyableLeg consecutiveLegs = new FlyableLeg(TF(), rf, null);

    Fix pathTerminator = consecutiveLegs.current().pathTerminator();
    Fix centerFix = consecutiveLegs.current().centerFix().get();

    double radiusNm = centerFix.distanceInNmTo(pathTerminator);
    RfScorer scorer = new RfScorer();

    LatLong pointLocation = centerFix.projectOut(350.0, radiusNm).latLong();
    ConformablePoint point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);

    pointLocation = centerFix.projectOut(35.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);

    pointLocation = centerFix.projectOut(170.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(0.0, scorer.score(point, consecutiveLegs).get(), 0.1);

    pointLocation = centerFix.projectOut(315.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);
  }

  @Test
  public void testRfScorerEitherAlwaysReturnsOffTrackScore() {
    Leg rf = RF();
    when(rf.turnDirection()).thenReturn((Optional) Optional.of(TurnDirection.either()));

    FlyableLeg consecutiveLegs = new FlyableLeg(TF(), rf, null);

    Fix pathTerminator = consecutiveLegs.current().pathTerminator();
    Fix centerFix = consecutiveLegs.current().centerFix().get();

    double radiusNm = centerFix.distanceInNmTo(pathTerminator);
    RfScorer scorer = new RfScorer();

    LatLong pointLocation = centerFix.projectOut(350.0, radiusNm).latLong();
    ConformablePoint point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);

    pointLocation = centerFix.projectOut(35.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);

    pointLocation = centerFix.projectOut(170.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);

    pointLocation = centerFix.projectOut(315.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);

    pointLocation = centerFix.projectOut(65.0, radiusNm).latLong();
    point = pointAt(pointLocation);

    assertEquals(1.0, scorer.score(point, consecutiveLegs).get(), 0.05);
  }

  private ConformablePoint pointAt(LatLong latLong) {
    ConformablePoint conformablePoint = mock(ConformablePoint.class);
    when(conformablePoint.latLong()).thenReturn(latLong);
    when(conformablePoint.latitude()).thenCallRealMethod();
    when(conformablePoint.longitude()).thenCallRealMethod();
    when(conformablePoint.distanceInNmTo(any())).thenCallRealMethod();
    when(conformablePoint.courseInDegrees(any())).thenCallRealMethod();
    return conformablePoint;
  }

  private Leg TF() {
    Leg leg = mock(Leg.class);
    when(leg.type()).thenReturn(PathTerm.TF);
    return leg;
  }

  private Leg RF() {
    MagneticVariation pathTermMagvar = new MagneticVariation() {
      @Override
      public Optional<Double> published() {
        return Optional.of(-10.8);
      }

      @Override
      public double modeled() {
        return -10.829837;
      }
    };

    Fix pathTerminator = mock(Fix.class);
    when(pathTerminator.latLong()).thenReturn(LatLong.of(38.875502777777776, -77.05605277777778));
    when(pathTerminator.latitude()).thenCallRealMethod();
    when(pathTerminator.longitude()).thenCallRealMethod();
    when(pathTerminator.magneticVariation()).thenReturn(pathTermMagvar);
    when(pathTerminator.distanceInNmTo(any())).thenCallRealMethod();
    when(pathTerminator.courseInDegrees(any())).thenCallRealMethod();
    when(pathTerminator.projectOut(any(), any())).thenCallRealMethod();

    MagneticVariation centerFixMagvar = new MagneticVariation() {
      @Override
      public Optional<Double> published() {
        return Optional.of(-10.8);
      }

      @Override
      public double modeled() {
        return -10.794219;
      }
    };

    Fix centerFix = mock(Fix.class);
    when(centerFix.latLong()).thenReturn(LatLong.of(38.838230555555555, -77.05605277777778));
    when(centerFix.latitude()).thenCallRealMethod();
    when(centerFix.longitude()).thenCallRealMethod();
    when(centerFix.magneticVariation()).thenReturn(centerFixMagvar);
    when(centerFix.distanceInNmTo(any())).thenCallRealMethod();
    when(centerFix.courseInDegrees(any())).thenCallRealMethod();
    when(centerFix.projectOut(any(), any())).thenCallRealMethod();

    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerminator);
    when(leg.type()).thenReturn(PathTerm.RF);
    when(leg.centerFix()).thenReturn((Optional) Optional.of(centerFix));
    when(leg.rnp()).thenReturn(Optional.of(0.5));
    when(leg.theta()).thenReturn(Optional.of(6.5));
    when(leg.routeDistance()).thenReturn(Optional.of(2.2));
    when(leg.outboundMagneticCourse()).thenReturn(Optional.of(326.6));
    when(leg.turnDirection()).thenReturn((Optional) Optional.of(TurnDirection.left()));

    return leg;
  }
}
