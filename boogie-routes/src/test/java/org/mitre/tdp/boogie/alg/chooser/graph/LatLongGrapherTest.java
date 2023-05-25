package org.mitre.tdp.boogie.alg.chooser.graph;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.LinkedLegs;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class LatLongGrapherTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  @Test
  void test_StandardLatLong() {

    ResolvedToken.StandardLatLong token = ResolvedToken.standardLatLong(LatLong.of(0., 0.));

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size(), "Should be one element.");

    LinkedLegs linked = representation.iterator().next();
    assertAll(
        () -> assertEquals(LatLong.of(0., 0.), linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals("SHERL", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, linked.source().pathTerminator()),
        () -> assertSame(linked.source(), linked.target())
    );
  }

  @Test
  void test_DirectToLatLong() {

    ResolvedToken.DirectToLatLong token = ResolvedToken.directToLatLong(LatLong.of(0., 0.));

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size(), "Should be one element.");

    LinkedLegs linked = representation.iterator().next();
    assertAll(
        () -> assertEquals(LatLong.of(0., 0.), linked.source().associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals("SHERL", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator()),
        () -> assertSame(linked.source(), linked.target())
    );
  }
}
