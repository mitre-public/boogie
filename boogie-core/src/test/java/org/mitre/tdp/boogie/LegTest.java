package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import com.google.common.collect.Range;
import nl.jqno.equalsverifier.EqualsVerifier;

class LegTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Leg.Standard.class).verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Leg.Record.class).verify();
  }

  @Test
  void test_Builder() {

    Fix fix = Fix.builder()
        .fixIdentifier("F")
        .latLong(LatLong.of(0., 0.))
        .magneticVariation(MagneticVariation.ZERO)
        .build();

    Leg leg = Leg.builder(PathTerminator.TF, 10)
        .associatedFix(fix)
        .build();

    assertAll(
        () -> assertEquals(Range.all(), leg.altitudeConstraint(), "Should be all by default."),
        () -> assertEquals(Range.all(), leg.speedConstraint(), "Should be all by default.")
    );
  }
}
