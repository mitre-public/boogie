package org.mitre.boogie.xml.v23_5;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import org.junit.jupiter.api.Test;

/**
 * Test class for StreamingMarshaller to demonstrate incremental XML writing.
 */
public class StreamingMarshallerTest {

  @Test
  public void testIncrementalWriting() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    try (StreamingMarshaller marshaller = new StreamingMarshaller(outputStream)) {
      marshaller.writeHeader();
      marshaller.startAirports();
      marshaller.writeAirports(Collections.emptyList());
      marshaller.endAirports();
      marshaller.writeAirports(Collections.emptyList());
      marshaller.writeWaypoints(Collections.emptyList());
      marshaller.writeNavaids(Collections.emptyList(), Collections.emptyList());
      marshaller.writeHoldingPatterns(Collections.emptyList());
      marshaller.writeFooter();
    }
    
    String xml = outputStream.toString(StandardCharsets.UTF_8);

    assertAll(
        () -> assertTrue(xml.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")),
        () -> assertTrue(xml.contains("<AeroPublication")),
        () -> assertTrue(xml.contains("http://www.arinc424.com/xml/schema")),
        () -> assertTrue(xml.contains("version=\"23.5\"")),
        () -> assertTrue(xml.contains("<airports>")),
        () -> assertTrue(xml.contains("</airports>")),
        () -> assertTrue(xml.contains("</AeroPublication>"))
    );
  }

  @Test
  public void testWriteAllConvenienceMethod() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    try (StreamingMarshaller marshaller = new StreamingMarshaller(outputStream)) {
      marshaller.writeAll(
          Collections.emptyList(),  // waypoints
          Collections.emptyList(),  // ndb navaids
          Collections.emptyList(),  // vhf navaids
          Collections.emptyList(),  // airways
          Collections.emptyList(),  // holding patterns
          Collections.emptyList()   // airports
      );
    }
    
    String xml = outputStream.toString(StandardCharsets.UTF_8);
    assertAll(
        () -> assertTrue(xml.contains("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")),
        () -> assertTrue(xml.contains("<AeroPublication")),
        () -> assertTrue(xml.contains("</AeroPublication>"))
    );
  }

  @Test
  public void testAutoCloseable() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    try (StreamingMarshaller marshaller = new StreamingMarshaller(outputStream)) {
      marshaller.writeHeader();
      marshaller.writeWaypoints(Collections.emptyList());
    }
    
    String xml = outputStream.toString(StandardCharsets.UTF_8);
    assertTrue(xml.contains("</AeroPublication>"));
  }

  @Test
  public void testStreamingAirports() throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    
    try (StreamingMarshaller marshaller = new StreamingMarshaller(outputStream)) {
      marshaller.writeHeader();

      marshaller.streamAirports(
          Collections.emptyList(),
          m -> m.writeAirport(null) // This won't be called since the list is empty
      );
      
      marshaller.writeFooter();
    }
    
    String xml = outputStream.toString(StandardCharsets.UTF_8);
    assertAll(
        () -> assertTrue(xml.contains("<airports>")),
        () -> assertTrue(xml.contains("</airports>"))
    );
  }
}
