package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory.consumerForVersion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Helipad;
import org.mitre.tdp.boogie.Heliport;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.arinc.assemble.AirportAssembler;
import org.mitre.tdp.boogie.arinc.assemble.AirportAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.AirwayAssembler;
import org.mitre.tdp.boogie.arinc.assemble.AirwayAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.ControlledAirspaceAssembler;
import org.mitre.tdp.boogie.arinc.assemble.ControlledAirspaceAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.FirUirAssembler;
import org.mitre.tdp.boogie.arinc.assemble.FirUirAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.FixAssembler;
import org.mitre.tdp.boogie.arinc.assemble.FixAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.HeliportAssembler;
import org.mitre.tdp.boogie.arinc.assemble.HeliportAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.ProcedureAssembler;
import org.mitre.tdp.boogie.arinc.assemble.ProcedureAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Oneshot implementation of a parser going from an {@link InputStream} (typically sourced from a file) to a collection of client
 * defined records of the given types.
 *
 * <p>I heard you like generics. All jokes aside - don't assign this class to a variable in your application unless you linewidth
 * limit is {@code >300} characters. Please use this in an inline call if you value your eyesight.
 */
public final class OneshotRecordParser<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> {

  private static final Logger LOG = LoggerFactory.getLogger(OneshotRecordParser.class);

  private final ArincVersion version;

  private final Predicate<ArincRecord> dropRecord;

  private final AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;

  private final FixAssemblyStrategy<FIX> fixStrategy;

  private final AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;

  private final ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;

  private final FirUirAssemblyStrategy<AIR, ASEQ> firUirStrategy;

  private final ControlledAirspaceAssemblyStrategy<AIR, FIX, ASEQ> controlledAirspaceStrategy;

  private final HeliportAssemblyStrategy<HPT, HLPD> heliportAssemblyStrategy;

  private OneshotRecordParser(Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> builder) {
    this.version = requireNonNull(builder.version);
    this.dropRecord = requireNonNull(builder.dropRecord);
    this.airportStrategy = requireNonNull(builder.airportStrategy);
    this.fixStrategy = FixAssemblyStrategy.caching(builder.fixStrategy);
    this.airwayStrategy = requireNonNull(builder.airwayStrategy);
    this.procedureStrategy = requireNonNull(builder.procedureStrategy);
    this.firUirStrategy = requireNonNull(builder.firUirStrategy);
    this.controlledAirspaceStrategy = requireNonNull(builder.controlledAirspaceStrategy);
    this.heliportAssemblyStrategy = requireNonNull(builder.heliportAssemblyStrategy);
  }

  /**
   * Instantiate a new buildable version of the oneshot parser which can be used to construct the user-defined data models given
   * the configured strategy classes.
   */
  public static <APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> builder(ArincVersion version) {
    return new Builder<>(version);
  }

  /**
   * Instantiate a standard oneshot parser targeting the default implementations of the Boogie interfaces packaged alongside the
   * library.
   */
  public static OneshotRecordParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Airspace, AirspaceSequence, Helipad, Heliport> standard(ArincVersion version) {
    return OneshotRecordParser.<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Airspace, AirspaceSequence, Helipad, Heliport>builder(version)
        .airportStrategy(AirportAssemblyStrategy.standard())
        .fixStrategy(FixAssemblyStrategy.standard())
        .airwayStrategy(AirwayAssemblyStrategy.standard())
        .procedureStrategy(ProcedureAssemblyStrategy.standard())
        .firUirStrategy(FirUirAssemblyStrategy.standard())
        .controlledAirspaceStrategy(ControlledAirspaceAssemblyStrategy.standard())
        .heliportAssemblyStrategy(HeliportAssemblyStrategy.standard())
        .build();
  }

  /**
   * Assembles the collection of typed client records from an underlying 424 file represented as an {@link InputStream}. Typically
   * this will be called with a {@link FileInputStream} but should be flexible enough to handle other sources.
   *
   * @param inputStream an input stream containing the bytes of a 424 file
   */
  public ClientRecords<APT, FIX, AWY, PRC, AIR, HPT> assembleFrom(InputStream inputStream) {
    requireNonNull(inputStream);

    ClientRecords.Builder<APT, FIX, AWY, PRC, AIR, HPT> records = new ClientRecords.Builder<>();

    ArincRecordParser parser = ArincRecordParser.standard(version.specs());
    ConvertingArincRecordConsumer consumer = consumerForVersion(version);

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      reader.lines()
          .map(parser::parse)
          .flatMap(Optional::stream)
          .filter(dropRecord.negate())
          .forEach(consumer);
    } catch (IOException e) {
      LOG.error("Could not parse the arinc text into memory", e);
      return records.build();
    }

    LOG.debug("Finished parsing and converting supported record types.");

    ArincFixDatabase arincFixDatabase = ArincDatabaseFactory.newFixDatabase(
        consumer.arincNdbNavaids(),
        consumer.arincVhfNavaids(),
        consumer.arincWaypoints(),
        consumer.arincAirports(),
        consumer.arincHoldingPatterns()
    );
    LOG.debug("Finished instantiation of FixDatabase.");

