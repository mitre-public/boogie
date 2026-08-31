package org.mitre.tdp.boogie.arinc.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

/**
 * A non-thread-safe version of a {@link Consumer} class which can eat {@link ArincRecord}s from a collection or stream and saves
 * them as internal state within itself.
 * <br>
 * This class is built with a pre-configured set of record-to-Java POJO delegators and a set of record-to-POJO converters for a
 * pre-configured set of data types. {@link ArincRecordConverterFactory} provides some default implementations for a subset of
 * {@link ArincRecord} versions.
 * <br>
 * This class is provided for convenience, but it's limitations (especially the thread-safety) should be taken into account before
 * use.
 * <br>
 * If additional convertible record types are added this class can be extended relatively straightforwardly.
 */
public final class ConvertingArincRecordConsumer implements Consumer<ArincRecord> {

  private final DelegatableCollection<ArincAirport> arincAirports;
  private final DelegatableCollection<ArincAirportPrimaryExtension> arincAirportExtensions;
  private final DelegatableCollection<ArincRunway> arincRunways;
  private final DelegatableCollection<ArincLocalizerGlideSlope> arincLocalizerGlideSlopes;
  private final DelegatableCollection<ArincNdbNavaid> arincNdbNavaids;
  private final DelegatableCollection<ArincVhfNavaid> arincVhfNavaids;
  private final DelegatableCollection<ArincWaypoint> arincWaypoints;
  private final DelegatableCollection<ArincAirwayLeg> arincAirwayLegs;
  private final DelegatableCollection<ArincProcedureLeg> arincProcedureLegs;
  private final DelegatableCollection<ArincGnssLandingSystem> gnssLandingSystems;
  private final DelegatableCollection<ArincHoldingPattern> arincHoldingPatterns;
  private final DelegatableCollection<ArincFirUirLeg> arincFirUirLeg;
  private final DelegatableCollection<ArincHelipad> arincHelipads;
  private final DelegatableCollection<ArincControlledAirspaceLeg> arincControlledAirspaceLegs;
  private final DelegatableCollection<ArincRestrictiveAirspaceLeg> arincRestrictiveAirspaceLegs;
  private final DelegatableCollection<ArincHeaderOne> arincHeaderOnes;
  private final DelegatableCollection<ArincHeliport> arincHeliports;

  private final Consumer<ArincRecord> consumer;

  private ConvertingArincRecordConsumer(Builder builder) {
    this(builder, false);
  }

  private ConvertingArincRecordConsumer(Builder builder, boolean oneShot) {
    this.arincAirports = new DelegatableCollection<>(builder.airportDelegator, builder.airportConverter);
    this.arincAirportExtensions = new DelegatableCollection<>(builder.airportContinuationDelegator, builder.airportContinuationConverter);
    this.arincRunways = new DelegatableCollection<>(builder.runwayDelegator, builder.runwayConverter);
    this.arincLocalizerGlideSlopes = new DelegatableCollection<>(builder.localizerGlideSlopeDelegator, builder.localizerGlideSlopeConverter);
    this.arincNdbNavaids = new DelegatableCollection<>(builder.ndbNavaidDelegator, builder.ndbNavaidConverter);
    this.arincVhfNavaids = new DelegatableCollection<>(builder.vhfNavaidDelegator, builder.vhfNavaidConverter);
    this.arincWaypoints = new DelegatableCollection<>(builder.waypointDelegator, builder.waypointConverter);
    this.arincAirwayLegs = new DelegatableCollection<>(builder.airwayDelegator, builder.airwayConverter);
    this.arincProcedureLegs = new DelegatableCollection<>(builder.procedureDelegator, builder.procedureConverter, oneShot);
    this.gnssLandingSystems = new DelegatableCollection<>(builder.gnssLandingSystemDelegator, builder.gnssLandingSystemConverter);
    this.arincHoldingPatterns = new DelegatableCollection<>(builder.holdingPatternDelegator, builder.holdingPatternConverter);
    this.arincFirUirLeg = new DelegatableCollection<>(builder.firUirDelegator, builder.firUirConverter);
    this.arincHelipads = new DelegatableCollection<>(builder.helipadDelegator, builder.helipadConverter);
    this.arincControlledAirspaceLegs = new DelegatableCollection<>(builder.arincControlledAirspaceLegDelegator, builder.arincControlledAirspaceConverter);
    this.arincRestrictiveAirspaceLegs = new DelegatableCollection<>(builder.restrictiveAirspaceLegDelegator, builder.restrictiveAirspaceConverter);
    this.arincHeaderOnes = new DelegatableCollection<>(builder.headerDelegator, builder.headerConverter);
    this.arincHeliports = new DelegatableCollection<>(builder.heliportDelegator, builder.heliportConverter);

    DelegatableCollection<?>[] collections = {
        this.arincAirports,
        this.arincAirportExtensions,
        this.arincRunways,
        this.arincLocalizerGlideSlopes,
        this.arincNdbNavaids,
        this.arincVhfNavaids,
        this.arincWaypoints,
        this.arincAirwayLegs,
        this.arincProcedureLegs,
        this.gnssLandingSystems,
        this.arincHoldingPatterns,
        this.arincFirUirLeg,
        this.arincHelipads,
        this.arincControlledAirspaceLegs,
        this.arincRestrictiveAirspaceLegs,
        this.arincHeaderOnes,
        this.arincHeliports
    };
    this.consumer = oneShot
        ? new MostRecentlyUsedConvertingConsumer(collections)
        : new MostRecentlyUsedConsumer<>(collections);
  }

