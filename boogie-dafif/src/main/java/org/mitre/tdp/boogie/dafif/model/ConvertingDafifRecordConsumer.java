package org.mitre.tdp.boogie.dafif.model;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.v81.converter.DafifAddRunwayConverter;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

public class ConvertingDafifRecordConsumer implements Consumer<DafifRecord> {

  private final DelegatableCollection<DafifAirport> dafifAirports;
  private final DelegatableCollection<DafifRunway> dafifRunways;
  private final DelegatableCollection<DafifIls> dafifIls;
  private final DelegatableCollection<DafifNavaid> dafifNavaids;
  private final DelegatableCollection<DafifTerminalParent> dafifTerminalParents;
  private final DelegatableCollection<DafifTerminalSegment> dafifTerminalSegments;
  private final DelegatableCollection<DafifWaypoint> dafifWaypoints;
  private final DelegatableCollection<DafifAirTrafficService> dafifAts;
  private final DelegatableCollection<DafifAddRunway> dafifAddRunway;
  private final MRUDequeConsumer<DafifRecord, DelegatableCollection<?>> consumer;

  private ConvertingDafifRecordConsumer(Builder builder) {
    this.dafifAirports = new DelegatableCollection<>(builder.airportDelegator, builder.airportConverter);
    this.dafifRunways = new DelegatableCollection<>(builder.runwayDelegator, builder.runwayConverter);
    this.dafifIls = new DelegatableCollection<>(builder.ilsDelegator, builder.ilsConverter);
    this.dafifNavaids = new DelegatableCollection<>(builder.navaidDelegator, builder.navaidConverter);
    this.dafifTerminalParents = new DelegatableCollection<>(builder.terminalParentDelegator, builder.terminalParentConverter);
    this.dafifTerminalSegments = new DelegatableCollection<>(builder.terminalSegmentDelegator, builder.terminalSegmentConverter);
    this.dafifWaypoints = new DelegatableCollection<>(builder.waypointDelegator, builder.waypointConverter);
    this.dafifAts = new DelegatableCollection<>(builder.atsDelegator, builder.atsConverter);
    this.dafifAddRunway = new DelegatableCollection<>(builder.addRunwayDelegator, builder.addRunwayConverter);
    this.consumer = new MRUDequeConsumer<>(
        this.dafifAirports,
        this.dafifRunways,
        this.dafifIls,
        this.dafifNavaids,
        this.dafifTerminalParents,
        this.dafifTerminalSegments,
        this.dafifWaypoints,
        this.dafifAts,
        this.dafifAddRunway
    );
  }

  public Collection<DafifAirport> dafifAirports() {
    return dafifAirports.records();
  }

  public Collection<DafifRunway> dafifRunways() {
    return dafifRunways.records();
  }

  public Collection<DafifIls> dafifIls() {
    return dafifIls.records();
  }

  public Collection<DafifNavaid> dafifNavaids() {
    return dafifNavaids.records();
  }

  public Collection<DafifTerminalParent> dafifTerminalParents() {
    return dafifTerminalParents.records();
  }
  public Collection<DafifTerminalSegment> dafifTerminalSegments() {
    return dafifTerminalSegments.records();
  }
  public Collection<DafifWaypoint> dafifWaypoints() {
    return dafifWaypoints.records();
  }
  public Collection<DafifAirTrafficService> dafifAts() {
    return dafifAts.records();
  }
  public Collection<DafifAddRunway> dafifAddRunways() {
    return dafifAddRunway.records();
  }

