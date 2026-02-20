package org.mitre.tdp.boogie.dafif;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.model.DafifRecordConverterFactory.consumerForVersion;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.mitre.tdp.boogie.Airport;
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
import org.mitre.tdp.boogie.dafif.assemble.FixAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.assemble.ProcedureAssembler;
import org.mitre.tdp.boogie.dafif.assemble.ProcedureAssemblyStrategy;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.ConvertingDafifRecordConsumer;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Oneshot implementation of a parser going from an {@link InputStream} (typically a DAFIF zip file) to a collection of
 * client-defined assembled records.
 *
 * <p>This class mirrors the ARINC {@code OneshotRecordParser} pattern. It reads a DAFIF zip, parses all supported record
 * types, builds the necessary lookup databases, and assembles airports, fixes, airways, and procedures in one pass.
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
 */
public final class OneshotDafifParser<APT, RWY, FIX, LEG, TRS, AWY, PRC> {

  private static final Logger LOG = LoggerFactory.getLogger(OneshotDafifParser.class);

  private static final Set<String> PARSEABLE_FILES = Set.of(
      "ARPT.TXT", "RWY.TXT", "ADD_RWY.TXT", "ILS.TXT", "NAV.TXT", "WPT.TXT", "TRM_PAR.TXT", "TRM_SEG.TXT", "ATS.TXT"
  );

  private final DafifVersion version;
  private final AirportAssemblyStrategy<APT, RWY> airportStrategy;
  private final FixAssemblyStrategy<FIX> fixStrategy;
  private final AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;
  private final ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;

  private OneshotDafifParser(Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC> builder) {
    this.version = requireNonNull(builder.version);
    this.airportStrategy = requireNonNull(builder.airportStrategy);
    this.fixStrategy = builder.fixStrategy;
    this.airwayStrategy = requireNonNull(builder.airwayStrategy);
    this.procedureStrategy = requireNonNull(builder.procedureStrategy);
  }

  /**
   * Instantiate a new buildable version of the oneshot parser which can be used to construct user-defined data models given
   * the configured strategy classes.
   */
  public static <APT, RWY, FIX, LEG, TRS, AWY, PRC> Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC> builder(DafifVersion version) {
    return new Builder<>(version);
  }

  /**
   * Instantiate a standard oneshot parser targeting the default implementations of the Boogie interfaces.
   */
  public static OneshotDafifParser<Airport, Runway, Fix, Leg, Transition, Airway, Procedure> standard(DafifVersion version) {
    return OneshotDafifParser.<Airport, Runway, Fix, Leg, Transition, Airway, Procedure>builder(version)
        .airportStrategy(AirportAssemblyStrategy.standard())
        .airwayStrategy(AirwayAssemblyStrategy.standard())
        .procedureStrategy(new ProcedureAssemblyStrategy.Standard())
        .build();
  }

  /**
   * Assembles the collection of typed client records from a DAFIF zip file represented as an {@link InputStream}.
   *
   * @param inputStream an input stream containing the bytes of a DAFIF zip file
   */
  public ClientRecords<APT, FIX, AWY, PRC> assembleFrom(InputStream inputStream) {
    requireNonNull(inputStream);

    ClientRecords.Builder<APT, FIX, AWY, PRC> records = new ClientRecords.Builder<>();

    DafifFileParser parser = new DafifFileParser(version);
    ConvertingDafifRecordConsumer consumer = consumerForVersion(version);

    try (ZipInputStream zis = new ZipInputStream(inputStream)) {
      ZipEntry entry;
      while ((entry = zis.getNextEntry()) != null) {
        String entryName = entry.getName();
        String filename = entryName.contains("/") ? entryName.substring(entryName.lastIndexOf('/') + 1) : entryName;

        if (!entry.isDirectory() && entryName.contains("DAFIFT/") && !entryName.contains("TRMH/") && !entryName.contains("SUPPH/") && PARSEABLE_FILES.contains(filename)) {
          LOG.debug("Parsing zip entry: {}", entryName);
          Collection<DafifRecord> parsed = parser.apply(new NonClosingInputStream(zis), filename);
          parsed.forEach(consumer);
          LOG.debug("Parsed {} records from {}", parsed.size(), filename);
        }

        zis.closeEntry();
      }
    } catch (IOException e) {
      LOG.error("Could not parse the DAFIF zip into memory", e);
      return records.build();
    }

    LOG.debug("Finished parsing and converting supported record types.");

    DafifFixDatabase fixDatabase = DafifDatabaseFactory.newFixDatabase(consumer.dafifWaypoints(), consumer.dafifNavaids());
    LOG.debug("Finished instantiation of FixDatabase.");

    DafifTerminalAreaDatabase terminalAreaDatabase = DafifDatabaseFactory.newTerminalAreaDatabase(consumer);
    LOG.debug("Finished instantiation of TerminalAreaDatabase.");

    FixAssemblyStrategy<FIX> resolvedFixStrategy = fixStrategy != null
        ? fixStrategy
        : (FixAssemblyStrategy<FIX>) FixAssemblyStrategy.standard(terminalAreaDatabase, fixDatabase);

    return records
        .addAirports(assembleAirports(terminalAreaDatabase, consumer.dafifAirports()))
        .addFixes(assembleFixes(resolvedFixStrategy, consumer.dafifWaypoints(), consumer.dafifNavaids()))
        .addAirways(assembleAirways(fixDatabase, resolvedFixStrategy, consumer.dafifAts()))
        .addProcedures(assembleProcedures(fixDatabase, terminalAreaDatabase, resolvedFixStrategy, consumer.dafifTerminalParents()))
        .build();
  }

