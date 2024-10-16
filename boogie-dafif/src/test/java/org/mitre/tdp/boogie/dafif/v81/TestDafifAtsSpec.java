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
        () -> assertEquals("1AW1", airTrafficService.atsIdentifier()),
        () -> assertEquals(10, airTrafficService.atsRouteSequenceNumber()),
        () -> assertEquals("E", airTrafficService.atsRouteDirection()),
        () -> assertEquals("D", airTrafficService.atsRouteType()),
        () -> assertEquals("HSSS", airTrafficService.icaoCode()),
        () -> assertEquals("Y", airTrafficService.biDirectional().orElseThrow()),
        () -> assertEquals("A", airTrafficService.frequencyClass()),
        () -> assertEquals("L", airTrafficService.level()),
        () -> assertEquals("O", airTrafficService.atsRouteStatus()),
        () -> assertEquals("HLLL", airTrafficService.waypoint1IcaoCode()),
        () -> assertEquals("ORNAT", airTrafficService.waypoint1WaypointIdentifierWptIdent()),
        () -> assertEquals("LY", airTrafficService.waypoint1CountryCode()),
        () -> assertEquals("E", airTrafficService.waypoint1AtsWaypointDescriptionCode1()),
        () -> assertEquals("C", airTrafficService.waypoint1AtsWaypointDescriptionCode3().orElseThrow()),
        () -> assertEquals("N20000000", airTrafficService.waypoint1GeodeticLatitude().orElseThrow()),
        () -> assertEquals(20.000000, airTrafficService.waypoint1DegreesLatitude().orElseThrow()),
        () -> assertEquals("E025000000", airTrafficService.waypoint1GeodeticLongitude().orElseThrow()),
        () -> assertEquals(25.000000, airTrafficService.waypoint1DegreesLongitude().orElseThrow()),
        () -> assertEquals("HSSS", airTrafficService.waypoint2IcaoCode()),
        () -> assertEquals(4, airTrafficService.waypoint2NavaidType().orElseThrow()),
        () -> assertEquals("DOG", airTrafficService.waypoint2WaypointIdentifierWptIdent()),
        () -> assertEquals("SU", airTrafficService.waypoint2CountryCode()),
        () -> assertEquals("V", airTrafficService.waypoint2AtsWaypointDescriptionCode1()),
        () -> assertEquals("C", airTrafficService.waypoint2AtsWaypointDescriptionCode3().orElseThrow()),
        () -> assertEquals("N19105726", airTrafficService.waypoint2GeodeticLatitude().orElseThrow()),
        () -> assertEquals(19.182572, airTrafficService.waypoint2DegreesLatitude().orElseThrow()),
        () -> assertEquals("E030253822", airTrafficService.waypoint2GeodeticLongitude().orElseThrow()),
        () -> assertEquals(30.427283, airTrafficService.waypoint2DegreesLongitude().orElseThrow()),
        () -> assertEquals("99.0", airTrafficService.atsRouteOutboundMagneticCourse().orElseThrow()),
        () -> assertEquals(311.0, airTrafficService.atsRouteDistance().orElseThrow()),
        () -> assertEquals("99.0", airTrafficService.atsRouteInboundMagneticCourse().orElseThrow()),
        () -> assertEquals("FL55", airTrafficService.minimumAltitude().orElseThrow()),
        () -> assertEquals("FL285", airTrafficService.upperLimit().orElseThrow()),
        () -> assertEquals("FL55", airTrafficService.lowerLimit().orElseThrow()),
        () -> assertEquals(201805, airTrafficService.cycleDate().orElseThrow())
    );

  }
}