    ArincTerminalAreaDatabase arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        consumer.arincAirports(),
        consumer.arincRunways(),
        consumer.arincLocalizerGlideSlopes(),
        consumer.arincNdbNavaids(),
        consumer.arincVhfNavaids(),
        consumer.arincWaypoints(),
        consumer.arincProcedureLegs(),
        consumer.arincGnssLandingSystems(),
        consumer.arincHelipads()
    );
    LOG.debug("Finished instantiation of TerminalAreaDatabase.");

    return records
        .addAirports(assembleAirports(arincTerminalAreaDatabase, consumer.arincAirports()))
        .addFixes(assembleFixes(consumer.arincWaypoints(), consumer.arincNdbNavaids(), consumer.arincVhfNavaids()))
        .addAirways(assembleAirways(arincFixDatabase, consumer.arincAirwayLegs()))
        .addProcedures(assembleProcedures(arincFixDatabase, arincTerminalAreaDatabase, consumer.arincProcedureLegs()))
        .addFirUirs(assembleFirUirs(consumer.arincFirUirLegs()))
        .addControlledAirspaces(assembleControlledAirspaces(arincFixDatabase, consumer.arincControlledAirspaceLegs()))
        .addHeliports(assembleHeliports(arincTerminalAreaDatabase, consumer.arincHeliports()))
        .build();
  }

  private Collection<PRC> assembleProcedures(ArincFixDatabase arincFixDatabase, ArincTerminalAreaDatabase arincTerminalAreaDatabase,
                                             Collection<ArincProcedureLeg> procedureLegs) {

    ProcedureAssembler<PRC> assembler = ProcedureAssembler.withStrategy(
        arincTerminalAreaDatabase,
        arincFixDatabase,
        fixStrategy,
        procedureStrategy
    );

    return assembler.assemble(procedureLegs).toList();
  }

  private Collection<AWY> assembleAirways(ArincFixDatabase arincFixDatabase, Collection<ArincAirwayLeg> airwayLeg) {
    AirwayAssembler<AWY> assembler = AirwayAssembler.usingStrategy(arincFixDatabase, fixStrategy, airwayStrategy);
    return assembler.assemble(airwayLeg).toList();
  }

  private Collection<FIX> assembleFixes(Collection<ArincWaypoint> waypoints, Collection<ArincNdbNavaid> ndbs,
      Collection<ArincVhfNavaid> vhfs) {

    FixAssembler<FIX> assembler = FixAssembler.withStrategy(fixStrategy);

    return Stream.of(waypoints.stream(), ndbs.stream(), vhfs.stream())
        .flatMap(stream -> stream)
        .map(assembler::assemble)
        .toList();
  }

  private Collection<APT> assembleAirports(ArincTerminalAreaDatabase arincTerminalAreaDatabase, Collection<ArincAirport> airports) {
    AirportAssembler<APT> assembler = AirportAssembler.usingStrategy(arincTerminalAreaDatabase, airportStrategy);
    return airports.stream().map(assembler::assemble).toList();
  }

  private Collection<AIR> assembleFirUirs(Collection<ArincFirUirLeg> legs) {
    FirUirAssembler<AIR> assembler = FirUirAssembler.usingStrategy(firUirStrategy);
    return assembler.assemble(legs).toList();
  }

  private Collection<AIR> assembleControlledAirspaces(ArincFixDatabase fixDatabase, Collection<ArincControlledAirspaceLeg> legs) {
    ControlledAirspaceAssembler<AIR> assembler = ControlledAirspaceAssembler.usingStrategy(fixDatabase, fixStrategy, controlledAirspaceStrategy);
    return assembler.assemble(legs).toList();
  }

  private Collection<HPT> assembleHeliports(ArincTerminalAreaDatabase arincTerminalAreaDatabase, Collection<ArincHeliport> heliports) {
    HeliportAssembler<HPT> assembler = HeliportAssembler.usingStrategy(arincTerminalAreaDatabase, heliportAssemblyStrategy);
    return assembler.assemble(heliports).toList();
  }

  public static final class Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> {

    private final ArincVersion version;

    private Predicate<ArincRecord> dropRecord = new ContinuationRecordFilter().negate();

    private AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;

    private FixAssemblyStrategy<FIX> fixStrategy;

    private AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;

    private ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;

    private FirUirAssemblyStrategy<AIR, ASEQ> firUirStrategy;

    private ControlledAirspaceAssemblyStrategy<AIR, FIX, ASEQ> controlledAirspaceStrategy;

    private HeliportAssemblyStrategy<HPT, HLPD> heliportAssemblyStrategy;

    private Builder(ArincVersion version) {
      this.version = requireNonNull(version);
    }

    /**
     * Configure a filter which sits between the initial {@link ArincRecord} extraction and the converter implementation which
     * maps those generic records to concrete Java models like {@link ArincProcedureLeg}.
     *
     * @param dropRecord indicates the given record should be dropped prior to conversion, typically used to drop continuation
     *                   records which aren't otherwise being handled
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> dropRecord(Predicate<ArincRecord> dropRecord) {
      this.dropRecord = requireNonNull(dropRecord);
      return this;
    }

    /**
     * See the documentation on {@link AirportAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> airportStrategy(AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy) {
      this.airportStrategy = requireNonNull(airportStrategy);
      return this;
    }

    /**
     * See the documentation on {@link FixAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> fixStrategy(FixAssemblyStrategy<FIX> fixStrategy) {
      this.fixStrategy = requireNonNull(fixStrategy);
      return this;
    }

    /**
     * See the documentation on {@link AirwayAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> airwayStrategy(AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy) {
      this.airwayStrategy = requireNonNull(airwayStrategy);
      return this;
    }

    /**
     * See the documentation on {@link ProcedureAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> procedureStrategy(ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy) {
      this.procedureStrategy = requireNonNull(procedureStrategy);
      return this;
    }

    /**
     * See docs on {@link FirUirAssemblyStrategy}
     * @param firUirStrategy the strategy to use in this assembler
     * @return this builder.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> firUirStrategy(FirUirAssemblyStrategy<AIR, ASEQ> firUirStrategy) {
      this.firUirStrategy = requireNonNull(firUirStrategy);
      return this;
    }

    /**
     * See docs on {@link ControlledAirspaceAssemblyStrategy}
     * @param controlledAirspaceStrategy the one to use in this parser
     * @return this builder
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> controlledAirspaceStrategy(ControlledAirspaceAssemblyStrategy<AIR, FIX, ASEQ> controlledAirspaceStrategy) {
      this.controlledAirspaceStrategy = controlledAirspaceStrategy;
      return this;
    }

    /**
     *  See docs on {@link HeliportAssemblyStrategy}
     * @param heliportAssemblyStrategy the to use in this parser
     * @return this builder
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> heliportAssemblyStrategy(HeliportAssemblyStrategy<HPT, HLPD> heliportAssemblyStrategy) {
      this.heliportAssemblyStrategy = heliportAssemblyStrategy;
      return this;
    }

    public OneshotRecordParser<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> build() {
      return new OneshotRecordParser<>(this);
    }
  }

  /**
   * Wrapper class containing assembled client records of the templated types assembled by the oneshot parser.
   */
  public static final class ClientRecords<APT, FIX, AWY, PRC, AIR, HPT> {

    private final Collection<APT> airports;

    private final Collection<FIX> fixes;

    private final Collection<AWY> airways;

    private final Collection<PRC> procedures;

    private final Collection<AIR> firUirs;

    private final Collection<AIR> conrolledAirspaces;

    private final Collection<HPT> heliports;

    private ClientRecords(Builder<APT, FIX, AWY, PRC, AIR, HPT> builder) {
      this.airports = builder.airports;
      this.fixes = builder.fixes;
      this.airways = builder.airways;
      this.procedures = builder.procedures;
      this.firUirs = builder.firUirs;
      this.conrolledAirspaces = builder.conrolledAirspaces;
      this.heliports = builder.heliports;
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

    public Collection<AIR> firUirs() {
      return firUirs;
    }

    public Collection<AIR> conrolledAirspaces() {
      return conrolledAirspaces;
    }

    public Collection<HPT> heliports() {
      return heliports;
    }

    public static final class Builder<APT, FIX, AWY, PRC, AIR, HPT> {

      private final Collection<APT> airports = new ArrayList<>();

      private final Collection<FIX> fixes = new ArrayList<>();

      private final Collection<AWY> airways = new ArrayList<>();

      private final Collection<PRC> procedures = new ArrayList<>();

      private final Collection<AIR> firUirs = new ArrayList<>();

      private final Collection<AIR> conrolledAirspaces = new ArrayList<>();

      private final Collection<HPT> heliports = new ArrayList<>();

      private Builder() {
      }

      public Builder<APT, FIX, AWY, PRC, AIR, HPT> addAirports(Collection<APT> airports) {
        this.airports.addAll(airports);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR, HPT> addFixes(Collection<FIX> fixes) {
        this.fixes.addAll(fixes);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR, HPT> addAirways(Collection<AWY> airways) {
        this.airways.addAll(airways);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR, HPT> addProcedures(Collection<PRC> procedures) {
        this.procedures.addAll(procedures);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR, HPT> addFirUirs(Collection<AIR> firUirs) {
        this.firUirs.addAll(firUirs);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR, HPT> addControlledAirspaces(Collection<AIR> conrolledAirspaces) {
        this.conrolledAirspaces.addAll(conrolledAirspaces);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR, HPT> addHeliports(Collection<HPT> heliports) {
        this.heliports.addAll(heliports);
        return this;
      }

      public ClientRecords<APT, FIX, AWY, PRC, AIR, HPT> build() {
        return new ClientRecords<>(this);
      }
    }
  }
}
