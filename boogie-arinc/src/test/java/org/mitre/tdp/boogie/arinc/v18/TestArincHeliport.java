package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;

public class TestArincHeliport {
  static ArincRecordParser parser = ArincRecordParser.standard(new HeliportSpec());
  static HeliportConverter converter = new HeliportConverter();
  static String raw = "SUSAH 00INK5A   H1   0     NARN N41304100W087153800W002000634         1800018000P    040040M ST MARY MEDICAL CENTER        686471703";
  @Test
  void testParse() {
    ArincRecord record = parser.parse(raw).orElseThrow();
    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.H, record.requiredField("sectionCode")),
        () -> assertEquals("00IN", record.requiredField("heliportIdentifier")),
        () -> assertEquals("K5", record.requiredField("heliportIcaoRegion")),
        () -> assertEquals("A", record.requiredField("subSectionCode")),
        () -> assertEquals("H1", record.requiredField("helipadIdentifier")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertFalse(record.containsParsedField("speedLimitAltitude")),
        () -> assertEquals("NAR", record.requiredField("datumCode")),
        () -> assertEquals(false, record.requiredField("ifrCapability")),
        () -> assertEquals(41.51138888888889, record.requiredField("latitude")),
        () -> assertEquals(-87.26055555555556, record.requiredField("longitude")),
        () -> assertEquals(-2.0, record.requiredField("magneticVariation")),
        () -> assertEquals(634.0, record.requiredField("heliportElevation")),
        () -> assertFalse(record.containsParsedField("speedLimit")),
        () -> assertFalse(record.containsParsedField("recommendedVhfNavaid")),
        () -> assertFalse(record.containsParsedField("recommendedVhfNavaidIcaoRegion")),
        () -> assertEquals(18000.0, record.requiredField("transitionAltitude")),
        () -> assertEquals(18000.0, record.requiredField("transitionLevel")),
        () -> assertEquals(PublicMilitaryIndicator.P, record.requiredField("publicMilitaryIndicator")),
        () -> assertFalse(record.containsParsedField("timeZone")),
        () -> assertEquals(false, record.requiredField("daylightTimeIndicator")),
        () -> assertEquals("040040", record.requiredField("padDimensions")),
        () -> assertEquals("ST MARY MEDICAL CENTER", record.requiredField("heliportFullName")),
        () -> assertEquals(Integer.valueOf(68647), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1703", record.requiredField("lastUpdatedCycle"))
    );
  }
  @Test
  void testConvert() {
    ArincHeliport heliport = parser.parse(raw).flatMap(converter).orElseThrow();
    assertAll(
        () -> assertEquals(RecordType.S, heliport.recordType()),
        () -> assertEquals(CustomerAreaCode.USA, heliport.customerAreaCode()),
        () -> assertEquals(SectionCode.H, heliport.sectionCode()),
        () -> assertEquals("00IN", heliport.heliportIdentifier()),
        () -> assertEquals("K5", heliport.heliportIcaoRegion()),
        () -> assertEquals("A", heliport.subSectionCode().get()),
        () -> assertEquals("H1", heliport.padIdentifier().get()),
        () -> assertEquals("0", heliport.continuationRecordNumber().get()),
        () -> assertFalse(heliport.speedLimitAltitude().isPresent()),
        () -> assertEquals("NAR", heliport.datumCode().get()),
        () -> assertEquals(false, heliport.ifrCapability().get()),
        () -> assertEquals(41.51138888888889, heliport.latitude()),
        () -> assertEquals(-87.26055555555556, heliport.longitude()),
        () -> assertEquals(-2.0, heliport.magneticVariation().get()),
        () -> assertEquals(634.0, heliport.heliportElevation().get()),
        () -> assertFalse(heliport.speedLimit().isPresent()),
        () -> assertFalse(heliport.recommendedVhfNavaid().isPresent()),
        () -> assertFalse(heliport.navaidIcaoRegion().isPresent()),
        () -> assertEquals(18000.0, heliport.transitionAltitude().get()),
        () -> assertEquals(18000.0, heliport.transitionLevel().get()),
        () -> assertEquals(PublicMilitaryIndicator.P, heliport.publicMilitaryIndicator().get()),
        () -> assertEquals(false, heliport.daylightTimeIndicator().get()),
        () -> assertEquals(PadShape.R, heliport.padShape().get()),
        () -> assertEquals(40.0, heliport.padXDimension().get()),
        () -> assertEquals(40.0, heliport.padXDimension().get()),
        () -> assertEquals("ST MARY MEDICAL CENTER", heliport.heliportName().get()),
        () -> assertEquals(Integer.valueOf(68647), heliport.fileRecordNumber().get()),
        () -> assertEquals("1703", heliport.cycleDate().get())
    );
  }
}
