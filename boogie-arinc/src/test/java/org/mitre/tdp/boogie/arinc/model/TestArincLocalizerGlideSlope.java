package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeConverter;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincLocalizerGlideSlope {

  private static final String glideslope1 = "SUSAP KJFKK6IIHIQ1   011090RW04LN40390697W0734543950440N40373108W0734654910605 1087    300W01305700009                     587752003";

  private static final LocalizerGlideSlopeConverter converter = new LocalizerGlideSlopeConverter();

  @Test
  void testEqualsHashCode() {
    EqualsVerifier.forClass(ArincLocalizerGlideSlope.class).verify();
  }

  @Test
  void testFieldAccess() {
    ArincLocalizerGlideSlope localizer = ArincVersion.V18.parser().apply(glideslope1).flatMap(converter).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> assertEquals(RecordType.S, localizer.recordType(), "RecordType"),
        () -> assertEquals(CustomerAreaCode.USA, localizer.customerAreaCode().orElseThrow(AssertionError::new), "CustomerAreaCode"),
        () -> assertEquals(SectionCode.P, localizer.sectionCode(), "SectionCode"),
        () -> assertEquals("KJFK", localizer.airportIdentifier(), "AirportIdentifier"),
        () -> assertEquals("K6", localizer.airportIcaoRegion(), "AirportIcaoRegion"),
        () -> assertEquals("I", localizer.subSectionCode(), "SubSectionCode"),
        // https://airportnavfinder.com/charts/tpp/KJFK/00610IL4L.PDF
        () -> assertEquals("IHIQ", localizer.localizerIdentifier(), "LocalizerIdentifier"),
        () -> assertEquals("1", localizer.ilsMlsGlsCategory().orElseThrow(AssertionError::new), "IlsMlsGlsCategory"),
        () -> assertEquals("0", localizer.continuationRecordNumber().orElseThrow(AssertionError::new), "ContinuationRecordNumber"),
        () -> assertEquals(110.90d, localizer.localizerFrequency().orElseThrow(AssertionError::new), "LocalizerFrequency"),
        () -> assertEquals("RW04L", localizer.runwayIdentifier(), "RunwayIdentifier"),
        () -> assertEquals(40.65294d, localizer.localizerLatitude().orElseThrow(AssertionError::new), 0.02, "LocalizerLatitude"),
        () -> assertEquals(-73.76221d, localizer.localizerLongitude().orElseThrow(AssertionError::new), 0.02, "LocalizerLongitude"),
        () -> assertEquals(44.0d, localizer.localizerBearing().orElseThrow(AssertionError::new), "LocalizerBearing"),
        () -> assertEquals(40.6253d, localizer.glideSlopeLatitude().orElseThrow(AssertionError::new), 0.02, "GlideSlopeLatitude"),
        () -> assertEquals(-73.76221d, localizer.glideSlopeLongitude().orElseThrow(AssertionError::new), 0.02, "GlideSlopeLongitude"),
        () -> assertEquals(Integer.valueOf(605), localizer.localizerPosition().orElseThrow(AssertionError::new), "LocalizerPosition"),
        () -> assertEquals(" ", localizer.localizerPositionReference().orElseThrow(AssertionError::new), "LocalizerPositionReference"),
        () -> assertEquals(Integer.valueOf(1087), localizer.glideSlopePosition().orElseThrow(AssertionError::new), "GlideSlopePosition"),
        () -> assertEquals(3d, localizer.glideSlopeAngle().orElseThrow(AssertionError::new), 0.0001, "GlideSlopeAngle"),
        () -> assertEquals(-13d, localizer.stationDeclination().orElseThrow(AssertionError::new), 0.1, "StationDeclination"),
        () -> assertEquals(Integer.valueOf(57), localizer.glideSlopeHeightAtLandingThreshold().orElseThrow(AssertionError::new), "GlideSlopeHeightAtLandingThreshold"),
        () -> assertEquals(9d, localizer.glideSlopeElevation().orElseThrow(AssertionError::new), 0.001, "GlideSlopeElevation"),
        () -> assertFalse(localizer.supportingFacilityIdentifier().isPresent(), "SupportingFacilityIdentifier"),
        () -> assertFalse(localizer.supportingFacilityIcaoCode().isPresent(), "SupportingFacilityIcaoRegion"),
        () -> assertFalse(localizer.supportingFacilitySectionCode().isPresent(), "SupportingFacilitySectionCode"),
        () -> assertFalse(localizer.supportingFacilitySubSectionCode().isPresent(), "SupportingFacilitySubSectionCode"),
        () -> assertEquals(Integer.valueOf(58775), localizer.fileRecordNumber(), "FileRecordNumber"),
        () -> assertEquals("2003", localizer.lastUpdateCycle(), "LastUpdateCycle")
    );
  }
}
