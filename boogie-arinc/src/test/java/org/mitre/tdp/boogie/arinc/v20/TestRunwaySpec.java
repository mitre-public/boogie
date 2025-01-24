package org.mitre.tdp.boogie.arinc.v20;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.v18.RunwayConverter;
import org.mitre.tdp.boogie.arinc.v18.RunwayValidator;

public class TestRunwaySpec {
  static ArincRecordParser parser = ArincRecordParser.standard(new RunwaySpec());
  static RunwayConverter converter = new RunwayConverter();
  static String RAW = "SUSAP KDCAK6GRW01    0071690060 N38502965W077021229               000110000  150I     0000     154                         615342210";
  static String ERROR = "SUSAP KDCAK6GRW01    0071690060 N38502965W077021229               000110000  150I     0000     1R4                         615342210";

  @Test
  void testSpecMatchesRunwayRecord1() {
    assertTrue(new org.mitre.tdp.boogie.arinc.v18.RunwaySpec().matchesRecord(RAW));
  }

  @Test
  void testValidatorPasses_Runway1(){
    assertTrue(new RunwayValidator().test(parser.parse(RAW).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseRunway1() {
    ArincRecord record = parser.parse(RAW).orElseThrow(AssertionError::new);
    assertEquals(Integer.valueOf(154), record.requiredField("thresholdCrossingHeight"), "field became 3 digits long");
  }

  @Test
  void convert() {
    ArincRunway runway = parser.parse(RAW).flatMap(converter).orElseThrow();
    assertEquals(154, runway.thresholdCrossingHeight().orElseThrow());
  }

  @Test
  void robust() {
    ArincRecord record = parser.parse(ERROR).orElseThrow(AssertionError::new);
    assertFalse(record.containsParsedField("thresholdCrossingHeight"));
  }
}
