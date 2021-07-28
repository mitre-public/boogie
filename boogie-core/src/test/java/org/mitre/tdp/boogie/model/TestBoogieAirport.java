package org.mitre.tdp.boogie.model;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestBoogieAirport {

  @Test
  void testEqualsAndHashCode() {
    EqualsVerifier.forClass(BoogieAirport.class).verify();
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

    BoogieAirport airport = new BoogieAirport.Builder()
        .airportIdentifier("TEST")
        .airportRegion("TEST")
        .latitude(0.)
        .longitude(0.)
        .publishedVariation(0.)
        .modeledVariation(0.)
        .runways(singletonList(runway))
        .build();

    assertEquals(airport, airport.toBuilder().build(), "toBuilder().build() should return equivalent object.");
  }
}
