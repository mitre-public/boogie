package org.mitre.tdp.boogie.dafif.v81;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifTerminalSegmentSpec;

class DafifTerminalSegmentAltitudeValidationTest {

  private static final DafifTerminalSegmentSpec SPEC = new DafifTerminalSegmentSpec();
  private static final DafifRecordParser PARSER = DafifRecordParser.standard(SPEC);

  @ParameterizedTest
  @ValueSource(strings = {"", "-1299", "-1200", "-1000", "-0999", "-0300", "-0001", "-0000", "00000", "99999", "FL10", "FL65", "FL999"})
  void acceptsCrossingAltitudesWithinTheDocumentedRange(String value) {
    for (String field : List.of("altitude1", "altitude2")) {
      assertEquals(expected(value), parse(field, value).optionalField(field), field);
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"-1300", "-9999", "-10000", "-1", "-300", "0", "3000", "100000", "FL9", "FL09", "FL010", "FL1000", "abc", "30000.0"})
  void rejectsMalformedOrOutOfRangeCrossingAltitudes(String value) {
    for (String field : List.of("altitude1", "altitude2")) {
      RuntimeException error = assertThrows(RuntimeException.class, () -> parse(field, value), field);
      assertTrue(error.getMessage().contains("Unexpected value for " + field + " inside TRM_SEG"), error.getMessage());
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"", "0", "1", "10", "3000", "99999", "00000", "03000", "FL10", "FL65", "FL999"})
  void acceptsSpeedLimitAltitudesWithOrWithoutLeadingZeros(String value) {
    for (String field : List.of("speedLimitAltitude1", "speedLimitAltitude2")) {
      assertEquals(expected(value), parse(field, value).optionalField(field), field);
    }
  }

  @ParameterizedTest
  @ValueSource(strings = {"-0001", "-1", "100000", "FL9", "FL09", "FL010", "FL1000", "abc", "3000.0"})
  void rejectsMalformedOrOutOfRangeSpeedLimitAltitudes(String value) {
    for (String field : List.of("speedLimitAltitude1", "speedLimitAltitude2")) {
      RuntimeException error = assertThrows(RuntimeException.class, () -> parse(field, value), field);
      assertTrue(error.getMessage().contains("Unexpected value for " + field + " inside TRM_SEG"), error.getMessage());
    }
  }

  private static Optional<String> expected(String value) {
    return value.isEmpty() ? Optional.empty() : Optional.of(value);
  }

  private static DafifRecord parse(String fieldName, String value) {
    String[] fields = TestDafifTerminalSegmentSpec.RAW_TERMINAL_SEGMENT.replace("\n", "").split("\t", -1);
    for (int i = 0; i < SPEC.recordFields().size(); i++) {
      if (SPEC.recordFields().get(i).fieldName().equals(fieldName)) {
        fields[i] = value;
      }
    }
    return PARSER.parse(DafifRecordType.TRM_SEG, String.join("\t", fields)).orElseThrow();
  }
}
