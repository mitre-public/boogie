package org.mitre.tdp.boogie.v18;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.v18.record.TestTransitionSpec.TF;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

public class TestArincTransition {

  @Test
  public void testFieldAccess() {
    ArincTransition transition = ArincTransition.wrap(ArincVersion.V18.parse(TF));

    assertEquals(RecordType.S, transition.recordType());
    assertEquals(CustomerAreaCode.USA, transition.customerAreaCode());
    assertEquals(SectionCode.P, transition.sectionCode());
    assertEquals("KJFK", transition.airportIdentifier());
    assertEquals("K6", transition.airportIcaoRegion());
    assertEquals("F", transition.subSectionCode());
    assertEquals("L22R", transition.sidStarIdentifier());
    assertEquals("L", transition.routeType());
    assertFalse(transition.transitionIdentifier().isPresent());
    assertEquals(Integer.valueOf(20), transition.sequenceNumber());
    assertEquals("MATTR", transition.fixIdentifier().get());
    assertEquals("K6", transition.fixIcaoRegion().get());
    assertEquals(SectionCode.E, transition.fixSectionCode().get());
    assertEquals("A", transition.fixSubSectionCode().get());
    assertEquals("0", transition.continuationRecordNumber());
    assertEquals("E  F", transition.waypointDescription().get());
    assertFalse(transition.turnDirection().isPresent());
    assertFalse(transition.rnp().isPresent());
    assertEquals(PathTerm.TF, transition.pathTerm());
    assertEquals(false, transition.turnDirectionValid().get());
    assertEquals("IJOC", transition.recommendedNavaid().get());
    assertEquals("K6", transition.recommendedNavaidIcaoRegion().get());
    assertFalse(transition.arcRadius().isPresent());
    assertEquals(41.1d, transition.theta().get());
    assertEquals(6.6d, transition.rho().get());
    assertEquals(221.3d, transition.outboundMagneticCourse().get());
    assertEquals("0050", transition.routeHoldDistanceTime().get());
    assertEquals("+", transition.altitudeDescription().get());
    assertEquals(1900.0d, transition.minAltitude1().get());
    assertFalse(transition.minAltitude2().isPresent());
    assertFalse(transition.transitionAltitude().isPresent());
    assertFalse(transition.speedLimit().isPresent());
    assertEquals(-3.01, transition.verticalAngle().get());
    assertFalse(transition.centerFix().isPresent());
    assertFalse(transition.centerFixIcaoRegion().isPresent());
    assertFalse(transition.centerFixSectionCode().isPresent());
    assertFalse(transition.centerFixSubSectionCode().isPresent());

    assertEquals(Integer.valueOf(15338), transition.fileRecordNumber());
    assertEquals("2004", transition.cycle());
  }
}
