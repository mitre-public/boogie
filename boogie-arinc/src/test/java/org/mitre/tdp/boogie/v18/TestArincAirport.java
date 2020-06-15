package org.mitre.tdp.boogie.v18;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.record.TestAirportSpec;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

public class TestArincAirport {

  @Test
  public void testFieldAccess() {
    ArincAirport airport = ArincAirport.wrap(ArincVersion.V18.parse(TestAirportSpec.KJFK));
    assertNotNull(airport);

    assertEquals(RecordType.S, airport.recordType());
    assertEquals(CustomerAreaCode.USA, airport.customerAreaCode());
    assertEquals(SectionCode.P, airport.sectionCode());
    assertEquals("KJFK", airport.airportIdentifier());
    assertEquals("K6", airport.airportIcaoRegion());
    assertEquals("A", airport.subSectionCode());
    assertEquals("JFK", airport.iataDesignator().get());
    assertEquals("1", airport.continuationRecordNumber());
    assertEquals(10000.0d, airport.speedLimitAltitude().get());
    assertEquals(Integer.valueOf(14500), airport.longestRunway().get());
    assertEquals(true, airport.ifrCapability().get());
    assertFalse(airport.longestRunwaySurfaceCode().isPresent());
    assertEquals(40.63992777777778d, airport.latitude());
    assertEquals(-73.77869166666666d, airport.longitude());
    assertEquals(-13.0d, airport.magneticVariation().get());
    assertEquals(13.0d, airport.airportElevation().get());
    assertEquals(Integer.valueOf(250), airport.speedLimit().get());
    assertEquals("JFK", airport.recommendedNavaid().get());
    assertEquals("K6", airport.recommendedNavaidIcaoRegion().get());
    assertEquals(18000.0d, airport.transitionAltitude().get());
    assertEquals(18000.0d, airport.transitionLevel().get());
    assertEquals(PublicMilitaryIndicator.C, airport.publicMilitaryIndicator().get());
    assertEquals(true, airport.dayTimeIndicator().get());
    assertFalse(airport.magneticTrueIndicator().isPresent());
    assertEquals("NAR", airport.datumCode().get());
    assertEquals("NEW YORK/JOHN F KENNEDY INTL", airport.airportFullName());
    assertEquals(Integer.valueOf(14599), airport.fileRecordNumber());
    assertEquals("2003", airport.cycle());
  }
}