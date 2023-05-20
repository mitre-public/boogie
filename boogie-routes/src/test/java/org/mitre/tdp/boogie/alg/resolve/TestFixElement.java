package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;

class TestFixElement {

  @Test
  void testFixElement() {
    Fix fix = mock(Fix.class);

    LatLong fixLocation = LatLong.of(0.0, 0.0);
    String fixId = "SHERL";

    when(fix.latLong()).thenReturn(fixLocation);
    when(fix.fixIdentifier()).thenReturn(fixId);

    FixToken element = new FixToken(fix, "");

    assertEquals(element.toLinkedLegs().size(), 1);

    LinkedLegs linked = element.toLinkedLegs().get(0);

    assertAll(
        () -> assertEquals(fixLocation, linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals(fixLocation, linked.target().associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("SHERL", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("SHERL", linked.target().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator()),
        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator())
    );

    element = new FixToken(fix, "/");
    LinkedLegs linked2 = element.toLinkedLegs().iterator().next();

    assertAll(
        () -> assertEquals(PathTerminator.IF, linked2.source().pathTerminator()),
        () -> assertEquals(PathTerminator.IF, linked2.target().pathTerminator())
    );
  }
}
