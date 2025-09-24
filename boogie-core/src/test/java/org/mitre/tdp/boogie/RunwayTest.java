package org.mitre.tdp.boogie;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;

import nl.jqno.equalsverifier.EqualsVerifier;

class RunwayTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Runway.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", testRunway())
        .verify();
  }

  @Test
  void testEqualsAndHashCode_Record() {
    EqualsVerifier.forClass(Runway.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", Runway.record("WHATEVER", testRunway()))
        .verify();
  }

  private Runway.Standard testRunway() {
    return Runway.builder()
        .runwayIdentifier("RW")
        .origin(LatLong.of(0., 0.))
        .length(Distance.ofFeet(10))
        .course(Course.ofDegrees(10))
        .elevation(Distance.ofFeet(10))
        .build();
  }
}
