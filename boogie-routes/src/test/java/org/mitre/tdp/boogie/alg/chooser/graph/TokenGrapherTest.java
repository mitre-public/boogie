package org.mitre.tdp.boogie.alg.chooser.graph;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

class TokenGrapherTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  @Test
  void test_StandardAirport() {

    Airport airport = mock(Airport.class);
    when(airport.airportIdentifier()).thenReturn("KATL");
    when(airport.latLong()).thenReturn(LatLong.of(0.0, 0.0));

    ResolvedToken.StandardAirport token = ResolvedToken.standardAirport(airport);

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size());

    LinkedLegs linked = representation.iterator().next();
    assertAll(

    );
  }

  @Test
  void test_DirectToAirport() {
  }

  private Airport mockAirport(String identifier, LatLong location) {

    Airport airport = mock(Airport.class);

    when(airport.airportIdentifier()).thenReturn("KATL");
    when(airport.latLong()).thenReturn(LatLong.of(0.0, 0.0));
    when(airport.fixIdentifier()).thenCallRealMethod();

    return airport;
  }
}
