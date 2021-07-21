package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.arinc.v18.TestProcedureLegSpec.TF;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegConverter;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincProcedureLeg {

  private static final ProcedureLegConverter converter = new ProcedureLegConverter();

  @Test
  void testEqualsHashCode() {
    EqualsVerifier.forClass(ArincProcedureLeg.class).verify();
  }

  @Test
  void testFieldAccessTF() {
    ArincProcedureLeg procedureLeg = ArincVersion.V18.parser().apply(TF).flatMap(converter).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> assertEquals(RecordType.S, procedureLeg.recordType()),
        () -> assertEquals(CustomerAreaCode.USA, procedureLeg.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.P, procedureLeg.sectionCode()),
        () -> assertEquals("KJFK", procedureLeg.airportIdentifier()),
        () -> assertEquals("K6", procedureLeg.airportIcaoRegion()),
        () -> assertEquals("F", procedureLeg.subSectionCode()),
        () -> assertEquals("L22R", procedureLeg.sidStarIdentifier()),
        () -> assertEquals("L", procedureLeg.routeType()),
        () -> assertFalse(procedureLeg.transitionIdentifier().isPresent()),
        () -> assertEquals(Integer.valueOf(20), procedureLeg.sequenceNumber()),
        () -> assertEquals("MATTR", procedureLeg.fixIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", procedureLeg.fixIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.E, procedureLeg.fixSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("A", procedureLeg.fixSubSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("0", procedureLeg.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("E  F", procedureLeg.waypointDescription().orElseThrow(AssertionError::new)),
        () -> assertFalse(procedureLeg.turnDirection().isPresent()),
        () -> assertFalse(procedureLeg.rnp().isPresent()),
        () -> assertEquals(PathTerm.TF, procedureLeg.pathTerm()),
        () -> assertEquals(false, procedureLeg.turnDirectionValid().orElseThrow(AssertionError::new)),
        () -> assertEquals("IJOC", procedureLeg.recommendedNavaidIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", procedureLeg.recommendedNavaidIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertFalse(procedureLeg.arcRadius().isPresent()),
        () -> assertEquals(41.1d, procedureLeg.theta().orElseThrow(AssertionError::new)),
        () -> assertEquals(6.6d, procedureLeg.rho().orElseThrow(AssertionError::new)),
        () -> assertEquals(221.3d, procedureLeg.outboundMagneticCourse().orElseThrow(AssertionError::new)),
        () -> assertEquals("0050", procedureLeg.routeHoldDistanceTime().orElseThrow(AssertionError::new)),
        () -> assertEquals("+", procedureLeg.altitudeDescription().orElseThrow(AssertionError::new)),
        () -> assertEquals(1900.0d, procedureLeg.minAltitude1().orElseThrow(AssertionError::new)),
        () -> assertFalse(procedureLeg.minAltitude2().isPresent()),
        () -> assertFalse(procedureLeg.transitionAltitude().isPresent()),
        () -> assertFalse(procedureLeg.speedLimit().isPresent()),
        () -> assertEquals(-3.01, procedureLeg.verticalAngle().orElseThrow(AssertionError::new)),
        () -> assertFalse(procedureLeg.centerFixIdentifier().isPresent()),
        () -> assertFalse(procedureLeg.centerFixIcaoRegion().isPresent()),
        () -> assertFalse(procedureLeg.centerFixSectionCode().isPresent()),
        () -> assertFalse(procedureLeg.centerFixSubSectionCode().isPresent()),
        () -> assertEquals("N", procedureLeg.routeTypeQualifier1().orElseThrow(AssertionError::new)),
        () -> assertEquals("S", procedureLeg.routeTypeQualifier2().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(15338), procedureLeg.fileRecordNumber()),
        () -> assertEquals("2004", procedureLeg.lastUpdateCycle())
    );
  }

  public static final String HF = "SEEUP UITTUIFN12   ACI4   020CI   UIDB0EE AR   HF                     1210T053      04160                              S   785571907";

  @Test
  void testFieldAccessHF() {
    ArincProcedureLeg procedureLeg = ArincVersion.V18.parser().apply(HF).flatMap(converter).orElseThrow(AssertionError::new);
    assertEquals(Duration.ofSeconds(318), procedureLeg.holdTime().orElseThrow(AssertionError::new));
  }
}
