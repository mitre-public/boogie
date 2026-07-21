package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory.consumerForVersion;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
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
import org.mitre.tdp.boogie.arinc.model.ArincGnssLandingSystem;
import org.mitre.tdp.boogie.arinc.model.ArincHeaderOne;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.mitre.tdp.boogie.arinc.v18.Header01Spec;
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

  private final Predicate<ArincRecord> keepRecord;

  private final AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;

  private final FixAssemblyStrategy<FIX> fixStrategy;

  private final AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;

  private final ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;

  private final FirUirAssemblyStrategy<AIR, ASEQ> firUirStrategy;

  private final ControlledAirspaceAssemblyStrategy<AIR, FIX, ASEQ> controlledAirspaceStrategy;

  private final HeliportAssemblyStrategy<HPT, HLPD> heliportStrategy;

  private OneshotRecordParser(Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> builder) {
    this.version = requireNonNull(builder.version);
    this.keepRecord = requireNonNull(builder.keepRecord);
    this.airportStrategy = requireNonNull(builder.airportStrategy);
    this.fixStrategy = FixAssemblyStrategy.caching(builder.fixStrategy);
    this.airwayStrategy = requireNonNull(builder.airwayStrategy);
    this.procedureStrategy = requireNonNull(builder.procedureStrategy);
    this.firUirStrategy = requireNonNull(builder.firUirStrategy);
    this.controlledAirspaceStrategy = requireNonNull(builder.controlledAirspaceStrategy);
    this.heliportStrategy = requireNonNull(builder.heliportStrategy);
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

    ConvertingArincRecordConsumer consumer = consumerForVersion(version);
    if (!consumeInto(inputStream, consumer)) {
      return new ClientRecords.Builder<APT, FIX, AWY, PRC, AIR, HPT>().build();
    }
    return assemble(RecordSets.of(consumer));
  }

  /**
   * Assembles one collection of typed client records from <em>multiple</em> prioritized 424 sources — e.g. a hand-built patch
   * file layered over a full published cycle, or local overrides layered over a database-served subset of a cycle.
   *
   * <p>Sources <strong>earlier in the list take precedence</strong>: when the same record (by its ARINC identity — the spec key
   * fields of its type, e.g. {@code (airport, region, runway)} for a runway or {@code (airport, region, subsection, procedure,
   * route type, transition, sequence)} for a procedure leg) appears in more than one source, the earliest source's version is
   * kept and later ones are dropped. Records unique to any source are all retained. Precedence is deterministic and by record
   * identity — a later source cannot "half override" a record by differing in content only.
   *
   * <p>All sources are merged <em>before</em> assembly, so cross-source references resolve: a patch source's procedure legs may
   * reference fixes, navaids, or runways defined only in the base source and still assemble complete. The
   * {@link ClientRecords#headerOne() header} is the first one present in source order.
   *
   * @param orderedSources the 424 sources, highest-precedence first; an empty list yields empty {@link ClientRecords}
   */
  public ClientRecords<APT, FIX, AWY, PRC, AIR, HPT> assembleFrom(List<InputStream> orderedSources) {
    requireNonNull(orderedSources);

    List<ConvertingArincRecordConsumer> consumers = new ArrayList<>(orderedSources.size());
    for (InputStream source : orderedSources) {
      ConvertingArincRecordConsumer consumer = consumerForVersion(version);
      if (!consumeInto(source, consumer)) {
        return new ClientRecords.Builder<APT, FIX, AWY, PRC, AIR, HPT>().build();
      }
      consumers.add(consumer);
    }
    return assemble(RecordSets.merged(consumers));
  }

  /** Parse and convert one source into the consumer; false (after logging) when the source cannot be read. */
  private boolean consumeInto(InputStream inputStream, ConvertingArincRecordConsumer consumer) {
    ArincRecordParser parser = ArincRecordParser.standard(version.specs());

    try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
      reader.lines()
          .map(parser::parse)
          .flatMap(Optional::stream)
          .filter(keepRecord)
          .forEach(consumer);
      return true;
    } catch (IOException e) {
      LOG.error("Could not parse the arinc text into memory", e);
      return false;
    }
  }

  private ClientRecords<APT, FIX, AWY, PRC, AIR, HPT> assemble(RecordSets records) {
    LOG.debug("Finished parsing and converting supported record types.");

    ArincFixDatabase arincFixDatabase = ArincDatabaseFactory.newFixDatabase(
        records.ndbNavaids,
        records.vhfNavaids,
        records.waypoints,
        records.airports,
        records.holdingPatterns,
        records.heliports
    );
    LOG.debug("Finished instantiation of FixDatabase.");

    ArincTerminalAreaDatabase arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        records.airports,
        records.runways,
        records.localizerGlideSlopes,
        records.ndbNavaids,
        records.vhfNavaids,
        records.waypoints,
        records.procedureLegs,
        records.gnssLandingSystems,
        records.helipads,
        records.heliports
    );
    LOG.debug("Finished instantiation of TerminalAreaDatabase.");

    return new ClientRecords.Builder<APT, FIX, AWY, PRC, AIR, HPT>()
        .addAirports(assembleAirports(arincTerminalAreaDatabase, records.airports))
        .addFixes(assembleFixes(records.waypoints, records.ndbNavaids, records.vhfNavaids))
        .addAirways(assembleAirways(arincFixDatabase, records.airwayLegs))
        .addProcedures(assembleProcedures(arincFixDatabase, arincTerminalAreaDatabase, records.procedureLegs))
        .addFirUirs(assembleFirUirs(records.firUirLegs))
        .addControlledAirspaces(assembleControlledAirspaces(arincFixDatabase, records.controlledAirspaceLegs))
        .headerOne(records.headerOne.orElse(null))
        .addHeliport(assembleHeliports(arincTerminalAreaDatabase, records.heliports))
        .build();
  }

  /**
   * The converted-record collections assembly consumes, either passed straight through from one consumer or merged across
   * several prioritized consumers with earliest-source-wins resolution per record identity.
   */
  private static final class RecordSets {

    private final Collection<ArincAirport> airports;
    private final Collection<ArincRunway> runways;
    private final Collection<ArincLocalizerGlideSlope> localizerGlideSlopes;
    private final Collection<ArincNdbNavaid> ndbNavaids;
    private final Collection<ArincVhfNavaid> vhfNavaids;
    private final Collection<ArincWaypoint> waypoints;
    private final Collection<ArincAirwayLeg> airwayLegs;
    private final Collection<ArincProcedureLeg> procedureLegs;
    private final Collection<ArincGnssLandingSystem> gnssLandingSystems;
    private final Collection<ArincHoldingPattern> holdingPatterns;
    private final Collection<ArincFirUirLeg> firUirLegs;
    private final Collection<ArincControlledAirspaceLeg> controlledAirspaceLegs;
    private final Collection<ArincHelipad> helipads;
    private final Collection<ArincHeliport> heliports;
    private final Optional<ArincHeaderOne> headerOne;

    private RecordSets(
        Collection<ArincAirport> airports,
        Collection<ArincRunway> runways,
        Collection<ArincLocalizerGlideSlope> localizerGlideSlopes,
        Collection<ArincNdbNavaid> ndbNavaids,
        Collection<ArincVhfNavaid> vhfNavaids,
        Collection<ArincWaypoint> waypoints,
        Collection<ArincAirwayLeg> airwayLegs,
        Collection<ArincProcedureLeg> procedureLegs,
        Collection<ArincGnssLandingSystem> gnssLandingSystems,
        Collection<ArincHoldingPattern> holdingPatterns,
        Collection<ArincFirUirLeg> firUirLegs,
        Collection<ArincControlledAirspaceLeg> controlledAirspaceLegs,
        Collection<ArincHelipad> helipads,
        Collection<ArincHeliport> heliports,
        Optional<ArincHeaderOne> headerOne) {
      this.airports = airports;
      this.runways = runways;
      this.localizerGlideSlopes = localizerGlideSlopes;
      this.ndbNavaids = ndbNavaids;
      this.vhfNavaids = vhfNavaids;
      this.waypoints = waypoints;
      this.airwayLegs = airwayLegs;
      this.procedureLegs = procedureLegs;
      this.gnssLandingSystems = gnssLandingSystems;
      this.holdingPatterns = holdingPatterns;
      this.firUirLegs = firUirLegs;
      this.controlledAirspaceLegs = controlledAirspaceLegs;
      this.helipads = helipads;
      this.heliports = heliports;
      this.headerOne = headerOne;
    }

    /** One source: the consumer's collections pass straight through, identical to historical single-stream behavior. */
    static RecordSets of(ConvertingArincRecordConsumer consumer) {
      return new RecordSets(
          consumer.arincAirports(),
          consumer.arincRunways(),
          consumer.arincLocalizerGlideSlopes(),
          consumer.arincNdbNavaids(),
          consumer.arincVhfNavaids(),
          consumer.arincWaypoints(),
          consumer.arincAirwayLegs(),
          consumer.arincProcedureLegs(),
          consumer.arincGnssLandingSystems(),
          consumer.arincHoldingPatterns(),
          consumer.arincFirUirLegs(),
          consumer.arincControlledAirspaceLegs(),
          consumer.arincHelipads(),
          consumer.arincHeliports(),
          consumer.arincHeaderOne()
      );
    }

    /** Several prioritized sources: earliest source wins per record identity; the header is the first one present. */
    static RecordSets merged(List<ConvertingArincRecordConsumer> ordered) {
      return new RecordSets(
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincAirports, RecordSets::airportIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincRunways, RecordSets::runwayIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincLocalizerGlideSlopes, RecordSets::localizerIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincNdbNavaids, RecordSets::ndbIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincVhfNavaids, RecordSets::vhfIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincWaypoints, RecordSets::waypointIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincAirwayLegs, RecordSets::airwayLegIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincProcedureLegs, RecordSets::procedureLegIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincGnssLandingSystems, RecordSets::glsIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincHoldingPatterns, RecordSets::holdingPatternIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincFirUirLegs, RecordSets::firUirLegIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincControlledAirspaceLegs, RecordSets::controlledAirspaceLegIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincHelipads, RecordSets::helipadIdentity),
          mergeByIdentity(ordered, ConvertingArincRecordConsumer::arincHeliports, RecordSets::heliportIdentity),
          ordered.stream().map(ConvertingArincRecordConsumer::arincHeaderOne).flatMap(Optional::stream).findFirst()
      );
    }

    /**
     * Union the sources' records of one type, keeping the first record seen for each identity — so earlier (higher-precedence)
     * sources shadow later ones, and a duplicated identity within one source keeps its first occurrence.
     */
    private static <T> Collection<T> mergeByIdentity(List<ConvertingArincRecordConsumer> ordered,
        Function<ConvertingArincRecordConsumer, Collection<T>> records, Function<T, Object> identity) {

      Map<Object, T> merged = new LinkedHashMap<>();
      for (ConvertingArincRecordConsumer source : ordered) {
        for (T record : records.apply(source)) {
          merged.putIfAbsent(identity.apply(record), record);
        }
      }
      return List.copyOf(merged.values());
    }

    // Record identities: the ARINC spec key fields of each converted type. Two records with equal identity describe the same
    // logical entity (possibly with different content) and are candidates for cross-source shadowing.

    private static Object airportIdentity(ArincAirport airport) {
      return Arrays.asList(airport.airportIdentifier(), airport.airportIcaoRegion());
    }

    private static Object runwayIdentity(ArincRunway runway) {
      return Arrays.asList(runway.airportIdentifier(), runway.airportIcaoRegion(), runway.runwayIdentifier());
    }

    private static Object localizerIdentity(ArincLocalizerGlideSlope localizer) {
      return Arrays.asList(localizer.airportIdentifier(), localizer.airportIcaoRegion(),
          localizer.localizerIdentifier(), localizer.runwayIdentifier());
    }

    private static Object ndbIdentity(ArincNdbNavaid ndb) {
      return Arrays.asList(ndb.sectionCode(), ndb.subSectionCode().orElse(null),
          ndb.airportIdentifier().orElse(null), ndb.ndbIdentifier(), ndb.ndbIcaoRegion());
    }

    private static Object vhfIdentity(ArincVhfNavaid vhf) {
      return Arrays.asList(vhf.airportIdentifier().orElse(null), vhf.vhfIdentifier(), vhf.vhfIcaoRegion());
    }

    private static Object waypointIdentity(ArincWaypoint waypoint) {
      return Arrays.asList(waypoint.sectionCode(), waypoint.subSectionCode().orElse(null),
          waypoint.airportIdentifier().orElse(null), waypoint.waypointIdentifier(), waypoint.waypointIcaoRegion());
    }

    private static Object airwayLegIdentity(ArincAirwayLeg leg) {
      return Arrays.asList(leg.routeIdentifier(), leg.sequenceNumber(), leg.fixIdentifier(), leg.fixIcaoRegion());
    }

    private static Object procedureLegIdentity(ArincProcedureLeg leg) {
      return Arrays.asList(leg.airportIdentifier(), leg.airportIcaoRegion(), leg.subSectionCode().orElse(null),
          leg.sidStarIdentifier(), leg.routeType(), leg.transitionIdentifier().orElse(null), leg.sequenceNumber());
    }

    private static Object glsIdentity(ArincGnssLandingSystem gls) {
      return Arrays.asList(gls.airportIdentifier(), gls.airportIcaoRegion(),
          gls.glsRefPathIdentifier(), gls.runwayIdentifier());
    }

    private static Object holdingPatternIdentity(ArincHoldingPattern holdingPattern) {
      return Arrays.asList(holdingPattern.regionCode().orElse(null), holdingPattern.icaoRegion().orElse(null),
          holdingPattern.fixIdentifier(), holdingPattern.fixIcaoRegion(),
          holdingPattern.duplicateIdentifier().orElse(null));
    }

    private static Object firUirLegIdentity(ArincFirUirLeg leg) {
      return Arrays.asList(leg.firUirIdentifier(), leg.firUirAddress().orElse(null),
          leg.firUirIndicator(), leg.sequenceNumber());
    }

    private static Object controlledAirspaceLegIdentity(ArincControlledAirspaceLeg leg) {
      return Arrays.asList(leg.icaoRegion(), leg.airspaceType(), leg.airspaceCenter(),
          leg.multipleCode().orElse(null), leg.sequenceNumber());
    }

    private static Object helipadIdentity(ArincHelipad helipad) {
      return Arrays.asList(helipad.airportHeliportIdentifier(), helipad.icaoCode(), helipad.helipadIdentifier());
    }

    private static Object heliportIdentity(ArincHeliport heliport) {
      return Arrays.asList(heliport.heliportIdentifier(), heliport.heliportIcaoRegion(),
          heliport.padIdentifier().orElse(null));
    }
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
    HeliportAssembler<HPT> assembler = HeliportAssembler.usingStrategy(arincTerminalAreaDatabase, heliportStrategy);
    return heliports.stream().map(assembler::assemble).toList();
  }

  public static final class Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> {

    private final ArincVersion version;

    private Predicate<ArincRecord> keepRecord = new IsThisAHeader().or(new IsThisAPrimaryRecord());

    private AirportAssemblyStrategy<APT, RWY, HLPD> airportStrategy;

    private FixAssemblyStrategy<FIX> fixStrategy;

    private AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;

    private ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;

    private FirUirAssemblyStrategy<AIR, ASEQ> firUirStrategy;

    private ControlledAirspaceAssemblyStrategy<AIR, FIX, ASEQ> controlledAirspaceStrategy;

    private HeliportAssemblyStrategy<HPT, HLPD> heliportStrategy;

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
      this.keepRecord = requireNonNull(dropRecord);
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
     * See docs on {@link HeliportAssemblyStrategy}
     * @param heliportAssemblyStrategy the one to use in this parser
     * @return this builder
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ, HLPD, HPT> heliportAssemblyStrategy(HeliportAssemblyStrategy<HPT, HLPD> heliportAssemblyStrategy) {
      this.heliportStrategy = requireNonNull(heliportAssemblyStrategy);
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

    private final ArincHeaderOne headerOne;

    private final Collection<HPT> heliports;

    private ClientRecords(Builder<APT, FIX, AWY, PRC, AIR, HPT> builder) {
      this.airports = builder.airports;
      this.fixes = builder.fixes;
      this.airways = builder.airways;
      this.procedures = builder.procedures;
      this.firUirs = builder.firUirs;
      this.conrolledAirspaces = builder.conrolledAirspaces;
      this.headerOne = builder.headerOne;
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

    public Optional<ArincHeaderOne> headerOne() {
      return Optional.ofNullable(headerOne);
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

      private ArincHeaderOne headerOne;

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

      public Builder<APT, FIX, AWY, PRC, AIR, HPT> headerOne(ArincHeaderOne headerOne) {
        this.headerOne = headerOne;
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR, HPT> addHeliport(Collection<HPT> heliports) {
        this.heliports.addAll(heliports);
        return this;
      }

      public ClientRecords<APT, FIX, AWY, PRC, AIR, HPT> build() {
        return new ClientRecords<>(this);
      }
    }
  }
}
