package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.RunwayConverter;
import org.mitre.tdp.boogie.arinc.v18.RunwaySpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincRunway {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new RunwaySpec());

  private static final RunwayConverter converter = new RunwayConverter();

  private static final String runway1 = "SUSAP KJFKK6GRW04L   1120790440 N40372318W073470505+0000          00012046057200 IHIQ10000           CONC     090RBWT      155192003";

  public static final String noLat = "SAFRP FC04FCGRW11    1029531100                                   011610000  075D     0000     050                         559832403";


  @Test
  void testRobust() {
    assertTrue(PARSER.parse(noLat).flatMap(converter).isEmpty());
  }

  @Test
  void testEqualsHashCode() {
    EqualsVerifier.forClass(ArincRunway.class).verify();
  }

  @Test
  void testFieldAccess() {
    ArincRunway runway = PARSER.parse(runway1).flatMap(converter).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> assertEquals(RecordType.S, runway.recordType()),
        () -> assertEquals(CustomerAreaCode.USA, runway.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.P, runway.sectionCode()),
        () -> assertEquals("KJFK", runway.airportIdentifier()),
        () -> assertEquals("K6", runway.airportIcaoRegion()),
        () -> assertEquals(Optional.of("G"), runway.subSectionCode()),
        () -> assertEquals("RW04L", runway.runwayIdentifier()),
        () -> assertEquals("1", runway.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(12079), runway.runwayLength().orElseThrow(AssertionError::new)),
        () -> assertEquals(44.0d, runway.runwayMagneticBearing().orElseThrow(AssertionError::new)),
        () -> assertEquals(40.623105555555555d, runway.latitude(), .000001),
        () -> assertEquals(-73.78473611111111d, runway.longitude(), .000001),
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
        () -> assertEquals(Integer.valueOf(15519), runway.fileRecordNumber()),
        () -> assertEquals("2003", runway.lastUpdateCycle())
    );
  }
}
