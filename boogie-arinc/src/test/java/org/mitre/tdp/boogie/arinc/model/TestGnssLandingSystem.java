package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemConverter;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemSpec;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemValidator;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestGnssLandingSystem {
  public static final String G16A1 = "SSPAP YSSYYMTG16A1   021146RW16R                   1550S33575258E151110525YSSY         300E0130  00021WGE     00034        464902003";
  public static final String G25A1 = "SSPAP YSSYYMTG25A1   021557RW25                    2420S33575258E151110525YSSY         300E0130  00021WGE     00034        464922003";

  private static final GnssLandingSystemConverter converter = new GnssLandingSystemConverter();
  @Test
  void testMatches() {
    Assertions.assertTrue(new GnssLandingSystemSpec().matchesRecord(G16A1));
  }

  @Test
  void testValidatorPasses_G16A1() {
    Assertions.assertTrue(new GnssLandingSystemValidator().test(ArincVersion.V18.parser().apply(G16A1).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParse_G16A1() {
    ArincGnssLandingSystem gls = ArincVersion.V18.parser().apply(G16A1).flatMap(converter).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> assertEquals(RecordType.S, gls.recordType()),
        () -> assertEquals(CustomerAreaCode.SPA, gls.customerAreaCode()),
        () -> assertEquals(SectionCode.P, gls.sectionCode()),
        () -> assertEquals("YSSY", gls.airportIdentifier()),
        () -> assertEquals("YM", gls.airportIcaoRegion()),
        () -> assertEquals("T", gls.subSectionCode().orElseThrow()),
        () -> assertEquals("G16A", gls.glsRefPathIdentifier()),
        () -> assertEquals("1", gls.glsCategory().orElseThrow()),
        () -> assertEquals("RW16R", gls.runwayIdentifier()),
        () -> assertEquals(155.0, gls.glsApproachBearing().orElseThrow()), //"1550"
        () -> assertEquals(-33.96460, gls.stationLatitude(), .0001),
        () -> assertEquals(151.18479, gls.stationLongitude(), .0001),
        () -> assertEquals("YSSY", gls.glsStationIdent().orElseThrow()),
        () -> assertTrue(gls.serviceVolumeRadius().isEmpty()),
        () -> assertTrue(gls.tdmaSlots().isEmpty()),
        () -> assertEquals(3.0, gls.glsApproachSlope().orElseThrow()),
        () -> assertEquals(13, gls.magneticVariation()),
        () -> assertEquals(21, gls.stationElevation().orElseThrow()),
        () -> assertEquals("WGE", gls.datumCode().orElseThrow()),
        () -> assertEquals(34, gls.stationElevationWgs84().orElseThrow()),
        () -> assertEquals(46490, gls.fileRecordNumber()),
        () -> assertEquals("2003", gls.lastUpdatedCycle()),
        () -> assertEquals("0", gls.continuationRecordNumber().orElseThrow()),
        () -> assertTrue(gls.stationType().isEmpty())
    );
  }

  @Test
  void testParse_G25A1() {
    ArincGnssLandingSystem gls = ArincVersion.V18.parser().apply(G25A1).flatMap(converter).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> assertEquals(RecordType.S, gls.recordType()),
        () -> assertEquals(CustomerAreaCode.SPA, gls.customerAreaCode()),
        () -> assertEquals(SectionCode.P, gls.sectionCode()),
        () -> assertEquals("YSSY", gls.airportIdentifier()),
        () -> assertEquals("YM", gls.airportIcaoRegion()),
        () -> assertEquals("T", gls.subSectionCode().orElseThrow()),
        () -> assertEquals("G25A", gls.glsRefPathIdentifier()),
        () -> assertEquals("1", gls.glsCategory().orElseThrow()),
        () -> assertEquals("RW25", gls.runwayIdentifier()),
        () -> assertEquals(242.0, gls.glsApproachBearing().orElseThrow()), //"1550"
        () -> assertEquals(-33.96460, gls.stationLatitude(), .0001),
        () -> assertEquals(151.184791, gls.stationLongitude(), .0001),
        () -> assertEquals("YSSY", gls.glsStationIdent().orElseThrow()),
        () -> assertTrue(gls.serviceVolumeRadius().isEmpty()),
        () -> assertTrue(gls.tdmaSlots().isEmpty()),
        () -> assertEquals(3.0, gls.glsApproachSlope().orElseThrow()),
        () -> assertEquals(13.0, gls.magneticVariation()),
        () -> assertEquals(21, gls.stationElevation().orElseThrow()),
        () -> assertEquals("WGE", gls.datumCode().orElseThrow()),
        () -> assertEquals(34.0, gls.stationElevationWgs84().orElseThrow()),
        () -> assertEquals(46492, gls.fileRecordNumber()),
        () -> assertEquals("2003", gls.lastUpdatedCycle()),
        () -> assertEquals("0", gls.continuationRecordNumber().orElseThrow()),
        () -> assertTrue(gls.stationType().isEmpty())
    );
  }


}
