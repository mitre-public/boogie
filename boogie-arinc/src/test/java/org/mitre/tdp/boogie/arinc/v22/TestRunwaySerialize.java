package org.mitre.tdp.boogie.arinc.v22;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;

public class TestRunwaySerialize {
  static String rawNoCoords = "SAFRP DBBNDBGRW04    1039370350                                   014240000  148D     0197     050                         350332310";
  static String withAccuracy = "SUSAP KDCAK6GRW04    1050000370 N38502984W077022815               000120200  150D     0000     050YY                       431982210";
  static ArincRecordParser parser = ArincRecordParser.standard(new RunwaySpec());
  static RunwayConverter converter = new RunwayConverter();
  @Test
  void testSerialize() {
    ArincRunway runway = parser.parse(withAccuracy).flatMap(converter).orElseThrow();



  }

}
