package org.mitre.tdp.boogie.arinc.v22;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegBuilder;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegValidator;

/**
 * Converter class for functionally transforming a {@link ArincRecord} into a {@link ArincAirwayLeg} concrete data model.
 * <br>
 * This class has some expectations on the data {@link ArincRecord} provided to it - these expectations are communicated through
 * the {@link AirwayLegValidator}.
 */
public final class AirwayLegConverter implements Function<ArincRecord, Optional<ArincAirwayLeg>> {

  private static final Predicate<ArincRecord> valid = new AirwayLegValidator();

  @Override
  public Optional<ArincAirwayLeg> apply(ArincRecord arincRecord) {
    return Optional.ofNullable(arincRecord)
        .map(i -> requireNonNull(i, "Cannot apply conversion to null ArincRecord."))
        .filter(valid)
        .map(AirwayLegBuilder.INSTANCE)
        .map(builder -> ADD_22.apply(builder, arincRecord))
        .map(ArincAirwayLeg.Builder::build);
  }

  private static final BiFunction<ArincAirwayLeg.Builder, ArincRecord, ArincAirwayLeg.Builder> ADD_22 = (builder, arincRecord) -> {
    Optional<Integer> verticalScale = arincRecord.optionalField("verticalScaleFactor");
    Optional<Integer> rvsmMinLevel = arincRecord.optionalField("rvsmMinLevel");
    Optional<Integer> rvsmMaxLevel = arincRecord.optionalField("rvsmMaxLevel");
    Optional<String> routeQual1 = arincRecord.optionalField("routeTypeQualifier1");
    Optional<String> routeQual2 = arincRecord.optionalField("routeTypeQualifier2");
    Optional<String> routeQual3 = arincRecord.optionalField("routeTypeQualifier3");
    return builder.verticalScaleFactor(verticalScale.orElse(null))
        .rvsmMinAltitude(rvsmMinLevel.orElse(null))
        .rvsmMaxAltitude(rvsmMaxLevel.orElse(null))
        .routeTypeQualifier1(routeQual1.orElse(null))
        .routeTypeQualifier3(routeQual2.orElse(null))
        .routeTypeQualifier3(routeQual3.orElse(null));
  };
}
