package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestGnssLandingSystemSpec {
  public static final String G16A1 = "SSPAP YSSYYMTG16A1   021146RW16R                   1550S33575258E151110525YSSY         300E0130  00021WGE     00034        464902003";

  @Test
  void testG16A1() {
    ArincRecord gls = ArincVersion.V18.parser().apply(G16A1).orElseThrow();

    assertAll(
        () -> assertEquals(RecordType.S, gls.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.SPA, gls.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, gls.requiredField("sectionCode")),
        () -> assertEquals("YSSY", gls.requiredField("airportIdentifier")),
        () -> assertEquals("YM", gls.requiredField("airportIcaoRegion")),
        () -> assertEquals("T", gls.requiredField("subSectionCode")),
        () -> assertEquals("G16A", gls.requiredField("glsRefPathIdentifier")),
        () -> assertEquals("1", gls.requiredField("glsCategory")),
        () -> assertEquals("RW16R", gls.requiredField("runwayIdentifier")),
        () -> assertEquals(155.0, gls.requiredField("glsApproachBearing")), //"1550"
        () -> assertEquals(-33.96460, gls.requiredField("latitude"), .0001),
        () -> assertEquals(151.18479, gls.requiredField("longitude"), .0001),
        () -> assertEquals("YSSY", gls.requiredField("glsStationIdentifier")),
        () -> assertTrue(gls.optionalField("serviceVolumeRadius").isEmpty()),
        () -> assertTrue(gls.optionalField("tdmaSlots").isEmpty()),
        () -> assertEquals(3.0, gls.requiredField("glsApproachSlope")),
        () -> assertEquals(13D, gls.requiredField("magneticVariation")),
        () -> assertEquals(21D, gls.requiredField("stationElevation")),
        () -> assertEquals("WGE", gls.requiredField("datumCode")),
        () -> assertEquals(34D, gls.requiredField("stationElevationWgs84")),
        () -> assertEquals(Integer.valueOf(46490), gls.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", gls.requiredField("lastUpdatedCycle")),
        () -> assertEquals("0", gls.requiredField("continuationRecordNumber")),
        () -> assertTrue(gls.optionalField("stationType").isEmpty())
    );
  }

}
