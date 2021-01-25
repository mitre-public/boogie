package org.mitre.tdp.boogie.alg.resolve.resolver;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mitre.tdp.boogie.test.MockObjects.fix;
import static org.mitre.tdp.boogie.test.MockObjects.magneticVariation;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.service.impl.FixService;

class TestFixResolver {

  @Test
  void testSingleFixResolution() {
    Fix fix = fix("JIMMY", 0.0, 0.0);
    FixResolver resolver = resolver(fix);

    List<ResolvedElement<?>> resolved = resolver.resolve(split("JIMMY"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertEquals("JIMMY", resolved.get(0).reference().identifier()),
        () -> assertEquals(ElementType.FIX, resolved.get(0).type())
    );
  }

  @Test
  void testSingleTailoredResolution() {
    Fix fix = fix("JIMMY", 0.0, 0.0);

    MagneticVariation variation = magneticVariation(10.0f, 10.0f);
    when(fix.magneticVariation()).thenReturn(variation);

    FixResolver resolver = resolver(fix);

    List<ResolvedElement<?>> resolved = resolver.resolve(split("JIMMY111018"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertEquals("JIMMY", resolved.get(0).reference().identifier()),
        () -> assertEquals(ElementType.TAILORED, resolved.get(0).type()),
        () -> assertNotEquals(LatLong.of(0.0, 0.0), resolved.get(0).legs().get(0).source().leg().pathTerminator().latLong())
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).setWildcards("").build();

  }

  private FixResolver resolver(Fix fix) {
    return new FixResolver(FixService.with(Collections.singleton(fix)));
  }
}
