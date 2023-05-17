package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.airway;
import static org.mitre.tdp.boogie.alg.LookupService.inMemory;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class TestAirwayResolver {

  @Test
  void testSingleAirwayResolution() {
    Airway airway = airway("J121", emptyList());
    AirwayResolver resolver = new AirwayResolver(inMemory(singleton(airway), a -> Stream.of(a.airwayIdentifier())));

    List<ResolvedElement> resolved = resolver.resolve(RouteToken.standard("J121", 0.));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0) instanceof AirwayElement)
    );
  }
}
