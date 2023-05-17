package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.airport;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class TestAirportResolver {

  @Test
  void testSingleAirportResolution() {
    Airport airport = airport("JIMMY", 0.0, 0.0);
    AirportResolver resolver = new AirportResolver(LookupService.inMemory(singleton(airport), a -> Stream.of(a.airportIdentifier())));

    RouteToken token = RouteToken.standard("JIMMY", 0.);
    List<ResolvedElement> resolved = resolver.resolve(token);

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0) instanceof AirportElement)
    );
  }
}
