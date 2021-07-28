package org.mitre.tdp.boogie.alg.resolve.element;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.graph.LinkedLegs;

class TestAirportElement {

  @Test
  void testAirportElement() {
    Airport airport = testAirport();
    AirportElement element = new AirportElement("", airport);

    LinkedLegs linked = element.legs().get(0);

    assertAll(
        () -> assertEquals(1, element.legs().size()),
        () -> assertEquals(airportLocation, linked.source().leg().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals(airportLocation, linked.target().leg().associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("KATL", linked.source().leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("KATL", linked.target().leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.IF, linked.source().leg().pathTerminator()),
        () -> assertEquals(PathTerminator.IF, linked.source().leg().pathTerminator())
    );
  }

  @Test
  void testAirportElementFiledDirect() {
    Airport airport = testAirport();
    AirportElement element = new AirportElement(" ", airport);

    LinkedLegs linked = element.legs().get(0);

    assertAll(
        () -> assertEquals(PathTerminator.DF, linked.source().leg().pathTerminator()),
        () -> assertEquals(PathTerminator.DF, linked.source().leg().pathTerminator())
    );
  }

  private static final LatLong airportLocation = LatLong.of(0.0, 0.0);

  private Airport testAirport() {
    Airport airport = mock(Airport.class);
    when(airport.latLong()).thenReturn(airportLocation);
    when(airport.airportIdentifier()).thenReturn("KATL");
    when(airport.fixIdentifier()).thenCallRealMethod();
    return airport;
  }
}
