package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.test.MockObjects.magneticVariation;
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
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

public class TestCfScorer {

  @Test
  public void testFailOnMissingOutboundMagneticCourse() {
    Leg VI = VI();
    Leg CF = CF();
    when(CF.outboundMagneticCourse()).thenReturn(empty());

    FlyableLeg flyableLeg = new FlyableLeg(VI, CF, null);

    assertThrows(MissingRequiredFieldException.class, () -> new CfScorer().scoreAgainstLeg(dummyPoint(), flyableLeg));
  }

  @Test
  public void testFailOnMissingRecommendedNavaid() {
    Leg VI = VI();
    Leg CF = CF();
    when(CF.recommendedNavaid()).thenReturn(empty());

    FlyableLeg flyableLeg = new FlyableLeg(VI, CF, null);

    assertThrows(MissingRequiredFieldException.class, () -> new CfScorer().scoreAgainstLeg(dummyPoint(), flyableLeg));
  }

  @Test
  public void testScoreDecreasesWithDistance() {
    FlyableLeg flyableLeg = new FlyableLeg(VI(), CF(), null);
    ConformablePoint pt = dummyPoint();

    when(pt.distanceInNmTo(any())).thenReturn(0.1);
    double nearPointScore = new CfScorer().scoreAgainstLeg(pt, flyableLeg);
    assertEquals(1, nearPointScore, 0.1);

    when(pt.distanceInNmTo(any())).thenReturn(40.);
    double farPointScore = new CfScorer().scoreAgainstLeg(pt, flyableLeg);
    assertTrue(nearPointScore - farPointScore > 0.5);
  }

  private ConformablePoint dummyPoint() {
    ConformablePoint pt = mock(ConformablePoint.class);
    when(pt.trueCourse()).thenReturn(Optional.of(212.));
    return pt;
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
    when(CF.type()).thenReturn(PathTerm.CF);
    when(CF.recommendedNavaid()).thenReturn((Optional) Optional.of(navaid));
    when(CF.outboundMagneticCourse()).thenReturn(Optional.of(199.4));
    when(CF.theta()).thenReturn(Optional.of(13.7));
    when(CF.rho()).thenReturn(Optional.of(137.2));
    when(CF.routeDistance()).thenReturn(Optional.of(2.7));

    return CF;
  }

  private FlyableLeg conformableLeg() {
    return new FlyableLeg(VI(), CF(), null);
  }
}
