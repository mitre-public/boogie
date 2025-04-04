package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegSpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincProcedureLeg {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new ProcedureLegSpec());

  private static final ProcedureLegConverter converter = new ProcedureLegConverter();

  private static final String TF = "SUSAP KJFKK6FL22R  L      020MATTRK6EA0E  F    TF IJOCK6      0411006622130050PI  + 01900             -301          U NS   153382004";

  private static final String HF = "SEEUP UITTUIFN12   ACI4   020CI   UIDB0EE AR   HF                     1210T053      04160                              S   785571907";

  private static final String AF = "SLAMP MKJSMKEOMAXI51ENARI 050SIA13MKPC0EE  L   AF SIA MK      252301303430    D                                            533482014";

  @Test
  void testEqualsHashCode() {
    EqualsVerifier.forClass(ArincProcedureLeg.class).verify();
  }

  @Test
  void testFieldAccessTF() {
    ArincProcedureLeg procedureLeg = PARSER.parse(TF).flatMap(converter).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> assertEquals(RecordType.S, procedureLeg.recordType()),
        () -> assertEquals(CustomerAreaCode.USA, procedureLeg.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.P, procedureLeg.sectionCode()),
        () -> assertEquals("KJFK", procedureLeg.airportIdentifier()),
        () -> assertEquals("K6", procedureLeg.airportIcaoRegion()),
        () -> assertEquals(Optional.of("F"), procedureLeg.subSectionCode()),
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
        () -> assertEquals(PathTerminator.TF, procedureLeg.pathTerm()),
        () -> assertEquals(false, procedureLeg.turnDirectionValid().orElseThrow(AssertionError::new)),
        () -> assertEquals("IJOC", procedureLeg.recommendedNavaidIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", procedureLeg.recommendedNavaidIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertFalse(procedureLeg.arcRadius().isPresent()),
        () -> assertEquals(41.1d, procedureLeg.theta().orElseThrow(AssertionError::new), .0001, "Theta"),
        () -> assertEquals(6.6d, procedureLeg.rho().orElseThrow(AssertionError::new), .0001, "Rho"),
        () -> assertEquals(221.3d, procedureLeg.outboundMagneticCourse().orElseThrow(AssertionError::new), .0001, "Outbound Magnetic Course"),
        () -> assertEquals("0050", procedureLeg.routeHoldDistanceTime().orElseThrow(AssertionError::new)),
        () -> assertEquals("+", procedureLeg.altitudeDescription().orElseThrow(AssertionError::new)),
        () -> assertEquals(1900.0d, procedureLeg.minAltitude1().orElseThrow(AssertionError::new), .0001, "MinAltitude1"),
        () -> assertFalse(procedureLeg.minAltitude2().isPresent()),
        () -> assertFalse(procedureLeg.transitionAltitude().isPresent()),
        () -> assertFalse(procedureLeg.speedLimit().isPresent()),
        () -> assertEquals(-3.01, procedureLeg.verticalAngle().orElseThrow(AssertionError::new), .0001, "VerticalAngle"),
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

  @Test
  void testFieldAccessHF() {
    ArincProcedureLeg procedureLeg = PARSER.parse(HF).flatMap(converter).orElseThrow(AssertionError::new);
    assertEquals(Optional.of(Duration.ofSeconds(318)), procedureLeg.holdTime(), "HoldTime");
  }

  @Test
  void testFieldAccessAF() {
    ArincProcedureLeg procedureLeg = PARSER.parse(AF).flatMap(converter).orElseThrow(AssertionError::new);
    assertEquals(Optional.of(TurnDirection.L), procedureLeg.turnDirection(), "TurnDirection");
  }
}
