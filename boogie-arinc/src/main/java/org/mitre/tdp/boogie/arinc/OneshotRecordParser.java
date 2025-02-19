package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
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
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.arinc.assemble.AirportAssembler;
import org.mitre.tdp.boogie.arinc.assemble.AirportAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.AirwayAssembler;
import org.mitre.tdp.boogie.arinc.assemble.AirwayAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.FirUirAssembler;
import org.mitre.tdp.boogie.arinc.assemble.FirUirAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.FixAssembler;
import org.mitre.tdp.boogie.arinc.assemble.FixAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.assemble.ProcedureAssembler;
import org.mitre.tdp.boogie.arinc.assemble.ProcedureAssemblyStrategy;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;
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
public final class OneshotRecordParser<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> {

  private static final Logger LOG = LoggerFactory.getLogger(OneshotRecordParser.class);

  private final ArincVersion version;

  private final Predicate<ArincRecord> dropRecord;

  private final AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;

  private final FixAssemblyStrategy<FIX> fixStrategy;

  private final AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;

  private final ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;

  private final FirUirAssemblyStrategy<AIR, ASEQ> firUirStrategy;

  private OneshotRecordParser(Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> builder) {
    this.version = requireNonNull(builder.version);
    this.dropRecord = requireNonNull(builder.dropRecord);
    this.airportStrategy = requireNonNull(builder.airportStrategy);
    this.fixStrategy = FixAssemblyStrategy.caching(builder.fixStrategy);
    this.airwayStrategy = requireNonNull(builder.airwayStrategy);
    this.procedureStrategy = requireNonNull(builder.procedureStrategy);
    this.firUirStrategy = requireNonNull(builder.firUirStrategy);
  }

  /**
   * Instantiate a new buildable version of the oneshot parser which can be used to construct the user-defined data models given
   * the configured strategy classes.
   */
  public static <APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> builder(ArincVersion version) {
    return new Builder<>(version);
  }

  /**
   * Instantiate a standard oneshot parser targeting the default implementations of the Boogie interfaces packaged alongside the
   * library.
   */
  public static OneshotRecordParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Airspace, AirspaceSequence, Helipad> standard(ArincVersion version) {
    return OneshotRecordParser.<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Airspace, AirspaceSequence, Helipad>builder(version)
        .airportStrategy(AirportAssemblyStrategy.standard())
        .fixStrategy(FixAssemblyStrategy.standard())
        .airwayStrategy(AirwayAssemblyStrategy.standard())
        .procedureStrategy(ProcedureAssemblyStrategy.standard())
        .firUirStrategy(FirUirAssemblyStrategy.standard())
        .build();
  }

  /**
   * Assembles the collection of typed client records from an underlying 424 file represented as an {@link InputStream}. Typically
   * this will be called with a {@link FileInputStream} but should be flexible enough to handle other sources.
   *
   * @param inputStream an input stream containing the bytes of a 424 file
   */
  public ClientRecords<APT, FIX, AWY, PRC, AIR> assembleFrom(InputStream inputStream) {
    requireNonNull(inputStream);

    ClientRecords.Builder<APT, FIX, AWY, PRC, AIR> records = new ClientRecords.Builder<>();

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

    return assembler.assemble(procedureLegs).collect(toList());
  }

  private Collection<AWY> assembleAirways(ArincFixDatabase arincFixDatabase, Collection<ArincAirwayLeg> airwayLeg) {
    AirwayAssembler<AWY> assembler = AirwayAssembler.usingStrategy(arincFixDatabase, fixStrategy, airwayStrategy);
    return assembler.assemble(airwayLeg).collect(toList());
  }

  private Collection<FIX> assembleFixes(Collection<ArincWaypoint> waypoints, Collection<ArincNdbNavaid> ndbs,
      Collection<ArincVhfNavaid> vhfs) {

    FixAssembler<FIX> assembler = FixAssembler.withStrategy(fixStrategy);

    return Stream.of(waypoints.stream(), ndbs.stream(), vhfs.stream())
        .flatMap(stream -> stream)
        .map(assembler::assemble)
        .collect(toList());
  }

