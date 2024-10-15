package org.mitre.tdp.boogie.dafif.v81;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficService;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifAtsConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifAtsSpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifAtsValidator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDafifAtsSpec {

  private static final DafifRecordParser parser = DafifRecordParser.standard(new DafifAtsSpec());

  private static final DafifAtsValidator validator = new DafifAtsValidator();
  private static final DafifAtsConverter converter = new DafifAtsConverter();

  public static final String RAW_ATS = "1AW1\t10\tE\tD\tHSSS\tY\tA\tL\tO\tHLLL\t\tORNAT\tLY\tE\t\tC\t\tN20000000\t20.000000\tE025000000\t25.000000\tHSSS\t4\tDOG\tSU\tV\t\tC\t\tN19105726\t19.182572\tE030253822\t30.427283\t99.0\t311.0\t99.0\tFL55\tFL285\tFL55\t\t\t\t201805\t\n";

  @Test
  void testParseDafifAirport() {
    DafifRecord record = parser.parse(DafifRecordType.ATS, RAW_ATS).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals("1AW1", record.requiredField("atsIdentifier")),
        () -> assertEquals(Integer.valueOf(10), record.requiredField("atsRouteSequenceNumber")),
        () -> assertEquals("E", record.requiredField("atsRouteDirection")),
        () -> assertEquals("D", record.requiredField("atsRouteType")),
        () -> assertEquals("HSSS", record.requiredField("icaoCode")),
        () -> assertEquals("Y", record.requiredField("biDirectional")),
        () -> assertEquals("A", record.requiredField("frequencyClass")),
        () -> assertEquals("L", record.requiredField("level")),
        () -> assertEquals("O", record.requiredField("atsRouteStatus")),
        () -> assertEquals("HLLL", record.requiredField("waypoint1IcaoCode")),
        () -> assertFalse(record.optionalField("waypoint1NavaidType").isPresent()),
        () -> assertEquals("ORNAT", record.requiredField("waypoint1WaypointIdentifierWptIdent")),
        () -> assertEquals("LY", record.requiredField("waypoint1CountryCode")),
        () -> assertEquals("E", record.requiredField("waypoint1AtsWaypointDescriptionCode1")),
        () -> assertFalse(record.optionalField("waypoint1AtsWaypointDescriptionCode2").isPresent()),
        () -> assertEquals("C", record.requiredField("waypoint1AtsWaypointDescriptionCode3")),
        () -> assertFalse(record.optionalField("waypoint1AtsWaypointDescriptionCode4").isPresent()),
        () -> assertEquals("N20000000", record.requiredField("waypoint1GeodeticLatitude")),
        () -> assertEquals(20.000000, record.requiredField("waypoint1DegreesLatitude")),
        () -> assertEquals("E025000000", record.requiredField("waypoint1GeodeticLongitude")),
        () -> assertEquals(25.000000, record.requiredField("waypoint1DegreesLongitude")),
        () -> assertEquals("HSSS", record.requiredField("waypoint2IcaoCode")),
        () -> assertEquals(Integer.valueOf(4), record.requiredField("waypoint2NavaidType")),
        () -> assertEquals("DOG", record.requiredField("waypoint2WaypointIdentifierWptIdent")),
        () -> assertEquals("SU", record.requiredField("waypoint2CountryCode")),
        () -> assertEquals("V", record.requiredField("waypoint2AtsWaypointDescriptionCode1")),
        () -> assertFalse(record.optionalField("waypoint2AtsWaypointDescriptionCode2").isPresent()),
        () -> assertEquals("C", record.requiredField("waypoint2AtsWaypointDescriptionCode3")),
        () -> assertFalse(record.optionalField("waypoint2AtsWaypointDescriptionCode4").isPresent()),
        () -> assertEquals("N19105726", record.requiredField("waypoint2GeodeticLatitude")),
        () -> assertEquals(19.182572, record.requiredField("waypoint2DegreesLatitude")),
        () -> assertEquals("E030253822", record.requiredField("waypoint2GeodeticLongitude")),
        () -> assertEquals(30.427283, record.requiredField("waypoint2DegreesLongitude")),
        () -> assertEquals("99.0", record.requiredField("atsRouteOutboundMagneticCourse")),
        () -> assertEquals(311.0, record.requiredField("atsRouteDistance")),
        () -> assertEquals("99.0", record.requiredField("atsRouteInboundMagneticCourse")),
        () -> assertEquals("FL55", record.requiredField("minimumAltitude")),
        () -> assertEquals("FL285", record.requiredField("upperLimit")),
        () -> assertEquals("FL55", record.requiredField("lowerLimit")),
        () -> assertFalse(record.optionalField("maxAuthorizedAltitude").isPresent()),
        () -> assertFalse(record.optionalField("cruiseLevelIndicator").isPresent()),
        () -> assertFalse(record.optionalField("requiredNavPerformance").isPresent()),
        () -> assertEquals(Integer.valueOf(201805), record.requiredField("cycleDate")),
        () -> assertFalse(record.optionalField("atsDesignator").isPresent())
    );

    assertTrue(validator.test(record));

    DafifAirTrafficService airTrafficService = converter.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals("1AW1", airTrafficService.getAtsIdentifier()),
        () -> assertEquals(10, airTrafficService.getAtsRouteSequenceNumber()),
        () -> assertEquals("E", airTrafficService.getAtsRouteDirection()),
        () -> assertEquals("D", airTrafficService.getAtsRouteType()),
        () -> assertEquals("HSSS", airTrafficService.getIcaoCode()),
        () -> assertEquals("Y", airTrafficService.getBiDirectional()),
        () -> assertEquals("A", airTrafficService.getFrequencyClass()),
        () -> assertEquals("L", airTrafficService.getLevel()),
        () -> assertEquals("O", airTrafficService.getAtsRouteStatus()),
        () -> assertEquals("HLLL", airTrafficService.getWaypoint1IcaoCode()),
        () -> assertEquals("ORNAT", airTrafficService.getWaypoint1WaypointIdentifierWptIdent()),
        () -> assertEquals("LY", airTrafficService.getWaypoint1CountryCode()),
        () -> assertEquals("E", airTrafficService.getWaypoint1AtsWaypointDescriptionCode1()),
        () -> assertEquals("C", airTrafficService.getWaypoint1AtsWaypointDescriptionCode3()),
        () -> assertEquals("N20000000", airTrafficService.getWaypoint1GeodeticLatitude()),
        () -> assertEquals(20.000000, airTrafficService.getWaypoint1DegreesLatitude()),
        () -> assertEquals("E025000000", airTrafficService.getWaypoint1GeodeticLongitude()),
        () -> assertEquals(25.000000, airTrafficService.getWaypoint1DegreesLongitude()),
        () -> assertEquals("HSSS", airTrafficService.getWaypoint2IcaoCode()),
        () -> assertEquals(4, airTrafficService.getWaypoint2NavaidType()),
        () -> assertEquals("DOG", airTrafficService.getWaypoint2WaypointIdentifierWptIdent()),
        () -> assertEquals("SU", airTrafficService.getWaypoint2CountryCode()),
        () -> assertEquals("V", airTrafficService.getWaypoint2AtsWaypointDescriptionCode1()),
        () -> assertEquals("C", airTrafficService.getWaypoint2AtsWaypointDescriptionCode3()),
        () -> assertEquals("N19105726", airTrafficService.getWaypoint2GeodeticLatitude()),
        () -> assertEquals(19.182572, airTrafficService.getWaypoint2DegreesLatitude()),
        () -> assertEquals("E030253822", airTrafficService.getWaypoint2GeodeticLongitude()),
        () -> assertEquals(30.427283, airTrafficService.getWaypoint2DegreesLongitude()),
        () -> assertEquals("99.0", airTrafficService.getAtsRouteOutboundMagneticCourse()),
        () -> assertEquals(311.0, airTrafficService.getAtsRouteDistance()),
        () -> assertEquals("99.0", airTrafficService.getAtsRouteInboundMagneticCourse()),
        () -> assertEquals("FL55", airTrafficService.getMinimumAltitude()),
        () -> assertEquals("FL285", airTrafficService.getUpperLimit()),
        () -> assertEquals("FL55", airTrafficService.getLowerLimit()),
        () -> assertEquals(201805, airTrafficService.getCycleDate())
    );

  }
}
