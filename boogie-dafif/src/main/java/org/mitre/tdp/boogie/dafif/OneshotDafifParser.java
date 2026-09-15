package org.mitre.tdp.boogie.dafif;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.model.DafifRecordConverterFactory.consumerForVersion;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.dafif.assemble.AirportAssembler;
import org.mitre.tdp.boogie.dafif.assemble.AirportAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.AirwayAssembler;
import org.mitre.tdp.boogie.dafif.assemble.AirwayAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.BoundaryAssembler;
import org.mitre.tdp.boogie.dafif.assemble.BoundaryAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.FixAssembler;
import org.mitre.tdp.boogie.dafif.assemble.FixAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.ProcedureAssembler;
import org.mitre.tdp.boogie.dafif.assemble.ProcedureAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.SuasAssembler;
import org.mitre.tdp.boogie.dafif.assemble.SuasAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.ConvertingDafifRecordConsumer;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifBoundaryParent;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifSuasParent;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Oneshot implementation of a parser going from an {@link InputStream} (typically a DAFIF zip file) to a collection of
 * client-defined assembled records.
 *
 * <p>This class mirrors the ARINC {@code OneshotRecordParser} pattern. It reads a DAFIF zip, parses all supported record
 * types, builds the necessary lookup databases, and assembles airports, fixes, airways, procedures, and airspaces in one pass.
 *
 * <p>Use the {@link #standard(DafifVersion)} factory for default Boogie implementations, or the {@link Builder} for custom
 * assembly strategies.
 *
 * @param <APT> the airport type
 * @param <RWY> the runway type
 * @param <FIX> the fix type
 * @param <LEG> the leg type
 * @param <TRS> the transition type
 * @param <AWY> the airway type
 * @param <PRC> the procedure type
 * @param <AIR> the airspace type
 * @param <ASEQ> the airspace sequence type
 */
public final class OneshotDafifParser<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> {

  private static final Logger LOG = LoggerFactory.getLogger(OneshotDafifParser.class);

  private static final Set<String> PARSEABLE_FILES = Set.of(
      "ARPT.TXT", "RWY.TXT", "ADD_RWY.TXT", "ILS.TXT", "NAV.TXT", "WPT.TXT", "TRM_PAR.TXT", "TRM_SEG.TXT", "ATS.TXT",
      "BDRY.TXT", "BDRY_PAR.TXT", "SUAS.TXT", "SUAS_PAR.TXT"
  );

  private final DafifVersion version;
  private final AirportAssemblyStrategy<APT, RWY> airportStrategy;
  private final FixAssemblyStrategy<FIX> fixStrategy;
  private final AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;
  private final ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;

  private final BoundaryAssemblyStrategy<AIR, ASEQ> boundaryStrategy;

  private final SuasAssemblyStrategy<AIR, ASEQ> suasStrategy;

  private OneshotDafifParser(Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> builder) {
    this.version = requireNonNull(builder.version);
    this.airportStrategy = requireNonNull(builder.airportStrategy);
    this.fixStrategy = requireNonNull(builder.fixStrategy);
    this.airwayStrategy = requireNonNull(builder.airwayStrategy);
    this.procedureStrategy = requireNonNull(builder.procedureStrategy);
    this.boundaryStrategy = requireNonNull(builder.boundaryStrategy);
    this.suasStrategy = requireNonNull(builder.suasStrategy);
  }

  /**
   * Instantiate a new buildable version of the oneshot parser which can be used to construct user-defined data models given
   * the configured strategy classes.
   */
  public static <APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> builder(DafifVersion version) {
    return new Builder<>(version);
  }

  /**
   * Instantiate a standard oneshot parser targeting the default implementations of the Boogie interfaces.
   */
  public static OneshotDafifParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Airspace, AirspaceSequence> standard(DafifVersion version) {
    return OneshotDafifParser.<Airport, Runway, Fix, Leg, Transition, Airway, Procedure, Airspace, AirspaceSequence>builder(version)
        .airportStrategy(AirportAssemblyStrategy.standard())
        .airwayStrategy(AirwayAssemblyStrategy.standard())
        .procedureStrategy(ProcedureAssemblyStrategy.standard())
        .fixStrategy(FixAssemblyStrategy.standard())
        .boundaryStrategy(BoundaryAssemblyStrategy.standard())
        .suasStrategy(SuasAssemblyStrategy.standard())
        .build();
  }

  /**
   * Assembles the collection of typed client records from a DAFIF zip file represented as an {@link InputStream}.
   *
   * @param inputStream an input stream containing the bytes of a DAFIF zip file
   */
  public ClientRecords<APT, FIX, AWY, PRC, AIR> assembleFrom(InputStream inputStream) {
    requireNonNull(inputStream);

    ConvertingDafifRecordConsumer consumer = parse(inputStream);
    LOG.debug("Finished parsing and converting supported record types.");

    DafifFixDatabase fixDatabase = DafifDatabaseFactory.newFixDatabase(consumer.dafifWaypoints(), consumer.dafifNavaids());
    LOG.debug("Finished instantiation of FixDatabase.");

    DafifTerminalAreaDatabase terminalAreaDatabase = DafifDatabaseFactory.newTerminalAreaDatabase(consumer);
    LOG.debug("Finished instantiation of TerminalAreaDatabase.");

    return new ClientRecords.Builder<APT, FIX, AWY, PRC, AIR>()
        .addAirports(assembleAirports(terminalAreaDatabase, consumer.dafifAirports()))
        .addFixes(assembleFixes(terminalAreaDatabase, fixDatabase, consumer.dafifWaypoints(), consumer.dafifNavaids()))
        .addAirways(assembleAirways(fixDatabase, consumer.dafifAts()))
        .addProcedures(assembleProcedures(fixDatabase, terminalAreaDatabase, consumer.dafifTerminalParents()))
        .addBoundaries(assembleBoundaries(consumer.dafifBoundaryParents(), consumer.dafifBoundarySegments()))
        .addSpecialUseAirspaces(assembleSpecialUseAirspaces(consumer.dafifSuasParents(), consumer.dafifSuasSegments()))
        .build();
  }

  private ConvertingDafifRecordConsumer parse(InputStream inputStream) {
    DafifFileParser parser = new DafifFileParser(version);
    ConvertingDafifRecordConsumer consumer = consumerForVersion(version);
    try (ZipInputStream zis = new ZipInputStream(inputStream)) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        if (entry.isDirectory()) continue;
        String name = entry.getName();
        if (!name.contains("DAFIFT/") || name.contains("TRMH/") || name.contains("SUPPH/")) continue;
        String file = filename(name);
        if (!PARSEABLE_FILES.contains(file)) continue;
        LOG.debug("Parsing zip entry: {}", name);
        try (Stream<DafifRecord> records = parser.stream(new NonClosingInputStream(zis), file)) {
          records.forEach(consumer);
        }
      }
    } catch (IOException e) {
      LOG.error("Could not parse the DAFIF zip into memory", e);
    }
    return consumer;
  }

  private Collection<APT> assembleAirports(DafifTerminalAreaDatabase tad, Collection<DafifAirport> airports) {
    AirportAssembler<APT> assembler = AirportAssembler.usingStrategy(tad, airportStrategy);
    return airports.stream().map(assembler::assemble).toList();
  }

  private Collection<FIX> assembleFixes(DafifTerminalAreaDatabase tad, DafifFixDatabase fdb, Collection<DafifWaypoint> waypoints, Collection<DafifNavaid> navaids) {
    FixAssembler<FIX> assembler = FixAssembler.withStrategy(tad, fdb, fixStrategy);
    return Stream.concat(waypoints.stream(), navaids.stream())
        .flatMap(model -> assembler.assemble(model).stream())
        .toList();
  }

  private Collection<AWY> assembleAirways(DafifFixDatabase fixDatabase,
                                           Collection<org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment> atsSegments) {
    AirwayAssembler<AWY> assembler = AirwayAssembler.withStrategy(fixDatabase, fixStrategy, airwayStrategy);
    return assembler.assemble(atsSegments).toList();
  }

  private Collection<PRC> assembleProcedures(DafifFixDatabase fixDatabase, DafifTerminalAreaDatabase tad,
                                              Collection<org.mitre.tdp.boogie.dafif.model.DafifTerminalParent> parents) {
    ProcedureAssembler<PRC> assembler = ProcedureAssembler.withStrategy(tad, fixDatabase, procedureStrategy, fixStrategy);
    return assembler.assemble(parents).toList();
  }

  private Collection<AIR> assembleBoundaries(Collection<DafifBoundaryParent> parents, Collection<DafifBoundarySegment> segments) {
    BoundaryAssembler<AIR> assembler = BoundaryAssembler.usingStrategy(boundaryStrategy);
    return assembler.assemble(parents, segments).toList();
  }

  private Collection<AIR> assembleSpecialUseAirspaces(Collection<DafifSuasParent> parents, Collection<DafifSuasSegment> segments) {
    SuasAssembler<AIR> assembler = SuasAssembler.usingStrategy(suasStrategy);
    return assembler.assemble(parents, segments).toList();
  }

  private static String filename(String entryName) {
    return entryName.contains("/") ? entryName.substring(entryName.lastIndexOf('/') + 1) : entryName;
  }

  /**
   * Wrapper that prevents the underlying stream from being closed, so that {@link DafifFileParser} doesn't
   * close the {@link ZipInputStream} when it finishes reading an entry.
   */
  private static final class NonClosingInputStream extends FilterInputStream {
    NonClosingInputStream(InputStream in) {
      super(in);
    }

    @Override
    public void close() {
      // intentionally do not close the underlying stream
    }
  }

  public static final class Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> {

    private final DafifVersion version;
    private AirportAssemblyStrategy<APT, RWY> airportStrategy;
    private FixAssemblyStrategy<FIX> fixStrategy;
    private AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;
    private ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;

    private BoundaryAssemblyStrategy<AIR, ASEQ> boundaryStrategy;

    private SuasAssemblyStrategy<AIR, ASEQ> suasStrategy;

    private Builder(DafifVersion version) {
      this.version = requireNonNull(version);
    }

    /**
     * See the documentation on {@link AirportAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> airportStrategy(AirportAssemblyStrategy<APT, RWY> airportStrategy) {
      this.airportStrategy = requireNonNull(airportStrategy);
      return this;
    }

    /**
     * See the documentation on {@link FixAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> fixStrategy(FixAssemblyStrategy<FIX> fixStrategy) {
      this.fixStrategy = requireNonNull(fixStrategy);
      return this;
    }

    /**
     * See the documentation on {@link AirwayAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> airwayStrategy(AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy) {
      this.airwayStrategy = requireNonNull(airwayStrategy);
      return this;
    }

    /**
     * See the documentation on {@link ProcedureAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> procedureStrategy(ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy) {
      this.procedureStrategy = requireNonNull(procedureStrategy);
      return this;
    }

    /**
     * See the documentation on {@link BoundaryAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> boundaryStrategy(BoundaryAssemblyStrategy<AIR, ASEQ> boundaryStrategy) {
      this.boundaryStrategy = requireNonNull(boundaryStrategy);
      return this;
    }

    /**
     * See the documentation on {@link SuasAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> suasStrategy(SuasAssemblyStrategy<AIR, ASEQ> suasStrategy) {
      this.suasStrategy = requireNonNull(suasStrategy);
      return this;
    }

    public OneshotDafifParser<APT, RWY, FIX, LEG, TRS, AWY, PRC, AIR, ASEQ> build() {
      return new OneshotDafifParser<>(this);
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
    private final Collection<AIR> boundaries;
    private final Collection<AIR> specialUseAirspaces;

    private ClientRecords(Builder<APT, FIX, AWY, PRC, AIR> builder) {
      this.airports = builder.airports;
      this.fixes = builder.fixes;
      this.airways = builder.airways;
      this.procedures = builder.procedures;
      this.boundaries = builder.boundaries;
      this.specialUseAirspaces = builder.specialUseAirspaces;
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

    /**
     * Boundaries assembled using the configured boundary strategy.
     */
    public Collection<AIR> boundaries() {
      return boundaries;
    }

    /**
     * Special use airspaces assembled using the configured special use airspace strategy.
     */
    public Collection<AIR> specialUseAirspaces() {
      return specialUseAirspaces;
    }

    public Collection<AIR> airspaces() {
      return Stream.concat(boundaries.stream(), specialUseAirspaces.stream()).toList();
    }

    public static final class Builder<APT, FIX, AWY, PRC, AIR> {

      private final Collection<APT> airports = new ArrayList<>();
      private final Collection<FIX> fixes = new ArrayList<>();
      private final Collection<AWY> airways = new ArrayList<>();
      private final Collection<PRC> procedures = new ArrayList<>();
      private final Collection<AIR> boundaries = new ArrayList<>();
      private final Collection<AIR> specialUseAirspaces = new ArrayList<>();

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

      public Builder<APT, FIX, AWY, PRC, AIR> addBoundaries(Collection<AIR> boundaries) {
        this.boundaries.addAll(boundaries);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC, AIR> addSpecialUseAirspaces(Collection<AIR> specialUseAirspaces) {
        this.specialUseAirspaces.addAll(specialUseAirspaces);
        return this;
      }

      public ClientRecords<APT, FIX, AWY, PRC, AIR> build() {
        return new ClientRecords<>(this);
      }
    }
  }
}
