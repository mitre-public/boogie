package org.mitre.tdp.boogie.alg.resolve.resolver;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.DefaultLookupService.newLookupService;
import static org.mitre.tdp.boogie.test.MockObjects.fix;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

class TestFixResolver {

  @Test
  void testSingleFixResolution() {
    Fix fix = fix("JIMMY", 0.0, 0.0);
    FixResolver resolver = resolver(fix);

    List<ResolvedElement<?>> resolved = resolver.resolve(split("JIMMY"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0).reference() instanceof Fix)
    );

    Fix resolvedFix = (Fix) resolved.get(0).reference();

    assertAll(
        () -> assertEquals("JIMMY", resolvedFix.fixIdentifier()),
        () -> assertEquals(ElementType.FIX, resolved.get(0).type())
    );
  }

  @Test
  void testSingleTailoredResolution() {
    Fix fix = fix("JIMMY", 0.0, 0.0);

    when(fix.publishedVariation()).thenReturn(Optional.of(10.));
    when(fix.modeledVariation()).thenReturn(10.);

    FixResolver resolver = resolver(fix);

    List<ResolvedElement<?>> resolved = resolver.resolve(split("JIMMY111018"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0).reference() instanceof Fix)
    );

    ResolvedElement<Fix> resolvedFix = (ResolvedElement<Fix>) resolved.get(0);
    LatLong generatedLocation = resolvedFix.legs().get(0).source().leg().associatedFix().map(Fix::latLong).orElse(LatLong.of(0., 0.));

    assertAll(
        () -> assertEquals("JIMMY", resolvedFix.reference().fixIdentifier()),
        () -> assertEquals(ElementType.TAILORED, resolved.get(0).type()),
        () -> assertNotEquals(LatLong.of(0., 0.), generatedLocation, "Generated location in the final LinkedLegs should reflect the offset from the tailoring.")
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).setWildcards("").build();

  }

  private FixResolver resolver(Fix fix) {
    return new FixResolver(newLookupService(Fix::fixIdentifier, singleton(fix)));
  }
}
