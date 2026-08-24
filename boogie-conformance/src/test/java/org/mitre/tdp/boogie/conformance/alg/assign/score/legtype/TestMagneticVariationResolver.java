package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.Route;

class TestMagneticVariationResolver {

  private static final MagneticVariationResolver RESOLVER = MagneticVariationResolver.getInstance();

  @Test
  void testResolutionPrefersRecommendedNavaid() {
    FlyableLeg leg = new FlyableLeg(null, leg(), null, Route.newRoute(Collections.emptyList(), new Object()));
    MagneticVariation magneticVariation = RESOLVER.magneticVariation(point(), leg);

    assertEquals(MagneticVariation.ofDegrees(10), magneticVariation);
  }

  @Test
  void testResolutionPrefersPathTermAfterNavaid() {
    Leg l = leg().toBuilder().recommendedNavaid(null).build();

    FlyableLeg leg = new FlyableLeg(null, l, null, Route.newRoute(Collections.emptyList(), new Object()));
    MagneticVariation magneticVariation = RESOLVER.magneticVariation(point(), leg);

    assertEquals(MagneticVariation.ofDegrees(0.), magneticVariation);
  }

  @Test
  void testResolutionFailoverToPointMagvar() {
    Leg l = leg().toBuilder().recommendedNavaid(null).associatedFix(null).build();

    FlyableLeg leg = new FlyableLeg(null, l, null, Route.newRoute(Collections.emptyList(), new Object()));
    MagneticVariation magneticVariation = RESOLVER.magneticVariation(point(), leg);

    assertEquals(-4.546, magneticVariation.angle().inDegrees(), .001);
  }

  @Test
  void testTrueOutboundCourseDoesNotResolveMagneticVariation() {
    Leg leg = Leg.builder(PathTerminator.VM, 0).outboundTrueCourse(125.).build();
    FlyableLeg flyableLeg = new FlyableLeg(null, leg, null, Route.newRoute(Collections.emptyList(), new Object()));

    assertEquals(125., RESOLVER.outboundTrueCourse(null, flyableLeg).orElseThrow());
  }

  @Test
  void testThetaRemainsMagneticWhenOutboundCourseIsTrue() {
    Leg leg = Leg.builder(PathTerminator.AF, 0)
        .recommendedNavaid(navaid())
        .outboundTrueCourse(125.)
        .theta(100.)
        .build();
    FlyableLeg flyableLeg = new FlyableLeg(null, leg, null, Route.newRoute(Collections.emptyList(), new Object()));

    assertEquals(125., RESOLVER.outboundTrueCourse(point(), flyableLeg).orElseThrow());
    assertEquals(110., RESOLVER.thetaTrueCourse(point(), flyableLeg).orElseThrow());
  }

  private ConformablePoint point() {
    ConformablePoint conformablePoint = mock(ConformablePoint.class);
    when(conformablePoint.latitude()).thenReturn(0.0);
    when(conformablePoint.longitude()).thenReturn(0.0);
    when(conformablePoint.pressureAltitude()).thenReturn(Optional.empty());
    when(conformablePoint.time()).thenReturn(Instant.parse("2020-10-01T00:00:00Z"));
    return conformablePoint;
  }

  private Leg.Standard leg() {
    Fix associated = Fix.builder()
        .fixIdentifier("associated")
        .latLong(LatLong.of(0., 0.))
        .magneticVariation(MagneticVariation.ofDegrees(0.))
        .build();

    return Leg.dfBuilder(associated, 0)
        .recommendedNavaid(navaid())
        .build();
  }

  private Fix navaid() {
    return Fix.builder()
        .fixIdentifier("navaid")
        .latLong(LatLong.of(0., 0.))
        .magneticVariation(MagneticVariation.ofDegrees(10.))
        .build();
  }
}
