package org.mitre.tdp.boogie.conformance.scorers.impl;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mitre.tdp.boogie.test.MockObjects.magneticVariation;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;

public class TestAfScorer {

  @Test
  public void testFailOnMissingTheta() {
    Leg TF = TF();
    Leg AF = AF();
    when(AF.theta()).thenReturn(empty());

    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
    when(legs.previous()).thenReturn(Optional.of(TF));
    when(legs.current()).thenReturn(AF);

    assertThrows(MissingRequiredFieldException.class, () -> new AfScorer(legs).score(dummyPoint()));
  }

  @Test
  public void testFailOnMissingRho() {
    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
    Leg TF = TF();
    Leg AF = AF();
    when(AF.rho()).thenReturn(empty());

    when(legs.previous()).thenReturn(Optional.of(TF));
    when(legs.current()).thenReturn(AF);

    assertThrows(MissingRequiredFieldException.class, () -> new AfScorer(legs).score(dummyPoint()));
  }

  @Test
  public void testFailOnMissingOutboundCourse() {
    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
    Leg TF = TF();
    Leg AF = AF();
    when(AF.outboundMagneticCourse()).thenReturn(empty());

    when(legs.previous()).thenReturn(Optional.of(TF));
    when(legs.current()).thenReturn(AF);

    assertThrows(MissingRequiredFieldException.class, () -> new AfScorer(legs).score(dummyPoint()));
  }

  @Test
  public void testFailOnMissingRecommendedNavaid() {
    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
    Leg TF = TF();
    Leg AF = AF();
    when(AF.recommendedNavaid()).thenReturn(empty());

    when(legs.previous()).thenReturn(Optional.of(TF));
    when(legs.current()).thenReturn(AF);

    assertThrows(MissingRequiredFieldException.class, () -> new AfScorer(legs).score(dummyPoint()));
  }

  @Test
  public void testAFPastTerminatorScore() {
    AfScorer scorer = scorer();

    ConformablePoint point = mock(ConformablePoint.class);

    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation();
    double projCourse = localVariation.magneticToTrue(AF.theta().get()) + 10.0;

    LatLong loc = AF.recommendedNavaid().get().projectOut(projCourse, AF.rho().get()).latLong();
    when(point.latLong()).thenReturn(loc);

    double score = scorer.score(point);
    assertEquals(0.0, score, 0.01, "Points past the end of the path terminator radial should have a 0.0 score.");
  }

  @Test
  public void testAFPreBoundaryRadialScore() {
    AfScorer scorer = scorer();

    ConformablePoint point = mock(ConformablePoint.class);

    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation();
    double projCourse = localVariation.magneticToTrue(AF.outboundMagneticCourse().get()) - 10.0;

    LatLong loc = AF.recommendedNavaid().get().projectOut(projCourse, AF.rho().get()).latLong();
    when(point.latLong()).thenReturn(loc);

    double score = scorer.score(point);
    assertEquals(0.0, score, 0.01, "Points prior to the boundary radial should have a 0.0 score.");
  }

  @Test
  public void testAFMagneticConversionMovesCourseToOutsideRadialsScore() {
    AfScorer scorer = scorer();

    ConformablePoint point = mock(ConformablePoint.class);

    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation();
    double projCourse = localVariation.magneticToTrue(AF.outboundMagneticCourse().get()) - 10.0;
  }

  @Test
  public void testAFInBoundaryOnArcScoreHigh() {
    AfScorer scorer = scorer();

    ConformablePoint point = mock(ConformablePoint.class);
    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation();
    double halfTrueCourse = localVariation.magneticToTrue((AF.outboundMagneticCourse().get() + AF.theta().get()) / 2.0d);

    LatLong loc = AF.recommendedNavaid().get()
        .projectOut(halfTrueCourse, AF.rho().get()).latLong();
    when(point.latLong()).thenReturn(loc);
    when(point.latitude()).thenCallRealMethod();
    when(point.longitude()).thenCallRealMethod();

    double score = scorer.score(point);
    assertEquals(0.95d, score, 0.02, "Points directly on the defined arc should get a high score.");
  }

  @Test
  public void testAFInBoundaryOffset1NMScoreModerate() {
    AfScorer scorer = scorer();

    ConformablePoint point = mock(ConformablePoint.class);
    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation();
    double halfTrueCourse = localVariation.magneticToTrue((AF.outboundMagneticCourse().get() + AF.theta().get()) / 2.0d);

    LatLong loc = AF.recommendedNavaid().get()
        .projectOut(halfTrueCourse, AF.rho().get() - 1.0).latLong();
    when(point.latLong()).thenReturn(loc);
    when(point.latitude()).thenCallRealMethod();
    when(point.longitude()).thenCallRealMethod();

    double score = scorer.score(point);
    assertEquals(0.5d, score, 0.01, "Point only off by ~a mile should get a moderate score.");
  }

  @Test
  public void testAFInBoundaryOffset2NMScoreLow() {
    AfScorer scorer = scorer();

    ConformablePoint point = mock(ConformablePoint.class);
    Leg AF = AF();

    MagneticVariation localVariation = AF.recommendedNavaid().get().magneticVariation();
    double halfTrueCourse = localVariation.magneticToTrue((AF.outboundMagneticCourse().get() + AF.theta().get()) / 2.0d);

    LatLong loc = AF.recommendedNavaid().get().projectOut(halfTrueCourse, AF.rho().get() + 2.0).latLong();
    when(point.latLong()).thenReturn(loc);
    when(point.latitude()).thenCallRealMethod();
    when(point.longitude()).thenCallRealMethod();

    double score = scorer.score(point);
    assertEquals(0.05d, score, 0.01, "Point offset 2.0nm should get a low score.");
  }

  private ConformablePoint dummyPoint() {
    return mock(ConformablePoint.class);
  }

  private Leg TF() {
    return mock(Leg.class);
  }

  private Leg AF() {
    Fix navaid = mock(Fix.class);

    LatLong ll = LatLong.of(42.479575000000004, -122.91293333333334);
    when(navaid.latLong()).thenReturn(ll);
    when(navaid.latitude()).thenCallRealMethod();
    when(navaid.longitude()).thenCallRealMethod();
    when(navaid.courseInDegrees(any())).thenCallRealMethod();
    when(navaid.distanceInNmTo(any())).thenCallRealMethod();
    when(navaid.projectOut(any(), any())).thenCallRealMethod();

    MagneticVariation var = magneticVariation(19.0f, 14.513932612651612f);
    when(var.magneticToTrue(any())).thenCallRealMethod();
    when(var.trueToMagnetic(any())).thenCallRealMethod();
    when(navaid.magneticVariation()).thenReturn(var);

    Leg AF = mock(Leg.class);
    when(AF.type()).thenReturn(LegType.AF);
    when(AF.recommendedNavaid()).thenReturn((Optional) of(navaid));
    when(AF.outboundMagneticCourse()).thenReturn(of(98.0));
    when(AF.theta()).thenReturn(of(138.0));
    when(AF.rho()).thenReturn(of(15.0));
    when(AF.turnDirection()).thenReturn(of(TurnDirection.RIGHT));

    return AF;
  }

  private AfScorer scorer() {
    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
    Leg TF = TF();
    Leg AF = AF();
    when(legs.previous()).thenReturn(Optional.of(TF));
    when(legs.current()).thenReturn(AF);
    return new AfScorer(legs);
  }
}
