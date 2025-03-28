package org.mitre.tdp.boogie.arinc.v22;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;

public class TestAirwayLegSpec {
  static ArincRecordParser parser = ArincRecordParser.standard(new AirwayLegSpec());
  static AirwayLegConverter converter = new AirwayLegConverter();
  static String US_ER = "SUSAER       A216        0060FACEDPGEA0E C PRH RR                     352329943600 FL180     FL600                        N399282405";

  @Test
  void testParse() {
    ArincAirwayLeg leg = parser.parse(US_ER).flatMap(converter).orElseThrow();
    assertAll(
        () -> assertTrue(leg.routeTypeQualifier1().isEmpty()),
        () -> assertTrue(leg.routeTypeQualifier2().isEmpty()),
        () -> assertEquals("N", leg.routeTypeQualifier3().orElseThrow(), "non pbn leg in a pbn route, yay")
    );
  }
}
