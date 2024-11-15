package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

import com.google.common.collect.Range;
import nl.jqno.equalsverifier.EqualsVerifier;

class AirspaceTest {

  @Test
  void verifyStandard() {
    EqualsVerifier.forClass(Airspace.Standard.class)
        .withCachedHashCode("hashCode", "computeHashCode", createStandard())
        .verify();
  }

  @Test
  void verifyRecord() {
    EqualsVerifier.forClass(Airspace.Record.class)
        .withCachedHashCode("hashCode", "computeHashCode", Airspace.record("WHATEVER", createStandard()))
        .verify();
  }

  @Test
  void testModel() {
    Airspace airspace = createStandard();
    assertAll(
        () -> assertEquals("name", airspace.identifier()),
        () -> assertEquals(AirspaceType.CONTROLLED, airspace.airspaceType()),
        () -> assertEquals("N", airspace.center().get().fixIdentifier()),
        () -> assertEquals(Range.all(), airspace.altitudeLimit())
    );
  }

  private Airspace.Standard createStandard() {
    return Airspace.builder()
        .identifier("name")
        .center(Fix.builder().fixIdentifier("N").latLong(LatLong.of(0., 1.)).build())
        .airspaceType(AirspaceType.CONTROLLED)
        .sequences(List.of())
        .altitudeLimit(Range.all())
        .build();
  }
}