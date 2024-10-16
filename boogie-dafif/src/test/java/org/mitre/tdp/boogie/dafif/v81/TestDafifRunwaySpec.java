package org.mitre.tdp.boogie.dafif.v81;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifRunwayConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifRunwaySpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifRunwayValidator;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDafifRunwaySpec {

  private static final DafifRecordParser parser = DafifRecordParser.standard(new DafifRunwaySpec());

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
        () -> assertEquals("AA30079", runway.airportIdentification()),
        () -> assertEquals("29", runway.highEndIdentifier()),
        () -> assertEquals("11", runway.lowEndIdentifier()),
        () -> assertEquals(294.3, runway.highEndMagneticHeading().orElseThrow()),
        () -> assertEquals(114.3, runway.lowEndMagneticHeading().orElseThrow()),
        () -> assertEquals(9278, runway.length()),
        () -> assertEquals(148, runway.width()),
        () -> assertEquals("ASP", runway.surface().orElseThrow()),
        () -> assertEquals("068FAWT", runway.pavementClassificationNumber().orElseThrow()),
        () -> assertEquals("N12295445", runway.highEndGeodeticLatitude().orElseThrow()),
        () -> assertEquals(12.498458, runway.highEndDegreesLatitude().orElseThrow()),
        () -> assertEquals("W070001068", runway.highEndGeodeticLongitude().orElseThrow()),
        () -> assertEquals(-70.002967, runway.highEndDegreesLongitude().orElseThrow()),
        () -> assertEquals("62.0", runway.highEndElevation().orElseThrow()),
        () -> assertEquals("-0.5", runway.highEndSlope()),
        () -> assertEquals("U", runway.highEndTDZE().orElseThrow()),
        () -> assertEquals(82, runway.highEndDisplacedThreshold().orElseThrow()),
        () -> assertEquals("62.0", runway.highEndDisplacedThresholdElevation().orElseThrow()),
        () -> assertEquals(List.of(12, 5, 50), runway.highEndLightingSystem().orElseThrow()),
        () -> assertEquals("N12301614", runway.lowEndGeodeticLatitude().orElseThrow()),
        () -> assertEquals(12.504483, runway.lowEndDegreesLatitude().orElseThrow()),
        () -> assertEquals("W070014168", runway.lowEndGeodeticLongitude().orElseThrow()),
        () -> assertEquals(-70.028244, runway.lowEndDegreesLongitude().orElseThrow()),
        () -> assertEquals("12.0", runway.lowEndElevation().orElseThrow()),
        () -> assertEquals("0.5", runway.lowEndSlope()),
        () -> assertEquals("U", runway.lowEndTDZE().orElseThrow()),
        () -> assertEquals(275, runway.lowEndDisplacedThreshold().orElseThrow()),
        () -> assertEquals("12.0", runway.lowEndDisplacedThresholdElevation().orElseThrow()),
        () -> assertEquals(List.of(12, 5, 50), runway.lowEndLightingSystem().orElseThrow()),
        () -> assertEquals(283.6, runway.trueHeadingHighEnd()),
        () -> assertEquals(103.6, runway.trueHeadingLowEnd()),
        () -> assertEquals(Optional.empty(), runway.usableRunway()),
        () -> assertEquals(8921, runway.highEndLandingDistance().orElseThrow()),
        () -> assertEquals(9003, runway.highEndRunwayDistance().orElseThrow()),
        () -> assertEquals(9003, runway.highEndTakeOffDistance().orElseThrow()),
        () -> assertEquals(9003, runway.highEndAccelerateStopDistance().orElseThrow()),
        () -> assertEquals(8921, runway.lowEndLandingDistance().orElseThrow()),
        () -> assertEquals(9196, runway.lowEndRunwayDistance().orElseThrow()),
        () -> assertEquals(9196, runway.lowEndTakeOffDistance().orElseThrow()),
        () -> assertEquals(9196, runway.lowEndAccelerateStopDistance().orElseThrow()),
        () -> assertEquals(202106, runway.cycleDate()),
        () -> assertEquals(0, runway.coordinatePrecision().orElseThrow())
    );
  }

}
