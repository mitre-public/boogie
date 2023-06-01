package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;

import nl.jqno.equalsverifier.EqualsVerifier;

class FixTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Fix.Standard.class).verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Fix.Record.class).verify();
  }

  @Test
  void testEqualsAndHashCode_FixRadialDistance() {
    EqualsVerifier.forClass(Fix.FixRadialDistance.class).verify();
  }

  @Test
  void testFixRadialDistance_Identifier() {
    assertEquals("SHERL000011", frd().fixIdentifier());
  }

  @Test
  void testFixRadialDistance_LatLong() {

    LatLong projected = frd().latLong();

    assertAll(
        () -> assertNotEquals(0., projected.latitude(), "Latitude"),
        () -> assertNotEquals(0., projected.longitude(), "Longitude")
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
        Distance.ofNauticalMiles(11.)
    );
  }
}
