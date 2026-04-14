package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.caasd.commons.util.DemotedException;

class StreamingUnmarshallerTest {

  private static final File xmlTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/v23_4/gibberish-sample.xml"));

  @Test
  void test() {
    StreamingUnmarshaller unmarshaller = new StreamingUnmarshaller(
        ArincXmlVersion.V23_4.jaxbContextClasses(),
        ArincXmlVersion.V23_4.handlers());

    Optional<ArincRecords> result;
    try (FileInputStream fis = new FileInputStream(xmlTestFile)) {
      result = unmarshaller.apply(fis);
    } catch (IOException e) {
      throw DemotedException.demote("Exception opening and parsing XML file: " + xmlTestFile, e);
    }

    assertTrue(result.isPresent());
    ArincRecords records = result.get();

    assertAll(
        () -> assertEquals(5, records.waypoints().size(), "Waypoints"),
        () -> assertEquals(5, records.airports().size(), "Airports"),
        () -> assertEquals(5, records.ndbNavaids().size(), "NDB Navaids"),
        () -> assertEquals(3, records.vhfNavaids().size(), "VHF Navaids"),
        () -> assertEquals(5, records.arincAirways().size(), "Airways"),
        () -> assertEquals(5, records.holdingPatterns().size(), "Holding Patterns"),
        () -> assertEquals(5, records.heliports().size(), "Heliports")
    );
  }

  @Test
  void testWithExternalRecords() {
    StreamingUnmarshaller unmarshaller = new StreamingUnmarshaller(
        ArincXmlVersion.V23_4.jaxbContextClasses(),
        ArincXmlVersion.V23_4.handlers());

    ArincRecords records = ArincRecords.standard();
    Optional<ArincRecords> result;
    try (FileInputStream fis = new FileInputStream(xmlTestFile)) {
      result = unmarshaller.apply(fis, records);
    } catch (IOException e) {
      throw DemotedException.demote("Exception opening and parsing XML file: " + xmlTestFile, e);
    }

    assertTrue(result.isPresent());
    assertEquals(records, result.get(), "Should return the same ArincRecords instance");

    assertAll(
        () -> assertEquals(5, records.waypoints().size(), "Waypoints"),
        () -> assertEquals(5, records.airports().size(), "Airports"),
        () -> assertEquals(5, records.ndbNavaids().size(), "NDB Navaids"),
        () -> assertEquals(3, records.vhfNavaids().size(), "VHF Navaids"),
        () -> assertEquals(5, records.arincAirways().size(), "Airways"),
        () -> assertEquals(5, records.holdingPatterns().size(), "Holding Patterns"),
        () -> assertEquals(5, records.heliports().size(), "Heliports")
    );
  }
}