  public Collection<ArincAirport> arincAirports() {
    return arincAirports.records();
  }

  public Collection<ArincAirportPrimaryExtension> arincAirportExtensions() {
    return arincAirportExtensions.records();
  }

  public Collection<ArincRunway> arincRunways() {
    return arincRunways.records();
  }

  public Collection<ArincLocalizerGlideSlope> arincLocalizerGlideSlopes() {
    return arincLocalizerGlideSlopes.records();
  }

  public Collection<ArincNdbNavaid> arincNdbNavaids() {
    return arincNdbNavaids.records();
  }

  public Collection<ArincVhfNavaid> arincVhfNavaids() {
    return arincVhfNavaids.records();
  }

  public Collection<ArincWaypoint> arincWaypoints() {
    return arincWaypoints.records();
  }

  public Collection<ArincAirwayLeg> arincAirwayLegs() {
    return arincAirwayLegs.records();
  }

  public Collection<ArincProcedureLeg> arincProcedureLegs() {
    return arincProcedureLegs.records();
  }

  public Collection<ArincGnssLandingSystem> arincGnssLandingSystems() {
    return gnssLandingSystems.records();
  }

  public Collection<ArincHoldingPattern> arincHoldingPatterns() {
    return arincHoldingPatterns.records();
  }

  public Collection<ArincFirUirLeg> arincFirUirLegs() {
    return arincFirUirLeg.records();
  }

  public Collection<ArincHelipad> arincHelipads() {
    return arincHelipads.records();
  }

  public Collection<ArincControlledAirspaceLeg> arincControlledAirspaceLegs() {
    return arincControlledAirspaceLegs.records();
  }

  public Collection<ArincRestrictiveAirspaceLeg> arincRestrictiveAirspaceLegs() {
    return arincRestrictiveAirspaceLegs.records();
  }

  public Optional<ArincHeaderOne>  arincHeaderOne() {
    return arincHeaderOnes.records().stream().findFirst();
  }

  public Collection<ArincHeliport> arincHeliports() {
    return arincHeliports.records();
  }

  @Override
  public void accept(ArincRecord arincRecord) {
    requireNonNull(arincRecord);
    this.consumer.accept(arincRecord);
  }

  /**
   * Returns a separate, immutable snapshot of all converted records. This method does not change the consumer's state: it can
   * continue accepting records, and a later call returns a new snapshot containing those additions.
   *
   * <p>A one-shot caller should retain this result and release the consumer after ingestion. That makes the consumer's temporary
   * duplicate-suppression sets eligible for reclamation while preserving encounter order and procedure-leg duplicates in the
   * compact result lists.
   */
  public ConvertedArincRecords snapshot() {
    return new ConvertedArincRecords(
        arincAirports.snapshot(),
        arincAirportExtensions.snapshot(),
        arincRunways.snapshot(),
        arincLocalizerGlideSlopes.snapshot(),
        arincNdbNavaids.snapshot(),
        arincVhfNavaids.snapshot(),
        arincWaypoints.snapshot(),
        arincAirwayLegs.snapshot(),
        arincProcedureLegs.snapshot(),
        gnssLandingSystems.snapshot(),
        arincHoldingPatterns.snapshot(),
        arincFirUirLeg.snapshot(),
        arincHelipads.snapshot(),
        arincControlledAirspaceLegs.snapshot(),
        arincRestrictiveAirspaceLegs.snapshot(),
        arincHeaderOnes.snapshot().stream().findFirst(),
        arincHeliports.snapshot()
    );
  }

