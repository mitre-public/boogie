package org.mitre.tdp.boogie.conformance.alg.assign.score;

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
  void testResolutionPrefersRecommendedNavaid() {
    FlyableLeg leg = new FlyableLeg(null, leg(), null, Route.newRoute(Collections.emptyList(), new Object()));
    MagneticVariation magneticVariation = MagneticVariationResolver.getInstance().magneticVariation(point(), leg);

    assertEquals(10., magneticVariation.modeled());
  }

  @Test
  void testResolutionPrefersPathTermAfterNavaid() {
    Leg l = leg();
    when(l.recommendedNavaid()).thenReturn(Optional.empty());

    FlyableLeg leg = new FlyableLeg(null, l, null, Route.newRoute(Collections.emptyList(), new Object()));
    MagneticVariation magneticVariation = MagneticVariationResolver.getInstance().magneticVariation(point(), leg);

    assertEquals(0., magneticVariation.modeled());
  }

  @Test
  void testResolutionFailoverToPointMagvar() {
    Leg l = leg();
    when(l.recommendedNavaid()).thenReturn(Optional.empty());
    when(l.associatedFix()).thenReturn(Optional.empty());

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

  private Leg leg() {
    Fix navaid = mock(Fix.class);
    when(navaid.modeledVariation()).thenReturn(10.);
    when(navaid.magneticVariation()).thenCallRealMethod();

    Fix pathTerm = mock(Fix.class);
    when(pathTerm.modeledVariation()).thenReturn(0.);
    when(pathTerm.magneticVariation()).thenCallRealMethod();

    Leg leg = mock(Leg.class);
    when(leg.associatedFix()).thenReturn((Optional) Optional.of(pathTerm));
    when(leg.recommendedNavaid()).thenReturn((Optional) Optional.of(navaid));

    return leg;
  }
}
