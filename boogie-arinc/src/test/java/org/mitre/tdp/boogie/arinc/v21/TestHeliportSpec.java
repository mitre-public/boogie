package org.mitre.tdp.boogie.arinc.v21;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v21.field.HeliportType;

public class TestHeliportSpec {
  static ArincRecordParser parser = ArincRecordParser.standard(new HeliportSpec());
  static HeliportConverter converter = new HeliportConverter();
  static String raw = "SUSAH 00INK5A        0     NARNHN41304100W087153800W002000634         1800018000P          M ST MARY MEDICAL CENTER        686471703";
  @Test
  void testParse() {
    ArincRecord record = parser.parse(raw).orElseThrow();
    assertAll("New stuff in 21 is 1 new and 2 missing",
        () -> assertEquals(HeliportType.H, record.requiredField("heliportType")),
        () -> assertFalse(record.containsParsedField("padDimensions")),
        () -> assertFalse(record.containsParsedField("padIdentifier"))
    );
  }
  @Test
  void testConvert() {
    ArincHeliport heliport = parser.parse(raw).flatMap(converter).orElseThrow();
    assertAll("Should all be the same minus the 3 changes",
        () -> assertEquals(RecordType.S, heliport.recordType()),
        () -> assertEquals(CustomerAreaCode.USA, heliport.customerAreaCode()),
        () -> assertEquals(SectionCode.H, heliport.sectionCode()),
        () -> assertEquals("00IN", heliport.heliportIdentifier()),
        () -> assertEquals("K5", heliport.heliportIcaoRegion()),
        () -> assertEquals("A", heliport.subSectionCode().get()),
        () -> assertTrue(heliport.padIdentifier().isEmpty()),
        () -> assertEquals("0", heliport.continuationRecordNumber().get()),
        () -> assertFalse(heliport.speedLimitAltitude().isPresent()),
        () -> assertEquals("NAR", heliport.datumCode().get()),
        () -> assertEquals(false, heliport.ifrCapability().get()),
        () -> assertEquals(HeliportType.H, heliport.heliportType()),
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
        () -> assertTrue( heliport.padShape().isEmpty()),
        () -> assertTrue(heliport.padXDimension().isEmpty()),
        () -> assertTrue(heliport.padXDimension().isEmpty()),
        () -> assertEquals("ST MARY MEDICAL CENTER", heliport.heliportName().get()),
        () -> assertEquals(Integer.valueOf(68647), heliport.fileRecordNumber().get()),
        () -> assertEquals("1703", heliport.cycleDate().get())
    );
  }
}
