package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.v18.field.AltitudeLimit;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimit;

import com.google.common.collect.ImmutableMap;

class TestArincRecord {

  @Test
  void testSpecRetrievalForField() {
    AltitudeLimit altitudeLimit = new AltitudeLimit();

    ArincRecord record = new ArincRecord(ImmutableMap.of("altitudeLimit", new ArincField(altitudeLimit, "")));

    Optional<AltitudeLimit> limitSpec = record.specForField("altitudeLimit");
    AltitudeLimit actual = limitSpec.orElseThrow(AssertionError::new);

    assertEquals(altitudeLimit, actual, "Returned field spec instance should be identical.");
  }

  @Test
  void testRawFieldValueRetrieval() {
    AltitudeLimit altitudeLimit = new AltitudeLimit();

    ArincRecord record = new ArincRecord(ImmutableMap.of("altitudeLimit", new ArincField(altitudeLimit, "ALTITUDELIMIT")));

    String actual = record.rawField("altitudeLimit");
    assertEquals("ALTITUDELIMIT", actual, "Raw field should be the inserted value.");
  }

  @Test
  void testOptionalFieldValueRetrieval() {
    ArincRecord record = new ArincRecord(ImmutableMap.of(
        "altitudeLimit", new ArincField(new AltitudeLimit(), "180600"),
        "speedLimit", new ArincField(new SpeedLimit(), ""))
    );

    assertAll(
        () -> assertEquals(Optional.of(Pair.of(18000., 60000.)), record.optionalField("altitudeLimit")),
        () -> assertEquals(Optional.empty(), record.optionalField("speedLimit"))
    );
  }

  @Test
  void testRequiredFieldValueRetrieval() {
    ArincRecord record = new ArincRecord(ImmutableMap.of(
        "altitudeLimit", new ArincField(new AltitudeLimit(), "180600"),
        "speedLimit", new ArincField(new SpeedLimit(), ""))
    );

    assertAll(
        () -> assertEquals(Pair.of(18000., 60000.), record.requiredField("altitudeLimit")),
        () -> assertThrows(MissingRequiredFieldException.class, () -> record.requiredField("speedLimit"))
    );
  }
}
