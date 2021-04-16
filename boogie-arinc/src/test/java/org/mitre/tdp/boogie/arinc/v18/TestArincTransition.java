package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.arinc.v18.spec.TestTransitionSpec.TF;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincTransition {

  @Test
  void testFieldAccess() {
    ArincTransition transition = ArincTransition.wrap(ArincVersion.V18.parser().apply(TF).orElseThrow(AssertionError::new));

    assertAll(
        () -> assertEquals(RecordType.S, transition.recordType().orElseThrow(AssertionError::new)),
        () -> assertEquals(CustomerAreaCode.USA, transition.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.P, transition.sectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("KJFK", transition.airportIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", transition.airportIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("F", transition.subSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("L22R", transition.sidStarIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("L", transition.routeType().orElseThrow(AssertionError::new)),
        () -> assertFalse(transition.transitionIdentifier().isPresent()),
        () -> assertEquals(Integer.valueOf(20), transition.sequenceNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("MATTR", transition.fixIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", transition.fixIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.E, transition.fixSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("A", transition.fixSubSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("0", transition.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("E  F", transition.waypointDescription().orElseThrow(AssertionError::new)),
        () -> assertFalse(transition.turnDirection().isPresent()),
        () -> assertFalse(transition.rnp().isPresent()),
        () -> assertEquals(PathTerm.TF, transition.pathTerm().orElseThrow(AssertionError::new)),
        () -> assertEquals(false, transition.turnDirectionValid().orElseThrow(AssertionError::new)),
        () -> assertEquals("IJOC", transition.recommendedNavaid().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", transition.recommendedNavaidIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertFalse(transition.arcRadius().isPresent()),
        () -> assertEquals(41.1d, transition.theta().orElseThrow(AssertionError::new)),
        () -> assertEquals(6.6d, transition.rho().orElseThrow(AssertionError::new)),
        () -> assertEquals(221.3d, transition.outboundMagneticCourse().orElseThrow(AssertionError::new)),
        () -> assertEquals("0050", transition.routeHoldDistanceTime().orElseThrow(AssertionError::new)),
        () -> assertEquals("+", transition.altitudeDescription().orElseThrow(AssertionError::new)),
        () -> assertEquals(1900.0d, transition.minAltitude1().orElseThrow(AssertionError::new)),
        () -> assertFalse(transition.minAltitude2().isPresent()),
        () -> assertFalse(transition.transitionAltitude().isPresent()),
        () -> assertFalse(transition.speedLimit().isPresent()),
        () -> assertEquals(-3.01, transition.verticalAngle().orElseThrow(AssertionError::new)),
        () -> assertFalse(transition.centerFix().isPresent()),
        () -> assertFalse(transition.centerFixIcaoRegion().isPresent()),
        () -> assertFalse(transition.centerFixSectionCode().isPresent()),
        () -> assertFalse(transition.centerFixSubSectionCode().isPresent()),
        () -> assertEquals("N", transition.routeTypeQualifier1().orElseThrow(AssertionError::new)),
        () -> assertEquals("S", transition.routeTypeQualifier2().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(15338), transition.fileRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("2004", transition.cycle().orElseThrow(AssertionError::new))
    );
  }
}
