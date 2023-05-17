package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.split.RouteToken;

class TestLatLonResolver {

  @Test
  void testSingleLatLonResolution() {
    LatLonResolver resolver = new LatLonResolver();

    List<ResolvedElement> resolved = resolver.resolve(RouteToken.standard("5300N/14000W", 0.));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0) instanceof LatLonElement)
    );

    Fix resolvedFix = resolved.get(0).toLinkedLegs().get(0).source().associatedFix().orElseThrow(IllegalStateException::new);

    assertAll(
        () -> assertEquals("5300N/14000W", resolvedFix.fixIdentifier()),
        () -> assertEquals(LatLong.of(53.0, -140.0), resolvedFix.latLong())
    );
  }
}
