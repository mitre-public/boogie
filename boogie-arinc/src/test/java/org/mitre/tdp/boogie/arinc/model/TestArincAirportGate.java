package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.AirportGateConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportGateSpec;
import org.mitre.tdp.boogie.arinc.v18.AirportGateValidator;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestArincAirportGate {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new AirportGateSpec());

  public static final String JFK_GATE = "SUSAP KJFKK6B2T29    0          N40383000W073472400                                                                        733432003";

  public static final AirportGateConverter converter = new AirportGateConverter();

  @Test
  void testMatches() {
    Assertions.assertTrue(new AirportGateSpec().matchesRecord(JFK_GATE));
  }

  @Test
  void testValidatorPasses() {
    Assertions.assertTrue(new AirportGateValidator().test(PARSER.parse(JFK_GATE).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParse_JFK_GATE() {
    ArincAirportGate gate = PARSER.parse(JFK_GATE).flatMap(converter).orElseThrow(AssertionError::new);
    ArincAirportGate rebuilt = gate.toBuilder().build();
    assertAll(
        () -> assertEquals(RecordType.S, gate.getRecordType()),
        () -> assertEquals(CustomerAreaCode.USA, gate.getCustomerAreaCode()),
        () -> assertEquals(SectionCode.P, gate.sectionCode()),
        () -> assertEquals("KJFK", gate.getAirportIdentifier()),
        () -> assertEquals("K6", gate.getAirportIcaoRegion()),
        () -> assertEquals(Optional.of("B"), gate.subSectionCode()),
        () -> assertEquals("2T29", gate.getGateIdentifier()),
        () -> assertEquals(40.641666666666666, gate.getLatitude()),
        () -> assertEquals(-73.78999999999999, gate.getLongitude()),
        () -> assertNull(gate.getName()),
        () -> assertEquals(73343, gate.getFileRecordNumber()),
        () -> assertEquals(Optional.of("0"), gate.continuationRecordNumber()),
        () -> assertEquals("2003", gate.getLastUpdatedCycle()),
        () -> assertEquals(gate, rebuilt)
    );
  }
}