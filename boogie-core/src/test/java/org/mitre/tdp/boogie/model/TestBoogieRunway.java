package org.mitre.tdp.boogie.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestBoogieRunway {

  @Test
  void testEqualsAndHashCode() {
    EqualsVerifier.forClass(BoogieRunway.class).verify();
  }

  @Test
  void testToFromBuilder() {
    BoogieRunway runway = new BoogieRunway.Builder()
        .runwayIdentifier("TEST")
        .airportIdentifier("TEST")
        .airportRegion("TEST")
        .length(0.)
        .width(0.)
        .trueCourse(0.)
        .arrivalRunwayEnd(LatLong.of(0., 0.))
        .departureRunwayEnd(LatLong.of(0., 0.))
        .build();

    assertEquals(runway, runway.toBuilder().build(), "toBuilder().build() should return equivalent runway.");
  }
}
