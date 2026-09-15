package org.mitre.tdp.boogie.dafif.v81;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.model.enums.SpecialUseAirspaceType;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifFileType;
import org.mitre.tdp.boogie.dafif.model.DafifSuasParent;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifSuasParentConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifSuasParentSpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifSuasParentValidator;

class TestDafifSuasParentSpec {

  private final DafifRecordParser parser = DafifRecordParser.standard(new DafifSuasParentSpec());
  private final DafifSuasParentValidator validator = new DafifSuasParentValidator();
  private final DafifSuasParentConverter converter = new DafifSuasParentConverter();

  private static final String RAW_RECORD = String.join("\t",
      "R1234 (A)",
      "1A",
      "R",
      "TEST AIRSPACE",
      "KZNY",
      "TEST AUTHORITY",
      "WGE",
      "WGE",
      "TEST CENTER",
      "128.6   M",
      "5062.5  K",
      "B",
      "FL1010",
      "00500AGL",
      "MON-FRI, 0600-2200Z",
      "VMC-IMC",
      "202601",
      "15 Jan 26");

  @Test
  void parsesEveryColumnInSpecificationOrder() {
    DafifRecord record = parser.parse(DafifRecordType.SUAS_PAR, RAW_RECORD).orElseThrow();
    assertTrue(validator.test(record));
    DafifSuasParent model = converter.apply(record).orElseThrow();

    assertAll(
        () -> assertEquals("R", record.<String>requiredField("specialUseAirspaceType")),
        () -> assertEquals("R1234 (A)", model.suasIdentification()),
        () -> assertEquals("1A", model.sector().orElseThrow()),
        () -> assertEquals(SpecialUseAirspaceType.RESTRICTED, model.specialUseAirspaceType()),
        () -> assertEquals("TEST AIRSPACE", model.name().orElseThrow()),
        () -> assertEquals("KZNY", model.icaoCode()),
        () -> assertEquals("TEST AUTHORITY", model.controllingAuthority().orElseThrow()),
        () -> assertEquals("WGE", model.localHorizontalDatum().orElseThrow()),
        () -> assertEquals("WGE", model.geodeticDatum()),
        () -> assertEquals("TEST CENTER", model.communicationName().orElseThrow()),
        () -> assertEquals("128.6   M", model.communicationsFrequency1().orElseThrow()),
        () -> assertEquals("5062.5  K", model.communicationsFrequency2().orElseThrow()),
        () -> assertEquals("B", model.level()),
        () -> assertEquals("FL1010", model.upperAltitude()),
        () -> assertEquals("00500AGL", model.lowerAltitude()),
        () -> assertEquals("MON-FRI, 0600-2200Z", model.suasEffectiveTimes().orElseThrow()),
        () -> assertEquals("VMC-IMC", model.suasWeather().orElseThrow()),
        () -> assertEquals(202601, model.cycleDate()),
        () -> assertEquals("15 Jan 26", model.effectiveDate()),
        () -> assertEquals(DafifFileType.SUAS_PARENT, model.getFileType()),
        () -> assertEquals(model, model.toBuilder().build())
    );
  }

  @Test
  void acceptsBlankOptionalColumns() {
    String[] fields = RAW_RECORD.split("\t", -1);
    fields[1] = "";
    fields[3] = "";
    fields[5] = "";
    fields[6] = "";
    fields[8] = "";
    fields[9] = "";
    fields[10] = "";
    fields[14] = "";
    fields[15] = "";
    DafifRecord record = parser.parse(DafifRecordType.SUAS_PAR, String.join("\t", fields)).orElseThrow();
    assertTrue(validator.test(record));
    DafifSuasParent model = converter.apply(record).orElseThrow();
    assertAll(
        () -> assertTrue(model.sector().isEmpty()),
        () -> assertTrue(model.name().isEmpty()),
        () -> assertTrue(model.controllingAuthority().isEmpty()),
        () -> assertTrue(model.localHorizontalDatum().isEmpty()),
        () -> assertTrue(model.communicationName().isEmpty()),
        () -> assertTrue(model.communicationsFrequency1().isEmpty()),
        () -> assertTrue(model.communicationsFrequency2().isEmpty()),
        () -> assertTrue(model.suasEffectiveTimes().isEmpty()),
        () -> assertTrue(model.suasWeather().isEmpty())
    );
  }

  @Test
  void rejectsMalformedRequiredFields() {
    String[] fields = RAW_RECORD.split("\t", -1);
    fields[0] = "";
    assertThrows(RuntimeException.class,
        () -> parser.parse(DafifRecordType.SUAS_PAR, String.join("\t", fields)));
  }
}
