package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestAirportSpec {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new AirportSpec());

  public static final String _01GE = "SUSAP 01GEK7A        110000025N N32402800W082461600W006000375250      1800018000PR00Y NAR    WRIGHTSVILLE/THE FARM         818881902";

  @Test
  void testSpecMatches_01GE() {
    assertTrue(new AirportSpec().matchesRecord(_01GE));
  }

  @Test
  void testValidatorPasses_01GE() {
    assertTrue(new AirportValidator().test(PARSER.parse(_01GE).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseAirport_01GE() {
    ArincRecord record = PARSER.parse(_01GE).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("01GE", record.requiredField("airportIdentifier")),
        () -> assertEquals("K7", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("A", record.requiredField("subSectionCode")),
        () -> assertFalse(record.optionalField("iataDesignator").isPresent()),
        () -> assertEquals("1", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(10000.0d, record.requiredField("speedLimitAltitude")),
        () -> assertEquals(Integer.valueOf(2500), record.requiredField("longestRunway")),
        () -> assertEquals(false, record.requiredField("ifrCapability")),
        () -> assertFalse(record.optionalField("longestRunwaySurfaceCode").isPresent()),
        () -> assertEquals(32.67444444444444d, record.requiredField("latitude")),
        () -> assertEquals(-82.77111111111111d, record.requiredField("longitude")),
        () -> assertEquals(-6.0d, record.requiredField("magneticVariation")),
        () -> assertEquals(375.0d, record.requiredField("airportElevation")),
        () -> assertEquals(Integer.valueOf(250), record.requiredField("speedLimit")),
        () -> assertFalse(record.optionalField("recommendedNavaid").isPresent()),
        () -> assertFalse(record.optionalField("recommendedNavaidIcaoRegion").isPresent()),
        () -> assertEquals(18000.0d, record.requiredField("transitionAltitude")),
        () -> assertEquals(18000.0d, record.requiredField("transitionLevel")),
        () -> assertEquals(PublicMilitaryIndicator.P, record.requiredField("publicMilitaryIndicator")),
        () -> assertEquals(true, record.requiredField("daylightTimeIndicator")),
        () -> assertFalse(record.optionalField("magneticTrueIndicator").isPresent()),
        () -> assertEquals("NAR", record.requiredField("datumCode")),
        () -> assertEquals("WRIGHTSVILLE/THE FARM", record.requiredField("airportFullName")),
        () -> assertEquals(Integer.valueOf(81888), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1902", record.requiredField("lastUpdateCycle"))
    );
  }

  public static String _CAL4 = "SCANP CAL4CYA        010000000  N57132624W111250817E014001048250      1800018000             ALBIAN                        000281813";

  @Test
  void testSpecMatches_CAL4() {
    assertTrue(new AirportSpec().matchesRecord(_CAL4));
  }

  @Test
  void testValidatorPasses_CAL4() {
    assertTrue(new AirportValidator().test(PARSER.parse(_CAL4).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseAirport_CAL4() {
    ArincRecord record = PARSER.parse(_CAL4).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.CAN, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("CAL4", record.requiredField("airportIdentifier")),
        () -> assertEquals("CY", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("A", record.requiredField("subSectionCode")),
        () -> assertFalse(record.optionalField("iataDesignator").isPresent()),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(10000.0d, record.requiredField("speedLimitAltitude")),
        () -> assertEquals(Integer.valueOf(0), record.requiredField("longestRunway")),
        () -> assertEquals(Optional.of(false), record.optionalField("ifrCapability"), "IfrCapability"),
        () -> assertFalse(record.optionalField("longestRunwaySurfaceCode").isPresent()),
        () -> assertEquals(57.223955555555555d, record.requiredField("latitude")),
        () -> assertEquals(-111.41893611111112d, record.requiredField("longitude")),
        () -> assertEquals(14.0d, record.requiredField("magneticVariation")),
        () -> assertEquals(1048.0d, record.requiredField("airportElevation")),
        () -> assertEquals(Integer.valueOf(250), record.requiredField("speedLimit")),
        () -> assertFalse(record.optionalField("recommendedNavaid").isPresent()),
        () -> assertFalse(record.optionalField("recommendedNavaidIcaoRegion").isPresent()),
        () -> assertEquals(18000.0d, record.requiredField("transitionAltitude")),
        () -> assertEquals(18000.0d, record.requiredField("transitionLevel")),
        () -> assertFalse(record.optionalField("publicMilitaryIndicator").isPresent()),
        () -> assertEquals(false, record.requiredField("daylightTimeIndicator"), "DaylightTimeIndicator"),
        () -> assertEquals(Optional.empty(), record.optionalField("magneticTrueIndicator"), "MagneticTrueIndicator"),
        () -> assertFalse(record.optionalField("datumCode").isPresent()),
        () -> assertEquals("ALBIAN", record.requiredField("airportFullName")),
        () -> assertEquals(Integer.valueOf(28), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1813", record.requiredField("lastUpdateCycle"))
    );
  }

  public static final String KJFK = "SUSAP KJFKK6AJFK     110000145Y N40382374W073464329W013000013250JFK K61800018000CR00Y NAR    NEW YORK/JOHN F KENNEDY INTL  145992003";

  @Test
  void testSpecMatches_KJFK() {
    assertTrue(new AirportSpec().matchesRecord(KJFK));
  }

  @Test
  void testValidatorPasses_KJFK() {
    assertTrue(new AirportValidator().test(PARSER.parse(KJFK).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseAirport_KJFK() {
    ArincRecord record = PARSER.parse(KJFK).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("KJFK", record.requiredField("airportIdentifier")),
        () -> assertEquals("K6", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("A", record.requiredField("subSectionCode")),
        () -> assertEquals("JFK", record.requiredField("iataDesignator")),
        () -> assertEquals("1", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(10000.0d, record.requiredField("speedLimitAltitude")),
        () -> assertEquals(Integer.valueOf(14500), record.requiredField("longestRunway")),
        () -> assertEquals(true, record.requiredField("ifrCapability")),
        () -> assertFalse(record.optionalField("longestRunwaySurfaceCode").isPresent()),
        () -> assertEquals(40.63992777777778d, record.requiredField("latitude")),
        () -> assertEquals(-73.77869166666666d, record.requiredField("longitude")),
        () -> assertEquals(-13.0d, record.requiredField("magneticVariation")),
        () -> assertEquals(13.0d, record.requiredField("airportElevation")),
        () -> assertEquals(Integer.valueOf(250), record.requiredField("speedLimit")),
        () -> assertEquals("JFK", record.requiredField("recommendedNavaid")),
        () -> assertEquals("K6", record.requiredField("recommendedNavaidIcaoRegion")),
        () -> assertEquals(18000.0d, record.requiredField("transitionAltitude")),
        () -> assertEquals(18000.0d, record.requiredField("transitionLevel")),
        () -> assertEquals(PublicMilitaryIndicator.C, record.requiredField("publicMilitaryIndicator")),
        () -> assertEquals(true, record.requiredField("daylightTimeIndicator")),
        () -> assertFalse(record.optionalField("magneticTrueIndicator").isPresent()),
        () -> assertEquals("NAR", record.requiredField("datumCode")),
        () -> assertEquals("NEW YORK/JOHN F KENNEDY INTL", record.requiredField("airportFullName")),
        () -> assertEquals(Integer.valueOf(14599), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", record.requiredField("lastUpdateCycle"))
    );
  }
}
