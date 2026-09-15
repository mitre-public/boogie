package org.mitre.boogie.xml;

import static java.util.Objects.requireNonNull;

import java.io.InputStream;
import java.util.Collection;

import com.google.common.annotations.Beta;
import org.mitre.boogie.xml.assemble.AirportAssembler;
import org.mitre.boogie.xml.assemble.AirportAssemblyStrategy;
import org.mitre.boogie.xml.assemble.AirwayAssembler;
import org.mitre.boogie.xml.assemble.AirwayAssemblyStrategy;
import org.mitre.boogie.xml.assemble.FixAssembler;
import org.mitre.boogie.xml.assemble.FixAssemblyStrategy;
import org.mitre.boogie.xml.assemble.HeliportAssembler;
import org.mitre.boogie.xml.assemble.HeliportAssemblyStrategy;
import org.mitre.boogie.xml.assemble.ProcedureAssembler;
import org.mitre.boogie.xml.assemble.ProcedureAssemblyStrategy;
import org.mitre.boogie.xml.database.XmlFixDatabase;
import org.mitre.boogie.xml.database.XmlTerminalAreaDatabase;
import org.mitre.boogie.xml.exi.ExiOptions;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.Transition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Oneshot implementation of a parser going from an {@link InputStream} (typically sourced from an ARINC 424 XML file)
 * to a collection of client-defined records of the given types.
 *
 * <p>This class mirrors the ARINC {@code OneshotRecordParser} pattern but is tailored for XML input. It unmarshals the
 * XML into {@link org.mitre.boogie.xml.model.ArincRecords ArincRecords}, builds a {@link XmlFixDatabase} for cross-reference resolution, and then uses the configured
 * assembly strategies to produce airports, fixes, airways, procedures, and heliports.
 *
 * <p>Usage with standard Boogie types:
 * <pre>{@code
 * try (InputStream is = new FileInputStream("arinc424.xml")) {
 *   ClientRecords<Airport, Fix, Airway, Procedure, Heliport> records =
 *       OneshotXmlParser.standard(ArincXmlVersion.V23_4).assembleFrom(is);
 * }
 * }</pre>
 */
@Beta
public final class OneshotXmlParser<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> {

  private static final Logger LOG = LoggerFactory.getLogger(OneshotXmlParser.class);

  private final StreamingUnmarshaller unmarshaller;
  private final FixAssemblyStrategy<FIX> fixStrategy;
  private final AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;
  private final AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;
  private final ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;
  private final HeliportAssemblyStrategy<HPT, HLPD> heliportStrategy;

  private OneshotXmlParser(
      StreamingUnmarshaller unmarshaller,
      FixAssemblyStrategy<FIX> fixStrategy,
      AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy,
      AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy,
      ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy,
      HeliportAssemblyStrategy<HPT, HLPD> heliportStrategy) {
    this.unmarshaller = unmarshaller;
    this.fixStrategy = fixStrategy;
    this.airportStrategy = airportStrategy;
    this.airwayStrategy = airwayStrategy;
    this.procedureStrategy = procedureStrategy;
    this.heliportStrategy = heliportStrategy;
  }

  /**
   * Instantiate a new buildable version of the oneshot parser which can be used to construct user-defined data models
   * given the configured strategy classes.
   */
  public static <APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> builder() {
    return new Builder<>(StreamingUnmarshaller.builder());
  }

  /**
   * Instantiate a standard oneshot parser targeting the default implementations of the Boogie interfaces packaged
   * alongside the library.
   *
   * @param version the {@link ArincXmlVersion} defining the XML schema version to parse
   */
  public static OneshotXmlParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Helipad, Heliport> standard(ArincXmlVersion version) {
    return standardBuilder(version).build();
  }

  /**
   * Instantiate a parser targeting the standard Boogie types from EXI input.
   * The options select schema-less or schema-informed decoding independently of the ARINC model version.
   */
  public static OneshotXmlParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Helipad, Heliport> standard(ArincXmlVersion version, ExiOptions exiOptions) {
    return standardBuilder(version).exiOptions(exiOptions).build();
  }

  /**
   * Configure a parser with the standard Boogie assembly strategies. The returned builder can
   * select EXI input or replace individual strategies before building the parser.
   */
  public static Builder<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Helipad, Heliport> standardBuilder(ArincXmlVersion version) {
    return OneshotXmlParser.<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Helipad, Heliport>builder()
        .version(version)
        .fixStrategy(FixAssemblyStrategy.standard())
        .airportStrategy(AirportAssemblyStrategy.standard())
        .airwayStrategy(AirwayAssemblyStrategy.standard())
        .procedureStrategy(ProcedureAssemblyStrategy.standard())
        .heliportStrategy(HeliportAssemblyStrategy.standard());
  }

