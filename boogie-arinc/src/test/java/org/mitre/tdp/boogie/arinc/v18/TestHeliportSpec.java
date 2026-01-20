package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticTrueIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;

public class TestHeliportSpec {
  static String heliport = "SUSAH 20CTK6A   H1   0     NARN N41585008W072232928W013500880         1800018000P    040050M JOHNSON MEML HOSPITAL         730792403";
  static ArincRecordParser parser = ArincRecordParser.standard(new HeliportSpec());
  static HeliportConverter converter = new HeliportConverter();
  @Test
  void test() {
    ArincRecord record = parser.parse(heliport).orElseThrow(AssertionError::new);
    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.H, record.requiredField("sectionCode")),
        () -> assertEquals("20CT",  record.requiredField("heliportIdentifier")),
        () -> assertEquals("K6", record.requiredField("heliportIcaoRegion")),
        () -> assertEquals("A", record.requiredField("subSectionCode")),
        () -> assertTrue(record.optionalField("iataDesignator").isEmpty()),
        () -> assertEquals("H1", record.requiredField("helipadIdentifier")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertTrue(record.optionalField("speedLimitAltitude").isEmpty()),
        () -> assertEquals("NAR", record.requiredField("datumCode")),
        () -> assertEquals(false, record.requiredField("ifrCapability")),
        () -> assertEquals(41.980577777777775, record.requiredField("latitude")),
        () -> assertEquals(-72.39146666666667, record.requiredField("longitude")),
        () -> assertEquals(-13.5, record.requiredField("magneticVariation")),
        () -> assertEquals(880.0, record.requiredField("heliportElevation")),
        () -> assertTrue(record.optionalField("speedLimit").isEmpty()),
        () -> assertTrue(record.optionalField("recommendedVhfNavaid").isEmpty()),
        () -> assertTrue(record.optionalField("recommendedVhfNavaidIcaoRegion").isEmpty()),
        () -> assertEquals(18000.0, record.requiredField("transitionAltitude")),
        () -> assertEquals(PublicMilitaryIndicator.P, record.requiredField("publicMilitaryIndicator")),
        () -> assertEquals(false, record.optionalField("daylightTimeIndicator").orElseThrow()),
        () -> assertEquals("040050", record.requiredField("padDimensions")),
        () -> assertEquals(MagneticTrueIndicator.M, record.requiredField("magneticTrueIndicator")),
        () -> assertEquals("JOHNSON MEML HOSPITAL", record.requiredField("heliportFullName")),
        () -> assertEquals(Integer.valueOf(73079), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2403", record.requiredField("lastUpdatedCycle"))
    );
  }

  @Test
  void testConverted() {
    ArincHeliport hpt = parser.parse(heliport).flatMap(converter).orElseThrow(AssertionError::new);
    ArincHeliport rebuilt = hpt.toBuilder().build();
    assertAll(
        () -> assertEquals(RecordType.S, hpt.recordType()),
        () -> assertEquals(CustomerAreaCode.USA, hpt.customerAreaCode()),
        () -> assertEquals(SectionCode.H, hpt.sectionCode()),
        () -> assertEquals("20CT",  hpt.heliportIdentifier()),
        () -> assertEquals("K6", hpt.heliportIcaoRegion()),
        () -> assertEquals("A", hpt.subSectionCode().orElseThrow()),
        () -> assertTrue(hpt.iataDesignator().isEmpty()),
        () -> assertEquals("H1", hpt.padIdentifier().orElseThrow()),
        () -> assertEquals("0", hpt.continuationRecordNumber().orElseThrow()),
        () -> assertTrue(hpt.speedLimitAltitude().isEmpty()),
        () -> assertEquals("NAR", hpt.datumCode().orElseThrow()),
        () -> assertEquals(false, hpt.ifrCapability().orElseThrow()),
        () -> assertEquals(41.980577777777775, hpt.latitude()),
        () -> assertEquals(-72.39146666666667, hpt.longitude()),
        () -> assertEquals(-13.5, hpt.magneticVariation().orElseThrow()),
        () -> assertEquals(880.0, hpt.heliportElevation().orElseThrow()),
        () -> assertTrue(hpt.speedLimit().isEmpty()),
        () -> assertTrue(hpt.recommendedVhfNavaid().isEmpty()),
        () -> assertTrue(hpt.navaidIcaoRegion().isEmpty()),
        () -> assertEquals(18000.0, hpt.transitionAltitude().orElseThrow()),
        () -> assertEquals(PublicMilitaryIndicator.P, hpt.publicMilitaryIndicator().orElseThrow()),
        () -> assertEquals(false, hpt.daylightTimeIndicator().orElseThrow()),
        () -> assertEquals(PadShape.R, hpt.padShape().orElseThrow()),
        () -> assertEquals(50, hpt.padXDimension().orElseThrow()),
        () -> assertEquals(40, hpt.padYDimension().orElseThrow()),
        () -> assertTrue(hpt.padDiameter().isEmpty()),
        () -> assertEquals(MagneticTrueIndicator.M, hpt.magneticTrueIndicator().orElseThrow()),
        () -> assertEquals("JOHNSON MEML HOSPITAL", hpt.heliportName().orElseThrow()),
        () -> assertEquals(Integer.valueOf(73079), hpt.fileRecordNumber().orElseThrow()),
        () -> assertEquals("2403", hpt.cycleDate().orElseThrow()),
        () -> assertEquals(hpt, rebuilt)
    );
  }
}
