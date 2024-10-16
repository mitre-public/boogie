package org.mitre.tdp.boogie.dafif.v81;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordParser;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifAddRunwayConverter;
import org.mitre.tdp.boogie.dafif.v81.spec.DafifAddRuwaySpec;
import org.mitre.tdp.boogie.dafif.v81.validator.DafifAddRunwayValidator;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestDafifAddRunwaySpec {

  private static final DafifRecordParser parser = DafifRecordParser.standard(new DafifAddRuwaySpec());

  private static final DafifAddRunwayValidator validator = new DafifAddRunwayValidator();
  private static final DafifAddRunwayConverter converter = new DafifAddRunwayConverter();

  public static final String RAW_ADD_RUNWAY = "AA30079\t29\t11\tTNCA\tN12295464\t12.498511\tW070001148\t-70.003189\t185\tASP\tN12301657\t12.504604\tW070014349\t-70.028749\tN12301550\t12.504306\tW070013898\t-70.027494\t170\tASP\tN12295405\t12.498347\tW070000901\t-70.002503\t201901\n";

  @Test
  void testParseDafifAirport() {
    DafifRecord record = parser.parse(DafifRecordType.ADD_RWY, RAW_ADD_RUNWAY).orElseThrow(AssertionError::new);
    assertNotNull(record);

    assertAll(
        () -> assertEquals("AA30079", record.requiredField("airportIdentification")),
        () -> assertEquals("29", record.requiredField("highEndRunwayIdentifier")),
        () -> assertEquals("11", record.requiredField("lowEndRunwayIdentifier")),
        () -> assertEquals("TNCA", record.requiredField("icaoCode")),
        () -> assertEquals("N12295464", record.requiredField("highEndDisplacedThresholdGeodeticLatitude")),
        () -> assertEquals(12.498511, record.requiredField("highEndDisplacedThresholdDegreesLatitude")),
        () -> assertEquals("W070001148", record.requiredField("highEndDisplacedThresholdGeodeticLongitude")),
        () -> assertEquals(-70.003189, record.requiredField("highEndDisplacedThresholdDegreesLongitude")),
        () -> assertEquals(Integer.valueOf(185), record.requiredField("highEndOverrunDistance")),
        () -> assertEquals("ASP", record.requiredField("highEndOverrunSurface")),
        () -> assertEquals("N12301657", record.requiredField("highEndOverrunGeodeticLatitude")),
        () -> assertEquals(12.504604, record.requiredField("highEndOverrunDegreesLatitude")),
        () -> assertEquals("W070014349", record.requiredField("highEndOverrunGeodeticLongitude")),
        () -> assertEquals(-70.028749, record.requiredField("highEndOverrunDegreesLongitude")),
        () -> assertEquals("N12301550", record.requiredField("lowEndDisplacedThresholdGeodeticLatitude")),
        () -> assertEquals(12.504306, record.requiredField("lowEndDisplacedThresholdDegreesLatitude")),
        () -> assertEquals("W070013898", record.requiredField("lowEndDisplacedThresholdGeodeticLongitude")),
        () -> assertEquals(-70.027494, record.requiredField("lowEndDisplacedThresholdDegreesLongitude")),
        () -> assertEquals(Integer.valueOf(170), record.requiredField("lowEndOverrunDistance")),
        () -> assertEquals("ASP", record.requiredField("lowEndOverrunSurface")),
        () -> assertEquals("N12295405", record.requiredField("lowEndOverrunGeodeticLatitude")),
        () -> assertEquals(12.498347, record.requiredField("lowEndOverrunDegreesLatitude")),
        () -> assertEquals("W070000901", record.requiredField("lowEndOverrunGeodeticLongitude")),
        () -> assertEquals(-70.002503, record.requiredField("lowEndOverrunDegreesLongitude")),
        () -> assertEquals(Integer.valueOf(201901), record.requiredField("cycleDate"))
    );


    assertTrue(validator.test(record));

    DafifAddRunway addRunway = converter.apply(record).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals("AA30079", addRunway.airportIdentification()),
        () -> assertEquals("29", addRunway.highEndRunwayIdentifier()),
        () -> assertEquals("11", addRunway.lowEndRunwayIdentifier()),
        () -> assertEquals("TNCA", addRunway.icaoCode()),
        () -> assertEquals("N12295464", addRunway.highEndDisplacedThresholdGeodeticLatitude().orElseThrow()),
        () -> assertEquals(12.498511, addRunway.highEndDisplacedThresholdDegreesLatitude().orElseThrow()),
        () -> assertEquals("W070001148", addRunway.highEndDisplacedThresholdGeodeticLongitude().orElseThrow()),
        () -> assertEquals(-70.003189, addRunway.highEndDisplacedThresholdDegreesLongitude().orElseThrow()),
        () -> assertEquals(185, addRunway.highEndOverrunDistance().orElseThrow()),
        () -> assertEquals("ASP", addRunway.highEndOverrunSurface()),
        () -> assertEquals("N12301657", addRunway.highEndOverrunGeodeticLatitude().orElseThrow()),
        () -> assertEquals(12.504604, addRunway.highEndOverrunDegreesLatitude().orElseThrow()),
        () -> assertEquals("W070014349", addRunway.highEndOverrunGeodeticLongitude().orElseThrow()),
        () -> assertEquals(-70.028749, addRunway.highEndOverrunDegreesLongitude().orElseThrow()),
        () -> assertEquals("N12301550", addRunway.lowEndDisplacedThresholdGeodeticLatitude().orElseThrow()),
        () -> assertEquals(12.504306, addRunway.lowEndDisplacedThresholdDegreesLatitude().orElseThrow()),
        () -> assertEquals("W070013898", addRunway.lowEndDisplacedThresholdGeodeticLongitude().orElseThrow()),
        () -> assertEquals(-70.027494, addRunway.lowEndDisplacedThresholdDegreesLongitude().orElseThrow()),
        () -> assertEquals(170, addRunway.lowEndOverrunDistance().orElseThrow()),
        () -> assertEquals("ASP", addRunway.lowEndOverrunSurface()),
        () -> assertEquals("N12295405", addRunway.lowEndOverrunGeodeticLatitude().orElseThrow()),
        () -> assertEquals(12.498347, addRunway.lowEndOverrunDegreesLatitude().orElseThrow()),
        () -> assertEquals("W070000901", addRunway.lowEndOverrunGeodeticLongitude().orElseThrow()),
        () -> assertEquals(-70.002503, addRunway.lowEndOverrunDegreesLongitude().orElseThrow()),
        () -> assertEquals(201901, addRunway.cycleDate())
    );
  }
}
