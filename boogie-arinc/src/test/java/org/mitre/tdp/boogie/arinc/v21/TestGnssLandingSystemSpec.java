package org.mitre.tdp.boogie.arinc.v21;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;

public class TestGnssLandingSystemSpec {
  static ArincRecordParser parser = ArincRecordParser.standard(new GnssLandingSystemSpec());
  static GnssLandingSystemConverter converter = new GnssLandingSystemConverter();
  static String PT = "SEEUP UEEEUETG05A1   020665RW05R                   0520N62051670E129450240UEEE         317W0150  00323WGE     00323053     626262210";
  @Test
  void parse() {
    ArincRecord record = parser.parse(PT).orElseThrow();
    assertEquals(Integer.valueOf(53), record.requiredField("glidePathTch"));
  }
  @Test
  void testConvert() {
    ArincGnssLandingSystem system = parser.parse(PT).flatMap(converter).orElseThrow();
    assertEquals(53, system.glidePathTCH().get());
  }

}
