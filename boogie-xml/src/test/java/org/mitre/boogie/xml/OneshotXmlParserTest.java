package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.util.DemotedException;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Runway;

class OneshotXmlParserTest {

  private static final File xmlTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/v23_4/gibberish-sample.xml"));

  @Test
  void testParse() {
    OneshotXmlParser.ClientRecords<Airport, Runway, Fix, Airway, Procedure, Helipad, Heliport> records;

    try (FileInputStream fis = new FileInputStream(xmlTestFile)) {
      records = OneshotXmlParser.standard(ArincXmlVersion.V23_4).assembleFrom(fis);
    } catch (IOException e) {
      throw DemotedException.demote("Exception opening and parsing XML file: " + xmlTestFile, e);
    }

    assertAll(
        () -> assertEquals(5, records.airports().size(), "Airports"),
        () -> assertEquals(13, records.fixes().size(), "Fixes"),
        () -> assertEquals(5, records.airways().size(), "Airways"),
        () -> assertEquals(75, records.procedures().size(), "Procedures"),
        () -> assertEquals(5, records.heliports().size(), "Heliports"),
        () -> assertNotNull(records.fixDatabase()),
        () -> assertNotNull(records.terminalAreaDatabase())
    );
  }
}
