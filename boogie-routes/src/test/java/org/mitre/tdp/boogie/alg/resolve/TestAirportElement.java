package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.AirportElement;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;

class TestAirportElement {

  @Test
  void testAirportElement() {
    Airport airport = testAirport();
    AirportElement element = new AirportElement(airport, "");

    LinkedLegs linked = element.toLinkedLegs().get(0);

    assertAll(
        () -> assertEquals(1, element.toLinkedLegs().size()),
        () -> assertEquals(airportLocation, linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals(airportLocation, linked.target().associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("KATL", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("KATL", linked.target().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.IF, linked.source().pathTerminator()),
        () -> assertEquals(PathTerminator.IF, linked.source().pathTerminator())
    );
  }

  @Test
  void testAirportElementFiledDirect() {
    Airport airport = testAirport();
    AirportElement element = new AirportElement( airport, " ");

    LinkedLegs linked = element.toLinkedLegs().get(0);

    assertAll(
        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator()),
        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator())
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
