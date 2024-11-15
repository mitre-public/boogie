package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegSpec;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeSpec;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegSpec;
import org.mitre.tdp.boogie.arinc.v18.RunwaySpec;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.WaypointSpec;

/**
 * Simple parser test to ensure we can parse records from a logic file with our configured set of {@link RecordSpec}s.
 */
class TestArincFileParser {

  // statically configure V18 parsers - as that's the format of the sample data
  private static final ArincFileParser fileParser = new ArincFileParser(
      ArincRecordParser.standard(
          new AirportSpec(),
          new AirwayLegSpec(),
          new LocalizerGlideSlopeSpec(),
          new NdbNavaidSpec(),
          new ProcedureLegSpec(),
          new RunwaySpec(),
          new VhfNavaidSpec(),
          new WaypointSpec()
      ));

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-kjfk-v18.txt"));

  @Test
  void testParserOnAll_KJFK_RelatedRecords() {
    Collection<ArincRecord> records = fileParser.apply(arincTestFile);
    assertEquals(546, records.size(), "Expected 558 records given the explicitly configured list of specs in the test parser. Except for the 12 continuation records that should not parse into their primary record models");
  } //7 jfk runway continuations // 5 procedure legs with a continuation
}
