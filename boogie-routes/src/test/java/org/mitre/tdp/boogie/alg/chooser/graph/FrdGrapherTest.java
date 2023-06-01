package org.mitre.tdp.boogie.alg.chooser.graph;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.alg.resolve.ResolvedToken;

class FrdGrapherTest {

  private static final TokenGrapher GRAPHER = TokenGrapher.standard();

  @Test
  void test_StandardFrd() {

    ResolvedToken.StandardFrd token = ResolvedToken.standardFrd(frd());

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size(), "Expected single element.");

    LinkedLegs linked = representation.iterator().next();
    assertAll(
        () -> assertEquals("SHERL000001", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, linked.source().pathTerminator()),
        () -> assertSame(linked.source(), linked.target())
    );
  }

  @Test
  void test_DirectToFrd() {

    ResolvedToken.DirectToFrd token = ResolvedToken.directToFrd(frd());

    Collection<LinkedLegs> representation = GRAPHER.graphRepresentationOf(token);
    assertEquals(1, representation.size(), "Expected single element.");

    LinkedLegs linked = representation.iterator().next();
    assertAll(
        () -> assertEquals("SHERL000001", linked.source().associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, linked.source().pathTerminator()),
        () -> assertSame(linked.source(), linked.target())
    );
  }

  private Fix.FixRadialDistance frd() {

    Fix fix = Fix.builder()
        .fixIdentifier("SHERL")
        .latLong(LatLong.of(0., 0.))
        .magneticVariation(MagneticVariation.ofDegrees(10.))
        .build();

    return Fix.fixRadialDistance(
        fix,
        Course.ofDegrees(0.),
        Distance.ofNauticalMiles(1.)
    );
  }
}
