package org.mitre.tdp.boogie.alg.resolve.resolver;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.test.MockObjects.airport;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.service.impl.AirportService;

class TestAirportResolver {

  @Test
  void testSingleAirportResolution() {
    Airport airport = airport("JIMMY", 0.0, 0.0);
    AirportResolver resolver = new AirportResolver(AirportService.with(singleton(airport)));

    List<ResolvedElement<?>> resolved = resolver.resolve(split("JIMMY"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertEquals("JIMMY", resolved.get(0).reference().identifier()),
        () -> assertEquals(ElementType.AIRPORT, resolved.get(0).type())
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).build();
  }
}
