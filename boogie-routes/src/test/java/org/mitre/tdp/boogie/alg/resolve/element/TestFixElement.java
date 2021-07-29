package org.mitre.tdp.boogie.alg.resolve.element;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.graph.LinkedLegs;

class TestFixElement {

  @Test
  void testFixElement() {
    Fix fix = mock(Fix.class);

    LatLong fixLocation = LatLong.of(0.0, 0.0);
    String fixId = "SHERL";

    when(fix.latLong()).thenReturn(fixLocation);
    when(fix.fixIdentifier()).thenReturn(fixId);

    FixElement element = new FixElement(fix, "");

    assertEquals(element.legs().size(), 1);

    LinkedLegs linked = element.legs().get(0);

    assertAll(
        () -> assertEquals(fixLocation, linked.source().leg().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals(fixLocation, linked.target().leg().associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("SHERL", linked.source().leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("SHERL", linked.target().leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.DF, linked.source().leg().pathTerminator()),
        () -> assertEquals(PathTerminator.DF, linked.source().leg().pathTerminator())
    );

    element = new FixElement(fix, "/");
    LinkedLegs linked2 = element.legs().iterator().next();

    assertAll(
        () -> assertEquals(PathTerminator.IF, linked2.source().leg().pathTerminator()),
        () -> assertEquals(PathTerminator.IF, linked2.target().leg().pathTerminator())
    );
  }
}
