package org.mitre.boogie.xml;

import static java.util.Objects.requireNonNull;

import java.io.InputStream;

import com.google.common.annotations.Beta;
import org.mitre.boogie.xml.exi.ExiOptions;
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
@Beta
public final class OneshotXmlModelParser {

  private static final Logger LOG = LoggerFactory.getLogger(OneshotXmlModelParser.class);

  private final StreamingUnmarshaller unmarshaller;

  private OneshotXmlModelParser(StreamingUnmarshaller unmarshaller) {
    this.unmarshaller = unmarshaller;
  }

  /** Configure the ARINC model version and the XML or EXI reader. */
  public static Builder builder() {
    return new Builder(StreamingUnmarshaller.builder());
  }

  /**
   * Create a model-only parser for the given XML schema version.
   *
   * @param version the {@link ArincXmlVersion} defining the XML schema version to parse
   */
  public static OneshotXmlModelParser standard(ArincXmlVersion version) {
    return builder().version(version).build();
  }

  /**
   * Create a model-only parser for EXI input with the given ARINC model version and EXI options.
   * The options select schema-less or schema-informed decoding independently of the model version.
   */
  public static OneshotXmlModelParser standard(ArincXmlVersion version, ExiOptions exiOptions) {
    return builder().version(version).exiOptions(exiOptions).build();
  }

  /**
   * Parse the configured XML or EXI input into an {@link ArincRecords} containing the raw model objects.
   *
   * <p>No assembly or fix resolution is performed &mdash; the configured handlers convert the
   * unmarshalled JAXB objects into the intermediate ARINC model records.
   *
   * @param inputStream an input stream containing an ARINC 424 document in the configured encoding;
   *                    the caller remains responsible for closing it
   */
  public ArincRecords parseFrom(InputStream inputStream) {
    requireNonNull(inputStream);

    ArincRecords records = unmarshaller
        .apply(inputStream)
        .orElseThrow(() -> new RuntimeException("Failed to unmarshal XML or EXI input."));

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

  /** Builder for the model version and input reader. XML is the default encoding. */
  public static final class Builder {

    private final StreamingUnmarshaller.Builder unmarshallerBuilder;

    private Builder(StreamingUnmarshaller.Builder unmarshallerBuilder) {
      this.unmarshallerBuilder = unmarshallerBuilder;
    }

    public Builder version(ArincXmlVersion version) {
      unmarshallerBuilder.version(version);
      return this;
    }

    /** Select ordinary XML input, including when reusing a builder previously configured for EXI. */
    public Builder xml() {
      unmarshallerBuilder.xml();
      return this;
    }

    public Builder exiOptions(ExiOptions exiOptions) {
      unmarshallerBuilder.exiOptions(exiOptions);
      return this;
    }

    /** Supply a reader directly, for example from a codec with several registered schemas. */
    public Builder readerFactory(StreamingUnmarshaller.ReaderFactory readerFactory) {
      unmarshallerBuilder.readerFactory(readerFactory);
      return this;
    }

    public OneshotXmlModelParser build() {
      return new OneshotXmlModelParser(unmarshallerBuilder.build());
    }
  }
}
