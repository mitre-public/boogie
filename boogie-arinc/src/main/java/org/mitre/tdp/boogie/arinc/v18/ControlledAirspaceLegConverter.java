package org.mitre.tdp.boogie.arinc.v18;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public final class ControlledAirspaceLegConverter implements Function<ArincRecord, Optional<ArincControlledAirspaceLeg>> {
  
  private static final Predicate<ArincRecord> isInvalidRecord = new ControlledAirspaceValidator().negate();
  
  @Override
  public Optional<ArincControlledAirspaceLeg> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null ArincRecord.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");
    Optional<SectionCode> supplierSectionCode = arincRecord.optionalField("sectionCode2");
    Optional<String> supplierSubSectionCode = arincRecord.optionalField("subSectionCode2");
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
    Optional<Double> rnp = arincRecord.optionalField("rnp");
    Optional<Double> lowerLimit = arincRecord.optionalField("lowerLimit");
    Optional<String> lowerUnitIndicator = arincRecord.optionalField("lowerIndicator");
    Optional<Double> upperLimit = arincRecord.optionalField("upperLimit");
    Optional<String> upperUnitIndicator = arincRecord.optionalField("upperIndicator");
    Optional<String> controlledAirspaceName = arincRecord.optionalField("controlledAirspaceName");
    Optional<String> airspaceClassification = arincRecord.optionalField("airspaceClassification");

    ArincControlledAirspaceLeg controlledAirspace = new ArincControlledAirspaceLeg.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .subSectionCode(subSectionCode.orElse(null))
        .icaoCode(arincRecord.requiredField("icaoRegion"))
        .airspaceType(arincRecord.requiredField("airspaceType"))
        .airspaceCenter(arincRecord.requiredField("airspaceCenter"))
        .suppliedSectionCode(supplierSectionCode.map(Enum::name).orElse(null))
        .supplierSubSectionCode(supplierSubSectionCode.orElse(null))
        .airspaceClassification(airspaceClassification.orElse(null))
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
        .rnp(rnp.orElse(null))
        .lowerLimit(lowerLimit.orElse(null))
        .lowerUnitIndicator(lowerUnitIndicator.orElse(null))
        .upperLimit(upperLimit.orElse(null))
        .upperUnitIndicator(upperUnitIndicator.orElse(null))
        .controlledAirspaceName(controlledAirspaceName.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .cycleDate(arincRecord.requiredField("cycle"))
        .build();
  
    return Optional.of(controlledAirspace);
  }
}
