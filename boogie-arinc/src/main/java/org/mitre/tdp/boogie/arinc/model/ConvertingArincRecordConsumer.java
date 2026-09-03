package org.mitre.tdp.boogie.arinc.model;

import org.mitre.tdp.boogie.arinc.ArincRecord;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

/**
 * A non-thread-safe version of a {@link Consumer} class which can eat {@link ArincRecord}s from a collection or stream and saves
 * them as internal state within itself.
 * <p>
 * This class is built with a pre-configured set of record-to-Java POJO converters. Each converter must return
 * {@link Optional#empty()} for records it does not own, and exactly one configured converter should accept any given record.
 * {@link ArincRecordConverterFactory} provides implementations for the supported ARINC versions.
 * <p>
 * This class is provided for convenience, but its limitations (especially the thread-safety) should be taken into account before
 * use.
 */
public final class ConvertingArincRecordConsumer implements Consumer<ArincRecord> {

  private final RecordAccumulator<ArincAirport> arincAirports;
  private final RecordAccumulator<ArincAirportPrimaryExtension> arincAirportExtensions;
  private final RecordAccumulator<ArincRunway> arincRunways;
  private final RecordAccumulator<ArincLocalizerGlideSlope> arincLocalizerGlideSlopes;
  private final RecordAccumulator<ArincNdbNavaid> arincNdbNavaids;
  private final RecordAccumulator<ArincVhfNavaid> arincVhfNavaids;
  private final RecordAccumulator<ArincWaypoint> arincWaypoints;
  private final RecordAccumulator<ArincAirwayLeg> arincAirwayLegs;
  private final RecordAccumulator<ArincProcedureLeg> arincProcedureLegs;
  private final RecordAccumulator<ArincGnssLandingSystem> gnssLandingSystems;
  private final RecordAccumulator<ArincHoldingPattern> arincHoldingPatterns;
  private final RecordAccumulator<ArincFirUirLeg> arincFirUirLeg;
  private final RecordAccumulator<ArincHelipad> arincHelipads;
  private final RecordAccumulator<ArincControlledAirspaceLeg> arincControlledAirspaceLegs;
  private final RecordAccumulator<ArincRestrictiveAirspaceLeg> arincRestrictiveAirspaceLegs;
  private final RecordAccumulator<ArincHeaderOne> arincHeaderOnes;
  private final RecordAccumulator<ArincHeliport> arincHeliports;

  private final Consumer<ArincRecord> consumer;

  private ConvertingArincRecordConsumer(Builder builder) {
    this.arincAirports = RecordAccumulator.deduplicating(builder.airportConverter);
    this.arincAirportExtensions = RecordAccumulator.deduplicating(builder.airportContinuationConverter);
    this.arincRunways = RecordAccumulator.deduplicating(builder.runwayConverter);
    this.arincLocalizerGlideSlopes = RecordAccumulator.deduplicating(builder.localizerGlideSlopeConverter);
    this.arincNdbNavaids = RecordAccumulator.deduplicating(builder.ndbNavaidConverter);
    this.arincVhfNavaids = RecordAccumulator.deduplicating(builder.vhfNavaidConverter);
    this.arincWaypoints = RecordAccumulator.deduplicating(builder.waypointConverter);
    this.arincAirwayLegs = RecordAccumulator.deduplicating(builder.airwayConverter);
    this.arincProcedureLegs = RecordAccumulator.appendOnly(builder.procedureConverter);
    this.gnssLandingSystems = RecordAccumulator.deduplicating(builder.gnssLandingSystemConverter);
    this.arincHoldingPatterns = RecordAccumulator.deduplicating(builder.holdingPatternConverter);
    this.arincFirUirLeg = RecordAccumulator.deduplicating(builder.firUirConverter);
    this.arincHelipads = RecordAccumulator.deduplicating(builder.helipadConverter);
    this.arincControlledAirspaceLegs = RecordAccumulator.deduplicating(builder.arincControlledAirspaceConverter);
    this.arincRestrictiveAirspaceLegs = RecordAccumulator.deduplicating(builder.restrictiveAirspaceConverter);
    this.arincHeaderOnes = RecordAccumulator.deduplicating(builder.headerConverter);
    this.arincHeliports = RecordAccumulator.deduplicating(builder.heliportConverter);

    RecordAccumulator<?>[] accumulators = {
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
    this.consumer = new MostRecentlyUsedConverter(accumulators);
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
   * <p>Callers should retain this result and release the consumer after ingestion. That makes the consumer's temporary duplicate
   * suppression sets eligible for reclamation while preserving encounter order and procedure-leg duplicates in compact lists.
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
        arincHeaderOnes.first(),
        arincHeliports.snapshot()
    );
  }

