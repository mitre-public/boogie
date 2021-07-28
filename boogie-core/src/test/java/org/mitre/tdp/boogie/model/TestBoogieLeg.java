package org.mitre.tdp.boogie.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;

import com.google.common.collect.Range;
import nl.jqno.equalsverifier.EqualsVerifier;

class TestBoogieLeg {

  @Test
  void testEqualsAndHashCode() {
    EqualsVerifier.forClass(BoogieLeg.class).verify();
  }

  @Test
  void testToFromBuilder() {
    BoogieFix fix = new BoogieFix.Builder()
        .fixIdentifier("TEST")
        .fixRegion("TEST")
        .latitude(0.)
        .longitude(0.)
        .publishedVariation(0.)
        .modeledVariation(0.)
        .elevation(0.)
        .build();

    BoogieLeg leg = new BoogieLeg.Builder()
        .associatedFix(fix)
        .recommendedNavaid(fix)
        .centerFix(fix)
        .pathTerminator(PathTerminator.TF)
        .sequenceNumber(0)
        .outboundMagneticCourse(0.)
        .rho(0.)
        .theta(0.)
        .rnp(0.)
        .routeDistance(0.)
        .verticalAngle(0.)
        .speedConstraint(Range.all())
        .altitudeConstraint(Range.all())
        .turnDirection(TurnDirection.either())
        .isFlyOverFix(true)
        .isPublishedHoldingFix(true)
        .build();

    assertEquals(leg, leg.toBuilder().build(), "toBuilder().build() should return equal leg.");
  }
}
