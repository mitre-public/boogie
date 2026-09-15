package org.mitre.tdp.boogie.dafif.v81;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.model.enums.BoundaryType;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;
import org.mitre.tdp.boogie.dafif.model.enums.Derivation;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifFileType;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifBoundaryConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifBoundarySpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifBoundaryValidator;

class TestDafifBoundarySpec {

  private final DafifRecordParser parser = DafifRecordParser.standard(new DafifBoundarySpec());
  private final DafifBoundaryValidator validator = new DafifBoundaryValidator();
  private final DafifBoundaryConverter converter = new DafifBoundaryConverter();

  private static final String RAW_RECORD = String.join("\t",
      "KZ00001",
      "10",
      "TEST AIRSPACE",
      "08",
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
    DafifRecord record = parser.parse(DafifRecordType.BDRY, RAW_RECORD).orElseThrow();
    assertTrue(validator.test(record));
    DafifBoundarySegment model = converter.apply(record).orElseThrow();

    assertAll(
        () -> assertEquals(8, record.<Integer>requiredField("boundaryType")),
        () -> assertEquals("R", record.<String>requiredField("shape")),
        () -> assertEquals("B", record.<String>requiredField("derivation")),
        () -> assertEquals("KZ00001", model.boundaryIdentification()),
        () -> assertEquals(10, model.segmentNumber()),
        () -> assertEquals("TEST AIRSPACE", model.name().orElseThrow()),
        () -> assertEquals(BoundaryType.FIR, model.boundaryType()),
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
        () -> assertEquals(DafifFileType.BOUNDARY_SEGMENT, model.getFileType()),
        () -> assertEquals(model, model.toBuilder().build())
    );
  }

  @Test
  void acceptsBlankOptionalColumns() {
    String[] fields = RAW_RECORD.split("\t", -1);
    fields[2] = "";
    fields[6] = "";
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
    fields[5] = "A";
    DafifRecord record = parser.parse(DafifRecordType.BDRY, String.join("\t", fields)).orElseThrow();
    assertTrue(validator.test(record));
    DafifBoundarySegment model = converter.apply(record).orElseThrow();
    assertAll(
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
        () -> parser.parse(DafifRecordType.BDRY, String.join("\t", fields)));
  }

  @Test
  void arcsRequireDerivation() {
    String[] fields = RAW_RECORD.split("\t", -1);
    fields[6] = "";
    DafifRecord record = parser.parse(DafifRecordType.BDRY, String.join("\t", fields)).orElseThrow();
    assertFalse(validator.test(record));
  }
}
