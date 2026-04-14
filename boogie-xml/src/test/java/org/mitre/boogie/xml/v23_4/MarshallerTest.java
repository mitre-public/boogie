package org.mitre.boogie.xml.v23_4;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincRecords;

/**
 * Test class for Marshaller to demonstrate complete XML writing.
 */
public class MarshallerTest {

  @Test
  public void testWriteCompleteXml() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    try (Marshaller marshaller = new Marshaller(outputStream)) {
      ArincRecords records = ArincRecords.standard()
          .waypoints(Collections.emptySet())
          .airports(Collections.emptySet())
          .ndbNavaids(Collections.emptySet())
          .vhfNavaids(Collections.emptySet())
          .arincAirways(Collections.emptySet())
          .holdingPatterns(Collections.emptySet());
      
      marshaller.write(records);
    }
    
    String xml = outputStream.toString(StandardCharsets.UTF_8);

    assertAll(
        () -> assertTrue(xml.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")),
        () -> assertTrue(xml.contains("<AeroPublication")),
        () -> assertTrue(xml.contains("http://www.arinc424.com/xml/schema")),
        () -> assertTrue(xml.contains("<enrouteWaypoints>")),
        () -> assertTrue(xml.contains("</enrouteWaypoints>")),
        () -> assertTrue(xml.contains("<vhfNavaids>")),
        () -> assertTrue(xml.contains("</vhfNavaids>")),
        () -> assertTrue(xml.contains("<enrouteNdbs>")),
        () -> assertTrue(xml.contains("</enrouteNdbs>")),
        () -> assertTrue(xml.contains("<airways>")),
        () -> assertTrue(xml.contains("</airways>")),
        () -> assertTrue(xml.contains("<holdingPatterns>")),
        () -> assertTrue(xml.contains("</holdingPatterns>")),
        () -> assertTrue(xml.contains("<airports>")),
        () -> assertTrue(xml.contains("</airports>")),
        () -> assertTrue(xml.contains("</AeroPublication>"))
    );
  }

  @Test
  public void testAutoCloseable() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    // Test that try-with-resources automatically closes
    try (Marshaller marshaller = new Marshaller(outputStream)) {
      ArincRecords records = ArincRecords.standard()
          .waypoints(Collections.emptySet())
          .airports(Collections.emptySet())
          .ndbNavaids(Collections.emptySet())
          .vhfNavaids(Collections.emptySet())
          .arincAirways(Collections.emptySet())
          .holdingPatterns(Collections.emptySet());
      
      marshaller.write(records);
    }
    
    String xml = outputStream.toString(StandardCharsets.UTF_8);
    assertTrue(xml.contains("</AeroPublication>"));
  }

  @Test
  public void testXmlStructure() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    try (Marshaller marshaller = new Marshaller(outputStream)) {
      ArincRecords records = ArincRecords.standard()
          .waypoints(Collections.emptySet())
          .airports(Collections.emptySet())
          .ndbNavaids(Collections.emptySet())
          .vhfNavaids(Collections.emptySet())
          .arincAirways(Collections.emptySet())
          .holdingPatterns(Collections.emptySet());
      
      marshaller.write(records);
    }
    
    String xml = outputStream.toString(StandardCharsets.UTF_8);

    int waypointsIndex = xml.indexOf("<enrouteWaypoints>");
    int vhfNavaidsIndex = xml.indexOf("<vhfNavaids>");
    int ndbNavaidsIndex = xml.indexOf("<enrouteNdbs>");
    int airwaysIndex = xml.indexOf("<airways>");
    int holdingPatternsIndex = xml.indexOf("<holdingPatterns>");
    int airportsIndex = xml.indexOf("<airports>");

    assertAll(
        () -> assertTrue(waypointsIndex > 0),
        () -> assertTrue(vhfNavaidsIndex > waypointsIndex),
        () -> assertTrue(ndbNavaidsIndex > vhfNavaidsIndex),
        () -> assertTrue(airwaysIndex > ndbNavaidsIndex),
        () -> assertTrue(holdingPatternsIndex > airwaysIndex),
        () -> assertTrue(airportsIndex > holdingPatternsIndex)
    );
  }
}
