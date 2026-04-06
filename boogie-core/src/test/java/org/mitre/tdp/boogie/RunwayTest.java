package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

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

  @Test
  void testOriginElevation_present() {
    Runway.Standard runway = Runway.builder()
        .runwayIdentifier("RW27L")
        .origin(LatLong.of(0., 0.))
        .length(Distance.ofFeet(10000))
        .course(Course.ofDegrees(270))
        .originElevation(Distance.ofFeet(100))
        .build();

    assertEquals(Optional.of(Distance.ofFeet(100)), runway.originElevation());
  }

  @Test
  void testOriginElevation_absent() {
    Runway.Standard runway = Runway.builder()
        .runwayIdentifier("RW27L")
        .origin(LatLong.of(0., 0.))
        .build();

    assertEquals(Optional.empty(), runway.originElevation());
  }

  @Test
  void testOriginElevation_roundTripsViaToBuilder() {
    Runway.Standard runway = Runway.builder()
        .runwayIdentifier("RW09R")
        .origin(LatLong.of(1., 1.))
        .originElevation(Distance.ofFeet(250))
        .build();

    Runway.Standard copy = runway.toBuilder().build();

    assertEquals(runway, copy);
    assertEquals(runway.originElevation(), copy.originElevation());
  }

  @Test
  void testOriginElevation_recordDelegates() {
    Runway.Standard delegate = Runway.builder()
        .runwayIdentifier("RW18")
        .origin(LatLong.of(0., 0.))
        .originElevation(Distance.ofFeet(500))
        .build();

    Runway.Record<String> record = Runway.record("datum", delegate);

    assertEquals(Optional.of(Distance.ofFeet(500)), record.originElevation());
  }

  private Runway.Standard testRunway() {
    return Runway.builder()
        .runwayIdentifier("RW")
        .origin(LatLong.of(0., 0.))
        .length(Distance.ofFeet(10))
        .course(Course.ofDegrees(10))
        .originElevation(Distance.ofFeet(100))
        .build();
  }
}
