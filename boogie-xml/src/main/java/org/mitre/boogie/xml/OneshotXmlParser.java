package org.mitre.boogie.xml;

import static java.util.Objects.requireNonNull;

import java.io.InputStream;
import java.util.Collection;

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
public final class OneshotXmlParser<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> {

  private static final Logger LOG = LoggerFactory.getLogger(OneshotXmlParser.class);

  private final ArincXmlVersion version;
  private final FixAssemblyStrategy<FIX> fixStrategy;
  private final AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;
  private final AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;
  private final ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;
  private final HeliportAssemblyStrategy<HPT, HLPD> heliportStrategy;

  private OneshotXmlParser(Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> builder) {
    this.version = requireNonNull(builder.version);
    this.fixStrategy = requireNonNull(builder.fixStrategy);
    this.airportStrategy = requireNonNull(builder.airportStrategy);
    this.airwayStrategy = requireNonNull(builder.airwayStrategy);
    this.procedureStrategy = requireNonNull(builder.procedureStrategy);
    this.heliportStrategy = requireNonNull(builder.heliportStrategy);
  }

  /**
   * Instantiate a new buildable version of the oneshot parser which can be used to construct user-defined data models
   * given the configured strategy classes.
   */
  public static <APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> builder() {
    return new Builder<>();
  }

  /**
   * Instantiate a standard oneshot parser targeting the default implementations of the Boogie interfaces packaged
   * alongside the library.
   *
   * @param version the {@link ArincXmlVersion} defining the XML schema version to parse
   */
  public static OneshotXmlParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Helipad, Heliport> standard(ArincXmlVersion version) {
    return OneshotXmlParser.<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Helipad, Heliport>builder()
        .version(version)
        .fixStrategy(FixAssemblyStrategy.standard())
        .airportStrategy(AirportAssemblyStrategy.standard())
        .airwayStrategy(AirwayAssemblyStrategy.standard())
        .procedureStrategy(ProcedureAssemblyStrategy.standard())
        .heliportStrategy(HeliportAssemblyStrategy.standard())
        .build();
  }

  /**
   * Assembles the collection of typed client records from an underlying ARINC 424 XML file represented as an
   * {@link InputStream}.
   *
   * <p>All record types are assembled on-the-fly during streaming. The {@link XmlFixDatabase} is created eagerly
   * (sharing its backing maps with the builder) so that airway and procedure assembly can resolve fix references
   * inline as each record arrives in the stream.
   *
   * @param inputStream an input stream containing the bytes of an ARINC 424 XML file
   */
  public ClientRecords<APT, FIX, AWY, PRC, HPT> assembleFrom(InputStream inputStream) {
    requireNonNull(inputStream);

    XmlFixDatabase.Builder<FIX> fixDatabaseBuilder = XmlFixDatabase.builder();
    XmlFixDatabase<FIX> xmlFixDatabase = fixDatabaseBuilder.build();

    StreamAssemblyRecords<FIX, APT, AWY, PRC, HPT> context = new StreamAssemblyRecords<>(
        FixAssembler.withStrategy(fixStrategy),
        AirportAssembler.withStrategy(airportStrategy),
        AirwayAssembler.withStrategy(airwayStrategy, xmlFixDatabase),
        ProcedureAssembler.withStrategy(procedureStrategy, xmlFixDatabase),
        HeliportAssembler.withStrategy(heliportStrategy),
        fixDatabaseBuilder);

    StreamingUnmarshaller.fromVersion(version)
        .apply(inputStream, context)
        .orElseThrow(() -> new RuntimeException("Failed to unmarshal XML input."));
    LOG.debug("Finished streaming XML — all records assembled.");

    XmlTerminalAreaDatabase<FIX> terminalAreaDatabase = context.buildTerminalAreaDatabase();

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

    private ArincXmlVersion version;
    private FixAssemblyStrategy<FIX> fixStrategy;
    private AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;
    private AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;
    private ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;
    private HeliportAssemblyStrategy<HPT, HLPD> heliportStrategy;

    private Builder() {}

    /**
     * See the documentation on {@link ArincXmlVersion}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, HLPD, HPT> version(ArincXmlVersion version) {
      this.version = requireNonNull(version);
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
      return new OneshotXmlParser<>(this);
    }
  }

  /**
   * Wrapper class containing assembled client records and databases produced by the oneshot parser.
   */
  public static final class ClientRecords<APT, FIX, AWY, PRC, HPT> {

    private final Collection<APT> airports;
    private final Collection<FIX> fixes;
    private final Collection<AWY> airways;
    private final Collection<PRC> procedures;
    private final Collection<HPT> heliports;
    private final XmlFixDatabase<FIX> xmlFixDatabase;
    private final XmlTerminalAreaDatabase<FIX> terminalAreaDatabase;

    private ClientRecords(
        Collection<APT> airports,
        Collection<FIX> fixes,
        Collection<AWY> airways,
        Collection<PRC> procedures,
        Collection<HPT> heliports,
        XmlFixDatabase<FIX> xmlFixDatabase,
        XmlTerminalAreaDatabase<FIX> terminalAreaDatabase
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

    public XmlTerminalAreaDatabase<FIX> terminalAreaDatabase() {
      return terminalAreaDatabase;
    }
  }
}