  private Collection<APT> assembleAirports(ArincTerminalAreaDatabase arincTerminalAreaDatabase, Collection<ArincAirport> airports) {
    AirportAssembler<APT> assembler = AirportAssembler.usingStrategy(arincTerminalAreaDatabase, airportStrategy);
    return airports.stream().map(assembler::assemble).collect(toList());
  }

  private Collection<AIR> assembleFirUirs(Collection<ArincFirUirLeg> legs) {
    FirUirAssembler<AIR> assembler = FirUirAssembler.usingStrategy(firUirStrategy);
    return assembler.assemble(legs).collect(toList());
  }

  private Collection<ArincRecord> parseRecords(InputStream inputStream) {
    return new ArincFileParser(ArincRecordParser.standard(version.specs())).apply(inputStream);
  }

  public static final class Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> {

    private final ArincVersion version;

    private Predicate<ArincRecord> dropRecord = new ContinuationRecordFilter().negate();

    private AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;

    private FixAssemblyStrategy<FIX> fixStrategy;

    private AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;

    private ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;

    private FirUirAssemblyStrategy<AIR, ASEQ> firUirStrategy;

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
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> dropRecord(Predicate<ArincRecord> dropRecord) {
      this.dropRecord = requireNonNull(dropRecord);
      return this;
    }

    /**
     * See the documentation on {@link AirportAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> airportStrategy(AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy) {
      this.airportStrategy = requireNonNull(airportStrategy);
      return this;
    }

    /**
     * See the documentation on {@link FixAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> fixStrategy(FixAssemblyStrategy<FIX> fixStrategy) {
      this.fixStrategy = requireNonNull(fixStrategy);
      return this;
    }

    /**
     * See the documentation on {@link AirwayAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> airwayStrategy(AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy) {
      this.airwayStrategy = requireNonNull(airwayStrategy);
      return this;
    }

    /**
     * See the documentation on {@link ProcedureAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> procedureStrategy(ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy) {
      this.procedureStrategy = requireNonNull(procedureStrategy);
      return this;
    }

    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> firUirStrategy(FirUirAssemblyStrategy<AIR, ASEQ> firUirStrategy) {
      this.firUirStrategy = requireNonNull(firUirStrategy);
      return this;
    }

    public OneshotRecordParser<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD> build() {
      return new OneshotRecordParser<>(this);
    }
  }

  /**
   * Wrapper class containing assembled client records of the templated types assembled by the oneshot parser.
   */
  public static final class ClientRecords<APT, FIX, AWY, PRC, AIR> {

    private final Collection<APT> airports;

    private final Collection<FIX> fixes;

    private final Collection<AWY> airways;

    private final Collection<PRC> procedures;

    private final Collection<AIR> firUirs;

    private ClientRecords(Builder<APT, FIX, AWY, PRC, AIR> builder) {
      this.airports = builder.airports;
      this.fixes = builder.fixes;
      this.airways = builder.airways;
      this.procedures = builder.procedures;
      this.firUirs = builder.firUirs;
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

    public static final class Builder<APT, FIX, AWY, PRC, AIR> {

      private final Collection<APT> airports = new ArrayList<>();

      private final Collection<FIX> fixes = new ArrayList<>();

      private final Collection<AWY> airways = new ArrayList<>();

      private final Collection<PRC> procedures = new ArrayList<>();

      private final Collection<AIR> firUirs = new ArrayList<>();

      private Builder() {
      }

      public Builder<APT, FIX, AWY, PRC, AIR> addAirports(Collection<APT> airports) {
        this.airports.addAll(airports);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR> addFixes(Collection<FIX> fixes) {
        this.fixes.addAll(fixes);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR> addAirways(Collection<AWY> airways) {
        this.airways.addAll(airways);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR> addProcedures(Collection<PRC> procedures) {
        this.procedures.addAll(procedures);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR> addFirUirs(Collection<AIR> firUirs) {
        this.firUirs.addAll(firUirs);
        return this;
      }

      public ClientRecords<APT, FIX, AWY, PRC, AIR> build() {
        return new ClientRecords<>(this);
      }
    }
  }
}