  /**
   * ARINC records are typically grouped by type, so trying the converter which most recently succeeded avoids scanning all
   * configured converters for most records.
   */
  private static final class MostRecentlyUsedConverter implements Consumer<ArincRecord> {

    private final RecordAccumulator<?>[] accumulators;

    private MostRecentlyUsedConverter(RecordAccumulator<?>[] accumulators) {
      this.accumulators = accumulators.clone();
    }

    @Override
    public void accept(ArincRecord arincRecord) {
      for (int i = 0; i < accumulators.length; i++) {
        RecordAccumulator<?> match = accumulators[i];
        if (match.convertAndAdd(arincRecord)) {
          if (i > 0) {
            System.arraycopy(accumulators, 0, accumulators, 1, i);
            accumulators[0] = match;
          }
          return;
        }
      }
    }
  }

  private static final class RecordAccumulator<T> {

    private final Function<ArincRecord, Optional<T>> converter;
    private final Collection<T> records;

    private RecordAccumulator(Function<ArincRecord, Optional<T>> converter, Collection<T> records) {
      this.converter = requireNonNull(converter);
      this.records = requireNonNull(records);
    }

    private static <T> RecordAccumulator<T> deduplicating(Function<ArincRecord, Optional<T>> converter) {
      return new RecordAccumulator<>(converter, new LinkedHashSet<>());
    }

    private static <T> RecordAccumulator<T> appendOnly(Function<ArincRecord, Optional<T>> converter) {
      return new RecordAccumulator<>(converter, new ArrayList<>());
    }

    private List<T> snapshot() {
      return List.copyOf(records);
    }

    private Optional<T> first() {
      return records.isEmpty() ? Optional.empty() : Optional.of(records.iterator().next());
    }

    private boolean convertAndAdd(ArincRecord arincRecord) {
      Optional<T> converted = converter.apply(arincRecord);
      if (converted.isEmpty()) {
        return false;
      }
      records.add(converted.get());
      return true;
    }
  }

  public static final class Builder {
    private Function<ArincRecord, Optional<ArincAirport>> airportConverter;
    private Function<ArincRecord, Optional<ArincAirportPrimaryExtension>> airportContinuationConverter;
    private Function<ArincRecord, Optional<ArincAirwayLeg>> airwayConverter;
    private Function<ArincRecord, Optional<ArincLocalizerGlideSlope>> localizerGlideSlopeConverter;
    private Function<ArincRecord, Optional<ArincNdbNavaid>> ndbNavaidConverter;
    private Function<ArincRecord, Optional<ArincProcedureLeg>> procedureConverter;
    private Function<ArincRecord, Optional<ArincRunway>> runwayConverter;
    private Function<ArincRecord, Optional<ArincVhfNavaid>> vhfNavaidConverter;
    private Function<ArincRecord, Optional<ArincWaypoint>> waypointConverter;
    private Function<ArincRecord, Optional<ArincGnssLandingSystem>> gnssLandingSystemConverter;
    private Function<ArincRecord, Optional<ArincHoldingPattern>> holdingPatternConverter;
    private Function<ArincRecord, Optional<ArincFirUirLeg>> firUirConverter;
    private Function<ArincRecord, Optional<ArincHelipad>> helipadConverter;
    private Function<ArincRecord, Optional<ArincControlledAirspaceLeg>> arincControlledAirspaceConverter;
    private Function<ArincRecord, Optional<ArincRestrictiveAirspaceLeg>> restrictiveAirspaceConverter;
    private Function<ArincRecord, Optional<ArincHeaderOne>> headerConverter;
    private Function<ArincRecord, Optional<ArincHeliport>> heliportConverter;

