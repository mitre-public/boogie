package org.mitre.tdp.boogie.dafif.v81;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifTerminalSegmentConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifTerminalSegmentSpec;

class DafifTerminalSegmentConverterTest {

  @ParameterizedTest
  @CsvSource({"100, 10.0", "050, 5.0", "010, 1.0", "031, 0.3", "152, 0.15", "011, 0.1", "992, 0.99"})
  void convertsTheTwoDigitRnpMantissaAndDecimalExponent(String source, double nauticalMiles) {
    var segment = convert(Map.of("requiredNavPerformance", source));
    assertEquals(nauticalMiles, segment.requiredNavPerformance().orElseThrow(), 1e-12);
  }

  @Test
  void retainsBothSpeedLimitsAndTheSecondaryNavaidCountry() {
    var segment = convert(Map.of(
        "speedLimit1", "210", "speedLimitAircraftType1", "J", "speedLimitAltitude1", "FL100",
        "speedLimit2", "180", "speedLimitAircraftType2", "T", "speedLimitAltitude2", "FL80",
        "navaid2Identifier", "ABC", "navaid2Type", "4", "navaid2CountryCode", "US", "navaid2KeyCode", "1"));

    assertAll(
        () -> assertEquals(210.0, segment.speedLimit1().orElseThrow()),
        () -> assertEquals("J", segment.speedLimitAircraftType1().orElseThrow()),
        () -> assertEquals("FL100", segment.speedLimitAltitude1().orElseThrow()),
        () -> assertEquals(180.0, segment.speedLimit2().orElseThrow()),
        () -> assertEquals("T", segment.speedLimitAircraftType2().orElseThrow()),
        () -> assertEquals("FL80", segment.speedLimitAltitude2().orElseThrow()),
        () -> assertEquals("US", segment.navaid2CountryCode().orElseThrow())
    );
  }

  @ParameterizedTest
  @CsvSource({"210, '', 210.0,", "'', 180, , 180.0"})
  void retainsEitherSpeedLimitIndependently(String speedLimit1, String speedLimit2, Double expected1, Double expected2) {
    var segment = convert(Map.of("speedLimit1", speedLimit1, "speedLimit2", speedLimit2));
    assertAll(
        () -> assertEquals(expected1, segment.speedLimit1().orElse(null)),
        () -> assertEquals(expected2, segment.speedLimit2().orElse(null))
    );
  }

  @Test
  void leavesUnspecifiedRnpAndSpeedsAbsent() {
    var segment = convert(Map.of());
    assertAll(
        () -> assertTrue(segment.requiredNavPerformance().isEmpty()),
        () -> assertTrue(segment.speedLimit1().isEmpty()),
        () -> assertTrue(segment.speedLimit2().isEmpty())
    );
  }

  private static DafifTerminalSegment convert(Map<String, String> replacements) {
    var spec = new DafifTerminalSegmentSpec();
    String[] fields = TestDafifTerminalSegmentSpec.RAW_TERMINAL_SEGMENT.replace("\n", "").split("\t", -1);
    for (int i = 0; i < spec.recordFields().size(); i++) {
      String replacement = replacements.get(spec.recordFields().get(i).fieldName());
      if (replacement != null) {
        fields[i] = replacement;
      }
    }
    var record = DafifRecordParser.standard(spec).parse(DafifRecordType.TRM_SEG, String.join("\t", fields)).orElseThrow();
    return new DafifTerminalSegmentConverter().apply(record).orElseThrow();
  }
}
