package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.airway;
import static org.mitre.tdp.boogie.alg.DefaultLookupService.newLookupService;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

class TestAirwayResolver {

  @Test
  void testSingleAirwayResolution() {
    Airway airway = airway("J121", emptyList());
    AirwayResolver resolver = new AirwayResolver(newLookupService(singleton(airway), Airway::airwayIdentifier));

    List<ResolvedElement> resolved = resolver.resolve(split("J121"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0) instanceof AirwayElement)
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).build();
  }
}