  /**
   * If run on hundreds of thousands of records making the collector apply each delegation predicate can start to take some time
   * (especially if any of those delegators require partial parses).
   * <br>
   * Since the 424 records are <i>typically</i> fairly well sorted by record type, some time can be saved by first checking with
   * the delegator which matched the previous record.
   * <br>
   * Plus the implementation is pretty lightweight.
   */
  private static final class MostRecentlyUsedConsumer<T, V extends Predicate<T> & Consumer<T>> implements Consumer<T> {

    private final V[] predicates;

    @SafeVarargs
    private MostRecentlyUsedConsumer(V... predicates) {
      this.predicates = predicates.clone();
    }

    @Override
    public void accept(T t) {
      for (int i = 0; i < predicates.length; i++) {
        V match = predicates[i];
        if (match.test(t)) {
          match.accept(t);
          if (i > 0) {
            System.arraycopy(predicates, 0, predicates, 1, i);
            predicates[0] = match;
          }
          return;
        }
      }
    }
  }

  /**
   * The converters already validate records before converting them. The one-shot path can therefore use the conversion result
   * itself for delegation and avoid applying a separate validator to every accepted record.
   */
  private static final class MostRecentlyUsedConvertingConsumer implements Consumer<ArincRecord> {

    private final DelegatableCollection<?>[] collections;

    private MostRecentlyUsedConvertingConsumer(DelegatableCollection<?>[] collections) {
      this.collections = collections.clone();
    }

    @Override
    public void accept(ArincRecord arincRecord) {
      for (int i = 0; i < collections.length; i++) {
        DelegatableCollection<?> match = collections[i];
        if (match.convertAndAdd(arincRecord)) {
          if (i > 0) {
            System.arraycopy(collections, 0, collections, 1, i);
            collections[0] = match;
          }
          return;
        }
      }
    }
  }

  private static final class DelegatableCollection<T> implements Consumer<ArincRecord>, Predicate<ArincRecord> {

    private final Predicate<ArincRecord> delegator;
    private final Function<ArincRecord, Optional<T>> converter;

    private final Collection<T> records;
    private final Collection<T> listView;
    private ImmutableCollection<T> snapshot;

    public DelegatableCollection(
        Predicate<ArincRecord> delegator,
        Function<ArincRecord, Optional<T>> converter
    ) {
      this(delegator, converter, false);
    }

    public DelegatableCollection(
        Predicate<ArincRecord> delegator,
        Function<ArincRecord, Optional<T>> converter,
        boolean listBacked
    ) {
      this.delegator = requireNonNull(delegator);
      this.converter = requireNonNull(converter);
      if (listBacked) {
        ArrayList<T> list = new ArrayList<>();
        this.records = list;
        this.listView = Collections.unmodifiableList(list);
      } else {
        this.records = new LinkedHashSet<>();
        this.listView = null;
      }
    }

    public Collection<T> records() {
      if (listView != null) {
        return listView;
      }
      if (snapshot == null) {
        snapshot = ImmutableList.copyOf(records);
      }
      return snapshot;
    }

    private List<T> snapshot() {
      return List.copyOf(records);
    }

    private boolean convertAndAdd(ArincRecord arincRecord) {
      Optional<T> converted = converter.apply(arincRecord);
      if (converted.isEmpty()) {
        return false;
      }
      if (records.add(converted.get())) {
        snapshot = null;
      }
      return true;
    }

    @Override
    public void accept(ArincRecord arincRecord) {
      converter.apply(arincRecord).ifPresent(record -> {
        if (records.add(record)) {
          snapshot = null;
        }
      });
    }

    @Override
    public boolean test(ArincRecord arincRecord) {
      return delegator.test(arincRecord);
    }
  }

