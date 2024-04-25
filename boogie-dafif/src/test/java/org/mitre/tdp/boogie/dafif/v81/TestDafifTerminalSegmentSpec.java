package org.mitre.tdp.boogie.dafif.v81;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifTerminalSegmentConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifTerminalSegmentValidator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDafifTerminalSegmentSpec {

  private static final DafifRecordParser parser = DafifRecordParser.all();

  private static final DafifTerminalSegmentValidator validator = new DafifTerminalSegmentValidator();

  private static final DafifTerminalSegmentConverter converter = new DafifTerminalSegmentConverter();

  public static final String RAW_TERMINAL_SEGMENT = "AA30079\t1\tADRI1C ADRIV 1C (RNAV)\t10\t5\tRW29\tTNCA\tIF\tADRIV\tAA\tE\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t+\tFL40\t\t\t201707\tN12234179\t12.394942\tW069361217\t-69.603381\t-11.652500\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\n";

  @Test
  void testParseTerminalParent() {
    DafifRecord record = parser.parse(DafifRecordType.TRM_SEG, RAW_TERMINAL_SEGMENT).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals("AA30079", record.requiredField("airportIdentification")),
        () -> assertEquals(Integer.valueOf(1), record.requiredField("terminalProcedureType")),
        () -> assertEquals("ADRI1C ADRIV 1C (RNAV)", record.requiredField("terminalIdentifier")),
        () -> assertEquals(Integer.valueOf(10), record.requiredField("terminalSequenceNumber")),
        () -> assertEquals("5", record.requiredField("terminalApproachType")),
        () -> assertEquals("RW29", record.requiredField("transitionIdentifier")),
        () -> assertEquals("TNCA", record.requiredField("icaoCode")),
        () -> assertEquals("IF", record.requiredField("trackDescriptionCode")),
        () -> assertEquals("ADRIV", record.requiredField("termSegWaypointIdentifier")),
        () -> assertEquals("AA", record.requiredField("waypointCountryCode")),
        () -> assertEquals("E", record.requiredField("terminalWaypointDescriptionCode1Arpt")),
        () -> assertFalse(record.optionalField("terminalWaypointDescriptionCode2").isPresent()),
        () -> assertFalse(record.optionalField("terminalWaypointDescriptionCode3").isPresent()),
        () -> assertFalse(record.optionalField("terminalWaypointDescriptionCode4").isPresent()),
        () -> assertFalse(record.optionalField("terminalSegmentTurnDirection").isPresent()),
        () -> assertFalse(record.optionalField("navaid1Identifier").isPresent()),
        () -> assertFalse(record.optionalField("navaid1Type").isPresent()),
        () -> assertFalse(record.optionalField("navaid1CountryCode").isPresent()),
        () -> assertFalse(record.optionalField("navaid1KeyCode").isPresent()),
        () -> assertFalse(record.optionalField("fix1Bearing").isPresent()),
        () -> assertFalse(record.optionalField("fix1Distance").isPresent()),
        () -> assertFalse(record.optionalField("navaid2Identifier").isPresent()),
        () -> assertFalse(record.optionalField("navaid2Type").isPresent()),
        () -> assertFalse(record.optionalField("navaid2CountryCode").isPresent()),
        () -> assertFalse(record.optionalField("navaid2KeyCode").isPresent()),
        () -> assertFalse(record.optionalField("fix2Bearing").isPresent()),
        () -> assertFalse(record.optionalField("fix2Distance").isPresent()),
        () -> assertFalse(record.optionalField("terminalMagneticCourse").isPresent()),
        () -> assertFalse(record.optionalField("distance").isPresent()),
        () -> assertEquals("+", record.requiredField("altitudeDescription")),
        () -> assertEquals("FL40", record.requiredField("altitude1")),
        () -> assertFalse(record.optionalField("altitude2").isPresent()),
        () -> assertFalse(record.optionalField("requiredNavPerformance").isPresent()),
        () -> assertEquals(Integer.valueOf(201707), record.requiredField("cycleDate")),
        () -> assertEquals("N12234179", record.requiredField("waypointGeodeticLatitude")),
        () -> assertEquals(12.394942, record.requiredField("waypointDegreesLatitude")),
        () -> assertEquals("W069361217", record.requiredField("waypointGeodeticLongitude")),
        () -> assertEquals(-69.603381, record.requiredField("waypointDegreesLongitude")),
        () -> assertEquals(-11.652500, record.requiredField("waypointMagneticVariation")),
        () -> assertFalse(record.optionalField("navaid1GeodeticLatitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid1DegreesLatitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid1GeodeticLongitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid1DegreesLongitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid1MagneticVariation").isPresent()),
        () -> assertFalse(record.optionalField("navaid1DmeGeodeticLatitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid1DmeDegreesLatitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid1DmeGeodeticLongitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid1DmeDegreesLongitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid2GeodeticLatitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid2DegreesLatitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid2GeodeticLongitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid2DegreesLongitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid2MagneticVariation").isPresent()),
        () -> assertFalse(record.optionalField("navaid2DmeGeodeticLatitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid2DmeDegreesLatitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid2DmeGeodeticLongitude").isPresent()),
        () -> assertFalse(record.optionalField("navaid2DmeDegreesLongitude").isPresent()),
        () -> assertFalse(record.optionalField("speedLimit1").isPresent()),
        () -> assertFalse(record.optionalField("speedLimitAircraftType1").isPresent()),
        () -> assertFalse(record.optionalField("speedLimitAltitude1").isPresent()),
        () -> assertFalse(record.optionalField("speedLimit2").isPresent()),
        () -> assertFalse(record.optionalField("speedLimitAircraftType2").isPresent()),
        () -> assertFalse(record.optionalField("speedLimitAltitude2").isPresent()),
        () -> assertFalse(record.optionalField("verticalNavigationVnav").isPresent()),
        () -> assertFalse(record.optionalField("thresholdCrossingHeight").isPresent()),
        () -> assertFalse(record.optionalField("arcWaypointIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("arcWaypointCountryCode").isPresent()),
        () -> assertFalse(record.optionalField("arcRadius").isPresent())
    );

    assertTrue(validator.test(record));

    DafifTerminalSegment terminalSegment = converter.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals("AA30079", terminalSegment.getAirportIdentification()),
        () -> assertEquals(1, terminalSegment.getTerminalProcedureType()),
        () -> assertEquals("ADRI1C ADRIV 1C (RNAV)", terminalSegment.getTerminalIdentifier()),
        () -> assertEquals(10, terminalSegment.getTerminalSequenceNumber()),
        () -> assertEquals("5", terminalSegment.getTerminalApproachType()),
        () -> assertEquals("RW29", terminalSegment.getTransitionIdentifier()),
        () -> assertEquals("TNCA", terminalSegment.getIcaoCode()),
        () -> assertEquals("IF", terminalSegment.getTrackDescriptionCode()),
        () -> assertEquals("ADRIV", terminalSegment.getTermSegWaypointIdentifier()),
        () -> assertEquals("AA", terminalSegment.getWaypointCountryCode()),
        () -> assertEquals("E", terminalSegment.getTerminalWaypointDescriptionCode1Arpt()),
        () -> assertEquals("+", terminalSegment.getAltitudeDescription()),
        () -> assertEquals("FL40", terminalSegment.getAltitude1()),
        () -> assertEquals(201707, terminalSegment.getCycleDate()),
        () -> assertEquals("N12234179", terminalSegment.getWaypointGeodeticLatitude()),
        () -> assertEquals(12.394942, terminalSegment.getWaypointDegreesLatitude()),
        () -> assertEquals("W069361217", terminalSegment.getWaypointGeodeticLongitude()),
        () -> assertEquals(-69.603381, terminalSegment.getWaypointDegreesLongitude()),
        () -> assertEquals(-11.652500, terminalSegment.getWaypointMagneticVariation())
    );
  }
}
