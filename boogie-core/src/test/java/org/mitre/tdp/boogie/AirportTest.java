package org.mitre.tdp.boogie;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;

import nl.jqno.equalsverifier.EqualsVerifier;

class AirportTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Airport.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", testAirport())
        .verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Airport.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", Airport.record("WHATEVER", testAirport()))
        .verify();
  }

  private Airport.Standard testAirport() {
    return Airport.builder()
        .airportIdentifier("APT")
        .latLong(LatLong.of(0., 0.))
        .magneticVariation(MagneticVariation.ZERO)
        .runways(List.of(
            Runway.builder()
                .runwayIdentifier("RW")
                .origin(LatLong.of(0., 0.))
                .length(Distance.ofFeet(10))
                .course(Course.ofDegrees(10.))
                .build()
        ))
        .build();
  }
}
