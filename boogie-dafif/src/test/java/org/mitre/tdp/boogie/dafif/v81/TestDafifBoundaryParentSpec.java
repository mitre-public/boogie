package org.mitre.tdp.boogie.dafif.v81;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.model.enums.BoundaryType;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifFileType;
import org.mitre.tdp.boogie.dafif.model.DafifBoundaryParent;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifBoundaryParentConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifBoundaryParentSpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifBoundaryParentValidator;

class TestDafifBoundaryParentSpec {

  private final DafifRecordParser parser = DafifRecordParser.standard(new DafifBoundaryParentSpec());
  private final DafifBoundaryParentValidator validator = new DafifBoundaryParentValidator();
  private final DafifBoundaryParentConverter converter = new DafifBoundaryParentConverter();

  private static final String RAW_RECORD = String.join("\t",
      "KZ00001",
      "08",
      "TEST AIRSPACE",
      "KZNY",
      "TEST AUTHORITY",
      "WGE",
      "WGE",
      "TEST CENTER",
      "128.6   M",
      "5062.5  K",
      "C",
      "Y",
      "CLASS E AT NIGHT",
      "B",
      "060000AMSL",
      "00500AGL",
      "031",
      "202601",
      "FL390",
      "FL290");

  @Test
  void parsesEveryColumnInSpecificationOrder() {
    DafifRecord record = parser.parse(DafifRecordType.BDRY_PAR, RAW_RECORD).orElseThrow();
    assertTrue(validator.test(record));
    DafifBoundaryParent model = converter.apply(record).orElseThrow();

    assertAll(
        () -> assertEquals(8, record.<Integer>requiredField("boundaryType")),
        () -> assertEquals("KZ00001", model.boundaryIdentification()),
        () -> assertEquals(BoundaryType.FIR, model.boundaryType()),
        () -> assertEquals("TEST AIRSPACE", model.name().orElseThrow()),
        () -> assertEquals("KZNY", model.icaoCode()),
        () -> assertEquals("TEST AUTHORITY", model.controllingAuthority().orElseThrow()),
        () -> assertEquals("WGE", model.localHorizontalDatum().orElseThrow()),
        () -> assertEquals("WGE", model.geodeticDatum()),
        () -> assertEquals("TEST CENTER", model.communicationName().orElseThrow()),
        () -> assertEquals("128.6   M", model.communicationsFrequency1().orElseThrow()),
        () -> assertEquals("5062.5  K", model.communicationsFrequency2().orElseThrow()),
        () -> assertEquals("C", model.airspaceClass().orElseThrow()),
        () -> assertEquals("Y", model.classExceptionFlag().orElseThrow()),
        () -> assertEquals("CLASS E AT NIGHT", model.classExceptionRemarks().orElseThrow()),
        () -> assertEquals("B", model.level()),
        () -> assertEquals("060000AMSL", model.upperAltitude()),
        () -> assertEquals("00500AGL", model.lowerAltitude()),
        () -> assertEquals(31, model.requiredNavPerformance().orElseThrow()),
        () -> assertEquals(202601, model.cycleDate()),
        () -> assertEquals("FL390", model.upperRvsm()),
        () -> assertEquals("FL290", model.lowerRvsm()),
        () -> assertEquals(DafifFileType.BOUNDARY_PARENT, model.getFileType()),
        () -> assertEquals(model, model.toBuilder().build())
    );
  }

  @Test
  void acceptsBlankOptionalColumns() {
    String[] fields = RAW_RECORD.split("\t", -1);
    fields[2] = "";
    fields[4] = "";
    fields[5] = "";
    fields[7] = "";
    fields[8] = "";
    fields[9] = "";
    fields[10] = "";
    fields[11] = "";
    fields[12] = "";
    fields[16] = "";
    DafifRecord record = parser.parse(DafifRecordType.BDRY_PAR, String.join("\t", fields)).orElseThrow();
    assertTrue(validator.test(record));
    DafifBoundaryParent model = converter.apply(record).orElseThrow();
    assertAll(
        () -> assertTrue(model.name().isEmpty()),
        () -> assertTrue(model.controllingAuthority().isEmpty()),
        () -> assertTrue(model.localHorizontalDatum().isEmpty()),
        () -> assertTrue(model.communicationName().isEmpty()),
        () -> assertTrue(model.communicationsFrequency1().isEmpty()),
        () -> assertTrue(model.communicationsFrequency2().isEmpty()),
        () -> assertTrue(model.airspaceClass().isEmpty()),
        () -> assertTrue(model.classExceptionFlag().isEmpty()),
        () -> assertTrue(model.classExceptionRemarks().isEmpty()),
        () -> assertTrue(model.requiredNavPerformance().isEmpty())
    );
  }

  @Test
  void rejectsMalformedRequiredFields() {
    String[] fields = RAW_RECORD.split("\t", -1);
    fields[0] = "";
    assertThrows(RuntimeException.class,
        () -> parser.parse(DafifRecordType.BDRY_PAR, String.join("\t", fields)));
  }
}
