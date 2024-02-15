package org.mitre.tdp.boogie;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import nl.jqno.equalsverifier.EqualsVerifier;

class AirwayTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Airway.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", testAirway())
        .verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Airway.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", Airway.record("WHATEVER", testAirway()))
        .verify();
  }

  private Airway.Standard testAirway() {
    return Airway.builder()
        .airwayIdentifier("AWY")
        .add(Leg.builder(PathTerminator.AF, 10)
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
            .build())
        .build();
  }
}
