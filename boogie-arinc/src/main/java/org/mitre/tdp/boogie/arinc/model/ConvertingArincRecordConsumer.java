package org.mitre.tdp.boogie.arinc.model;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
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
 * This class is built with a pre-configured set of record -> Java POJO delegators and a set of record -> POJO converters for a
 * pre-configured set of data types. {@link ArincRecordConverterFactory} provides some default implementations for a subset of
 * {@link ArincRecord} versions.
 * <br>
 * This class is provided for convenience, but it's limitations (especially the thread-safety) should be taken into account before
 * use.
 */
public final class ConvertingArincRecordConsumer implements Consumer<ArincRecord> {

  private final DelegatableCollection<ArincAirport> arincAirports;
  private final DelegatableCollection<ArincRunway> arincRunways;
  private final DelegatableCollection<ArincLocalizerGlideSlope> arincLocalizerGlideSlopes;
  private final DelegatableCollection<ArincNdbNavaid> arincNdbNavaids;
  private final DelegatableCollection<ArincVhfNavaid> arincVhfNavaids;
  private final DelegatableCollection<ArincWaypoint> arincWaypoints;
  private final DelegatableCollection<ArincAirwayLeg> arincAirwayLegs;
  private final DelegatableCollection<ArincProcedureLeg> arincProcedureLegs;

  private final MRUDequeConsumer<ArincRecord, DelegatableCollection<?>> consumer;

  private ConvertingArincRecordConsumer(Builder builder) {
    this.arincAirports = new DelegatableCollection<>(builder.airportDelegator, builder.airportConverter);
    this.arincRunways = new DelegatableCollection<>(builder.runwayDelegator, builder.runwayConverter);
    this.arincLocalizerGlideSlopes = new DelegatableCollection<>(builder.localizerGlideSlopeDelegator, builder.localizerGlideSlopeConverter);
    this.arincNdbNavaids = new DelegatableCollection<>(builder.ndbNavaidDelegator, builder.ndbNavaidConverter);
    this.arincVhfNavaids = new DelegatableCollection<>(builder.vhfNavaidDelegator, builder.vhfNavaidConverter);
    this.arincWaypoints = new DelegatableCollection<>(builder.waypointDelegator, builder.waypointConverter);
    this.arincAirwayLegs = new DelegatableCollection<>(builder.airwayDelegator, builder.airwayConverter);
    this.arincProcedureLegs = new DelegatableCollection<>(builder.procedureDelegator, builder.procedureConverter);

    this.consumer = new MRUDequeConsumer<>(
        this.arincAirports,
        this.arincRunways,
        this.arincLocalizerGlideSlopes,
        this.arincNdbNavaids,
        this.arincVhfNavaids,
        this.arincWaypoints,
        this.arincAirwayLegs,
        this.arincProcedureLegs
    );
  }

  public Collection<ArincAirport> arincAirports() {
    return arincAirports.records();
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

  @Override
  public void accept(ArincRecord arincRecord) {
    requireNonNull(arincRecord);
    this.consumer.accept(arincRecord);
  }

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
        predicateDeque.remove(m);
        predicateDeque.addFirst(m);
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

    public ConvertingArincRecordConsumer build() {
      return new ConvertingArincRecordConsumer(this);
    }
  }
}
