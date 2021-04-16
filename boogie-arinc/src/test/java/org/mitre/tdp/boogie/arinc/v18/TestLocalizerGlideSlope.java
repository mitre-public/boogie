package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.ArincLocalizerGlideSlope;

class TestLocalizerGlideSlope {

  private static final String glideslope1 = "SUSAP KJFKK6IIHIQ1   011090RW04LN40390697W0734543950440N40373108W0734654910605 1087    300W01305700009                     587752003";

  @Test
  void testFieldAccess() {
    ArincLocalizerGlideSlope localizer = ArincLocalizerGlideSlope.wrap(ArincVersion.V18.parser().apply(glideslope1).orElseThrow(AssertionError::new));

    assertAll(
        () -> assertEquals(RecordType.S, localizer.recordType().orElseThrow(AssertionError::new)),
        () -> assertEquals(CustomerAreaCode.USA, localizer.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.P, localizer.sectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("KJFK", localizer.airportIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", localizer.airportIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("I", localizer.subSectionCode().orElseThrow(AssertionError::new)),
        // https://airportnavfinder.com/charts/tpp/KJFK/00610IL4L.PDF
        () -> assertEquals("IHIQ", localizer.localizerIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("1", localizer.ilsMlsGlsCategory().orElseThrow(AssertionError::new)),
        () -> assertEquals("0", localizer.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals(110.90d, localizer.localizerFrequency().orElseThrow(AssertionError::new)),
        () -> assertEquals("RW04L", localizer.runwayIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals(40.65294d, localizer.localizerLatitude().orElseThrow(AssertionError::new), 0.02),
        () -> assertEquals(-73.76221d, localizer.localizerLongitude().orElseThrow(AssertionError::new), 0.02),
        () -> assertEquals(44.0d, localizer.localizerBearing().orElseThrow(AssertionError::new)),
        () -> assertEquals(40.6253d, localizer.glideSlopeLatitude().orElseThrow(AssertionError::new), 0.02),
        () -> assertEquals(-73.76221d, localizer.glideSlopeLongitude().orElseThrow(AssertionError::new), 0.02),
        () -> assertEquals(Integer.valueOf(605), localizer.localizerPosition().orElseThrow(AssertionError::new)),
        () -> assertEquals("", localizer.localizerPositionReference().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(1087), localizer.glideslopePosition().orElseThrow(AssertionError::new)),
        () -> assertEquals(3d, localizer.glideSlopeAngle().orElseThrow(AssertionError::new), 0.0001),
        () -> assertEquals(-13d, localizer.stationDeclination().orElseThrow(AssertionError::new), 0.1),
        () -> assertEquals(Integer.valueOf(57), localizer.glideSlopeHeightAtLandingThreshold().orElseThrow(AssertionError::new)),
        () -> assertEquals(9d, localizer.glideSlopeElevation().orElseThrow(AssertionError::new), 0.001),
        () -> assertEquals(Integer.valueOf(58775), localizer.fileRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("2003", localizer.cycle().orElseThrow(AssertionError::new))
    );
  }
}
