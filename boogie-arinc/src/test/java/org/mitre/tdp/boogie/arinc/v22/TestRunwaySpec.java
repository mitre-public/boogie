package org.mitre.tdp.boogie.arinc.v22;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;

public class TestRunwaySpec {
  static ArincRecordParser parser = ArincRecordParser.standard(new RunwaySpec());
  static RunwayConverter converter = new RunwayConverter();
  static String rawNoCoords = "SAFRP DBBNDBGRW04    1039370350                                   014240000  148D     0197     050                         350332310";
  static String withAccuracy = "SUSAP KDCAK6GRW04    1050000370 N38502984W077022815               000120200  150D     0000     050YN                       431982210";

  @Test
  public void testNoCoords() {
    ArincRecord record = parser.parse(rawNoCoords).orElseThrow();
    Optional<ArincRunway> runway = converter.apply(record);
    assertAll(
        () -> assertFalse(record.containsParsedField("runwayAccuracyComplianceFlag")),
        () -> assertFalse(record.containsParsedField("landingThresholdAccuracyComplianceFlag")),
        () -> assertTrue(runway.isEmpty())
    );
  }

  @Test
  public void testWithAccuracy() {
    ArincRecord record = parser.parse(withAccuracy).orElseThrow();
    ArincRunway runway = converter.apply(record).orElseThrow();
    ArincRunway rebuild = runway.toBuilder().build();
    assertAll(
        () -> assertTrue(record.containsParsedField("runwayAccuracyComplianceFlag")),
        () -> assertTrue(record.containsParsedField("landingThresholdElevationAccuracyComplianceFlag")),
        () -> assertEquals("Y", runway.runwayAccuracyComplianceFlag().orElseThrow()),
        () -> assertEquals("N", runway.landingThresholdElevationComplianceFlag().orElseThrow()),
        () -> assertEquals("Y", rebuild.runwayAccuracyComplianceFlag().orElseThrow()),
        () -> assertEquals("N", rebuild.landingThresholdElevationComplianceFlag().orElseThrow())
    );
  }
}
