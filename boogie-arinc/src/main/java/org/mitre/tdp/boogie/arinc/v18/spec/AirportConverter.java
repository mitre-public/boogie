package org.mitre.tdp.boogie.arinc.v18.spec;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.v18.ArincAirport;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.LongestRunwaySurfaceCode;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticTrueIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;

/**
 * Converter class for functionally transforming a {@link ArincRecord} into a {@link ArincAirport} concrete data model.
 * <br>
 * If the input record does not pass the {@link AirportValidator#test(ArincRecord)} call then this function returns {@link Optional#empty()}.
 */
public final class AirportConverter implements Function<ArincRecord, Optional<ArincAirport>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new AirportValidator().negate();

  @Override
  public Optional<ArincAirport> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null ArincRecord.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    // for optional fields we are forced to access them as such due to the template type return of the ArincRecord
    Optional<String> iataDesignator = arincRecord.optionalField("iataDesignator");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<Double> speedLimitAltitude = arincRecord.optionalField("speedLimitAltitude");
    Optional<Integer> longestRunway = arincRecord.optionalField("longestRunway");
    Optional<Boolean> ifrCapability = arincRecord.optionalField("ifrCapability");
    Optional<LongestRunwaySurfaceCode> longestRunwaySurfaceCode = arincRecord.optionalField("longestRunwaySurfaceCode");

    Optional<Double> magneticVariation = arincRecord.optionalField("magneticVariation");
    Optional<Double> airportElevation = arincRecord.optionalField("airportElevation");
    Optional<Integer> speedLimit = arincRecord.optionalField("speedLimit");
    Optional<String> recommendedNavaid = arincRecord.optionalField("recommendedNavaid");
    Optional<String> recommendedNavaidIcaoRegion = arincRecord.optionalField("recommendedNavaidIcaoRegion");
    Optional<Double> transitionAltitude = arincRecord.optionalField("transitionAltitude");
    Optional<Double> transitionLevel = arincRecord.optionalField("transitionLevel");
    Optional<PublicMilitaryIndicator> publicMilitaryIndicator = arincRecord.optionalField("publicMilitaryIndicator");
    Optional<Boolean> daylightTimeIndicator = arincRecord.optionalField("daylightTimeIndicator");
    Optional<MagneticTrueIndicator> magneticTrueIndicator = arincRecord.optionalField("magneticTrueIndicator");
    Optional<String> datumCode = arincRecord.optionalField("datumCode");
    Optional<String> airportFullName = arincRecord.optionalField("airportFullName");

    ArincAirport airport = new ArincAirport.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .airportIdentifier(arincRecord.requiredField("airportIdentifier"))
        .airportIcaoRegion(arincRecord.requiredField("airportIcaoRegion"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .iataDesignator(iataDesignator.orElse(null))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .speedLimitAltitude(speedLimitAltitude.orElse(null))
        .longestRunway(longestRunway.orElse(null))
        .ifrCapability(ifrCapability.orElse(null))
        .longestRunwaySurfaceCode(longestRunwaySurfaceCode.orElse(null))
        .latitude(arincRecord.requiredField("latitude"))
        .longitude(arincRecord.requiredField("longitude"))
        .magneticVariation(magneticVariation.orElse(null))
        .airportElevation(airportElevation.orElse(null))
        .speedLimit(speedLimit.orElse(null))
        .recommendedNavaid(recommendedNavaid.orElse(null))
        .recommendedNavaidIcaoRegion(recommendedNavaidIcaoRegion.orElse(null))
        .transitionAltitude(transitionAltitude.orElse(null))
        .transitionLevel(transitionLevel.orElse(null))
        .publicMilitaryIndicator(publicMilitaryIndicator.orElse(null))
        .daylightTimeIndicator(daylightTimeIndicator.orElse(null))
        .magneticTrueIndicator(magneticTrueIndicator.orElse(null))
        .datumCode(datumCode.orElse(null))
        .airportFullName(airportFullName.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdateCycle(arincRecord.requiredField("lastUpdateCycle"))
        .build();

    return Optional.of(airport);
  }
}
