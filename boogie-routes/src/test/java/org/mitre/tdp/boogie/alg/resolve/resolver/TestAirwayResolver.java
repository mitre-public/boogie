package org.mitre.tdp.boogie.alg.resolve.resolver;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.DefaultLookupService.newLookupService;
import static org.mitre.tdp.boogie.MockObjects.airway;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

class TestAirwayResolver {

  @Test
  void testSingleAirwayResolution() {
    Airway airway = airway("J121", emptyList());
    AirwayResolver resolver = new AirwayResolver(newLookupService(Airway::airwayIdentifier, singleton(airway)));

    List<ResolvedElement<?>> resolved = resolver.resolve(split("J121"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0).reference() instanceof Airway)
    );

    Airway resolvedAirway = (Airway) resolved.get(0).reference();

    assertAll(
        () -> assertEquals("J121", resolvedAirway.airwayIdentifier()),
        () -> assertEquals(ElementType.AIRWAY, resolved.get(0).type())
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).build();
  }
}
