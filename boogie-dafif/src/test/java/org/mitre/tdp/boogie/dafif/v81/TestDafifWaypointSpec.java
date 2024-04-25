package org.mitre.tdp.boogie.dafif.v81;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifWaypointConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifWaypointValidator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDafifWaypointSpec {
  private static final DafifRecordParser parser = DafifRecordParser.all();

  private static final DafifWaypointValidator validator = new DafifWaypointValidator();

  private static final DafifWaypointConverter converter = new DafifWaypointConverter();

  public static final String RAW_WAYPOINT = "02PAR\tGR\t\t\tI\t(PAR 245.0/002.0 HOST)\tLGRP\tT\t\t\t0342\tWGE\tWGE\tN36231860\t36.388500\tE028013490\t28.026361\tE005209 0124\t\t\t\t\t202313\t\t\t0\n";

  @Test
  void testParseWaypoint() {
    DafifRecord record = parser.parse(DafifRecordType.WPT, RAW_WAYPOINT).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals("02PAR", record.requiredField("waypointIdentifierWptIdent")),
        () -> assertEquals("GR", record.requiredField("countryCode")),
        () -> assertFalse(record.optionalField("stateProvinceCode").isPresent()),
        () -> assertFalse(record.optionalField("waypointPointNavaidFlag").isPresent()),
        () -> assertEquals("I", record.requiredField("waypointType")),
        () -> assertEquals("(PAR 245.0/002.0 HOST)", record.requiredField("waypointDescriptionName")),
        () -> assertEquals("LGRP", record.requiredField("icaoCode")),
        () -> assertEquals("T", record.requiredField("waypointUsageCode")),
        () -> assertFalse(record.optionalField("waypointBearing").isPresent()),
        () -> assertFalse(record.optionalField("distance").isPresent()),
        () -> assertEquals("0342", record.requiredField("wAC")),
        () -> assertEquals("WGE", record.requiredField("localHorizontalDatum")),
        () -> assertEquals("WGE", record.requiredField("geodeticDatum")),
        () -> assertEquals("N36231860", record.requiredField("geodeticLatitude")),
        () -> assertEquals(36.388500, record.requiredField("degreesLatitude")),
        () -> assertEquals("E028013490", record.requiredField("geodeticLongitude")),
        () -> assertEquals(28.026361, record.requiredField("degreesLongitude")),
        () -> assertEquals("E005209 0124", record.requiredField("magneticVariation")),
        () -> assertFalse(record.optionalField("navaidIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("navaidType").isPresent()),
        () -> assertFalse(record.optionalField("navaidCountryCode").isPresent()),
        () -> assertFalse(record.optionalField("navaidKeyCode").isPresent()),
        () -> assertEquals(Integer.valueOf(202313), record.requiredField("cycleDate")),
        () -> assertFalse(record.optionalField("waypointRunwayIdent").isPresent()),
        () -> assertFalse(record.optionalField("waypointRwyIcao").isPresent()),
        () -> assertEquals(Integer.valueOf(0), record.requiredField("coordinatePrecision"))
    );

    assertTrue(validator.test(record));

    DafifWaypoint waypoint = converter.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals("02PAR", waypoint.getWaypointIdentifierWptIdent()),
        () -> assertEquals("GR", waypoint.getCountryCode()),
        () -> assertEquals("I", waypoint.getWaypointType()),
        () -> assertEquals("(PAR 245.0/002.0 HOST)", waypoint.getWaypointDescriptionName()),
        () -> assertEquals("LGRP", waypoint.getIcaoCode()),
        () -> assertEquals("T", waypoint.getWaypointUsageCode()),
        () -> assertEquals("0342", waypoint.getWac()),
        () -> assertEquals("WGE", waypoint.getLocalHorizontalDatum()),
        () -> assertEquals("WGE", waypoint.getGeodeticDatum()),
        () -> assertEquals("N36231860", waypoint.getGeodeticLatitude()),
        () -> assertEquals(36.388500, waypoint.getDegreesLatitude()),
        () -> assertEquals("E028013490", waypoint.getGeodeticLongitude()),
        () -> assertEquals(28.026361, waypoint.getDegreesLongitude()),
        () -> assertEquals("E005209 0124", waypoint.getMagneticVariation()),
        () -> assertEquals(202313, waypoint.getCycleDate()),
        () -> assertEquals(0, waypoint.getCoordinatePrecision())
    );
  }
}
