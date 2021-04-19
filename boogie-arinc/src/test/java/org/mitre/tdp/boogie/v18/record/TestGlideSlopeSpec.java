package org.mitre.tdp.boogie.v18.record;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.record.GlideSlopeSpec;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGlideSlopeSpec {
  public static String glideslope1 = "SUSAP KJFKK6IIHIQ1   011090RW04LN40390697W0734543950440N40373108W0734654910605 1087    300W01305700009                     587752003";

  @Test
  public void testSpecMatchesGlideslopeRecord1() {
    assertTrue(new GlideSlopeSpec().matchesRecord(glideslope1));
  }

  @Test
  public void testParseGlideslope1() {
    ArincRecord record = ArincVersion.V18.parse(glideslope1);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.USA, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("KJFK", record.getRequiredField("airportIdentifier"));
    assertEquals("K6", record.getRequiredField("airportIcaoRegion"));
    assertEquals("I", record.getRequiredField("subSectionCode"));
    // https://airportnavfinder.com/charts/tpp/KJFK/00610IL4L.PDF
    assertEquals("IHIQ", record.getRequiredField("localizerIdentifier"));
    assertEquals("1", record.getRequiredField("cat"));
    assertEquals("0", record.getRequiredField("continuationRecordNumber"));
    assertEquals(110.90d, record.getRequiredField("localizerFrequency"));
    assertEquals("RW04L", record.getRequiredField("runwayIdentifier"));
    assertEquals(40.65294d, record.getRequiredField("localizerLatitude"), 0.02);
    assertEquals(-73.76221d, record.getRequiredField("localizerLongitude"), 0.02);
    assertEquals(44.0d, record.getRequiredField("localizerBearing"));
    assertEquals(40.6253d, record.getRequiredField("glideSlopeLatitude"), 0.02);
    assertEquals(-73.76221d, record.getRequiredField("glideSlopeLongitude"), 0.02);
    assertEquals(Integer.valueOf(605), record.getRequiredField("localizerPosition"));
    assertEquals("", record.getRequiredField("localizerPositionReference"));
    assertEquals(Integer.valueOf(1087), record.getRequiredField("glideslopePosition"));
    assertEquals(3d, record.getRequiredField("glideSlopeAngle"), 0.0001);
    assertEquals(-13d, record.getRequiredField("stationDeclination"), 0.1);
    assertEquals(Integer.valueOf(57), record.getRequiredField("glideSlopeHeightAtLandingThreshold"));
    assertEquals(9d, record.getRequiredField("glideSlopeElevation"), 0.001);
    assertEquals(Integer.valueOf(58775), record.getRequiredField("fileRecordNumber"));
    assertEquals("2003", record.getRequiredField("cycle"));
  }
}
