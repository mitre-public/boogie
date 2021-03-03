package org.mitre.tdp.boogie.alg.resolve.element;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.alg.graph.LinkedLegs;

class TestAirportElement {

  @Test
  void testAirportElement() {
    Airport airport = testAirport();
    AirportElement element = new AirportElement("", airport);

    LinkedLegs linked = element.legs().get(0);

    assertAll(
        () -> assertEquals(1, element.legs().size()),
        () -> assertEquals(airportLocation, linked.source().leg().pathTerminator().latLong()),
        () -> assertEquals(airportLocation, linked.target().leg().pathTerminator().latLong()),

        () -> assertEquals("KATL", linked.source().leg().pathTerminator().identifier()),
        () -> assertEquals("KATL", linked.target().leg().pathTerminator().identifier()),

        () -> assertEquals(PathTerm.IF, linked.source().leg().type()),
        () -> assertEquals(PathTerm.IF, linked.source().leg().type())
    );
  }

  @Test
  void testAirportElementFiledDirect() {
    Airport airport = testAirport();
    AirportElement element = new AirportElement(" ", airport);

    LinkedLegs linked = element.legs().get(0);

    assertAll(
        () -> assertEquals(PathTerm.DF, linked.source().leg().type()),
        () -> assertEquals(PathTerm.DF, linked.source().leg().type())
    );
  }

  private static final LatLong airportLocation = LatLong.of(0.0, 0.0);

  private Airport testAirport() {
    Airport airport = mock(Airport.class);

    String airportId = "KATL";
    when(airport.latLong()).thenReturn(airportLocation);
    when(airport.identifier()).thenReturn(airportId);
    when(airport.navigationSource()).thenReturn(() -> "CIFP");

    return airport;
  }
}
