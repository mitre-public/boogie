package org.mitre.tdp.boogie.alg.resolve.resolver;

import static java.util.Collections.emptyList;
import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.test.MockObjects.airway;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.service.impl.AirwayService;

class TestAirwayResolver {

  @Test
  void testSingleAirwayResolution() {
    Airway airway = airway("J121", emptyList());
    AirwayResolver resolver = new AirwayResolver(AirwayService.with(singleton(airway)));

    List<ResolvedElement<?>> resolved = resolver.resolve(split("J121"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertEquals("J121", resolved.get(0).reference().identifier()),
        () -> assertEquals(ElementType.AIRWAY, resolved.get(0).type())
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).build();
  }
}
