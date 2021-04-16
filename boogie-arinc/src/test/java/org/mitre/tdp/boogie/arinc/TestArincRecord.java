package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.v18.field.AltitudeLimit;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimit;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointUsage;

import com.google.common.collect.ImmutableMap;

class TestArincRecord {

  @Test
  void testRecordInstantiationFailsWithDifferentKeySetSizes() {
    assertThrows(IllegalArgumentException.class, () -> new ArincRecord("", ImmutableMap.of("", new WaypointUsage()), ImmutableMap.of()));
  }

  @Test
  void testSpecRetrievalForField() {
    AltitudeLimit altitudeLimit = new AltitudeLimit();

    ArincRecord record = new ArincRecord(
        "ARINCRECORD",
        ImmutableMap.of("altitudeLimit", altitudeLimit),
        ImmutableMap.of("altitudeLimit", "")
    );

    AltitudeLimit actual = record.specForField("altitudeLimit");
    assertEquals(altitudeLimit, actual, "Returned field spec instance should be identical.");
  }

  @Test
  void testRawFieldValueRetrieval() {
    AltitudeLimit altitudeLimit = new AltitudeLimit();

    ArincRecord record = new ArincRecord(
        "ARINCRECORD",
        ImmutableMap.of("altitudeLimit", altitudeLimit),
        ImmutableMap.of("altitudeLimit", "ALTITUDELIMIT")
    );

    String actual = record.rawField("altitudeLimit");
    assertEquals("ALTITUDELIMIT", actual, "Raw field should be the inserted value.");
  }

  @Test
  void testOptionalFieldValueRetrieval() {
    ArincRecord record = new ArincRecord(
        "ARINCRECORD",
        ImmutableMap.of("altitudeLimit", new AltitudeLimit(), "speedLimit", new SpeedLimit()),
        ImmutableMap.of("altitudeLimit", "180600", "speedLimit", "")
    );

    assertAll(
        () -> assertEquals(Optional.of(Pair.of(18000., 60000.)), record.optionalField("altitudeLimit")),
        () -> assertEquals(Optional.empty(), record.optionalField("speedLimit"))
    );
  }

  @Test
  void testRequiredFieldValueRetrieval() {
    ArincRecord record = new ArincRecord(
        "ARINCRECORD",
        ImmutableMap.of("altitudeLimit", new AltitudeLimit(), "speedLimit", new SpeedLimit()),
        ImmutableMap.of("altitudeLimit", "180600", "speedLimit", "")
    );

    assertAll(
        () -> assertEquals(Pair.of(18000., 60000.), record.requiredField("altitudeLimit")),
        () -> assertThrows(MissingRequiredFieldException.class, () -> record.requiredField("speedLimit"))
    );
  }
}
