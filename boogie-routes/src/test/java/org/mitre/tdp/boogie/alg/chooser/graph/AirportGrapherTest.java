package org.mitre.tdp.boogie.alg.chooser.graph;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

class AirportGrapherTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  @Test
  void test_StandardAirport() {

    Airport airport = mockAirport("KATL", LatLong.of(0., 0.));
    ResolvedToken.StandardAirport token = ResolvedToken.standardAirport(airport);

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size());

    LinkedLegs linked = representation.iterator().next();
    assertAll(
        () -> assertEquals(airport.latLong(), linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals(airport.latLong(), linked.target().associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("KATL", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("KATL", linked.target().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.IF, linked.source().pathTerminator()),
        () -> assertEquals(PathTerminator.IF, linked.source().pathTerminator())
    );
  }

  @Test
  void test_DirectToAirport() {

    Airport airport = mockAirport("KATL", LatLong.of(0., 0.));
    ResolvedToken.StandardAirport token = ResolvedToken.standardAirport(airport);

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size());

    LinkedLegs linked = representation.iterator().next();
    assertAll(
        () -> assertEquals(airport.latLong(), linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals(airport.latLong(), linked.target().associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("KATL", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("KATL", linked.target().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator()),
        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator())
    );
  }

  private Airport mockAirport(String identifier, LatLong location) {

    Airport airport = mock(Airport.class);

    when(airport.airportIdentifier()).thenReturn(identifier);
    when(airport.latLong()).thenReturn(location);
    when(airport.fixIdentifier()).thenCallRealMethod();

    return airport;
  }
}
