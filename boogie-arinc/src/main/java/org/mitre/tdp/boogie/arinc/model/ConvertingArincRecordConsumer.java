package org.mitre.tdp.boogie.arinc.model;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.*;
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
  private final DelegatableCollection<ArincHeliport> arincHeliports;
  private final DelegatableCollection<ArincControlledAirspaceLeg> arincControlledAirspaceLegs;

  private final MRUDequeConsumer<ArincRecord, DelegatableCollection<?>> consumer;

  private ConvertingArincRecordConsumer(Builder builder) {
    this.arincAirports = new DelegatableCollection<>(builder.airportDelegator, builder.airportConverter);
    this.arincAirportExtensions = new DelegatableCollection<>(builder.airportContinuationDelegator, builder.airportContinuationConverter);
    this.arincRunways = new DelegatableCollection<>(builder.runwayDelegator, builder.runwayConverter);
    this.arincLocalizerGlideSlopes = new DelegatableCollection<>(builder.localizerGlideSlopeDelegator, builder.localizerGlideSlopeConverter);
    this.arincNdbNavaids = new DelegatableCollection<>(builder.ndbNavaidDelegator, builder.ndbNavaidConverter);
    this.arincVhfNavaids = new DelegatableCollection<>(builder.vhfNavaidDelegator, builder.vhfNavaidConverter);
    this.arincWaypoints = new DelegatableCollection<>(builder.waypointDelegator, builder.waypointConverter);
    this.arincAirwayLegs = new DelegatableCollection<>(builder.airwayDelegator, builder.airwayConverter);
    this.arincProcedureLegs = new DelegatableCollection<>(builder.procedureDelegator, builder.procedureConverter);
    this.gnssLandingSystems = new DelegatableCollection<>(builder.gnssLandingSystemDelegator, builder.gnssLandingSystemConverter);
    this.arincHoldingPatterns = new DelegatableCollection<>(builder.holdingPatternDelegator, builder.holdingPatternConverter);
    this.arincFirUirLeg = new DelegatableCollection<>(builder.firUirDelegator, builder.firUirConverter);
    this.arincHelipads = new DelegatableCollection<>(builder.helipadDelegator, builder.helipadConverter);
    this.arincControlledAirspaceLegs = new DelegatableCollection<>(builder.arincControlledAirspaceLegDelegator, builder.arincControlledAirspaceConverter);
    this.arincHeliports = new DelegatableCollection<>(builder.heliportDelegator, builder.heliportConverter);

    this.consumer = new MRUDequeConsumer<>(
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
        this.arincHeliports
    );
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

  public Collection<ArincHeliport> arincHeliports() {
    return arincHeliports.records();
  }

  @Override
  public void accept(ArincRecord arincRecord) {
    requireNonNull(arincRecord);
    this.consumer.accept(arincRecord);
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
  private static final class MRUDequeConsumer<T, V extends Predicate<T> & Consumer<T>> implements Consumer<T> {

    private final ArrayDeque<V> predicateDeque;

    @SafeVarargs
    private MRUDequeConsumer(V... predicates) {
      this.predicateDeque = new ArrayDeque<>();
      predicateDeque.addAll(Arrays.asList(predicates));
    }

    @Override
    public void accept(T t) {
      Optional<V> match = predicateDeque.stream().filter(p -> p.test(t)).findFirst();
      match.ifPresent(m -> {
        m.accept(t);
        if (!predicateDeque.getFirst().equals(m)) {
          predicateDeque.remove(m);
          predicateDeque.addFirst(m);
        }
      });
    }
  }

  private static final class DelegatableCollection<T> implements Consumer<ArincRecord>, Predicate<ArincRecord> {

    private final Predicate<ArincRecord> delegator;
    private final Function<ArincRecord, Optional<T>> converter;

    private final Collection<T> records;

    public DelegatableCollection(
        Predicate<ArincRecord> delegator,
        Function<ArincRecord, Optional<T>> converter
    ) {
      this.delegator = requireNonNull(delegator);
      this.converter = requireNonNull(converter);
      this.records = new LinkedHashSet<>();
    }

    public ImmutableCollection<T> records() {
      return ImmutableList.copyOf(records);
    }

    @Override
    public void accept(ArincRecord arincRecord) {
      checkArgument(delegator.test(arincRecord));
      converter.apply(arincRecord).ifPresent(records::add);
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
  }
}
