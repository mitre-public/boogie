package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class MagneticVariationTest {

  @Test
  void testEqualsAndHashCode() {
    EqualsVerifier.forClass(MagneticVariation.class).verify();
  }

  @Test
  void testMagneticToTrue() {
    assertAll(
        () -> assertEquals(12.0d, MagneticVariation.ofDegrees(7.).magneticToTrue(5.0)),
        () -> assertEquals(12.0d, MagneticVariation.ofDegrees(10.).magneticToTrue(2.0))
    );
  }

  @Test
  void testTrueToMagnetic() {
    assertAll(
        () -> assertEquals(12.0d, MagneticVariation.ofDegrees(7.).trueToMagnetic(19.0)),
        () -> assertEquals(12.0d, MagneticVariation.ofDegrees(10.).trueToMagnetic(22.0))
    );
  }
}
