package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.AirportConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.TestAirportSpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincAirport {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new AirportSpec());

  @Test
  void testEqualsHashCode() {
    EqualsVerifier.forClass(ArincAirport.class).verify();
  }

  @Test
  void testFieldAccess() {
    ArincAirport airport = PARSER.parse(TestAirportSpec.KJFK).flatMap(new AirportConverter()).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> Assertions.assertEquals(RecordType.S, airport.recordType()),
        () -> Assertions.assertEquals(CustomerAreaCode.USA, airport.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> Assertions.assertEquals(SectionCode.P, airport.sectionCode()),
        () -> assertEquals("KJFK", airport.airportIdentifier()),
        () -> assertEquals("K6", airport.airportIcaoRegion()),
        () -> assertEquals(Optional.of("A"), airport.subSectionCode()),
        () -> assertEquals("JFK", airport.iataDesignator().orElseThrow(AssertionError::new)),
        () -> assertEquals("1", airport.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals(10000.0d, airport.speedLimitAltitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(14500), airport.longestRunway().orElseThrow(AssertionError::new)),
        () -> assertEquals(true, airport.ifrCapability().orElseThrow(AssertionError::new)),
        () -> assertFalse(airport.longestRunwaySurfaceCode().isPresent()),
        () -> assertEquals(40.63992777777777d, airport.latitude(), .000001),
        () -> assertEquals(-73.77869166666666d, airport.longitude(), .000001),
        () -> assertEquals(-13.0d, airport.magneticVariation().orElseThrow(AssertionError::new)),
        () -> assertEquals(13.0d, airport.airportElevation().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(250), airport.speedLimit().orElseThrow(AssertionError::new)),
        () -> assertEquals("JFK", airport.recommendedNavaid().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", airport.recommendedNavaidIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals(18000.0d, airport.transitionAltitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(18000.0d, airport.transitionLevel().orElseThrow(AssertionError::new)),
        () -> Assertions.assertEquals(PublicMilitaryIndicator.C, airport.publicMilitaryIndicator().orElseThrow(AssertionError::new)),
        () -> assertEquals(true, airport.daylightTimeIndicator().orElseThrow(AssertionError::new)),
        () -> assertFalse(airport.magneticTrueIndicator().isPresent()),
        () -> assertEquals("NAR", airport.datumCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("NEW YORK/JOHN F KENNEDY INTL", airport.airportFullName().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(14599), airport.fileRecordNumber()),
        () -> assertEquals("2003", airport.lastUpdateCycle())
    );
  }
}