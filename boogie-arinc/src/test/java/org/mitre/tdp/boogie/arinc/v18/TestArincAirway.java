package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.spec.TestAirwaySpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincAirway {

  @Test
  void testFieldAccess() {
    ArincAirway airway = ArincAirway.wrap(ArincVersion.V18.parser().apply(TestAirwaySpec.airway3).orElseThrow(AssertionError::new));

    assertAll(
        () -> assertEquals(RecordType.S, airway.recordType().orElseThrow(AssertionError::new)),
        () -> assertEquals(CustomerAreaCode.CAN, airway.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.E, airway.sectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("R", airway.subSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("A590", airway.routeIdentifier().orElseThrow(AssertionError::new)),
        () -> assertFalse(airway.sixthCharacter().isPresent()),
        () -> assertEquals(Integer.valueOf(210), airway.sequenceNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("PASRO", airway.fixIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("PA", airway.fixIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.E, airway.fixSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("A", airway.fixSubSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("0", airway.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("E C ", airway.waypointDescription().orElseThrow(AssertionError::new)),
        () -> assertFalse(airway.boundaryCode().isPresent()),
        () -> assertEquals("O", airway.routeType().orElseThrow(AssertionError::new)),
        () -> assertEquals(Level.H, airway.level().orElseThrow(AssertionError::new)),
        () -> assertEquals("F", airway.directionRestriction().orElseThrow(AssertionError::new)),
//    () -> assertFalse(record.getOptionalField("tcIndicator").isPresent()),
        () -> assertEquals(false, airway.euIndicator().orElseThrow(AssertionError::new)),
        () -> assertFalse(airway.recommendedNavaid().isPresent()),
        () -> assertFalse(airway.recommendedNavaidIcaoRegion().isPresent()),
        () -> assertFalse(airway.rnp().isPresent()),
        () -> assertFalse(airway.theta().isPresent()),
        () -> assertFalse(airway.rho().isPresent()),
        () -> assertEquals(59.4d, airway.outboundMagneticCourse().orElseThrow(AssertionError::new)),
        () -> assertEquals("2285", airway.routeHoldDistanceTime().orElseThrow(AssertionError::new)),
        () -> assertEquals(228.5, airway.routeDistance().orElseThrow(AssertionError::new)),
        () -> assertEquals(59.0d, airway.inboundMagneticCourse().orElseThrow(AssertionError::new)),
        () -> assertEquals(18000.0d, airway.minAltitude1().orElseThrow(AssertionError::new)),
        () -> assertFalse(airway.minAltitude2().isPresent()),
        () -> assertEquals(60000.0d, airway.maxAltitude().orElseThrow(AssertionError::new)),
        () -> assertFalse(airway.fixedRadiusTransitionIndicator().isPresent()),
        () -> assertEquals(Integer.valueOf(23365), airway.fileRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("2006", airway.cycle().orElseThrow(AssertionError::new))
    );
  }
}
