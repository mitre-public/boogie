package org.mitre.tdp.boogie.arinc.v18.spec;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestLocalizerGlideSlopeSpec {

  private static final String glideslope1 = "SUSAP KJFKK6IIHIQ1   011090RW04LN40390697W0734543950440N40373108W0734654910605 1087    300W01305700009                     587752003";

  @Test
  void testSpecMatchesGlideslopeRecord1() {
    assertTrue(new LocalizerGlideSlopeSpec().matchesRecord(glideslope1));
  }

  @Test
  void testParseGlideslope1() {
    ArincRecord record = ArincVersion.V18.parser().apply(glideslope1).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("KJFK", record.requiredField("airportIdentifier")),
        () -> assertEquals("K6", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("I", record.requiredField("subSectionCode")),
        // https://airportnavfinder.com/charts/tpp/KJFK/00610IL4L.PDF
        () -> assertEquals("IHIQ", record.requiredField("localizerIdentifier")),
        () -> assertEquals("1", record.requiredField("ilsMlsGlsCategory")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(110.90d, record.requiredField("localizerFrequency")),
        () -> assertEquals("RW04L", record.requiredField("runwayIdentifier")),
        () -> assertEquals(40.65294d, record.requiredField("localizerLatitude"), 0.02),
        () -> assertEquals(-73.76221d, record.requiredField("localizerLongitude"), 0.02),
        () -> assertEquals(44.0d, record.requiredField("localizerBearing")),
        () -> assertEquals(40.6253d, record.requiredField("glideSlopeLatitude"), 0.02),
        () -> assertEquals(-73.76221d, record.requiredField("glideSlopeLongitude"), 0.02),
        () -> assertEquals(Integer.valueOf(605), record.requiredField("localizerPosition")),
        () -> assertEquals("", record.requiredField("localizerPositionReference")),
        () -> assertEquals(Integer.valueOf(1087), record.requiredField("glideSlopePosition")),
        () -> assertEquals(3d, record.requiredField("glideSlopeAngle"), 0.0001),
        () -> assertEquals(-13d, record.requiredField("stationDeclination"), 0.1),
        () -> assertEquals(Integer.valueOf(57), record.requiredField("glideSlopeHeightAtLandingThreshold")),
        () -> assertEquals(9d, record.requiredField("glideSlopeElevation"), 0.001),
        () -> assertEquals(Integer.valueOf(58775), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", record.requiredField("lastUpdateCycle"))
    );
  }
}
