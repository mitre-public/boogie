package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import nl.jqno.equalsverifier.EqualsVerifier;

class AirspaceSequenceTest {
  static AirspaceSequence sequence = AirspaceSequence.builder(Geometry.CIRCLE, 10)
      .associatedFix(LatLong.of(0., 1.))
      .centerFix(LatLong.of(2., 3.))
      .arcRadius(10D)
      .arcBearing(11D)
      .build();

  @Test
  void verifyStandard() {
    EqualsVerifier.forClass(AirspaceSequence.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", createStandard())
        .verify();
  }

  @Test
  void verifyRecord() {
    EqualsVerifier.forClass(AirspaceSequence.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", AirspaceSequence.record("WHATEVER", createStandard()))
        .verify();
  }

  @Test
  void testBuilder() {
    assertAll(
        () -> assertEquals(0., sequence.associatedFix().get().latitude()),
        () -> assertEquals(1., sequence.associatedFix().get().longitude()),
        () -> assertEquals(2, sequence.centerFix().get().latitude()),
        () -> assertEquals(3, sequence.centerFix().get().longitude()),
        () -> assertEquals(10, sequence.arcRadius().get()),
        () -> assertEquals(11, sequence.arcBearing().get())
    );

  }

  private AirspaceSequence.Standard createStandard() {
    return AirspaceSequence.builder(Geometry.CIRCLE, 10)
        .associatedFix(LatLong.of(0., 0.))
        .centerFix(LatLong.of(0., 0.))
        .arcRadius(10D)
        .arcBearing(11D)
        .build();
  }

}