  /**
   * Assembles the collection of typed client records from an ARINC 424 document in the configured XML or EXI encoding represented as an
   * {@link InputStream}.
   *
   * <p>All record types are assembled on-the-fly during streaming. The {@link XmlFixDatabase} is created eagerly
   * (sharing its backing maps with the builder) so that airway and procedure assembly can resolve fix references
   * inline as each record arrives in the stream.
   *
   * @param inputStream an input stream containing an ARINC 424 document in the configured encoding;
   *                    the caller remains responsible for closing it
   */
  public ClientRecords<APT, RWY, FIX, AWY, PRC, HLPD, HPT> assembleFrom(InputStream inputStream) {
    requireNonNull(inputStream);

    XmlFixDatabase.Builder<FIX> fixDatabaseBuilder = XmlFixDatabase.builder();
    XmlFixDatabase<FIX> xmlFixDatabase = fixDatabaseBuilder.build();

    StreamAssemblyRecords<FIX, APT, RWY, AWY, PRC, HLPD, HPT> context = new StreamAssemblyRecords<>(
        FixAssembler.withStrategy(fixStrategy),
        AirportAssembler.withStrategy(airportStrategy),
        airportStrategy,
        AirwayAssembler.withStrategy(airwayStrategy, xmlFixDatabase),
        ProcedureAssembler.withStrategy(procedureStrategy, xmlFixDatabase),
        HeliportAssembler.withStrategy(heliportStrategy),
        heliportStrategy,
        fixDatabaseBuilder);

    unmarshaller
        .apply(inputStream, context)
        .orElseThrow(() -> new RuntimeException("Failed to unmarshal XML or EXI input."));
    LOG.debug("Finished streaming XML — all records assembled.");

    XmlTerminalAreaDatabase<FIX, RWY, HLPD> terminalAreaDatabase = context.buildTerminalAreaDatabase();

    return new ClientRecords<>(
        context.assembledAirports(),
        context.assembledFixes(),
        context.assembledAirways(),
        context.assembledProcedures(),
        context.assembledHeliports(),
        xmlFixDatabase,
        terminalAreaDatabase);
  }

  /**
   * Builder for configuring a {@link OneshotXmlParser} with custom assembly strategies.
   */
  public static final class Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> {

    private final StreamingUnmarshaller.Builder unmarshallerBuilder;
    private FixAssemblyStrategy<FIX> fixStrategy;
    private AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;
    private AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;
    private ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;
    private HeliportAssemblyStrategy<HPT, HLPD> heliportStrategy;

    private Builder(StreamingUnmarshaller.Builder unmarshallerBuilder) {
      this.unmarshallerBuilder = unmarshallerBuilder;
    }

    /**
     * See the documentation on {@link ArincXmlVersion}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> version(ArincXmlVersion version) {
      unmarshallerBuilder.version(version);
      return this;
    }

    /** Select ordinary XML input, including when reusing a builder previously configured for EXI. */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> xml() {
      unmarshallerBuilder.xml();
      return this;
    }

    /**
     * Configure EXI input. Omitting this option keeps ordinary XML input as the default.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> exiOptions(ExiOptions exiOptions) {
      unmarshallerBuilder.exiOptions(exiOptions);
      return this;
    }

    /** Supply a reader directly, for example from a codec with several registered schemas. */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> readerFactory(StreamingUnmarshaller.ReaderFactory readerFactory) {
      unmarshallerBuilder.readerFactory(readerFactory);
      return this;
    }

    /**
     * See the documentation on {@link FixAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> fixStrategy(FixAssemblyStrategy<FIX> fixStrategy) {
      this.fixStrategy = requireNonNull(fixStrategy);
      return this;
    }

    /**
     * See the documentation on {@link AirportAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> airportStrategy(AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy) {
      this.airportStrategy = requireNonNull(airportStrategy);
      return this;
    }

    /**
     * See the documentation on {@link AirwayAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> airwayStrategy(AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy) {
      this.airwayStrategy = requireNonNull(airwayStrategy);
      return this;
    }

    /**
     * See the documentation on {@link ProcedureAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> procedureStrategy(ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy) {
      this.procedureStrategy = requireNonNull(procedureStrategy);
      return this;
    }

    /**
     * See the documentation on {@link HeliportAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> heliportStrategy(HeliportAssemblyStrategy<HPT, HLPD> heliportStrategy) {
      this.heliportStrategy = requireNonNull(heliportStrategy);
      return this;
    }

    public OneshotXmlParser<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> build() {
      return new OneshotXmlParser<>(
          unmarshallerBuilder.build(),
          requireNonNull(fixStrategy, "fixStrategy"),
          requireNonNull(airportStrategy, "airportStrategy"),
          requireNonNull(airwayStrategy, "airwayStrategy"),
          requireNonNull(procedureStrategy, "procedureStrategy"),
          requireNonNull(heliportStrategy, "heliportStrategy"));
    }
  }

  /**
   * Wrapper class containing assembled client records and databases produced by the oneshot parser.
   */
  public static final class ClientRecords<APT, RWY, FIX, AWY, PRC, HLPD, HPT> {

    private final Collection<APT> airports;
    private final Collection<FIX> fixes;
    private final Collection<AWY> airways;
    private final Collection<PRC> procedures;
    private final Collection<HPT> heliports;
    private final XmlFixDatabase<FIX> xmlFixDatabase;
    private final XmlTerminalAreaDatabase<FIX, RWY, HLPD> terminalAreaDatabase;

    private ClientRecords(
        Collection<APT> airports,
        Collection<FIX> fixes,
        Collection<AWY> airways,
        Collection<PRC> procedures,
        Collection<HPT> heliports,
        XmlFixDatabase<FIX> xmlFixDatabase,
        XmlTerminalAreaDatabase<FIX, RWY, HLPD> terminalAreaDatabase
    ) {
      this.airports = airports;
      this.fixes = fixes;
      this.airways = airways;
      this.procedures = procedures;
      this.heliports = heliports;
      this.xmlFixDatabase = xmlFixDatabase;
      this.terminalAreaDatabase = terminalAreaDatabase;
    }

    public Collection<APT> airports() {
      return airports;
    }

    public Collection<FIX> fixes() {
      return fixes;
    }

    public Collection<AWY> airways() {
      return airways;
    }

    public Collection<PRC> procedures() {
      return procedures;
    }

    public Collection<HPT> heliports() {
      return heliports;
    }

    public XmlFixDatabase<FIX> fixDatabase() {
      return xmlFixDatabase;
    }

    public XmlTerminalAreaDatabase<FIX, RWY, HLPD> terminalAreaDatabase() {
      return terminalAreaDatabase;
    }
  }
}
