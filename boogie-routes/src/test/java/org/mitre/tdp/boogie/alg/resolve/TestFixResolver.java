package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singleton;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.fix;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class TestFixResolver {

  @Test
  void testSingleFixResolution() {
    Fix fix = fix("JIMMY", 0.0, 0.0);
    FixResolver resolver = resolver(fix);

    List<ResolvedElement> resolved = resolver.resolve(RouteToken.standard("JIMMY", 0.));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0) instanceof FixElement)
    );
  }

  @Test
  void testSingleTailoredResolution() {
    Fix fix = fix("JIMMY", 0.0, 0.0);
    when(fix.fixRegion()).thenReturn("K2");
    when(fix.publishedVariation()).thenReturn(Optional.of(10.));
    when(fix.modeledVariation()).thenReturn(10.);

    FixResolver resolver = resolver(fix);

    List<ResolvedElement> resolved = resolver.resolve(RouteToken.standard("JIMMY111018", 0.));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0) instanceof TailoredElement)
    );

    ResolvedElement resolvedFix = resolved.get(0);
    LatLong generatedLocation = resolvedFix.toLinkedLegs().get(0).source().associatedFix().map(Fix::latLong).orElse(LatLong.of(0., 0.));
    assertNotEquals(LatLong.of(0., 0.), generatedLocation, "Generated location in the final LinkedLegs should reflect the offset from the tailoring.");
  }

  private FixResolver resolver(Fix fix) {
    return new FixResolver(LookupService.inMemory(singleton(fix), f -> Stream.of(f.fixIdentifier())));
  }
}