  public static class Builder {
    private Predicate<ArincRecord> airportDelegator;
    private Function<ArincRecord, Optional<ArincAirport>> airportConverter;
    private Predicate<ArincRecord> airportContinuationDelegator;
    private Function<ArincRecord, Optional<ArincAirportPrimaryExtension>> airportContinuationConverter;
    private Predicate<ArincRecord> airwayDelegator;
    private Function<ArincRecord, Optional<ArincAirwayLeg>> airwayConverter;
    private Predicate<ArincRecord> localizerGlideSlopeDelegator;
    private Function<ArincRecord, Optional<ArincLocalizerGlideSlope>> localizerGlideSlopeConverter;
    private Predicate<ArincRecord> ndbNavaidDelegator;
    private Function<ArincRecord, Optional<ArincNdbNavaid>> ndbNavaidConverter;
    private Predicate<ArincRecord> procedureDelegator;
    private Function<ArincRecord, Optional<ArincProcedureLeg>> procedureConverter;
    private Predicate<ArincRecord> runwayDelegator;
    private Function<ArincRecord, Optional<ArincRunway>> runwayConverter;
    private Predicate<ArincRecord> vhfNavaidDelegator;
    private Function<ArincRecord, Optional<ArincVhfNavaid>> vhfNavaidConverter;
    private Predicate<ArincRecord> waypointDelegator;
    private Function<ArincRecord, Optional<ArincWaypoint>> waypointConverter;
    private Predicate<ArincRecord> gnssLandingSystemDelegator;
    private Function<ArincRecord, Optional<ArincGnssLandingSystem>> gnssLandingSystemConverter;
    private Predicate<ArincRecord> holdingPatternDelegator;
    private Function<ArincRecord, Optional<ArincHoldingPattern>> holdingPatternConverter;
    private Predicate<ArincRecord> firUirDelegator;
    private Function<ArincRecord, Optional<ArincFirUirLeg>> firUirConverter;
    private Predicate<ArincRecord> helipadDelegator;
    private Function<ArincRecord, Optional<ArincHelipad>> helipadConverter;
    private Predicate<ArincRecord> arincControlledAirspaceLegDelegator;
    private Function<ArincRecord, Optional<ArincControlledAirspaceLeg>> arincControlledAirspaceConverter;
    private Predicate<ArincRecord> restrictiveAirspaceLegDelegator;
    private Function<ArincRecord, Optional<ArincRestrictiveAirspaceLeg>> restrictiveAirspaceConverter;
    private Predicate<ArincRecord> headerDelegator;
    private Function<ArincRecord, Optional<ArincHeaderOne>> headerConverter;
    private Predicate<ArincRecord> heliportDelegator;
    private Function<ArincRecord, Optional<ArincHeliport>> heliportConverter;

    public Builder airportContinuationDelegator(Predicate<ArincRecord> airportContinuationDelegator) {
      this.airportContinuationDelegator = airportContinuationDelegator;
      return this;
    }

    public Builder airportContinuationConverter(Function<ArincRecord, Optional<ArincAirportPrimaryExtension>> airportContinuationConverter) {
      this.airportContinuationConverter = airportContinuationConverter;
      return this;
    }

    public Builder airportDelegator(Predicate<ArincRecord> airportDelegator) {
      this.airportDelegator = requireNonNull(airportDelegator);
      return this;
    }

    public Builder airportConverter(Function<ArincRecord, Optional<ArincAirport>> airportConverter) {
      this.airportConverter = requireNonNull(airportConverter);
      return this;
    }

    public Builder airwayLegDelegator(Predicate<ArincRecord> airwayDelegator) {
      this.airwayDelegator = requireNonNull(airwayDelegator);
      return this;
    }

    public Builder airwayLegConverter(Function<ArincRecord, Optional<ArincAirwayLeg>> airwayConverter) {
      this.airwayConverter = requireNonNull(airwayConverter);
      return this;
    }

    public Builder localizerGlideSlopeDelegator(Predicate<ArincRecord> localizerGlideSlopeDelegator) {
      this.localizerGlideSlopeDelegator = requireNonNull(localizerGlideSlopeDelegator);
      return this;
    }

    public Builder localizerGlideSlopeConverter(Function<ArincRecord, Optional<ArincLocalizerGlideSlope>> localizerGlideSlopeConverter) {
      this.localizerGlideSlopeConverter = requireNonNull(localizerGlideSlopeConverter);
      return this;
    }

    public Builder ndbNavaidDelegator(Predicate<ArincRecord> ndbNavaidDelegator) {
      this.ndbNavaidDelegator = requireNonNull(ndbNavaidDelegator);
      return this;
    }

    public Builder ndbNavaidConverter(Function<ArincRecord, Optional<ArincNdbNavaid>> ndbNavaidConverter) {
      this.ndbNavaidConverter = requireNonNull(ndbNavaidConverter);
      return this;
    }

    public Builder procedureLegDelegator(Predicate<ArincRecord> procedureDelegator) {
      this.procedureDelegator = requireNonNull(procedureDelegator);
      return this;
    }

    public Builder procedureLegConverter(Function<ArincRecord, Optional<ArincProcedureLeg>> procedureConverter) {
      this.procedureConverter = requireNonNull(procedureConverter);
      return this;
    }

    public Builder runwayDelegator(Predicate<ArincRecord> runwayDelegator) {
      this.runwayDelegator = requireNonNull(runwayDelegator);
      return this;
    }

