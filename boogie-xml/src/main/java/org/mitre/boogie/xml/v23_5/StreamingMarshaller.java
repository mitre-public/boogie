package org.mitre.boogie.xml.v23_5;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.function.Consumer;

import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincAirway;
import org.mitre.boogie.xml.model.ArincHoldingPattern;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;

import com.google.common.annotations.Beta;

/**
 * Streaming marshaller for writing ARINC XML files incrementally without loading everything into memory.
 * 
 * Usage:
 * <pre>
 * try (StreamingMarshaller marshaller = new StreamingMarshaller(outputStream)) {
 *   marshaller.writeHeader();
 *   
 *   marshaller.writeWaypoints(waypoints);
 *   marshaller.writeNavaids(ndbNavaids, vhfNavaids);
 *   marshaller.writeAirways(airways);
 *   
 *   // Write airports one at a time
 *   for (ArincAirport airport : airports) {
 *     marshaller.writeAirport(airport);
 *   }
 *   
 *   marshaller.writeFooter();
 * }
 * </pre>
 */
@Beta
public class StreamingMarshaller implements AutoCloseable {

  private final OutputStream outputStream;
  private final Writer writer;
  
  private boolean headerWritten = false;
  private boolean footerWritten = false;
  private boolean inAirports = false;

  public StreamingMarshaller(OutputStream outputStream) {
    this.outputStream = outputStream;
    this.writer = new OutputStreamWriter(outputStream, StandardCharsets.UTF_8);
  }

  private void writeStartElement(String elementName) {
    try {
      writer.write("<" + elementName + ">\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to write start element: " + elementName, e);
    }
  }

  private void writeEndElement(String elementName) {
    try {
      writer.write("</" + elementName + ">\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to write end element: " + elementName, e);
    }
  }

  private void writeXmlFragment(String xmlContent) {
    try {
      writer.write(xmlContent);
      writer.write("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to write XML fragment", e);
    }
  }

  /**
   * Writes the XML header and opening AeroPublication element.
   */
  public void writeHeader() {
    if (headerWritten) {
      throw new IllegalStateException("Header already written");
    }
    try {
      writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
      writer.write("<AeroPublication xmlns=\"http://www.arinc424.com/xml/schema\" effectiveFrom=\"" + 
                   DateTimeFormatter.ISO_INSTANT.format(Instant.now()) + "\" version=\"23.5\">\n");
      headerWritten = true;
    } catch (IOException e) {
      throw new RuntimeException("Failed to write header", e);
    }
  }

  /**
   * Writes waypoints and closes the waypoints section.
   * Note: This is a placeholder implementation - you'll need to implement actual XML generation.
   */
  public void writeWaypoints(Collection<ArincWaypoint> waypoints) {
    ensureHeaderWritten();
    ensureNotInAirports();
    try {
      writeStartElement("enrouteWaypoints");
      
      for (ArincWaypoint waypoint : waypoints) {
        // TODO: Convert waypoint to XML string
        // writeXmlFragment(convertWaypointToXml(waypoint));
        writeXmlFragment("<!-- Waypoint placeholder -->");
      }
      
      writeEndElement("enrouteWaypoints");
    } catch (Exception e) {
      throw new RuntimeException("Failed to write waypoints", e);
    }
  }

  /**
   * Writes NDB and VHF navaids and closes the navaids sections.
   * Note: This is a placeholder implementation - you'll need to implement actual XML generation.
   */
  public void writeNavaids(Collection<ArincNdbNavaid> ndbNavaids, Collection<ArincVhfNavaid> vhfNavaids) {
    ensureHeaderWritten();
    ensureNotInAirports();
    try {
      // Write VHF navaids
      writeStartElement("vhfNavaids");
      for (ArincVhfNavaid navaid : vhfNavaids) {
        // TODO: Convert navaid to XML string
        writeXmlFragment("<!-- VHF Navaid placeholder -->");
      }
      writeEndElement("vhfNavaids");

      // Write NDB navaids
      writeStartElement("enrouteNdbs");
      for (ArincNdbNavaid navaid : ndbNavaids) {
        // TODO: Convert navaid to XML string
        writeXmlFragment("<!-- NDB Navaid placeholder -->");
      }
      writeEndElement("enrouteNdbs");
    } catch (Exception e) {
      throw new RuntimeException("Failed to write navaids", e);
    }
  }

  /**
   * Writes airways and closes the airways section.
   * Note: This is a placeholder implementation - you'll need to implement actual XML generation.
   */
  public void writeAirways(Collection<ArincAirway> airways) {
    ensureHeaderWritten();
    ensureNotInAirports();
    try {
      writeStartElement("airways");
      
      for (ArincAirway airway : airways) {
        // TODO: Convert airway to XML string
        writeXmlFragment("<!-- Airway placeholder -->");
      }
      
      writeEndElement("airways");
    } catch (Exception e) {
      throw new RuntimeException("Failed to write airways", e);
    }
  }

