package org.mitre.tdp.boogie.alg.resolve.resolver;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
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
        () -> assertEquals("5300N/14000W", resolved.get(0).reference().identifier()),
        () -> assertEquals(ElementType.LATLON, resolved.get(0).type()),
        () -> assertEquals(LatLong.of(53.0, -140.0), resolved.get(0).legs().get(0).source().leg().pathTerminator().latLong())
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).setWildcards("").build();
  }
}
