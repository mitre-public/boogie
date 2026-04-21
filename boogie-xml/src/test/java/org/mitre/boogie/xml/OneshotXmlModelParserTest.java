package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.caasd.commons.util.DemotedException;

class OneshotXmlModelParserTest {

  private static final File xmlTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/v23_4/gibberish-sample.xml"));

  @Test
  void testParse_returnsRawModelRecords() {
    ArincRecords records;

    try (FileInputStream fis = new FileInputStream(xmlTestFile)) {
      records = OneshotXmlModelParser.standard(ArincXmlVersion.V23_4).parseFrom(fis);
    } catch (IOException e) {
      throw DemotedException.demote("Exception opening and parsing XML file: " + xmlTestFile, e);
    }

    assertAll(
        () -> assertEquals(5, records.airports().size(), "Airports"),
        () -> assertEquals(5, records.heliports().size(), "Heliports"),
        () -> assertFalse(records.waypoints().isEmpty(), "Waypoints"),
        () -> assertFalse(records.arincAirways().isEmpty(), "Airways"),
        () -> assertFalse(records.ndbNavaids().isEmpty() && records.vhfNavaids().isEmpty(), "Navaids")
    );
  }

  @Test
  void testParse_airportsContainNestedProcedures() {
    ArincRecords records;

    try (FileInputStream fis = new FileInputStream(xmlTestFile)) {
      records = OneshotXmlModelParser.standard(ArincXmlVersion.V23_4).parseFrom(fis);
    } catch (IOException e) {
      throw DemotedException.demote("Exception opening and parsing XML file: " + xmlTestFile, e);
    }

    long totalProcedures = records.airports().stream()
        .mapToLong(a -> a.portInfo().procedures().orElse(java.util.List.of()).size())
        .sum();

    assertAll(
        () -> assertEquals(75, totalProcedures, "Total procedures nested in airports"),
        () -> assertEquals(5, records.arincAirways().size(), "Airways")
    );
  }
}
