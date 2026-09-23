package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.caasd.commons.util.DemotedException;

class OneshotXmlModelParserTest {

  private static final File xmlTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/v23_4/parser-sample.xml"));

  @Test
  void testParse_returnsRawModelRecords() {
    ArincRecords records;

    try (FileInputStream fis = new FileInputStream(xmlTestFile)) {
      records = OneshotXmlModelParser.standard(ArincXmlVersion.V23_4).parseFrom(fis);
    } catch (IOException e) {
      throw DemotedException.demote("Exception opening and parsing XML file: " + xmlTestFile, e);
    }

    assertAll(
        () -> assertEquals(1, records.airports().size(), "Airports"),
        () -> assertEquals(1, records.heliports().size(), "Heliports"),
        () -> assertEquals(2, records.waypoints().size(), "Waypoints"),
        () -> assertEquals(1, records.arincAirways().size(), "Airways"),
        () -> assertEquals(1, records.ndbNavaids().size(), "NDB navaids"),
        () -> assertEquals(1, records.vhfNavaids().size(), "VHF navaids"),
        () -> assertEquals("KTST", records.airports().iterator().next().portInfo().pointInfo().identifier()),
        () -> assertEquals("H1", records.heliports().iterator().next().portInfo().helipads().orElseThrow()
            .get(0).pointInfo().identifier()),
        () -> assertEquals("FIX-ALPHA", records.holdingPatterns().iterator().next().fixRef().orElseThrow())
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
        () -> assertEquals(1, totalProcedures, "Total procedures nested in airports"),
        () -> assertEquals(1, records.arincAirways().size(), "Airways")
    );
    var procedure = records.airports().iterator().next().portInfo().procedures().orElseThrow().get(0);
    assertAll(
        () -> assertEquals("TEST1", procedure.identifier()),
        () -> assertEquals("BRAVO", procedure.transitions().get(0).identifier().orElseThrow()),
        () -> assertEquals(List.of("FIX-ALPHA", "FIX-BRAVO"), procedure.transitions().get(0).legs().stream()
            .map(leg -> leg.fixRef().orElseThrow()).toList()),
        () -> assertEquals("VOR-TST", procedure.transitions().get(0).legs().get(1).recNavaidRef().orElseThrow())
    );
  }
}
