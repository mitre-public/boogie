package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.MockObjects.airway;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class AirwayResolverTest {

  @Test
  void test_Standard() {

    RouteToken token = RouteToken.faaIfrBuilder("J121", 0.).build();

    Collection<ResolvedToken> resolved = resolver(token).resolve(token);
    assertEquals(1, resolved.size(), "Should be one resolved token");

    ResolvedToken.StandardAirway airway = (ResolvedToken.StandardAirway) resolved.iterator().next();
    assertEquals("J121", airway.infrastructure().airwayIdentifier());
  }

  private AirwayResolver resolver(RouteToken token) {
    Airway airway = airway(token.infrastructureName(), emptyList());
    return new AirwayResolver(LookupService.inMemory(List.of(airway), a -> Stream.of(a.airwayIdentifier())));
  }
}
