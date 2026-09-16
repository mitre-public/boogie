package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.v81.TestDafifNavaidSpec;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifNavaidConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifNavaidSpec;

class FixAssemblyStrategyTest {

  @ParameterizedTest
  @CsvSource({"W00390113, -3.9", "E00320199, 3.2", "W00070922, -0.7", "E00200310, 2.0"})
  void preservesNavaidSlavedVariationTenths(String slavedVariation, double expectedDegrees) {
    String[] fields = TestDafifNavaidSpec.RAW_NAVAID.replace("\n", "").split("\t", -1);
    fields[21] = slavedVariation;
    var record = DafifRecordParser.standard(new DafifNavaidSpec()).parse(DafifRecordType.NAV, String.join("\t", fields)).orElseThrow();
    var navaid = new DafifNavaidConverter().apply(record).orElseThrow();
    var fix = FixAssemblyStrategy.standard().convertNavaid(navaid).iterator().next();

    assertAll(
        () -> assertEquals(slavedVariation, navaid.navaidSlavedVariation().orElseThrow()),
        () -> assertEquals(expectedDegrees, fix.magneticVariation().orElseThrow().angle().inDegrees(), 1e-12)
    );
  }
}