  private Collection<APT> assembleAirports(DafifTerminalAreaDatabase tad, Collection<DafifAirport> airports) {
    AirportAssembler<APT> assembler = AirportAssembler.usingStrategy(tad, airportStrategy);
    return airports.stream().map(assembler::assemble).toList();
  }

  @SuppressWarnings("unchecked")
  private Collection<FIX> assembleFixes(FixAssemblyStrategy<FIX> resolvedFixStrategy, Collection<DafifWaypoint> waypoints, Collection<DafifNavaid> navaids) {
    org.mitre.tdp.boogie.dafif.assemble.FixAssembler<FIX> assembler = org.mitre.tdp.boogie.dafif.assemble.FixAssembler.withStrategy(resolvedFixStrategy);
    return Stream.concat(waypoints.stream(), navaids.stream())
        .flatMap(model -> assembler.assemble(model).stream())
        .toList();
  }

  private Collection<AWY> assembleAirways(DafifFixDatabase fixDatabase, FixAssemblyStrategy<FIX> resolvedFixStrategy,
                                           Collection<org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment> atsSegments) {
    AirwayAssembler<AWY> assembler = AirwayAssembler.withStrategy(fixDatabase, resolvedFixStrategy, airwayStrategy);
    return assembler.assemble(atsSegments).toList();
  }

  private Collection<PRC> assembleProcedures(DafifFixDatabase fixDatabase, DafifTerminalAreaDatabase tad,
                                              FixAssemblyStrategy<FIX> resolvedFixStrategy,
                                              Collection<org.mitre.tdp.boogie.dafif.model.DafifTerminalParent> parents) {
    ProcedureAssembler<PRC> assembler = ProcedureAssembler.withStrategy(tad, fixDatabase, procedureStrategy, resolvedFixStrategy);
    return assembler.assemble(parents).toList();
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

  public static final class Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC> {

    private final DafifVersion version;
    private AirportAssemblyStrategy<APT, RWY> airportStrategy;
    private FixAssemblyStrategy<FIX> fixStrategy;
    private AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy;
    private ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy;

    private Builder(DafifVersion version) {
      this.version = requireNonNull(version);
    }

    /**
     * See the documentation on {@link AirportAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC> airportStrategy(AirportAssemblyStrategy<APT, RWY> airportStrategy) {
      this.airportStrategy = requireNonNull(airportStrategy);
      return this;
    }

    /**
     * See the documentation on {@link FixAssemblyStrategy}. If not set, a standard instance will be created
     * using the databases built from the parsed data.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC> fixStrategy(FixAssemblyStrategy<FIX> fixStrategy) {
      this.fixStrategy = requireNonNull(fixStrategy);
      return this;
    }

    /**
     * See the documentation on {@link AirwayAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC> airwayStrategy(AirwayAssemblyStrategy<AWY, FIX, LEG> airwayStrategy) {
      this.airwayStrategy = requireNonNull(airwayStrategy);
      return this;
    }

    /**
     * See the documentation on {@link ProcedureAssemblyStrategy}.
     */
    public Builder<APT, RWY, FIX, LEG, TRS, AWY, PRC> procedureStrategy(ProcedureAssemblyStrategy<PRC, TRS, LEG, FIX> procedureStrategy) {
      this.procedureStrategy = requireNonNull(procedureStrategy);
      return this;
    }

    public OneshotDafifParser<APT, RWY, FIX, LEG, TRS, AWY, PRC> build() {
      return new OneshotDafifParser<>(this);
    }
  }

  /**
   * Wrapper class containing assembled client records of the templated types assembled by the oneshot parser.
   */
  public static final class ClientRecords<APT, FIX, AWY, PRC> {

    private final Collection<APT> airports;
    private final Collection<FIX> fixes;
    private final Collection<AWY> airways;
    private final Collection<PRC> procedures;

    private ClientRecords(Builder<APT, FIX, AWY, PRC> builder) {
      this.airports = builder.airports;
      this.fixes = builder.fixes;
      this.airways = builder.airways;
      this.procedures = builder.procedures;
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

    public static final class Builder<APT, FIX, AWY, PRC> {

      private final Collection<APT> airports = new ArrayList<>();
      private final Collection<FIX> fixes = new ArrayList<>();
      private final Collection<AWY> airways = new ArrayList<>();
      private final Collection<PRC> procedures = new ArrayList<>();

      private Builder() {
      }

      public Builder<APT, FIX, AWY, PRC> addAirports(Collection<APT> airports) {
        this.airports.addAll(airports);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC> addFixes(Collection<FIX> fixes) {
        this.fixes.addAll(fixes);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC> addAirways(Collection<AWY> airways) {
        this.airways.addAll(airways);
        return this;
      }

      public Builder<APT, FIX, AWY, PRC> addProcedures(Collection<PRC> procedures) {
        this.procedures.addAll(procedures);
        return this;
      }

      public ClientRecords<APT, FIX, AWY, PRC> build() {
        return new ClientRecords<>(this);
      }
    }
  }
}
