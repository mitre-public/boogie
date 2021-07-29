package org.mitre.tdp.boogie.alg.resolve.resolver;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.DefaultLookupService.newLookupService;
import static org.mitre.tdp.boogie.MockObjects.airport;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

class TestAirportResolver {

  @Test
  void testSingleAirportResolution() {
    Airport airport = airport("JIMMY", 0.0, 0.0);
    AirportResolver resolver = new AirportResolver(newLookupService(Airport::airportIdentifier, singleton(airport)));

    List<ResolvedElement<?>> resolved = resolver.resolve(split("JIMMY"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0).reference() instanceof Airport)
    );

    Airport resolvedAirport = (Airport) resolved.get(0).reference();

    assertAll(
        () -> assertEquals("JIMMY", resolvedAirport.airportIdentifier()),
        () -> assertEquals(ElementType.AIRPORT, resolved.get(0).type())
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).build();
  }
}
