package org.mitre.boogie.xml.v23_4;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collection;

import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincHoldingPattern;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;

/**
 * Marshaller for writing complete ARINC XML files in one operation.
 * 
 * Usage:
 * <pre>
 * try (Marshaller marshaller = new Marshaller(outputStream)) {
 *   marshaller.write(arincRecords);
 * }
 * </pre>
 */
public final class Marshaller implements AutoCloseable {

  private final OutputStream outputStream;
  private final Writer writer;

  public Marshaller(OutputStream outputStream) {
    this.outputStream = outputStream;
    this.writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
  }

  /**
   * Writes a complete ARINC XML file from ArincRecords.
   */
  public void write(ArincRecords arincRecords) {
    try {
      writeHeader();
      
      writeWaypoints(arincRecords.waypoints());
      writeNavaids(arincRecords.vhfNavaids(), arincRecords.ndbNavaids());
      writeAirways(arincRecords.arincAirways());
      writeHoldingPatterns(arincRecords.holdingPatterns());
      writeAirports(arincRecords.airports());
      
      writeFooter();
      writer.flush();
    } catch (Exception e) {
      throw new RuntimeException("Failed to write ARINC XML", e);
    }
  }

  private void writeHeader() throws Exception {
    writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    writer.write("<AeroPublication xmlns=\"http://www.arinc424.com/xml/schema\" effectiveFrom=\"" + 
                 DateTimeFormatter.ISO_INSTANT.format(Instant.now()) + "\" version=\"23.4\">\n");
  }

  private void writeWaypoints(Collection<ArincWaypoint> waypoints) throws Exception {
    writer.write("<enrouteWaypoints>\n");
    
    for (ArincWaypoint waypoint : waypoints) {
      // TODO: Convert waypoint to XML
      writer.write("<!-- Waypoint placeholder -->\n");
    }
    
    writer.write("</enrouteWaypoints>\n");
  }

  private void writeNavaids(Collection<ArincVhfNavaid> vhfNavaids, Collection<ArincNdbNavaid> ndbNavaids) throws Exception {
    // Write VHF navaids
    writer.write("<vhfNavaids>\n");
    for (ArincVhfNavaid navaid : vhfNavaids) {
      // TODO: Convert VHF navaid to XML
      writer.write("<!-- VHF Navaid placeholder -->\n");
    }
    writer.write("</vhfNavaids>\n");

    // Write NDB navaids
    writer.write("<enrouteNdbs>\n");
    for (ArincNdbNavaid navaid : ndbNavaids) {
      // TODO: Convert NDB navaid to XML
      writer.write("<!-- NDB Navaid placeholder -->\n");
    }
    writer.write("</enrouteNdbs>\n");
  }

  private void writeAirways(Collection<ArincAirway> airways) throws Exception {
    writer.write("<airways>\n");
    
    for (ArincAirway airway : airways) {
      // TODO: Convert airway to XML
      writer.write("<!-- Airway placeholder -->\n");
    }
    
    writer.write("</airways>\n");
  }

  private void writeHoldingPatterns(Collection<ArincHoldingPattern> holdingPatterns) throws Exception {
    writer.write("<holdingPatterns>\n");
    
    for (ArincHoldingPattern holdingPattern : holdingPatterns) {
      // TODO: Convert holding pattern to XML
      writer.write("<!-- Holding Pattern placeholder -->\n");
    }
    
    writer.write("</holdingPatterns>\n");
  }

  private void writeAirports(Collection<ArincAirport> airports) throws Exception {
    writer.write("<airports>\n");
    
    for (ArincAirport airport : airports) {
      // TODO: Convert airport to XML
      writer.write("<!-- Airport placeholder -->\n");
    }
    
    writer.write("</airports>\n");
  }

  private void writeFooter() throws Exception {
    writer.write("</AeroPublication>\n");
  }

  @Override
  public void close() {
    try {
      if (writer != null) {
        writer.close();
      }
      if (outputStream != null) {
        outputStream.close();
      }
    } catch (Exception e) {
      throw new RuntimeException("Failed to close marshaller", e);
    }
  }
}
