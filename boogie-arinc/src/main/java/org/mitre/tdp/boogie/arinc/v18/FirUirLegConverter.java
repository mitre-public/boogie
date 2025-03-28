package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincFirUirLeg;

public final class FirUirLegConverter implements Function<ArincRecord, Optional<ArincFirUirLeg>> {
  private static final Predicate<ArincRecord> isInvalidRecord = new FirUirLegValidator().negate();

  @Override
  public Optional<ArincFirUirLeg> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot apply conversion to null ArincRecord.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");
    Optional<String> firUirAddress = arincRecord.optionalField("firUirAddress");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<String> adjacentFirIdentifier = arincRecord.optionalField("adjacentFirIdentifier");
    Optional<String> adjacentUirIdentifier = arincRecord.optionalField("adjacentUirIdentifier");
    Optional<String> reportingUnitSpeed = arincRecord.optionalField("reportingUnitsSpeed");
    Optional<String> reportingUnitsAltitude = arincRecord.optionalField("reportingUnitsAltitude");
    Optional<Double> arcOriginLatitude = arincRecord.optionalField("arcOriginLatitude");
    Optional<Double> arcOriginLongitude = arincRecord.optionalField("arcOriginLongitude");
    Optional<Integer> arcDistance = arincRecord.optionalField("arcDistance");
    Optional<Integer> arcBearing = arincRecord.optionalField("arcBearing");
    Optional<Double> firUpperLimit = arincRecord.optionalField("firUpperLimit");
    Optional<Double> uirLowerLimit = arincRecord.optionalField("uirLowerLimit");
    Optional<Double> uirUpperLimit = arincRecord.optionalField("uirUpperLimit");
    Optional<String> cruiseTableIndicator = arincRecord.optionalField("cruiseTableIndicator");
    Optional<String> firUirName = arincRecord.optionalField("firUirName");
    Optional<Double> firUirLatitude = arincRecord.optionalField("firUirLatitude");
    Optional<Double> firUirLongitude = arincRecord.optionalField("firUirLongitude");
    Optional<Boolean> firUirEntryReport = arincRecord.optionalField("firUirEntryReport");

    ArincFirUirLeg leg = new ArincFirUirLeg.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .subSectionCode(subSectionCode.orElse(null))
        .firUirIdentifier(arincRecord.requiredField("firUirIdentifier"))
        .firUirAddress(firUirAddress.orElse(null))
        .firUirIndicator(arincRecord.requiredField("firUirIndicator"))
        .sequenceNumber(arincRecord.requiredField("sequenceNumber"))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .adjacentFirIdentifier(adjacentFirIdentifier.orElse(null))
        .adjacentUirIdentifier(adjacentUirIdentifier.orElse(null))
        .reportingUnitsSpeed(reportingUnitSpeed.orElse(null))
        .reportingUnitsAltitude(reportingUnitsAltitude.orElse(null))
        .firUirEntryReport(firUirEntryReport.orElse(null))
        .boundaryVia(arincRecord.requiredField("boundaryVia"))
        .firUirLatitude(firUirLatitude.orElse(null))
        .firUirLongitude(firUirLongitude.orElse(null))
        .arcOriginLatitude(arcOriginLatitude.orElse(null))
        .arcOriginLongitude(arcOriginLongitude.orElse(null))
        .arcDistance(arcDistance.orElse(null))
        .arcBearing(arcBearing.orElse(null))
        .firUpperLimit(firUpperLimit.orElse(null))
        .uirLowerLimit(uirLowerLimit.orElse(null))
        .uirUpperLimit(uirUpperLimit.orElse(null))
        .cruiseTableIndicator(cruiseTableIndicator.orElse(null))
        .firUirName(firUirName.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .cycleDate(arincRecord.requiredField("cycle"))
        .build();

    return Optional.of(leg);
  }
}
