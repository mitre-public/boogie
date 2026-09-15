package org.mitre.tdp.boogie.dafif.v81;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.model.enums.SpecialUseAirspaceType;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;
import org.mitre.tdp.boogie.dafif.model.enums.Derivation;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifFileType;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifSuasConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifSuasSpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifSuasValidator;

class TestDafifSuasSpec {

  private final DafifRecordParser parser = DafifRecordParser.standard(new DafifSuasSpec());
  private final DafifSuasValidator validator = new DafifSuasValidator();
  private final DafifSuasConverter converter = new DafifSuasConverter();

  private static final String RAW_RECORD = String.join("\t",
      "R1234 (A)",
      "1A",
      "10",
      "TEST AIRSPACE",
      "R",
      "KZNY",
      "R",
      "B",
      "N41000000",
      "41.000000",
      "W075000000",
      "-75.000000",
      "N40000000",
      "40.000000",
      "W074000000",
      "-74.000000",
      "N40000000",
      "40.000000",
      "W075000000",
      "-75.000000",
      "60.00",
      "5.00",
      "360.0",
      "90.0",
      "ABC",
      "4",
      "US",
      "3",
      "202601");

  @Test
  void parsesEveryColumnInSpecificationOrder() {
    DafifRecord record = parser.parse(DafifRecordType.SUAS, RAW_RECORD).orElseThrow();
    assertTrue(validator.test(record));
    DafifSuasSegment model = converter.apply(record).orElseThrow();

    assertAll(
        () -> assertEquals("R", record.<String>requiredField("specialUseAirspaceType")),
        () -> assertEquals("R", record.<String>requiredField("shape")),
        () -> assertEquals("B", record.<String>requiredField("derivation")),
        () -> assertEquals("R1234 (A)", model.suasIdentification()),
        () -> assertEquals("1A", model.sector().orElseThrow()),
        () -> assertEquals(10, model.segmentNumber()),
        () -> assertEquals("TEST AIRSPACE", model.name().orElseThrow()),
        () -> assertEquals(SpecialUseAirspaceType.RESTRICTED, model.specialUseAirspaceType()),
        () -> assertEquals("KZNY", model.icaoCode()),
        () -> assertEquals(Shape.CLOCKWISE_ARC, model.shape()),
        () -> assertEquals(Derivation.DISTANCE_AND_BEARING, model.derivation().orElseThrow()),
        () -> assertEquals("N41000000", model.geodeticLatitude1().orElseThrow()),
        () -> assertEquals(41.0, model.latitude1().orElseThrow()),
        () -> assertEquals("W075000000", model.geodeticLongitude1().orElseThrow()),
        () -> assertEquals(-75.0, model.longitude1().orElseThrow()),
        () -> assertEquals("N40000000", model.geodeticLatitude2().orElseThrow()),
        () -> assertEquals(40.0, model.latitude2().orElseThrow()),
        () -> assertEquals("W074000000", model.geodeticLongitude2().orElseThrow()),
        () -> assertEquals(-74.0, model.longitude2().orElseThrow()),
        () -> assertEquals("N40000000", model.geodeticLatitude0().orElseThrow()),
        () -> assertEquals(40.0, model.latitude0().orElseThrow()),
        () -> assertEquals("W075000000", model.geodeticLongitude0().orElseThrow()),
        () -> assertEquals(-75.0, model.longitude0().orElseThrow()),
        () -> assertEquals(60.0, model.radius1().orElseThrow()),
        () -> assertEquals(5.0, model.radius2().orElseThrow()),
        () -> assertEquals(360.0, model.bearing1().orElseThrow()),
        () -> assertEquals(90.0, model.bearing2().orElseThrow()),
        () -> assertEquals("ABC", model.navaidIdentifier().orElseThrow()),
        () -> assertEquals(4, model.navaidType().orElseThrow()),
        () -> assertEquals("US", model.navaidCountryCode().orElseThrow()),
        () -> assertEquals(3, model.navaidKeyCode().orElseThrow()),
        () -> assertEquals(202601, model.cycleDate()),
        () -> assertEquals(DafifFileType.SUAS_SEGMENT, model.getFileType()),
        () -> assertEquals(model, model.toBuilder().build())
    );
  }

  @Test
  void acceptsBlankOptionalColumns() {
    String[] fields = RAW_RECORD.split("\t", -1);
    fields[1] = "";
    fields[3] = "";
    fields[7] = "";
    fields[8] = "";
    fields[9] = "";
    fields[10] = "";
    fields[11] = "";
    fields[12] = "";
    fields[13] = "";
    fields[14] = "";
    fields[15] = "";
    fields[16] = "";
    fields[17] = "";
    fields[18] = "";
    fields[19] = "";
    fields[20] = "";
    fields[21] = "";
    fields[22] = "";
    fields[23] = "";
    fields[24] = "";
    fields[25] = "";
    fields[26] = "";
    fields[27] = "";
    fields[6] = "A";
    DafifRecord record = parser.parse(DafifRecordType.SUAS, String.join("\t", fields)).orElseThrow();
    assertTrue(validator.test(record));
    DafifSuasSegment model = converter.apply(record).orElseThrow();
    assertAll(
        () -> assertTrue(model.sector().isEmpty()),
        () -> assertTrue(model.name().isEmpty()),
        () -> assertTrue(model.derivation().isEmpty()),
        () -> assertTrue(model.geodeticLatitude1().isEmpty()),
        () -> assertTrue(model.latitude1().isEmpty()),
        () -> assertTrue(model.geodeticLongitude1().isEmpty()),
        () -> assertTrue(model.longitude1().isEmpty()),
        () -> assertTrue(model.geodeticLatitude2().isEmpty()),
        () -> assertTrue(model.latitude2().isEmpty()),
        () -> assertTrue(model.geodeticLongitude2().isEmpty()),
        () -> assertTrue(model.longitude2().isEmpty()),
        () -> assertTrue(model.geodeticLatitude0().isEmpty()),
        () -> assertTrue(model.latitude0().isEmpty()),
        () -> assertTrue(model.geodeticLongitude0().isEmpty()),
        () -> assertTrue(model.longitude0().isEmpty()),
        () -> assertTrue(model.radius1().isEmpty()),
        () -> assertTrue(model.radius2().isEmpty()),
        () -> assertTrue(model.bearing1().isEmpty()),
        () -> assertTrue(model.bearing2().isEmpty()),
        () -> assertTrue(model.navaidIdentifier().isEmpty()),
        () -> assertTrue(model.navaidType().isEmpty()),
        () -> assertTrue(model.navaidCountryCode().isEmpty()),
        () -> assertTrue(model.navaidKeyCode().isEmpty())
    );
  }

  @Test
  void rejectsMalformedRequiredFields() {
    String[] fields = RAW_RECORD.split("\t", -1);
    fields[0] = "";
    assertThrows(RuntimeException.class,
        () -> parser.parse(DafifRecordType.SUAS, String.join("\t", fields)));
  }

  @Test
  void arcsRequireDerivation() {
    String[] fields = RAW_RECORD.split("\t", -1);
    fields[7] = "";
    DafifRecord record = parser.parse(DafifRecordType.SUAS, String.join("\t", fields)).orElseThrow();
    assertFalse(validator.test(record));
  }
}
