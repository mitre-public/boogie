package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegConverter;
import org.mitre.tdp.boogie.arinc.v18.TestAirwayLegSpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincAirwayLeg {

  private static final AirwayLegConverter converter = new AirwayLegConverter();

  @Test
  void testEqualsHashCode() {
    EqualsVerifier.forClass(ArincAirwayLeg.class).verify();
  }

  @Test
  void testFieldAccess() {
    ArincAirwayLeg airway = ArincVersion.V18.parser().apply(TestAirwayLegSpec.airway3).flatMap(converter).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> assertEquals(RecordType.S, airway.recordType()),
        () -> assertEquals(CustomerAreaCode.CAN, airway.customerAreaCode()),
        () -> assertEquals(SectionCode.E, airway.sectionCode()),
        () -> assertEquals("R", airway.subSectionCode()),
        () -> assertEquals("A590", airway.routeIdentifier()),
        () -> assertFalse(airway.sixthCharacter().isPresent()),
        () -> assertEquals(Integer.valueOf(210), airway.sequenceNumber()),
        () -> assertEquals("PASRO", airway.fixIdentifier()),
        () -> assertEquals("PA", airway.fixIcaoRegion()),
        () -> assertEquals(SectionCode.E, airway.fixSectionCode()),
        () -> assertEquals("A", airway.fixSubSectionCode()),
        () -> assertEquals("0", airway.continuationRecordNumber()),
        () -> assertEquals("E C ", airway.waypointDescription().orElseThrow(AssertionError::new)),
        () -> assertFalse(airway.boundaryCode().isPresent()),
        () -> assertEquals("O", airway.routeType().orElseThrow(AssertionError::new)),
        () -> assertEquals(Level.H, airway.level().orElseThrow(AssertionError::new)),
        () -> assertEquals("F", airway.directionRestriction().orElseThrow(AssertionError::new)),
        () -> assertEquals("RA", airway.cruiseTableIndicator().orElseThrow(AssertionError::new)),
        () -> assertFalse(airway.euIndicator().orElseThrow(AssertionError::new)),
        () -> assertFalse(airway.recommendedNavaidIdentifier().isPresent()),
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
        () -> assertEquals(Integer.valueOf(23365), airway.fileRecordNumber()),
        () -> assertEquals("2006", airway.lastUpdateCycle())
    );
  }
}
