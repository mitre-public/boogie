package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import com.google.common.collect.Range;
import nl.jqno.equalsverifier.EqualsVerifier;

class LegTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Leg.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", testLeg())
        .verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Leg.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", Leg.record("WHATEVER", testLeg()))
        .verify();
  }

  @Test
  void test_Builder() {

    Fix fix = Fix.builder()
        .fixIdentifier("F")
        .latLong(LatLong.of(0., 0.))
        .magneticVariation(MagneticVariation.ZERO)
        .build();

    Leg leg = Leg.builder(PathTerminator.TF, 10)
        .associatedFix(fix)
        .arcRadius(29.)
        .build();

    assertAll(
        () -> assertEquals(Range.all(), leg.altitudeConstraint(), "Should be all by default."),
        () -> assertEquals(Range.all(), leg.speedConstraint(), "Should be all by default."),
        () -> assertEquals(29., leg.arcRadius().orElse(0.))
    );
  }

  private Leg.Standard testLeg() {
    return Leg.builder(PathTerminator.AF, 10)
        .associatedFix(Fix.builder()
            .fixIdentifier("FIX")
            .latLong(LatLong.of(0., 0.))
            .magneticVariation(MagneticVariation.ZERO)
            .build())
        .recommendedNavaid(Fix.builder()
            .fixIdentifier("NAVAID")
            .latLong(LatLong.of(0., 0.))
            .magneticVariation(MagneticVariation.ZERO)
            .build())
        .centerFix(Fix.builder()
            .fixIdentifier("CENTER")
            .latLong(LatLong.of(0., 0.))
            .magneticVariation(MagneticVariation.ZERO)
            .build())
        .rho(10.)
        .rnp(10.)
        .holdTime(Duration.ofSeconds(10))
        .isFlyOverFix(true)
        .isPublishedHoldingFix(true)
        .outboundMagneticCourse(10.)
        .routeDistance(10.)
        .theta(10.)
        .routeDistance(10.)
        .arcRadius(15.)
        .build();
  }
}
