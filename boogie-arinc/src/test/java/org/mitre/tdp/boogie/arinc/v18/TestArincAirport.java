package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.spec.TestAirportSpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincAirport {

  @Test
  void testFieldAccess() {
    ArincAirport airport = ArincAirport.wrap(ArincVersion.V18.parser().apply(TestAirportSpec.KJFK).orElseThrow(AssertionError::new));

    assertAll(
        () -> assertEquals(RecordType.S, airport.recordType().orElseThrow(AssertionError::new)),
        () -> assertEquals(CustomerAreaCode.USA, airport.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.P, airport.sectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("KJFK", airport.airportIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", airport.airportIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("A", airport.subSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("JFK", airport.iataDesignator().orElseThrow(AssertionError::new)),
        () -> assertEquals("1", airport.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals(10000.0d, airport.speedLimitAltitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(14500), airport.longestRunway().orElseThrow(AssertionError::new)),
        () -> assertEquals(true, airport.ifrCapability().orElseThrow(AssertionError::new)),
        () -> assertFalse(airport.longestRunwaySurfaceCode().isPresent()),
        () -> assertEquals(40.63992777777778d, airport.latitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(-73.77869166666666d, airport.longitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(-13.0d, airport.magneticVariation().orElseThrow(AssertionError::new)),
        () -> assertEquals(13.0d, airport.airportElevation().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(250), airport.speedLimit().orElseThrow(AssertionError::new)),
        () -> assertEquals("JFK", airport.recommendedNavaid().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", airport.recommendedNavaidIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals(18000.0d, airport.transitionAltitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(18000.0d, airport.transitionLevel().orElseThrow(AssertionError::new)),
        () -> assertEquals(PublicMilitaryIndicator.C, airport.publicMilitaryIndicator().orElseThrow(AssertionError::new)),
        () -> assertEquals(true, airport.daylightTimeIndicator().orElseThrow(AssertionError::new)),
        () -> assertFalse(airport.magneticTrueIndicator().isPresent()),
        () -> assertEquals("NAR", airport.datumCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("NEW YORK/JOHN F KENNEDY INTL", airport.airportFullName().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(14599), airport.fileRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("2003", airport.cycle().orElseThrow(AssertionError::new))
    );
  }
}