    public Builder runwayConverter(Function<ArincRecord, Optional<ArincRunway>> runwayConverter) {
      this.runwayConverter = requireNonNull(runwayConverter);
      return this;
    }

    public Builder vhfNavaidDelegator(Predicate<ArincRecord> vhfNavaidDelegator) {
      this.vhfNavaidDelegator = requireNonNull(vhfNavaidDelegator);
      return this;
    }

    public Builder vhfNavaidConverter(Function<ArincRecord, Optional<ArincVhfNavaid>> vhfNavaidConverter) {
      this.vhfNavaidConverter = requireNonNull(vhfNavaidConverter);
      return this;
    }

    public Builder waypointDelegator(Predicate<ArincRecord> waypointDelegator) {
      this.waypointDelegator = requireNonNull(waypointDelegator);
      return this;
    }

    public Builder waypointConverter(Function<ArincRecord, Optional<ArincWaypoint>> waypointConverter) {
      this.waypointConverter = requireNonNull(waypointConverter);
      return this;
    }

    public Builder gnssLandingSystemDelegator(Predicate<ArincRecord> gnssLandingSystemDelegator) {
      this.gnssLandingSystemDelegator = requireNonNull(gnssLandingSystemDelegator);
      return this;
    }

    public Builder gnssLandingSystemConverter(Function<ArincRecord, Optional<ArincGnssLandingSystem>> gnssLandingSystemConverter) {
      this.gnssLandingSystemConverter = requireNonNull(gnssLandingSystemConverter);
      return this;
    }

    public Builder holdingPatternDelegator(Predicate<ArincRecord> holdingPatternDelegator) {
      this.holdingPatternDelegator = holdingPatternDelegator;
      return this;
    }

    public Builder holdingPatternConverter(Function<ArincRecord, Optional<ArincHoldingPattern>> holdingPatternConverter) {
      this.holdingPatternConverter = holdingPatternConverter;
      return this;
    }

    public Builder firUirDelegator(Predicate<ArincRecord> firUirDelegator) {
      this.firUirDelegator = firUirDelegator;
      return this;
    }

    public Builder firUirConverter(Function<ArincRecord, Optional<ArincFirUirLeg>> firUirConverter) {
      this.firUirConverter = firUirConverter;
      return this;
    }

    public Builder helipadDelegator(Predicate<ArincRecord> helipadDelegator) {
      this.helipadDelegator = helipadDelegator;
      return this;
    }

    public Builder helipadConverter(Function<ArincRecord, Optional<ArincHelipad>> helipadConverter) {
      this.helipadConverter = helipadConverter;
      return this;
    }

    public Builder arincControlledAirspaceConverter(Function<ArincRecord, Optional<ArincControlledAirspaceLeg>> arincControlledAirspaceConverter) {
      this.arincControlledAirspaceConverter = arincControlledAirspaceConverter;
      return this;
    }

    public Builder arincControlledAirspaceLegDelegator(Predicate<ArincRecord> arincControlledAirspaceLegDelegator) {
      this.arincControlledAirspaceLegDelegator = arincControlledAirspaceLegDelegator;
      return this;
    }

    public Builder restrictiveAirspaceLegDelegator(Predicate<ArincRecord> restrictiveAirspaceLegDelegator) {
      this.restrictiveAirspaceLegDelegator = restrictiveAirspaceLegDelegator;
      return this;
    }

    public Builder restrictiveAirspaceConverter(Function<ArincRecord, Optional<ArincRestrictiveAirspaceLeg>> restrictiveAirspaceConverter) {
      this.restrictiveAirspaceConverter = restrictiveAirspaceConverter;
      return this;
    }

    public Builder headerConverter(Function<ArincRecord, Optional<ArincHeaderOne>> headerConverter) {
      this.headerConverter = headerConverter;
      return this;
    }

    public Builder headerDelegator(Predicate<ArincRecord> headerDelegator) {
      this.headerDelegator = headerDelegator;
      return this;
    }

    public Builder heliportDelegator(Predicate<ArincRecord> heliportDelegator) {
      this.heliportDelegator = heliportDelegator;
      return this;
    }

    public Builder heliportConverter(Function<ArincRecord, Optional<ArincHeliport>> heliportConverter) {
      this.heliportConverter = heliportConverter;
      return this;
    }

    public ConvertingArincRecordConsumer build() {
      return new ConvertingArincRecordConsumer(this);
    }

    ConvertingArincRecordConsumer buildOneShot() {
      return new ConvertingArincRecordConsumer(this, true);
    }
  }
}
