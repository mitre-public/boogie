package org.mitre.tdp.boogie.alg.resolve.resolver;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

class TestLatLonResolver {

  @Test
  void testSingleLatLonResolution() {
    LatLonResolver resolver = new LatLonResolver();

    List<ResolvedElement<?>> resolved = resolver.resolve(split("5300N/14000W"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0).reference() instanceof Fix)
    );

    Fix resolvedFix = (Fix) resolved.get(0).reference();

    assertAll(
        () -> assertEquals("5300N/14000W", resolvedFix.fixIdentifier()),
        () -> assertEquals(ElementType.LATLON, resolved.get(0).type()),
        () -> assertEquals(LatLong.of(53.0, -140.0), resolvedFix.latLong())
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).setWildcards("").build();
  }
}
