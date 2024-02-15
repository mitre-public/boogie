package org.mitre.tdp.boogie;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import nl.jqno.equalsverifier.EqualsVerifier;

class ProcedureTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Procedure.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", testProcedure())
        .verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Procedure.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", Procedure.record("WHATEVER", testProcedure()))
        .verify();
  }

  private Procedure.Standard testProcedure() {
    return Procedure.builder()
        .procedureIdentifier("PROC")
        .airportIdentifier("APT")
        .procedureType(ProcedureType.STAR)
        .requiredNavigationEquipage(RequiredNavigationEquipage.CONV)
        .transitions(List.of(Transition.builder()
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
            .build()))
        .build();
  }
}
