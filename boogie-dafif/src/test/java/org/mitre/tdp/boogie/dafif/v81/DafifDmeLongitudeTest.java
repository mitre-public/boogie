package org.mitre.tdp.boogie.dafif.v81;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Map;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordSpec;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifNavaidSpec;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifTerminalSegmentSpec;

class DafifDmeLongitudeTest {

  @ParameterizedTest
  @ValueSource(strings = {"180.000000", "179.999999", "-179.999999", "0.000000", "-0.000001"})
  void parsesDmeLongitudesIncludingTheAntimeridian(String longitude) {
    double expected = Double.parseDouble(longitude);
    var nav = navaid(longitude);
    var segment = segment(longitude);

    assertAll(
        () -> assertEquals(expected, nav.<Double>requiredField("dmeDegreesLongitude")),
        () -> assertEquals(expected, segment.<Double>requiredField("navaid1DmeDegreesLongitude")),
        () -> assertEquals(expected, segment.<Double>requiredField("navaid2DmeDegreesLongitude"))
    );
  }

  @ParameterizedTest
  @ValueSource(strings = {"180.000001", "-180.000000", "181.000000", "-0.000000", "180", "        180"})
  void rejectsDmeLongitudesOutsideThePublishedRangeOrFormat(String longitude) {
    assertAll(
        () -> assertThrows(RuntimeException.class, () -> navaid(longitude)),
        () -> assertThrows(RuntimeException.class, () -> segment(longitude))
    );
  }

  private static DafifRecord navaid(String longitude) {
    return parse(new DafifNavaidSpec(), DafifRecordType.NAV, TestDafifNavaidSpec.RAW_NAVAID,
        Map.of("dmeDegreesLongitude", longitude));
  }

  private static DafifRecord segment(String longitude) {
    return parse(new DafifTerminalSegmentSpec(), DafifRecordType.TRM_SEG, TestDafifTerminalSegmentSpec.RAW_TERMINAL_SEGMENT,
        Map.of("navaid1DmeDegreesLongitude", longitude, "navaid2DmeDegreesLongitude", longitude));
  }

  private static DafifRecord parse(DafifRecordSpec spec, DafifRecordType type, String raw, Map<String, String> replacements) {
    String[] fields = raw.replace("\n", "").split("\t", -1);
    for (int i = 0; i < spec.recordFields().size(); i++) {
      String replacement = replacements.get(spec.recordFields().get(i).fieldName());
      if (replacement != null) fields[i] = replacement;
    }
    return DafifRecordParser.standard(spec).parse(type, String.join("\t", fields)).orElseThrow();
  }
}
