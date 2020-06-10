package org.mitre.tdp.boogie.v18;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.v18.record.TestRunwaySpec.runway1;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

public class TestArincRunway {

  @Test
  public void testFieldAccess() {
    ArincRunway runway = ArincRunway.wrap(ArincVersion.V18.parse(runway1));

    assertEquals(RecordType.S, runway.recordType());
    assertEquals(CustomerAreaCode.USA, runway.customerAreaCode());
    assertEquals(SectionCode.P, runway.sectionCode());
    assertEquals("KJFK", runway.airportIdentifier());
    assertEquals("K6", runway.airportIcaoRegion());
    assertEquals("G", runway.subSectionCode());
    assertEquals("RW04L", runway.runwayIdentifier());
    assertEquals("1", runway.continuationRecordNumber());
    assertEquals(Integer.valueOf(12079), runway.runwayLength().get());
    assertEquals(44.0d, runway.runwayMagneticBearing().get());
    assertEquals(40.623105555555554d, runway.latitude());
    assertEquals(-73.78473611111112d, runway.longitude());
    assertEquals(0.0d, runway.runwayGradient().get());
    assertEquals(Integer.valueOf(12), runway.landingThresholdElevation().get());
    assertEquals(Integer.valueOf(460), runway.thresholdDisplacementDistance().get());
    assertEquals(Integer.valueOf(57), runway.thresholdCrossingHeight().get());
    assertEquals(Integer.valueOf(200), runway.runwayWidth().get());
    assertEquals("IHIQ", runway.ilsMlsGlsIdentifier().get());
    assertEquals("1", runway.ilsMlsGlsCategory().get());
    assertEquals(Integer.valueOf(0), runway.stopway().get());
    assertFalse(runway.secondaryIlsMlsGlsIdentifier().isPresent());
    assertFalse(runway.secondaryIlsMlsGlsCategory().isPresent());
    assertEquals("CONC     090RBWT", runway.runwayDescription().get());
    assertEquals(Integer.valueOf(15519), runway.fileRecordNumber());
    assertEquals("2003", runway.cycle());
  }
}
