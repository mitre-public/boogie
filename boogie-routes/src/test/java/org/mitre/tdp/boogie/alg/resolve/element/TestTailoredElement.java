package org.mitre.tdp.boogie.alg.resolve.element;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.graph.LinkedLegs;

class TestTailoredElement {

  @Test
  void testBearingDistanceTailored() {
    String tailoredId = "HTO354018";
    Pair<Double, Double> bearingAndDistance = TailoredElement.bearingDistance(tailoredId);

    assertAll(
        () -> assertEquals(bearingAndDistance.first(), 354.0, 0.01),
        () -> assertEquals(bearingAndDistance.second(), 18.0, 0.01)
    );
  }

  @Test
  void testTailoredElement() {
    String tailoredId = "HTO354018";
    Fix fix = mock(Fix.class);

    when(fix.fixIdentifier()).thenReturn("HTO");
    when(fix.latLong()).thenReturn(LatLong.of(0.0, 0.0));
    when(fix.publishedVariation()).thenReturn(Optional.of(-10.));
    when(fix.modeledVariation()).thenReturn(-9.);

    TailoredElement element = new TailoredElement(tailoredId, "/", fix);

    assertEquals(element.legs().size(), 1);

    LinkedLegs linked = element.legs().get(0);

    double projLat = 0.288345;
    double projLon = -0.082682;

    assertAll(
        () -> assertEquals(projLat, linked.source().leg().associatedFix().map(Fix::latitude).orElse(Double.MAX_VALUE), 0.0001),
        () -> assertEquals(projLon, linked.source().leg().associatedFix().map(Fix::longitude).orElse(Double.MAX_VALUE), 0.0001),
        () -> assertEquals(linked.source().leg().associatedFix().map(Fix::latLong), linked.target().leg().associatedFix().map(Fix::latLong)),

        // do we want the first tailored string
        () -> assertEquals("HTO354018", linked.source().leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("HTO354018", linked.target().leg().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.IF, linked.source().leg().pathTerminator()),
        () -> assertEquals(PathTerminator.IF, linked.source().leg().pathTerminator())
    );

    element = new TailoredElement(tailoredId, "", fix);
    LinkedLegs linked2 = element.legs().iterator().next();

    assertAll(
        () -> assertEquals(PathTerminator.DF, linked2.source().leg().pathTerminator()),
        () -> assertEquals(PathTerminator.DF, linked2.target().leg().pathTerminator())
    );
  }
}
