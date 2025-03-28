package org.mitre.tdp.boogie.dafif.v81;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifNavaidConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifNavaidSpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifNavaidValidator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDafifNavaidSpec {

  private static final DafifRecordParser parser = DafifRecordParser.standard(new DafifNavaidSpec());

  private static final DafifNavaidValidator validator = new DafifNavaidValidator();

  private static final DafifNavaidConverter converter = new DafifNavaidConverter();

  public static final String RAW_NAVAID = "1D\t5\tCA\t1\t\tCHARLOTTETOWN\tCY\t0178\t346000K\tL\t\tMH\t\tU\tU\tWGE\tWGE\tN52463148\t52.775411\tW056073346\t-56.125961\t\tW019052 0124\t00184\t\t\t\t\tU\t\tI\t201907\t0\n";

  @Test
  void testParseDafifNavaid() {
    DafifRecord record = parser.parse(DafifRecordType.NAV, RAW_NAVAID).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals("1D", record.requiredField("navaidIdentifier")),
        () -> assertEquals(Integer.valueOf(5), record.requiredField("navaidType")),
        () -> assertEquals("CA", record.requiredField("countryCode")),
        () -> assertEquals(Integer.valueOf(1), record.requiredField("navaidKeyCode")),
        () -> assertFalse(record.optionalField("stateProvinceCode").isPresent()),
        () -> assertEquals("CHARLOTTETOWN", record.requiredField("name")),
        () -> assertEquals("CY", record.requiredField("icaoRegion")),
        () -> assertEquals("0178", record.requiredField("wAC")),
        () -> assertEquals("346000K", record.requiredField("navaidFrequencyNav")),
        () -> assertEquals("L", record.requiredField("navaidUsageCode")),
        () -> assertFalse(record.optionalField("navaidChannel").isPresent()),
        () -> assertEquals("MH", record.requiredField("navaidRadioClassCode")),
        () -> assertFalse(record.optionalField("frequencyProtection").isPresent()),
        () -> assertEquals("U", record.requiredField("navaidPower")),
        () -> assertEquals("U", record.requiredField("navaidRange")),
        () -> assertEquals("WGE", record.requiredField("localHorizontalDatum")),
        () -> assertEquals("WGE", record.requiredField("geodeticDatum")),
        () -> assertEquals("N52463148", record.requiredField("geodeticLatitude")),
        () -> assertEquals(52.775411, record.requiredField("degreesLatitude")),
        () -> assertEquals("W056073346", record.requiredField("geodeticLongitude")),
        () -> assertEquals(-56.125961, record.requiredField("degreesLongitude")),
        () -> assertFalse(record.optionalField("navaidSlavedVariation").isPresent()),
        () -> assertEquals("W019052 0124", record.requiredField("magneticVariation")),
        () -> assertEquals("00184", record.requiredField("ilsNavaidElevation")),
        () -> assertFalse(record.optionalField("dmeGeodeticLatitude").isPresent()),
        () -> assertFalse(record.optionalField("dmeDegreesLatitude").isPresent()),
        () -> assertFalse(record.optionalField("dmeGeodeticLongitude").isPresent()),
        () -> assertFalse(record.optionalField("dmeDegreesLongitude").isPresent()),
        () -> assertEquals("U", record.requiredField("dmeElevation")),
        () -> assertFalse(record.optionalField("associatedIcaoFaaHostCtryCode").isPresent()),
        () -> assertEquals("I", record.requiredField("navaidStatus")),
        () -> assertEquals(Integer.valueOf(201907), record.requiredField("cycleDate")),
        () -> assertEquals(Integer.valueOf(0), record.requiredField("coordinatePrecision"))
    );

    assertTrue(validator.test(record));

    DafifNavaid navaid = converter.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals("1D", navaid.navaidIdentifier()),
        () -> assertEquals(5, navaid.navaidType()),
        () -> assertEquals("CA", navaid.countryCode()),
        () -> assertEquals(1, navaid.navaidKeyCode()),
        () -> assertEquals("CHARLOTTETOWN", navaid.name().orElseThrow()),
        () -> assertEquals("CY", navaid.icaoRegion()),
        () -> assertEquals("0178", navaid.wac()),
        () -> assertEquals("346000K", navaid.navaidFrequencyNav().orElseThrow()),
        () -> assertEquals("L", navaid.navaidUsageCode()),
        () -> assertEquals("MH", navaid.navaidRadioClassCode()),
        () -> assertEquals("U", navaid.navaidPower()),
        () -> assertEquals("U", navaid.navaidRange()),
        () -> assertEquals("WGE", navaid.localHorizontalDatum().orElseThrow()),
        () -> assertEquals("WGE", navaid.geodeticDatum().orElseThrow()),
        () -> assertEquals("N52463148", navaid.geodeticLatitude().orElseThrow()),
        () -> assertEquals(52.775411, navaid.degreesLatitude().orElseThrow()),
        () -> assertEquals("W056073346", navaid.geodeticLongitude().orElseThrow()),
        () -> assertEquals(-56.125961, navaid.degreesLongitude().orElseThrow()),
        () -> assertEquals("W019052 0124", navaid.magneticVariation()),
        () -> assertEquals("00184", navaid.ilsNavaidElevation()),
        () -> assertEquals("U", navaid.dmeElevation()),
        () -> assertEquals("I", navaid.navaidStatus()),
        () -> assertEquals(201907, navaid.cycleDate()),
        () -> assertEquals(0, navaid.coordinatePrecision().orElseThrow())
    );
  }
}
