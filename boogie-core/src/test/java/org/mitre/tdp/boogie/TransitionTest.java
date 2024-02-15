package org.mitre.tdp.boogie;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import nl.jqno.equalsverifier.EqualsVerifier;

class TransitionTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Transition.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", testTransition())
        .verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Transition.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", Transition.record("WHATEVER", testTransition()))
        .verify();
  }

  private Transition.Standard testTransition() {
    return Transition.builder()
        .transitionIdentifier("TRANSITION")
        .transitionType(TransitionType.RUNWAY)
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
