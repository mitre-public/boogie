package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.MockObjects.airport;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class AirportResolverTest {

  @Test
  void test_Standard() {

    RouteToken token = RouteToken.faaIfrBuilder("KATL", 0.).build();

    Collection<ResolvedToken> resolved = resolver(token).resolve(token);
    assertEquals(1, resolved.size(), "Should be one resolved token");

    ResolvedToken.StandardAirport airport = (ResolvedToken.StandardAirport) resolved.iterator().next();
    assertAll(
        () -> assertEquals("KATL", airport.infrastructure().airportIdentifier(), "Identifier"),
        () -> assertEquals(LatLong.of(0., 0.), airport.infrastructure().latLong(), "LatLong")
    );
  }

  @Test
  void test_Direct() {

    RouteToken token = RouteToken.faaIfrBuilder("KATL", 0.)
        .wildcards(" ")
        .build();

    Collection<ResolvedToken> resolved = resolver(token).resolve(token);
    assertEquals(1, resolved.size(), "Should be one resolved token");

    ResolvedToken.DirectToAirport airport = (ResolvedToken.DirectToAirport) resolved.iterator().next();
    assertAll(
        () -> assertEquals("KATL", airport.infrastructure().airportIdentifier(), "Identifier"),
        () -> assertEquals(LatLong.of(0., 0.), airport.infrastructure().latLong(), "LatLong")
    );
  }

  private AirportResolver resolver(RouteToken token) {
    Airport airport = airport(token.infrastructureName(), 0., 0.);
    return new AirportResolver(LookupService.inMemory(List.of(airport), a -> Stream.of(a.airportIdentifier())));
  }
}
