package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;

import nl.jqno.equalsverifier.EqualsVerifier;

class HeliportTest {

  @Test
  void testEqualsAndHashCode_Standard() {
    EqualsVerifier.forClass(Heliport.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", testHeliport())
        .verify();
  }

  @Test
  void testRunwaysRoundTripViaToBuilder() {
    Heliport.Standard heliport = testHeliport();

    Heliport.Standard copy = heliport.toBuilder().build();

    assertEquals(heliport, copy);
    assertEquals(heliport.runways(), copy.runways());
  }

  @Test
  void testRunwaysRecordDelegates() {
    Heliport.Standard delegate = testHeliport();

    Heliport.Record<String> record = Heliport.record("datum", delegate);

    assertEquals(delegate.runways(), record.runways());
  }

  @Test
  void testBuilderCopiesLandingSurfaces() {
    List<Runway> runways = new ArrayList<>(List.of(testRunway()));
    List<Helipad> helipads = new ArrayList<>(List.of(testHelipad()));
    Heliport.Standard heliport = Heliport.builder()
        .heliportIdentifier("HPT")
        .latLong(LatLong.of(0., 0.))
        .runways(runways)
        .helipads(helipads)
        .build();

    runways.clear();
    helipads.clear();

    assertEquals(1, heliport.runways().size());
    assertEquals(1, heliport.helipads().size());
  }

  @Test
  void testExistingImplementationsDefaultToNoRunways() {
    Heliport heliport = new Heliport() {
      @Override
      public String heliportIdentifier() {
        return "HPT";
      }

      @Override
      public Optional<MagneticVariation> magneticVariation() {
        return Optional.empty();
      }

      @Override
      public Collection<? extends Helipad> helipads() {
        return List.of();
      }

      @Override
      public void accept(Visitor visitor) {
      }

      @Override
      public LatLong latLong() {
        return LatLong.of(0., 0.);
      }
    };

    assertTrue(heliport.runways().isEmpty());
  }

  private Heliport.Standard testHeliport() {
    return Heliport.builder()
        .heliportIdentifier("HPT")
        .latLong(LatLong.of(0., 0.))
        .magneticVariation(MagneticVariation.ZERO)
        .runways(List.of(testRunway()))
        .helipads(List.of(testHelipad()))
        .build();
  }

  private Runway testRunway() {
    return Runway.builder()
        .runwayIdentifier("RW09")
        .origin(LatLong.of(0., 0.))
        .length(Distance.ofFeet(1000.))
        .course(Course.ofDegrees(90.))
        .build();
  }

  private Helipad testHelipad() {
    return Helipad.builder()
        .padIdentifier("H1")
        .origin(LatLong.of(0., 0.))
        .build();
  }
}
