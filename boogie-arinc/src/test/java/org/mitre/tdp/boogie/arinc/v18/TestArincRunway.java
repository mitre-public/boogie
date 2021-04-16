package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.arinc.v18.spec.TestRunwaySpec.runway1;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincRunway {

  @Test
  void testFieldAccess() {
    ArincRunway runway = ArincRunway.wrap(ArincVersion.V18.parser().apply(runway1).orElseThrow(AssertionError::new));

    assertAll(
        () -> assertEquals(RecordType.S, runway.recordType().orElseThrow(AssertionError::new)),
        () -> assertEquals(CustomerAreaCode.USA, runway.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.P, runway.sectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("KJFK", runway.airportIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", runway.airportIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("G", runway.subSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("RW04L", runway.runwayIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("1", runway.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(12079), runway.runwayLength().orElseThrow(AssertionError::new)),
        () -> assertEquals(44.0d, runway.runwayMagneticBearing().orElseThrow(AssertionError::new)),
        () -> assertEquals(40.623105555555554d, runway.latitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(-73.78473611111112d, runway.longitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(0.0d, runway.runwayGradient().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(12), runway.landingThresholdElevation().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(460), runway.thresholdDisplacementDistance().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(57), runway.thresholdCrossingHeight().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(200), runway.runwayWidth().orElseThrow(AssertionError::new)),
        () -> assertEquals("IHIQ", runway.ilsMlsGlsIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("1", runway.ilsMlsGlsCategory().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(0), runway.stopway().orElseThrow(AssertionError::new)),
        () -> assertFalse(runway.secondaryIlsMlsGlsIdentifier().isPresent()),
        () -> assertFalse(runway.secondaryIlsMlsGlsCategory().isPresent()),
        () -> assertEquals("CONC     090RBWT", runway.runwayDescription().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(15519), runway.fileRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("2003", runway.cycle().orElseThrow(AssertionError::new))
    );
  }
}
