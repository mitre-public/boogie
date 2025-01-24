package org.mitre.tdp.boogie.arinc.v19;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegBuilder;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegValidator;

public final class AirwayLegConverter implements Function<ArincRecord, Optional<ArincAirwayLeg>> {
  private static final Predicate<ArincRecord> isInvalidRecord = new AirwayLegValidator().negate();

  @Override
  public Optional<ArincAirwayLeg> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot apply conversion to null ArincRecord.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<Integer> verticalScale = arincRecord.optionalField("verticalScaleFactor");
    Optional<Integer> rvsmMinLevel = arincRecord.optionalField("rvsmMinLevel");
    Optional<Integer> rvsmMaxLevel = arincRecord.optionalField("rvsmMaxLevel");

    ArincAirwayLeg airwayLeg = AirwayLegBuilder.INSTANCE.apply(arincRecord)
        .verticalScaleFactor(verticalScale.orElse(null))
        .rvsmMinAltitude(rvsmMinLevel.orElse(null))
        .rvsmMaxAltitude(rvsmMaxLevel.orElse(null))
        .build();

    return Optional.of(airwayLeg);
  }
}
