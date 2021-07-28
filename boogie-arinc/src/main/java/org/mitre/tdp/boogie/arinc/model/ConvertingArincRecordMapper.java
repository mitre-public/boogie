package org.mitre.tdp.boogie.arinc.model;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;

/**
 * A simple pass-through mapper implementation for applying the appropriate POJO converter logic to the incoming ArincRecords.
 * <br>
 * This class is useful in applications which don't require direct field-level access to the underlying data and instead simply
 * want to create POJOs and then ship them off for serialization, etc.
 * <br>
 * e.g. This class is explicitly used within the REST API for on-the-fly conversions of input ARINC 424 records.
 * <br>
 * If this class is more desirable within some downstream application (than the consumer impl) the recommendation would be to
 * use something like the pattern in the unit tests to collection the output in a more structured way. It's not sexy but it'll
 * do and allows for "safe" casts afterward.
 */
public final class ConvertingArincRecordMapper implements Function<ArincRecord, Optional<?>> {

  private final DelegatingMapper<ArincAirport> arincAirports;
  private final DelegatingMapper<ArincRunway> arincRunways;
  private final DelegatingMapper<ArincLocalizerGlideSlope> arincLocalizerGlideSlopes;
  private final DelegatingMapper<ArincNdbNavaid> arincNdbNavaids;
  private final DelegatingMapper<ArincVhfNavaid> arincVhfNavaids;
  private final DelegatingMapper<ArincWaypoint> arincWaypoints;
  private final DelegatingMapper<ArincAirwayLeg> arincAirwayLegs;
  private final DelegatingMapper<ArincProcedureLeg> arincProcedureLegs;

  private ConvertingArincRecordMapper(Builder builder) {
    this.arincAirports = new DelegatingMapper<>(builder.airportDelegator, builder.airportConverter);
    this.arincRunways = new DelegatingMapper<>(builder.runwayDelegator, builder.runwayConverter);
    this.arincLocalizerGlideSlopes = new DelegatingMapper<>(builder.localizerGlideSlopeDelegator, builder.localizerGlideSlopeConverter);
    this.arincNdbNavaids = new DelegatingMapper<>(builder.ndbNavaidDelegator, builder.ndbNavaidConverter);
    this.arincVhfNavaids = new DelegatingMapper<>(builder.vhfNavaidDelegator, builder.vhfNavaidConverter);
    this.arincWaypoints = new DelegatingMapper<>(builder.waypointDelegator, builder.waypointConverter);
    this.arincAirwayLegs = new DelegatingMapper<>(builder.airwayDelegator, builder.airwayConverter);
    this.arincProcedureLegs = new DelegatingMapper<>(builder.procedureDelegator, builder.procedureConverter);
  }

  @Override
  public Optional<?> apply(ArincRecord arincRecord) {
    if (arincProcedureLegs.test(arincRecord)) {
      return arincProcedureLegs.apply(arincRecord);
    }
    if (arincWaypoints.test(arincRecord)) {
      return arincWaypoints.apply(arincRecord);
    }
    if (arincAirports.test(arincRecord)) {
      return arincAirports.apply(arincRecord);
    }
    if (arincRunways.test(arincRecord)) {
      return arincRunways.apply(arincRecord);
    }
    if (arincAirwayLegs.test(arincRecord)) {
      return arincAirwayLegs.apply(arincRecord);
    }
    if (arincLocalizerGlideSlopes.test(arincRecord)) {
      return arincLocalizerGlideSlopes.apply(arincRecord);
    }
    if (arincNdbNavaids.test(arincRecord)) {
      return arincNdbNavaids.apply(arincRecord);
    }
    if (arincVhfNavaids.test(arincRecord)) {
      return arincVhfNavaids.apply(arincRecord);
    }
    return Optional.empty();
  }

  private static final class DelegatingMapper<T> implements Function<ArincRecord, Optional<T>>, Predicate<ArincRecord> {

    private final Predicate<ArincRecord> delegator;
    private final Function<ArincRecord, Optional<T>> converter;

    public DelegatingMapper(Predicate<ArincRecord> delegator, Function<ArincRecord, Optional<T>> converter) {
      this.delegator = requireNonNull(delegator);
      this.converter = requireNonNull(converter);
    }

    @Override
    public Optional<T> apply(ArincRecord arincRecord) {
      return Optional.of(arincRecord).filter(delegator).flatMap(converter);
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

    public ConvertingArincRecordMapper build() {
      return new ConvertingArincRecordMapper(this);
    }
  }
}
