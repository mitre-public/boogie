package org.mitre.tdp.boogie.dafif.v81;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifRunwayConverter;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifRunwayValidator;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDafifRunwaySpec {

  private static final DafifRecordParser parser = DafifRecordParser.all();

  private static final DafifRunwayValidator validator = new DafifRunwayValidator();

  private static final DafifRunwayConverter converter = new DafifRunwayConverter();
  public static final String RAW_RUNWAY = "AA30079\t29\t11\t294.3\t114.3\t09278\t00148\tASP\t068FAWT\tN12295445\t12.498458\tW070001068\t-70.002967\t62.0\t-0.5\tU\t0082\t62.0\t12\t05\t50\t\t\t\t\t\tN12301614\t12.504483\tW070014168\t-70.028244\t12.0\t0.5\tU\t0275\t12.0\t12\t05\t50\t\t\t\t\t\t283.6\t103.6\t\t08921\t09003\t09003\t09003\t08921\t09196\t09196\t09196\t202106\t0\n";

  @Test
  void testParseDafifRunway() {
    DafifRecord record = parser.parse(DafifRecordType.RWY, RAW_RUNWAY).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals("AA30079", record.requiredField("airportIdentification")),
        () -> assertEquals("29", record.requiredField("highEndIdentifier")),
        () -> assertEquals("11", record.requiredField("lowEndIdentifier")),
        () -> assertEquals(294.3, record.requiredField("highEndMagneticHeading")),
        () -> assertEquals(114.3, record.requiredField("lowEndMagneticHeading")),
        () -> assertEquals(Integer.valueOf(9278), record.requiredField("length")),
        () -> assertEquals(Integer.valueOf(148), record.requiredField("width")),
        () -> assertEquals("ASP", record.requiredField("surface")),
        () -> assertEquals("068FAWT", record.requiredField("pavementClassificationNumber")),
        () -> assertEquals("N12295445", record.requiredField("highEndGeodeticLatitude")),
        () -> assertEquals(12.498458, record.requiredField("highEndDegreesLatitude")),
        () -> assertEquals("W070001068", record.requiredField("highEndGeodeticLongitude")),
        () -> assertEquals(-70.002967, record.requiredField("highEndDegreesLongitude")),
        () -> assertEquals("62.0", record.requiredField("highEndElevation")),
        () -> assertEquals("-0.5", record.requiredField("highEndSlope")),
        () -> assertEquals("U", record.requiredField("highEndTDZE")),
        () -> assertEquals(Integer.valueOf(82), record.requiredField("highEndDisplacedThreshold")),
        () -> assertEquals("62.0", record.requiredField("highEndDisplacedThresholdElevation")),
        () -> assertEquals(Integer.valueOf(12), record.requiredField("highEndLightingSystem1")),
        () -> assertEquals(Integer.valueOf(5), record.requiredField("highEndLightingSystem2")),
        () -> assertEquals(Integer.valueOf(50), record.requiredField("highEndLightingSystem3")),
        () -> assertFalse(record.optionalField("highEndLightingSystem4").isPresent()),
        () -> assertFalse(record.optionalField("highEndLightingSystem5").isPresent()),
        () -> assertFalse(record.optionalField("highEndLightingSystem6").isPresent()),
        () -> assertFalse(record.optionalField("highEndLightingSystem7").isPresent()),
        () -> assertFalse(record.optionalField("highEndLightingSystem8").isPresent()),
        () -> assertEquals("N12301614", record.requiredField("lowEndGeodeticLatitude")),
        () -> assertEquals(12.504483, record.requiredField("lowEndDegreesLatitude")),
        () -> assertEquals("W070014168", record.requiredField("lowEndGeodeticLongitude")),
        () -> assertEquals(-70.028244, record.requiredField("lowEndDegreesLongitude")),
        () -> assertEquals("12.0", record.requiredField("lowEndElevation")),
        () -> assertEquals("0.5", record.requiredField("lowEndSlope")),
        () -> assertEquals("U", record.requiredField("lowEndTDZE")),
        () -> assertEquals(Integer.valueOf(275), record.requiredField("lowEndDisplacedThreshold")),
        () -> assertEquals("12.0", record.requiredField("lowEndDisplacedThresholdElevation")),
        () -> assertEquals(Integer.valueOf(12), record.requiredField("lowEndLightingSystem1")),
        () -> assertEquals(Integer.valueOf(5), record.requiredField("lowEndLightingSystem2")),
        () -> assertEquals(Integer.valueOf(50), record.requiredField("lowEndLightingSystem3")),
        () -> assertFalse(record.optionalField("lowEndLightingSystem4").isPresent()),
        () -> assertFalse(record.optionalField("lowEndLightingSystem5").isPresent()),
        () -> assertFalse(record.optionalField("lowEndLightingSystem6").isPresent()),
        () -> assertFalse(record.optionalField("lowEndLightingSystem7").isPresent()),
        () -> assertFalse(record.optionalField("lowEndLightingSystem8").isPresent()),
        () -> assertEquals(283.6, record.requiredField("trueHeadingHighEnd")),
        () -> assertEquals(103.6, record.requiredField("trueHeadingLowEnd")),
        () -> assertFalse(record.optionalField("usableRunway").isPresent()),
        () -> assertEquals(Integer.valueOf(8921), record.requiredField("highEndLandingDistance")),
        () -> assertEquals(Integer.valueOf(9003), record.requiredField("highEndTakeoffRunwayDistance")),
        () -> assertEquals(Integer.valueOf(9003), record.requiredField("highEndTakeOffDistance")),
        () -> assertEquals(Integer.valueOf(9003), record.requiredField("highEndAccelerateStopDistance")),
        () -> assertEquals(Integer.valueOf(8921), record.requiredField("lowEndLandingDistance")),
        () -> assertEquals(Integer.valueOf(9196), record.requiredField("lowEndTakeoffRunwayDistance")),
        () -> assertEquals(Integer.valueOf(9196), record.requiredField("lowEndTakeOffDistance")),
        () -> assertEquals(Integer.valueOf(9196), record.requiredField("lowEndAccelerateStopDistance")),
        () -> assertEquals(Integer.valueOf(202106), record.requiredField("cycleDate")),
        () -> assertEquals(Integer.valueOf(0), record.requiredField("coordinatePrecision"))
    );

    assertTrue(validator.test(record));

    DafifRunway runway = converter.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals("AA30079", runway.getAirportIdentification()),
        () -> assertEquals("29", runway.getHighEndIdentifier()),
        () -> assertEquals("11", runway.getLowEndIdentifier()),
        () -> assertEquals(294.3, runway.getHighEndMagneticHeading()),
        () -> assertEquals(114.3, runway.getLowEndMagneticHeading()),
        () -> assertEquals(9278, runway.getLength()),
        () -> assertEquals(148, runway.getWidth()),
        () -> assertEquals("ASP", runway.getSurface()),
        () -> assertEquals("068FAWT", runway.getPavementClassificationNumber()),
        () -> assertEquals("N12295445", runway.getHighEndGeodeticLatitude()),
        () -> assertEquals(12.498458, runway.getHighEndDegreesLatitude()),
        () -> assertEquals("W070001068", runway.getHighEndGeodeticLongitude()),
        () -> assertEquals(-70.002967, runway.getHighEndDegreesLongitude()),
        () -> assertEquals("62.0", runway.getHighEndElevation()),
        () -> assertEquals("-0.5", runway.getHighEndSlope()),
        () -> assertEquals("U", runway.getHighEndTDZE()),
        () -> assertEquals(82, runway.getHighEndDisplacedThreshold()),
        () -> assertEquals("62.0", runway.getHighEndDisplacedThresholdElevation()),
        () -> assertEquals(List.of(12, 5, 50), runway.getHighEndLightingSystem()),
        () -> assertEquals("N12301614", runway.getLowEndGeodeticLatitude()),
        () -> assertEquals(12.504483, runway.getLowEndDegreesLatitude()),
        () -> assertEquals("W070014168", runway.getLowEndGeodeticLongitude()),
        () -> assertEquals(-70.028244, runway.getLowEndDegreesLongitude()),
        () -> assertEquals("12.0", runway.getLowEndElevation()),
        () -> assertEquals("0.5", runway.getLowEndSlope()),
        () -> assertEquals("U", runway.getLowEndTDZE()),
        () -> assertEquals(275, runway.getLowEndDisplacedThreshold()),
        () -> assertEquals("12.0", runway.getLowEndDisplacedThresholdElevation()),
        () -> assertEquals(List.of(12, 5, 50), runway.getLowEndLightingSystem()),
        () -> assertEquals(283.6, runway.getTrueHeadingHighEnd()),
        () -> assertEquals(103.6, runway.getTrueHeadingLowEnd()),
        () -> assertNull(runway.getUsableRunway()),
        () -> assertEquals(8921, runway.getHighEndLandingDistance()),
        () -> assertEquals(9003, runway.getHighEndRunwayDistance()),
        () -> assertEquals(9003, runway.getHighEndTakeOffDistance()),
        () -> assertEquals(9003, runway.getHighEndAccelerateStopDistance()),
        () -> assertEquals(8921, runway.getLowEndLandingDistance()),
        () -> assertEquals(9196, runway.getLowEndRunwayDistance()),
        () -> assertEquals(9196, runway.getLowEndTakeOffDistance()),
        () -> assertEquals(9196, runway.getLowEndAccelerateStopDistance()),
        () -> assertEquals(202106, runway.getCycleDate()),
        () -> assertEquals(0, runway.getCoordinatePrecision())
    );
  }

}
