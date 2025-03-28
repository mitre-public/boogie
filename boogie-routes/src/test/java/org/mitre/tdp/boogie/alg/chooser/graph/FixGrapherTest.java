package org.mitre.tdp.boogie.alg.chooser.graph;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

class FixGrapherTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  @Test
  void test_StandardFix() {

    ResolvedToken.StandardFix token = ResolvedToken.standardFix(dummy());

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size(), "Expected single element.");

    LinkedLegs linked = representation.iterator().next();
    assertAll(
        () -> assertEquals(LatLong.of(0., 0.), linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals("SHERL", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, linked.source().pathTerminator()),
        () -> assertSame(linked.source(), linked.target())
    );
  }

  @Test
  void test_DirectToFix() {

    ResolvedToken.DirectToFix token = ResolvedToken.directToFix(dummy());

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size(), "Expected single element.");

    LinkedLegs linked = representation.iterator().next();
    assertAll(
        () -> assertEquals(LatLong.of(0., 0.), linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals("SHERL", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator()),
        () -> assertSame(linked.source(), linked.target())
    );
  }

  private Fix dummy() {
    return Fix.builder()
        .fixIdentifier("SHERL")
        .latLong(LatLong.of(0., 0.))
        .magneticVariation(MagneticVariation.ZERO)
        .build();
  }
}
