package org.mitre.tdp.boogie.v18;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mitre.tdp.boogie.v18.record.TestGlideSlopeSpec.glideslope1;

public class TestLocalizerGlideSlope {

  @Test
  public void testFieldAccess() {
    ArincRecord record = ArincVersion.V18.parse(glideslope1);
    ArincLocalizerGlideSlope localizer = ArincLocalizerGlideSlope.wrap(record);
    assertNotNull(record);

    assertEquals(RecordType.S, localizer.recordType().get());
    assertEquals(CustomerAreaCode.USA, localizer.customerAreaCode().get());
    assertEquals(SectionCode.P, localizer.sectionCode().get());
    assertEquals("KJFK", localizer.airportIdentifier().get());
    assertEquals("K6", localizer.airportIcaoRegion().get());
    assertEquals("I", localizer.subSectionCode().get());
    // https://airportnavfinder.com/charts/tpp/KJFK/00610IL4L.PDF
    assertEquals("IHIQ", localizer.localizerIdentifier().get());
    assertEquals("1", localizer.ilsMlsGlsCategory().get());
    assertEquals("0", localizer.continuationRecordNumber().get());
    assertEquals(110.90d, localizer.localizerFrequency().get());
    assertEquals("RW04L", localizer.runwayIdentifier().get());
    assertEquals(40.65294d, localizer.localizerLatitude().get(), 0.02);
    assertEquals(-73.76221d, localizer.localizerLongitude().get(), 0.02);
    assertEquals(44.0d, localizer.localizerBearing().get());
    assertEquals(40.6253d, localizer.glideSlopeLatitude().get(), 0.02);
    assertEquals(-73.76221d, localizer.glideSlopeLongitude().get(), 0.02);
    assertEquals(Integer.valueOf(605), localizer.localizerPosition().get());
    assertEquals("", localizer.localizerPositionReference().get());
    assertEquals(Integer.valueOf(1087), localizer.glideslopePosition().get());
    assertEquals(3d, localizer.glideSlopeAngle().get(), 0.0001);
    assertEquals(-13d, localizer.stationDeclination().get(), 0.1);
    assertEquals(Integer.valueOf(57), localizer.glideSlopeHeightAtLandingThreshold().get());
    assertEquals(9d, localizer.glideSlopeElevation().get(), 0.001);
    assertEquals(Integer.valueOf(58775), localizer.fileRecordNumber().get());
    assertEquals("2003", localizer.cycle().get());
  }
}
