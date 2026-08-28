package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.v18.field.AltitudeLimit;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimit;

class TestArincRecord {

  @Test
  void testSpecRetrievalForField() {
    AltitudeLimit altitudeLimit = new AltitudeLimit();
    ArincRecord record = record(
        " ".repeat(altitudeLimit.fieldLength()),
        new RecordField<>("altitudeLimit", altitudeLimit)
    );

    Optional<AltitudeLimit> limitSpec = record.specForField("altitudeLimit");
    AltitudeLimit actual = limitSpec.orElseThrow(AssertionError::new);

    assertEquals(altitudeLimit, actual, "Returned field spec instance should be identical.");
  }

  @Test
  void testRawFieldValueRetrieval() {
    AltitudeLimit altitudeLimit = new AltitudeLimit();
    ArincRecord record = record("180600", new RecordField<>("altitudeLimit", altitudeLimit));

    String actual = record.rawField("altitudeLimit");
    assertEquals("180600", actual, "Raw field should be the inserted value.");
  }

  @Test
  void testOptionalFieldValueRetrieval() {
    ArincRecord record = recordWithAltitudeAndSpeedLimit();

    assertAll(
        () -> assertEquals(Optional.of(Pair.of(18000., 60000.)), record.optionalField("altitudeLimit")),
        () -> assertEquals(Optional.empty(), record.optionalField("speedLimit"))
    );
  }

  @Test
  void testRequiredFieldValueRetrieval() {
    ArincRecord record = recordWithAltitudeAndSpeedLimit();

    assertAll(
        () -> assertEquals(Pair.of(18000., 60000.), record.requiredField("altitudeLimit")),
        () -> assertThrows(MissingRequiredFieldException.class, () -> record.requiredField("speedLimit"))
    );
  }

  private static ArincRecord recordWithAltitudeAndSpeedLimit() {
    AltitudeLimit altitudeLimit = new AltitudeLimit();
    SpeedLimit speedLimit = new SpeedLimit();
    return record(
        "180600".concat(" ".repeat(speedLimit.fieldLength())),
        new RecordField<>("altitudeLimit", altitudeLimit),
        new RecordField<>("speedLimit", speedLimit)
    );
  }

  private static ArincRecord record(String rawRecord, RecordField<?>... recordFields) {
    List<RecordField<?>> fields = List.of(recordFields);
    RecordSpec recordSpec = new RecordSpec() {
      @Override
      public int recordLength() {
        return fields.stream().map(RecordField::fieldSpec).mapToInt(FieldSpec::fieldLength).sum();
      }

      @Override
      public List<RecordField<?>> recordFields() {
        return fields;
      }

      @Override
      public boolean matchesRecord(String arincRecord) {
        return true;
      }
    };

    return ArincRecordParser.standard(recordSpec).parse(rawRecord).orElseThrow();
  }
}
