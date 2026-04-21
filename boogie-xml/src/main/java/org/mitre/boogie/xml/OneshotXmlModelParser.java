package org.mitre.boogie.xml;

import static java.util.Objects.requireNonNull;

import java.io.InputStream;

import org.mitre.boogie.xml.model.ArincRecords;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Oneshot parser that unmarshals an ARINC 424 XML {@link InputStream} into the intermediate
 * {@link ArincRecords} model classes <b>without</b> performing any assembly into client types.
 *
 * <p>Use this when you need direct access to the raw parsed model objects (e.g. {@code ArincAirport},
 * {@code ArincWaypoint}, {@code ArincAirway}, etc.) rather than assembled Boogie domain types.
 *
 * <p>Usage:
 * <pre>{@code
 * try (InputStream is = new FileInputStream("arinc424.xml")) {
 *   ArincRecords records = OneshotXmlModelParser.standard(ArincXmlVersion.V23_4).parseFrom(is);
 *
 *   records.airports();       // Set<ArincAirport>
 *   records.waypoints();      // Set<ArincWaypoint>
 *   records.arincAirways();   // Set<ArincAirway>
 * }
 * }</pre>
 *
 * @see OneshotXmlParser for the assembling parser that produces client-defined domain types
 */
public final class OneshotXmlModelParser {

  private static final Logger LOG = LoggerFactory.getLogger(OneshotXmlModelParser.class);

  private final ArincXmlVersion version;

  private OneshotXmlModelParser(ArincXmlVersion version) {
    this.version = requireNonNull(version);
  }

  /**
   * Create a model-only parser for the given XML schema version.
   *
   * @param version the {@link ArincXmlVersion} defining the XML schema version to parse
   */
  public static OneshotXmlModelParser standard(ArincXmlVersion version) {
    return new OneshotXmlModelParser(version);
  }

  /**
   * Parse the given XML input stream into an {@link ArincRecords} containing the raw model objects.
   *
   * <p>No assembly or fix resolution is performed &mdash; the returned records contain only the
   * unmarshalled JAXB model objects as they appear in the XML.
   *
   * @param inputStream an input stream containing the bytes of an ARINC 424 XML file
   */
  public ArincRecords parseFrom(InputStream inputStream) {
    requireNonNull(inputStream);

    ArincRecords records = StreamingUnmarshaller.fromVersion(version)
        .apply(inputStream)
        .orElseThrow(() -> new RuntimeException("Failed to unmarshal XML input."));

    LOG.debug("Finished streaming XML — {} waypoints, {} NDB navaids, {} VHF navaids, {} airports, {} heliports, {} airways, {} holding patterns.",
        records.waypoints().size(),
        records.ndbNavaids().size(),
        records.vhfNavaids().size(),
        records.airports().size(),
        records.heliports().size(),
        records.arincAirways().size(),
        records.holdingPatterns().size());

    return records;
  }
}