  @Override
  public void accept(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord);
    this.consumer.accept(dafifRecord);
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
        if (!predicateDeque.getFirst().equals(m)) {
          predicateDeque.remove(m);
          predicateDeque.addFirst(m);
        }
      });
    }
  }

  private static final class DelegatableCollection<T> implements Consumer<DafifRecord>, Predicate<DafifRecord> {

    private final Predicate<DafifRecord> delegator;
    private final Function<DafifRecord, Optional<T>> converter;

    private final Collection<T> records;

    public DelegatableCollection(
        Predicate<DafifRecord> delegator,
        Function<DafifRecord, Optional<T>> converter
    ) {
      this.delegator = requireNonNull(delegator);
      this.converter = requireNonNull(converter);
      this.records = new LinkedHashSet<>();
    }

    public ImmutableCollection<T> records() {
      return ImmutableList.copyOf(records);
    }

    @Override
    public void accept(DafifRecord dafifRecord) {
      checkArgument(delegator.test(dafifRecord));
      converter.apply(dafifRecord).ifPresent(records::add);
    }

    @Override
    public boolean test(DafifRecord dafifRecord) {
      return delegator.test(dafifRecord);
    }
  }

  public static class Builder {
    private Predicate<DafifRecord> airportDelegator;
    private Function<DafifRecord, Optional<DafifAirport>> airportConverter;
    private Predicate<DafifRecord> runwayDelegator;
    private Function<DafifRecord, Optional<DafifRunway>> runwayConverter;
    private Predicate<DafifRecord> ilsDelegator;
    private Function<DafifRecord, Optional<DafifIls>> ilsConverter;
    private Predicate<DafifRecord> navaidDelegator;
    private Function<DafifRecord, Optional<DafifNavaid>> navaidConverter;
    private Predicate<DafifRecord> terminalParentDelegator;
    private Function<DafifRecord, Optional<DafifTerminalParent>> terminalParentConverter;
    private Predicate<DafifRecord> terminalSegmentDelegator;
    private Function<DafifRecord, Optional<DafifTerminalSegment>> terminalSegmentConverter;
    private Predicate<DafifRecord> waypointDelegator;
    private Function<DafifRecord, Optional<DafifWaypoint>> waypointConverter;
    private Predicate<DafifRecord> atsDelegator;
    private Function<DafifRecord, Optional<DafifAirTrafficService>> atsConverter;
    private Predicate<DafifRecord> addRunwayDelegator;
    private Function<DafifRecord, Optional<DafifAddRunway>> addRunwayConverter;

    public Builder airportDelegator(Predicate<DafifRecord> airportDelegator) {
      this.airportDelegator =  airportDelegator;
      return this;
    }

    public Builder airportConverter(Function<DafifRecord, Optional<DafifAirport>> airportConverter) {
      this.airportConverter = airportConverter;
      return this;
    }

    public Builder runwayDelegator(Predicate<DafifRecord> runwayDelegator) {
      this.runwayDelegator =  runwayDelegator;
      return this;
    }

    public Builder runwayConverter(Function<DafifRecord, Optional<DafifRunway>> runwayConverter) {
      this.runwayConverter = runwayConverter;
      return this;
    }

    public Builder ilsDelegator(Predicate<DafifRecord> ilsDelegator) {
      this.ilsDelegator =  ilsDelegator;
      return this;
    }

    public Builder ilsConverter(Function<DafifRecord, Optional<DafifIls>> ilsConverter) {
      this.ilsConverter = ilsConverter;
      return this;
    }

    public Builder navaidDelegator(Predicate<DafifRecord> navaidDelegator) {
      this.navaidDelegator = navaidDelegator;
      return this;
    }

    public Builder navaidConverter(Function<DafifRecord, Optional<DafifNavaid>> navaidConverter) {
      this.navaidConverter = navaidConverter;
      return this;
    }

    public Builder terminalParentDelegator(Predicate<DafifRecord> terminalParentDelegator) {
      this.terminalParentDelegator = terminalParentDelegator;
      return this;
    }

    public Builder terminalParentConverter(Function<DafifRecord, Optional<DafifTerminalParent>> terminalParentConverter) {
      this.terminalParentConverter = terminalParentConverter;
      return this;
    }

    public Builder terminalSegmentDelegator(Predicate<DafifRecord> terminalSegmentDelegator) {
      this.terminalSegmentDelegator = terminalSegmentDelegator;
      return this;
    }

    public Builder terminalSegmentConverter(Function<DafifRecord, Optional<DafifTerminalSegment>> terminalSegmentConverter) {
      this.terminalSegmentConverter = terminalSegmentConverter;
      return this;
    }

    public Builder waypointDelegator(Predicate<DafifRecord> waypointDelegator) {
      this.waypointDelegator = waypointDelegator;
      return this;
    }

    public Builder waypointConverter(Function<DafifRecord, Optional<DafifWaypoint>> waypointConverter) {
      this.waypointConverter = waypointConverter;
      return this;
    }

    public Builder atsDelegator(Predicate<DafifRecord> atsDelegator) {
      this.atsDelegator = atsDelegator;
      return this;
    }

    public Builder atsConverter(Function<DafifRecord, Optional<DafifAirTrafficService>> atsConverter) {
      this.atsConverter = atsConverter;
      return this;
    }
    public Builder addRunwayDelegator(Predicate<DafifRecord> addRunwayDelegator) {
      this.addRunwayDelegator = addRunwayDelegator;
      return this;
    }

    public Builder addRunwayConverter(Function<DafifRecord, Optional<DafifAddRunway>> addRunwayConverter) {
      this.addRunwayConverter = addRunwayConverter;
      return this;
    }

    public ConvertingDafifRecordConsumer build() {
      return new ConvertingDafifRecordConsumer(this);
    }
  }
}
