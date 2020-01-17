package org.mitre.tdp.boogie.conformance.scorers.impl;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.scorers.ConsecutiveLegs;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mitre.tdp.boogie.conformance.MockObjects.magneticVariation;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TestCFScorer {

//  @Test
//  public void testFailOnMissingTheta() {
//    Leg VI = VI();
//    Leg CF = CF();
//    when(CF.theta()).thenReturn(empty());
//
//    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
//    when(legs.from()).thenReturn(VI);
//    when(legs.to()).thenReturn(CF);
//
//    assertThrows(MissingRequiredFieldException.class, () -> new CFScorer(legs).score(dummyPoint()));
//  }
//
//  @Test
//  public void testFailOnMissingRho() {
//    Leg VI = VI();
//    Leg CF = CF();
//    when(CF.rho()).thenReturn(empty());
//
//    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
//    when(legs.from()).thenReturn(VI);
//    when(legs.to()).thenReturn(CF);
//
//    assertThrows(MissingRequiredFieldException.class, () -> new CFScorer(legs).score(dummyPoint()));
//  }
//
//  @Test
//  public void testFailOnMissingDistance() {
//    Leg VI = VI();
//    Leg CF = CF();
//    when(CF.distance()).thenReturn(empty());
//
//    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
//    when(legs.from()).thenReturn(VI);
//    when(legs.to()).thenReturn(CF);
//
//    assertThrows(MissingRequiredFieldException.class, () -> new CFScorer(legs).score(dummyPoint()));
//  }

  @Test
  public void testFailOnMissingOutboundMagneticCourse() {
    Leg VI = VI();
    Leg CF = CF();
    when(CF.outboundMagneticCourse()).thenReturn(empty());

    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
    when(legs.from()).thenReturn(VI);
    when(legs.to()).thenReturn(CF);

    assertThrows(MissingRequiredFieldException.class, () -> new CFScorer(legs).score(dummyPoint()));
  }

  @Test
  public void testFailOnMissingRecommendedNavaid() {
    Leg VI = VI();
    Leg CF = CF();
    when(CF.recommendedNavaid()).thenReturn(empty());

    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
    when(legs.from()).thenReturn(VI);
    when(legs.to()).thenReturn(CF);

    assertThrows(MissingRequiredFieldException.class, () -> new CFScorer(legs).score(dummyPoint()));
  }

  @Test
  public void testScoreWellCorrelatedCourses() {
    CFScorer scorer = scorer();

    ConformablePoint point = mock(ConformablePoint.class);

//    LatLong loc =
  }

  private ConformablePoint dummyPoint() {
    return mock(ConformablePoint.class);
  }

  private Leg VI() {
    return mock(Leg.class);
  }

  private Leg CF() {
    Fix navaid = mock(Fix.class);

    LatLong ll = LatLong.of(33.87001388888889, -116.42978333333333);
    when(navaid.latLong()).thenReturn(ll);
    when(navaid.courseInDegrees(any())).thenCallRealMethod();
    when(navaid.distanceInNmTo(any())).thenCallRealMethod();
    when(navaid.projectOut(any(), any())).thenCallRealMethod();

    MagneticVariation var = magneticVariation(13.0f, 11.482411518265646f);
    when(var.magneticToTrue(any())).thenCallRealMethod();
    when(var.trueToMagnetic(any())).thenCallRealMethod();
    when(navaid.magneticVariation()).thenReturn(var);

    Leg CF = mock(Leg.class);
    when(CF.type()).thenReturn(LegType.CF);
    when(CF.recommendedNavaid()).thenReturn((Optional) Optional.of(navaid));
    when(CF.outboundMagneticCourse()).thenReturn(Optional.of(199.4));
    when(CF.theta()).thenReturn(Optional.of(13.7));
    when(CF.rho()).thenReturn(Optional.of(137.2));
    when(CF.distance()).thenReturn(Optional.of(2.7));

    return CF;
  }

  private CFScorer scorer() {
    ConsecutiveLegs legs = mock(ConsecutiveLegs.class);
    Leg TF = VI();
    Leg CF = CF();
    when(legs.from()).thenReturn(TF);
    when(legs.to()).thenReturn(CF);
    return new CFScorer(legs);
  }
}