    public Builder airportContinuationConverter(Function<ArincRecord, Optional<ArincAirportPrimaryExtension>> airportContinuationConverter) {
      this.airportContinuationConverter = requireNonNull(airportContinuationConverter);
      return this;
    }

    public Builder airportConverter(Function<ArincRecord, Optional<ArincAirport>> airportConverter) {
      this.airportConverter = requireNonNull(airportConverter);
      return this;
    }

    public Builder airwayLegConverter(Function<ArincRecord, Optional<ArincAirwayLeg>> airwayConverter) {
      this.airwayConverter = requireNonNull(airwayConverter);
      return this;
    }

    public Builder localizerGlideSlopeConverter(Function<ArincRecord, Optional<ArincLocalizerGlideSlope>> localizerGlideSlopeConverter) {
      this.localizerGlideSlopeConverter = requireNonNull(localizerGlideSlopeConverter);
      return this;
    }

    public Builder ndbNavaidConverter(Function<ArincRecord, Optional<ArincNdbNavaid>> ndbNavaidConverter) {
      this.ndbNavaidConverter = requireNonNull(ndbNavaidConverter);
      return this;
    }

    public Builder procedureLegConverter(Function<ArincRecord, Optional<ArincProcedureLeg>> procedureConverter) {
      this.procedureConverter = requireNonNull(procedureConverter);
      return this;
    }

    public Builder runwayConverter(Function<ArincRecord, Optional<ArincRunway>> runwayConverter) {
      this.runwayConverter = requireNonNull(runwayConverter);
      return this;
    }

    public Builder vhfNavaidConverter(Function<ArincRecord, Optional<ArincVhfNavaid>> vhfNavaidConverter) {
      this.vhfNavaidConverter = requireNonNull(vhfNavaidConverter);
      return this;
    }

    public Builder waypointConverter(Function<ArincRecord, Optional<ArincWaypoint>> waypointConverter) {
      this.waypointConverter = requireNonNull(waypointConverter);
      return this;
    }

    public Builder gnssLandingSystemConverter(Function<ArincRecord, Optional<ArincGnssLandingSystem>> gnssLandingSystemConverter) {
      this.gnssLandingSystemConverter = requireNonNull(gnssLandingSystemConverter);
      return this;
    }

    public Builder holdingPatternConverter(Function<ArincRecord, Optional<ArincHoldingPattern>> holdingPatternConverter) {
      this.holdingPatternConverter = requireNonNull(holdingPatternConverter);
      return this;
    }

    public Builder firUirConverter(Function<ArincRecord, Optional<ArincFirUirLeg>> firUirConverter) {
      this.firUirConverter = requireNonNull(firUirConverter);
      return this;
    }

    public Builder helipadConverter(Function<ArincRecord, Optional<ArincHelipad>> helipadConverter) {
      this.helipadConverter = requireNonNull(helipadConverter);
      return this;
    }

    public Builder arincControlledAirspaceConverter(Function<ArincRecord, Optional<ArincControlledAirspaceLeg>> arincControlledAirspaceConverter) {
      this.arincControlledAirspaceConverter = requireNonNull(arincControlledAirspaceConverter);
      return this;
    }

    public Builder restrictiveAirspaceConverter(Function<ArincRecord, Optional<ArincRestrictiveAirspaceLeg>> restrictiveAirspaceConverter) {
      this.restrictiveAirspaceConverter = requireNonNull(restrictiveAirspaceConverter);
      return this;
    }

    public Builder headerConverter(Function<ArincRecord, Optional<ArincHeaderOne>> headerConverter) {
      this.headerConverter = requireNonNull(headerConverter);
      return this;
    }

    public Builder heliportConverter(Function<ArincRecord, Optional<ArincHeliport>> heliportConverter) {
      this.heliportConverter = requireNonNull(heliportConverter);
      return this;
    }

    public ConvertingArincRecordConsumer build() {
      return new ConvertingArincRecordConsumer(this);
    }

  }
}
