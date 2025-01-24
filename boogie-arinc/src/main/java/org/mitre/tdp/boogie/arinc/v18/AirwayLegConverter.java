package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryCode;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.RouteHoldDistanceTime;

/**
 * Converter class for functionally transforming a {@link ArincRecord} into a {@link ArincAirwayLeg} concrete data model.
 * <br>
 * This class has some expectations on the data {@link ArincRecord} provided to it - these expectations are communicated through
 * the {@link AirwayLegValidator}.
 */
public final class AirwayLegConverter implements Function<ArincRecord, Optional<ArincAirwayLeg>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new AirwayLegValidator().negate();

  @Override
  public Optional<ArincAirwayLeg> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot apply conversion to null ArincRecord.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    ArincAirwayLeg airwayLeg = AirwayLegBuilder.INSTANCE.apply(arincRecord)
        .build();

    return Optional.of(airwayLeg);
  }
}
