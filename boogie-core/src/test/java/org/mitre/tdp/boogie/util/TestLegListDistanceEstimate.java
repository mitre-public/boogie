package org.mitre.tdp.boogie.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.caasd.commons.LatLong;

class TestLegListDistanceEstimate {

  private static final LegListDistanceEstimate estimator = new LegListDistanceEstimate();

  @Test
  void testEmptyListReturnsZero() {
    assertEquals(0.0, estimator.apply(List.of()));
  }

  @Test
  void testSingleLegReturnsZero() {
    Fix fix = Fix.builder()
        .fixIdentifier("ABC")
        .latLong(LatLong.of(40.0, -75.0))
        .build();
    Leg leg = Leg.tfBuilder(fix, 1).build();
    assertEquals(0.0, estimator.apply(List.of(leg)));
  }

  @Test
  void testLegsWithoutFixesReturnsZero() {
    Leg leg1 = Leg.builder(PathTerminator.VA, 1).build();
    Leg leg2 = Leg.builder(PathTerminator.VA, 2).build();
    assertEquals(0.0, estimator.apply(List.of(leg1, leg2)));
  }

  @Test
  void testSimpleDistanceBetweenTwoFixes() {
    Fix fix1 = Fix.builder()
        .fixIdentifier("ABC")
        .latLong(LatLong.of(40.0, -75.0))
        .build();
    Fix fix2 = Fix.builder()
        .fixIdentifier("DEF")
        .latLong(LatLong.of(41.0, -76.0))
        .build();

    Leg leg1 = Leg.tfBuilder(fix1, 1).build();
    Leg leg2 = Leg.tfBuilder(fix2, 2).build();

    double expected = fix1.latLong().distanceInNM(fix2.latLong());
    assertEquals(expected, estimator.apply(List.of(leg1, leg2)), 0.01);
  }

  @Test
  void testMultipleLegsSumDistances() {
    Fix fix1 = Fix.builder()
        .fixIdentifier("ABC")
        .latLong(LatLong.of(40.0, -75.0))
        .build();
    Fix fix2 = Fix.builder()
        .fixIdentifier("DEF")
        .latLong(LatLong.of(41.0, -76.0))
        .build();
    Fix fix3 = Fix.builder()
        .fixIdentifier("GHI")
        .latLong(LatLong.of(42.0, -77.0))
        .build();

    Leg leg1 = Leg.tfBuilder(fix1, 1).build();
    Leg leg2 = Leg.tfBuilder(fix2, 2).build();
    Leg leg3 = Leg.tfBuilder(fix3, 3).build();

    double d1 = fix1.latLong().distanceInNM(fix2.latLong());
    double d2 = fix2.latLong().distanceInNM(fix3.latLong());
    assertEquals(d1 + d2, estimator.apply(List.of(leg1, leg2, leg3)), 0.01);
  }

  @Test
  void testFiltersLegsWithoutFixes() {
    Fix fix1 = Fix.builder()
        .fixIdentifier("ABC")
        .latLong(LatLong.of(40.0, -75.0))
        .build();
    Fix fix2 = Fix.builder()
        .fixIdentifier("DEF")
        .latLong(LatLong.of(41.0, -76.0))
        .build();

    Leg leg1 = Leg.tfBuilder(fix1, 1).build();
    Leg leg2 = Leg.builder(PathTerminator.VA, 2).build();
    Leg leg3 = Leg.tfBuilder(fix2, 3).build();

    double expected = fix1.latLong().distanceInNM(fix2.latLong());
    assertEquals(expected, estimator.apply(List.of(leg1, leg2, leg3)), 0.01);
  }

  @Test
  void testAfLegDistance() {
    Fix associatedFix = Fix.builder()
        .fixIdentifier("ABC")
        .latLong(LatLong.of(40.0, -75.0))
        .build();
    Fix recommendedNavaid = Fix.builder()
        .fixIdentifier("VOR")
        .latLong(LatLong.of(40.5, -75.5))
        .magneticVariation(MagneticVariation.ofDegrees(-10.0))
        .build();

    Leg leg1 = Leg.tfBuilder(associatedFix, 1).build();
    Leg leg2 = Leg.afBuilder(associatedFix, recommendedNavaid, 2, 90.0)
        .theta(45.0)
        .rho(10.0)
        .turnDirection(TurnDirection.right())
        .build();

    double result = estimator.apply(List.of(leg1, leg2));
    assertEquals(54.98, result, 0.1);
  }

  @Test
  void testRfLegDistance() {
    Fix centerFix = Fix.builder()
        .fixIdentifier("CENTER")
        .latLong(LatLong.of(40.0, -75.0))
        .build();
    Fix incomingFix = Fix.builder()
        .fixIdentifier("IN")
        .latLong(LatLong.of(40.1, -75.0))
        .build();
    Fix outgoingFix = Fix.builder()
        .fixIdentifier("OUT")
        .latLong(LatLong.of(39.9, -75.0))
        .build();

    Leg leg1 = Leg.tfBuilder(incomingFix, 1).build();
    Leg leg2 = Leg.builder(PathTerminator.RF, 2)
        .centerFix(centerFix)
        .associatedFix(outgoingFix)
        .turnDirection(TurnDirection.right())
        .build();

    double result = estimator.apply(List.of(leg1, leg2));
    assertEquals(18.84, result, 0.01);
  }
}