  /**
   * Writes holding patterns and closes the holding patterns section.
   * Note: This is a placeholder implementation - you'll need to implement actual XML generation.
   */
  public void writeHoldingPatterns(Collection<ArincHoldingPattern> holdingPatterns) {
    ensureHeaderWritten();
    ensureNotInAirports();
    try {
      writeStartElement("holdingPatterns");
      
      for (ArincHoldingPattern holdingPattern : holdingPatterns) {
        // TODO: Convert holding pattern to XML string
        writeXmlFragment("<!-- Holding Pattern: " + holdingPattern + " -->");
      }
      
      writeEndElement("holdingPatterns");
    } catch (Exception e) {
      throw new RuntimeException("Failed to write holding patterns", e);
    }
  }

  /**
   * Opens the airports section for incremental writing.
   */
  public void startAirports() {
    ensureHeaderWritten();
    if (inAirports) {
      throw new IllegalStateException("Airports section already started");
    }
    try {
      writeStartElement("airports");
      inAirports = true;
    } catch (Exception e) {
      throw new RuntimeException("Failed to start airports section", e);
    }
  }

  /**
   * Writes a single airport to the airports section.
   * Must be called after startAirports() and before endAirports().
   * Note: This is a placeholder implementation - you'll need to implement actual XML generation.
   */
  public void writeAirport(ArincAirport airport) {
    ensureHeaderWritten();
    if (!inAirports) {
      throw new IllegalStateException("Airports section not started. Call startAirports() first.");
    }
    // TODO: Convert airport to XML string
    writeXmlFragment("<!-- Airport placeholder -->");
  }

  /**
   * Writes multiple airports to the airports section.
   * Must be called after startAirports() and before endAirports().
   */
  public void writeAirports(Collection<ArincAirport> airports) {
    airports.forEach(this::writeAirport);
  }

  /**
   * Closes the airports section.
   */
  public void endAirports() {
    ensureHeaderWritten();
    if (!inAirports) {
      throw new IllegalStateException("Airports section not started");
    }
    try {
      writeEndElement("airports");
      inAirports = false;
    } catch (Exception e) {
      throw new RuntimeException("Failed to end airports section", e);
    }
  }

  /**
   * Convenience method to write all airports at once.
   */
  public void writeAirportsSection(Collection<ArincAirport> airports) {
    startAirports();
    writeAirports(airports);
    endAirports();
  }

  /**
   * Writes the XML footer and closes the document.
   */
  public void writeFooter() {
    ensureHeaderWritten();
    if (footerWritten) {
      throw new IllegalStateException("Footer already written");
    }
    if (inAirports) {
      endAirports();
    }
    try {
      writer.write("</AeroPublication>\n");
      writer.flush();
      footerWritten = true;
    } catch (IOException e) {
      throw new RuntimeException("Failed to write footer", e);
    }
  }

  /**
   * Convenience method to write a complete ARINC XML file.
   */
  public void writeAll(
      Collection<ArincWaypoint> waypoints,
      Collection<ArincNdbNavaid> ndbNavaids,
      Collection<ArincVhfNavaid> vhfNavaids,
      Collection<ArincAirway> airways,
      Collection<ArincHoldingPattern> holdingPatterns,
      Collection<ArincAirport> airports) {
    
    writeHeader();
    writeWaypoints(waypoints);
    writeNavaids(ndbNavaids, vhfNavaids);
    writeAirways(airways);
    writeHoldingPatterns(holdingPatterns);
    writeAirportsSection(airports);
    writeFooter();
  }

  /**
   * Convenience method for streaming airports with a consumer.
   */
  public void streamAirports(Collection<ArincAirport> airports, Consumer<StreamingMarshaller> airportWriter) {
    startAirports();
    airports.forEach(airport -> airportWriter.accept(this));
    endAirports();
  }

  private void ensureHeaderWritten() {
    if (!headerWritten) {
      throw new IllegalStateException("Header not written. Call writeHeader() first.");
    }
  }

  private void ensureNotInAirports() {
    if (inAirports) {
      throw new IllegalStateException("Cannot write other sections while airports section is open. Call endAirports() first.");
    }
  }

  @Override
  public void close() {
    if (!footerWritten && headerWritten) {
      writeFooter();
    }
    try {
      if (writer != null) {
        writer.close();
      }
      if (outputStream != null) {
        outputStream.close();
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to close streaming marshaller", e);
    }
  }
}
