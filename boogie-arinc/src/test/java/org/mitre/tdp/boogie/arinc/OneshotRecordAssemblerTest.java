package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.DemotedException;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;

@Tag("INTEGRATION")
class OneshotRecordAssemblerTest {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));

  @Test
  void testParse() {

    OneshotRecordParser.ClientRecords<Airport, Fix, Airway, Procedure> records;

    try (FileInputStream fis = new FileInputStream(arincTestFile)) {
      records = OneshotRecordParser.standard(ArincVersion.V19).assembleFrom(fis);
    } catch (IOException e) {
      throw DemotedException.demote("Exception opening and parsing 424 file: " + arincTestFile, e);
    }

    assertAll(
        () -> assertEquals(358, records.airports().size(), "Airports"),
        () -> assertEquals(1293, records.fixes().size(), "Fixes"),
        () -> assertEquals(204, records.airways().size(), "Airways"),
        () -> assertEquals(1436, records.procedures().size(), "Procedures")
    );
  }
}
