package org.mitre.tdp.boogie.alg.resolve;

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
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.TailoredElement;

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
    when(fix.fixRegion()).thenReturn("K2");
    when(fix.latLong()).thenReturn(LatLong.of(0.0, 0.0));
    when(fix.publishedVariation()).thenReturn(Optional.of(-10.));
    when(fix.modeledVariation()).thenReturn(-9.);

    TailoredElement element = new TailoredElement(fix, tailoredId, "/");
    assertEquals(element.toLinkedLegs().size(), 1);

    LinkedLegs linked = element.toLinkedLegs().get(0);

    double projLat = 0.288345;
    double projLon = -0.082682;

    assertAll(
        () -> assertEquals(projLat, linked.source().associatedFix().map(Fix::latitude).orElse(Double.MAX_VALUE), 0.0001),
        () -> assertEquals(projLon, linked.source().associatedFix().map(Fix::longitude).orElse(Double.MAX_VALUE), 0.0001),
        () -> assertEquals(linked.source().associatedFix().map(Fix::latLong), linked.target().associatedFix().map(Fix::latLong)),

        // do we want the first tailored string
        () -> assertEquals("HTO354018", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals("HTO354018", linked.target().associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(PathTerminator.IF, linked.source().pathTerminator()),
        () -> assertEquals(PathTerminator.IF, linked.source().pathTerminator())
    );

    element = new TailoredElement(fix, tailoredId, "");
    LinkedLegs linked2 = element.toLinkedLegs().iterator().next();

    assertAll(
        () -> assertEquals(PathTerminator.DF, linked2.source().pathTerminator()),
        () -> assertEquals(PathTerminator.DF, linked2.target().pathTerminator())
    );
  }
}
