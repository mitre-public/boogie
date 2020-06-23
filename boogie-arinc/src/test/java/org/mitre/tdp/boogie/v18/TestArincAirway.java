package org.mitre.tdp.boogie.v18;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.record.TestAirwaySpec;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.Level;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

public class TestArincAirway {

  @Test
  public void testFieldAccess() {
    ArincAirway airway = ArincAirway.wrap(ArincVersion.V18.parse(TestAirwaySpec.airway3));
    assertNotNull(airway);

    assertEquals(RecordType.S, airway.recordType().get());
    assertEquals(CustomerAreaCode.CAN, airway.customerAreaCode().get());
    assertEquals(SectionCode.E, airway.sectionCode().get());
    assertEquals("R", airway.subSectionCode().get());
    assertEquals("A590", airway.routeIdentifier().get());
    assertFalse(airway.sixthCharacter().isPresent());
    assertEquals(Integer.valueOf(210), airway.sequenceNumber().get());
    assertEquals("PASRO", airway.fixIdentifier().get());
    assertEquals("PA", airway.fixIcaoRegion().get());
    assertEquals(SectionCode.E, airway.fixSectionCode().get());
    assertEquals("A", airway.fixSubSectionCode().get());
    assertEquals("0", airway.continuationRecordNumber().get());
    assertEquals("E C ", airway.waypointDescription().get());
    assertFalse(airway.boundaryCode().isPresent());
    assertEquals("O", airway.routeType().get());
    assertEquals(Level.H, airway.level().get());
    assertEquals("F", airway.directionRestriction().get());
//    assertFalse(record.getOptionalField("tcIndicator").isPresent());
    assertEquals(false, airway.euIndicator().get());
    assertFalse(airway.recommendedNavaid().isPresent());
    assertFalse(airway.recommendedNavaidIcaoRegion().isPresent());
    assertFalse(airway.rnp().isPresent());
    assertFalse(airway.theta().isPresent());
    assertFalse(airway.rho().isPresent());
    assertEquals(59.4d, airway.outboundMagneticCourse().get());
    assertEquals("2285", airway.routeHoldDistanceTime().get());
    assertEquals(228.5, airway.routeDistance().get());
    assertEquals(59.0d, airway.inboundMagneticCourse().get());
    assertEquals(18000.0d, airway.minAltitude1().get());
    assertFalse(airway.minAltitude2().isPresent());
    assertEquals(60000.0d, airway.maxAltitude().get());
    assertFalse(airway.fixedRadiusTransitionIndicator().isPresent());
    assertEquals(Integer.valueOf(23365), airway.fileRecordNumber().get());
    assertEquals("2006", airway.cycle().get());
  }
}
