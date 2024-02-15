package org.mitre.tdp.boogie.alg.chooser.graph;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

class LatLongGrapherTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  @Test
  void test_StandardLatLong() {

    ResolvedToken.StandardLatLong token = ResolvedToken.standardLatLong("0000N/00000W", LatLong.of(0., 0.));

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size(), "Should be one element.");

    LinkedLegs linked = representation.iterator().next();
    assertAll(
        () -> assertEquals("0000N/00000W", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null), "Identifier"),
        () -> assertEquals(LatLong.of(0., 0.), linked.source().associatedFix().map(Fix::latLong).orElse(null), "LatLong"),
        () -> assertEquals(PathTerminator.IF, linked.source().pathTerminator(), "PathTerminator"),
        () -> assertSame(linked.source(), linked.target())
    );
  }

  @Test
  void test_DirectToLatLong() {

    ResolvedToken.DirectToLatLong token = ResolvedToken.directToLatLong("0000N/00000W", LatLong.of(0., 0.));

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size(), "Should be one element.");

    LinkedLegs linked = representation.iterator().next();
    assertAll(
        () -> assertEquals("0000N/00000W", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null), "Identifier"),
        () -> assertEquals(LatLong.of(0., 0.), linked.source().associatedFix().map(Fix::latLong).orElse(null), "LatLong"),
        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator(), "PathTerminator"),
        () -> assertSame(linked.source(), linked.target())
    );
  }
}
