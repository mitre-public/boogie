package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestMagneticVariation {

  @Test
  void testEqualsAndHashCode() {
    EqualsVerifier.forClass(MagneticVariation.class).verify();
  }

  @Test
  void testMagneticToTruePublished() {
    MagneticVariation localVariation = new MagneticVariation(7.0d, 10.0d);
    assertEquals(12.0d, localVariation.magneticToTrue(5.0));
  }

  @Test
  void testMagneticToTrueModeled() {
    MagneticVariation localVariation = new MagneticVariation(null, 10.0d);
    assertEquals(12.0d, localVariation.magneticToTrue(2.0));
  }

  @Test
  void testTrueToMagneticPublished() {
    MagneticVariation localVariation = new MagneticVariation(7.0d, 10.0d);
    assertEquals(12.0d, localVariation.trueToMagnetic(19.0));
  }

  @Test
  void testTrueToMagneticModeled() {
    MagneticVariation localVariation = new MagneticVariation(null, 10.0d);
    assertEquals(12.0d, localVariation.trueToMagnetic(22.0));
  }
}
