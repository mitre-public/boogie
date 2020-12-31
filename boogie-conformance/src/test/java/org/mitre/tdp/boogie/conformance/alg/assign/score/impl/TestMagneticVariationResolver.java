package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

class TestMagneticVariationResolver {

  @Test
  public void testResolutionPrefersRecommendedNavaid() {
    FlyableLeg leg = new FlyableLeg(null, leg(), null, Route.newRoute(Collections.emptyList(), new Object()));
    MagneticVariation magneticVariation = MagneticVariationResolver.getInstance().magneticVariation(point(), leg);

    assertEquals(10., magneticVariation.modeled());
  }

  @Test
  public void testResolutionPrefersPathTermAfterNavaid() {
    Leg l = leg();
    when(l.recommendedNavaid()).thenReturn(Optional.empty());

    FlyableLeg leg = new FlyableLeg(null, l, null, Route.newRoute(Collections.emptyList(), new Object()));
    MagneticVariation magneticVariation = MagneticVariationResolver.getInstance().magneticVariation(point(), leg);

    assertEquals(0., magneticVariation.modeled());
  }

  @Test
  public void testResolutionFailoverToPointMagvar() {
    Leg l = leg();
    when(l.recommendedNavaid()).thenReturn(Optional.empty());
    when(l.pathTerminator()).thenReturn(null);

    FlyableLeg leg = new FlyableLeg(null, l, null, Route.newRoute(Collections.emptyList(), new Object()));
    MagneticVariation magneticVariation = MagneticVariationResolver.getInstance().magneticVariation(point(), leg);

    assertEquals(-4.546, magneticVariation.modeled(), 0.001);
  }

  private ConformablePoint point() {
    ConformablePoint conformablePoint = mock(ConformablePoint.class);
    when(conformablePoint.latitude()).thenReturn(0.0);
    when(conformablePoint.longitude()).thenReturn(0.0);
    when(conformablePoint.pressureAltitude()).thenReturn(Optional.empty());
    when(conformablePoint.time()).thenReturn(Instant.parse("2020-10-01T00:00:00Z"));
    return conformablePoint;
  }

  private MagneticVariation magvar(double modeled) {
    MagneticVariation magneticVariation = mock(MagneticVariation.class);
    when(magneticVariation.modeled()).thenReturn(modeled);
    return magneticVariation;
  }

  private Leg leg() {
    MagneticVariation navVar = magvar(10.);
    Fix navaid = mock(Fix.class);
    when(navaid.magneticVariation()).thenReturn(navVar);

    MagneticVariation termVar = magvar(0.);
    Fix pathTerm = mock(Fix.class);
    when(pathTerm.magneticVariation()).thenReturn(termVar);

    Leg leg = mock(Leg.class);
    when(leg.pathTerminator()).thenReturn(pathTerm);
    when(leg.recommendedNavaid()).thenReturn((Optional) Optional.of(navaid));

    return leg;
  }
}
