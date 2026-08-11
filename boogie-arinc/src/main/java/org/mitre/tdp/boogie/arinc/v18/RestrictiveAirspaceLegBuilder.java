package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincRestrictiveAirspaceLeg;
import org.mitre.tdp.boogie.arinc.v18.field.Level;

public final class RestrictiveAirspaceLegBuilder implements Function<ArincRecord, ArincRestrictiveAirspaceLeg.Builder> {

  public static final RestrictiveAirspaceLegBuilder INSTANCE = new RestrictiveAirspaceLegBuilder();

  private RestrictiveAirspaceLegBuilder() {
  }

  @Override
  public ArincRestrictiveAirspaceLeg.Builder apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null ArincRecord.");

    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");
    Optional<String> multipleCode = arincRecord.optionalField("multipleCode");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<Level> level = arincRecord.optionalField("level");
    Optional<String> timeCode = arincRecord.optionalField("timeCode");
    Optional<String> notam = arincRecord.optionalField("notam");
    Optional<Double> latitude = arincRecord.optionalField("latitude");
    Optional<Double> longitude = arincRecord.optionalField("longitude");
    Optional<Double> arcOriginLatitude = arincRecord.optionalField("arcOriginLatitude");
    Optional<Double> arcOriginLongitude = arincRecord.optionalField("arcOriginLongitude");
    Optional<Integer> arcDistance = arincRecord.optionalField("arcDistance");
    Optional<Integer> arcBearing = arincRecord.optionalField("arcBearing");
    Optional<Double> lowerLimit = arincRecord.optionalField("lowerLimit");
    Optional<String> lowerUnitIndicator = arincRecord.optionalField("lowerIndicator");
    Optional<Double> upperLimit = arincRecord.optionalField("upperLimit");
    Optional<String> upperUnitIndicator = arincRecord.optionalField("upperIndicator");
    Optional<String> restrictiveAirspaceName = arincRecord.optionalField("restrictiveAirspaceName");

    return new ArincRestrictiveAirspaceLeg.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .subSectionCode(subSectionCode.orElse(null))
        .icaoCode(arincRecord.requiredField("icaoRegion"))
        .restrictiveAirspaceDesignation(arincRecord.requiredField("restrictiveAirspaceDesignation"))
        .multipleCode(multipleCode.orElse(null))
        .sequenceNumber(arincRecord.requiredField("sequenceNumber"))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .level(level.orElse(null))
        .timeCode(timeCode.orElse(null))
        .notam(notam.orElse(null))
        .boundaryVia(arincRecord.requiredField("boundaryVia"))
        .latitude(latitude.orElse(null))
        .longitude(longitude.orElse(null))
        .arcOriginLatitude(arcOriginLatitude.orElse(null))
        .arcOriginLongitude(arcOriginLongitude.orElse(null))
        .arcDistance(arcDistance.orElse(null))
        .arcBearing(arcBearing.orElse(null))
        .lowerLimit(lowerLimit.orElse(null))
        .lowerUnitIndicator(lowerUnitIndicator.orElse(null))
        .upperLimit(upperLimit.orElse(null))
        .upperUnitIndicator(upperUnitIndicator.orElse(null))
        .restrictiveAirspaceName(restrictiveAirspaceName.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .cycleDate(arincRecord.requiredField("cycle"));
  }
}
