package org.mitre.tdp.boogie.v18.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.record.AirportSpec;

public class TestAirportSpec {

  public static final String _01GE = "SUSAP 01GEK7A        110000025N N32402800W082461600W006000375250      1800018000PR00Y NAR    WRIGHTSVILLE/THE FARM         818881902";

  @Test
  public void testSpecMatches_01GE() {
    assertTrue(new AirportSpec().matchesRecord(_01GE));
  }

  @Test
  public void testParseAirport_01GE() {
    ArincRecord record = ArincVersion.V18.parse(_01GE);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.USA, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("01GE", record.getRequiredField("airportIdentifier"));
    assertEquals("K7", record.getRequiredField("icaoRegion"));
    assertEquals("A", record.getRequiredField("subSectionCode"));
    assertFalse(record.getOptionalField("iataDesignator").isPresent());
    assertEquals("1", record.getRequiredField("continuationRecordNumber"));
    assertEquals(10000.0d, record.getRequiredField("speedLimitAltitude"));
    assertEquals(Integer.valueOf(2500), record.getRequiredField("longestRunway"));
    assertEquals(false, record.getRequiredField("ifrCapability"));
    assertFalse(record.getOptionalField("longestRunwaySurfaceCode").isPresent());
    assertEquals(32.67444444444444d, record.getRequiredField("latitude"));
    assertEquals(-82.77111111111111d, record.getRequiredField("longitude"));
    assertEquals(-6.0d, record.getRequiredField("magneticVariation"));
    assertEquals(375.0d, record.getRequiredField("airportElevation"));
    assertEquals(Integer.valueOf(250), record.getRequiredField("speedLimit"));
    assertFalse(record.getOptionalField("recommendedNavaid").isPresent());
    assertFalse(record.getOptionalField("recommendedNavaidIcaoRegion").isPresent());
    assertEquals(18000.0d, record.getRequiredField("transitionAltitude"));
    assertEquals(18000.0d, record.getRequiredField("transitionLevel"));
    assertEquals(PublicMilitaryIndicator.P, record.getRequiredField("publicMilitaryIndicator"));
    assertEquals(true, record.getRequiredField("daylightTimeIndicator"));
    assertFalse(record.getOptionalField("magneticTrueIndicator").isPresent());
    assertEquals("NAR", record.getRequiredField("datumCode"));
    assertEquals("WRIGHTSVILLE/THE FARM", record.getRequiredField("airportFullName"));
    assertEquals(Integer.valueOf(81888), record.getRequiredField("fileRecordNumber"));
    assertEquals("1902", record.getRequiredField("cycle"));
  }

  public static String _CAL4 = "SCANP CAL4CYA        010000000  N57132624W111250817E014001048250      1800018000             ALBIAN                        000281813";

  @Test
  public void testSpecMatches_CAL4() {
    assertTrue(new AirportSpec().matchesRecord(_CAL4));
  }

  @Test
  public void testParseAirport_CAL4() {
    ArincRecord record = ArincVersion.V18.parse(_CAL4);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.CAN, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("CAL4", record.getRequiredField("airportIdentifier"));
    assertEquals("CY", record.getRequiredField("icaoRegion"));
    assertEquals("A", record.getRequiredField("subSectionCode"));
    assertFalse(record.getOptionalField("iataDesignator").isPresent());
    assertEquals("0", record.getRequiredField("continuationRecordNumber"));
    assertEquals(10000.0d, record.getRequiredField("speedLimitAltitude"));
    assertEquals(Integer.valueOf(0), record.getRequiredField("longestRunway"));
    assertFalse(record.getOptionalField("ifrCapability").isPresent());
    assertFalse(record.getOptionalField("longestRunwaySurfaceCode").isPresent());
    assertEquals(57.223955555555555d, record.getRequiredField("latitude"));
    assertEquals(-111.41893611111112d, record.getRequiredField("longitude"));
    assertEquals(14.0d, record.getRequiredField("magneticVariation"));
    assertEquals(1048.0d, record.getRequiredField("airportElevation"));
    assertEquals(Integer.valueOf(250), record.getRequiredField("speedLimit"));
    assertFalse(record.getOptionalField("recommendedNavaid").isPresent());
    assertFalse(record.getOptionalField("recommendedNavaidIcaoRegion").isPresent());
    assertEquals(18000.0d, record.getRequiredField("transitionAltitude"));
    assertEquals(18000.0d, record.getRequiredField("transitionLevel"));
    assertFalse(record.getOptionalField("publicMilitaryIndicator").isPresent());
    assertFalse(record.getOptionalField("daylightTimeIndicator").isPresent());
    assertFalse(record.getOptionalField("magneticTrueIndicator").isPresent());
    assertFalse(record.getOptionalField("datumCode").isPresent());
    assertEquals("ALBIAN", record.getRequiredField("airportFullName"));
    assertEquals(Integer.valueOf(28), record.getRequiredField("fileRecordNumber"));
    assertEquals("1813", record.getRequiredField("cycle"));
  }

  public static final String KJFK = "SUSAP KJFKK6AJFK     110000145Y N40382374W073464329W013000013250JFK K61800018000CR00Y NAR    NEW YORK/JOHN F KENNEDY INTL  145992003";

  @Test
  public void testSpecMatches_KJFK() {
    assertTrue(new AirportSpec().matchesRecord(KJFK));
  }

  @Test
  public void testParseAirport_KJFK() {
    ArincRecord record = ArincVersion.V18.parse(KJFK);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.USA, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("KJFK", record.getRequiredField("airportIdentifier"));
    assertEquals("K6", record.getRequiredField("icaoRegion"));
    assertEquals("A", record.getRequiredField("subSectionCode"));
    assertEquals("JFK", record.getRequiredField("iataDesignator"));
    assertEquals("1", record.getRequiredField("continuationRecordNumber"));
    assertEquals(10000.0d, record.getRequiredField("speedLimitAltitude"));
    assertEquals(Integer.valueOf(14500), record.getRequiredField("longestRunway"));
    assertEquals(true, record.getRequiredField("ifrCapability"));
    assertFalse(record.getOptionalField("longestRunwaySurfaceCode").isPresent());
    assertEquals(40.63992777777778d, record.getRequiredField("latitude"));
    assertEquals(-73.77869166666666d, record.getRequiredField("longitude"));
    assertEquals(-13.0d, record.getRequiredField("magneticVariation"));
    assertEquals(13.0d, record.getRequiredField("airportElevation"));
    assertEquals(Integer.valueOf(250), record.getRequiredField("speedLimit"));
    assertEquals("JFK", record.getRequiredField("recommendedNavaid"));
    assertEquals("K6", record.getRequiredField("recommendedNavaidIcaoRegion"));
    assertEquals(18000.0d, record.getRequiredField("transitionAltitude"));
    assertEquals(18000.0d, record.getRequiredField("transitionLevel"));
    assertEquals(PublicMilitaryIndicator.C, record.getRequiredField("publicMilitaryIndicator"));
    assertEquals(true, record.getRequiredField("daylightTimeIndicator"));
    assertFalse(record.getOptionalField("magneticTrueIndicator").isPresent());
    assertEquals("NAR", record.getRequiredField("datumCode"));
    assertEquals("NEW YORK/JOHN F KENNEDY INTL", record.getRequiredField("airportFullName"));
    assertEquals(Integer.valueOf(14599), record.getRequiredField("fileRecordNumber"));
    assertEquals("2003", record.getRequiredField("cycle"));
  }
}
