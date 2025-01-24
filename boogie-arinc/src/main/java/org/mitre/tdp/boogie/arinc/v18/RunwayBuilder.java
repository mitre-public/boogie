package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;

public final class RunwayBuilder implements Function<ArincRecord, ArincRunway.Builder> {
  public static final RunwayBuilder INSTANCE = new RunwayBuilder();
  private RunwayBuilder() {}
  @Override
  public ArincRunway.Builder apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Input ArincRecord cannot be null.");

    Optional<CustomerAreaCode> customerAreaCode = arincRecord.optionalField("customerAreaCode");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<Integer> runwayLength = arincRecord.optionalField("runwayLength");
    Optional<Double> runwayMagneticBearing = arincRecord.optionalField("runwayMagneticBearing");
    Optional<Double> runwayGradient = arincRecord.optionalField("runwayGradient");
    Optional<Integer> landingThresholdElevation = arincRecord.optionalField("landingThresholdElevation");
    Optional<Integer> thresholdDisplacementDistance = arincRecord.optionalField("thresholdDisplacementDistance");
    Optional<Integer> thresholdCrossingHeight = arincRecord.optionalField("thresholdCrossingHeight");
    Optional<Integer> runwayWidth = arincRecord.optionalField("runwayWidth");
    Optional<String> ilsGlsMlsIdentifier = arincRecord.optionalField("ilsMlsGlsIdentifier");
    Optional<String> ilsGlsMlsCategory = arincRecord.optionalField("ilsMlsGlsCategory");
    Optional<Integer> stopway = arincRecord.optionalField("stopway");
    Optional<String> secondaryIlsGlsMlsIdentifier = arincRecord.optionalField("secondaryIlsMlsGlsIdentifier");
    Optional<String> secondaryIlsGlsMlsCategory = arincRecord.optionalField("secondaryIlsMlsGlsCategory");
    Optional<String> runwayDescription = arincRecord.optionalField("runwayDescription");

   return new ArincRunway.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(customerAreaCode.orElse(null))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .airportIdentifier(arincRecord.requiredField("airportIdentifier"))
        .airportIcaoRegion(arincRecord.requiredField("airportIcaoRegion"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .runwayIdentifier(arincRecord.requiredField("runwayIdentifier"))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .runwayLength(runwayLength.orElse(null))
        .runwayMagneticBearing(runwayMagneticBearing.orElse(null))
        .latitude(arincRecord.requiredField("latitude"))
        .longitude(arincRecord.requiredField("longitude"))
        .runwayGradient(runwayGradient.orElse(null))
        .landingThresholdElevation(landingThresholdElevation.orElse(null))
        .thresholdDisplacementDistance(thresholdDisplacementDistance.orElse(null))
        .thresholdCrossingHeight(thresholdCrossingHeight.orElse(null))
        .runwayWidth(runwayWidth.orElse(null))
        .ilsMlsGlsIdentifier(ilsGlsMlsIdentifier.orElse(null))
        .ilsMlsGlsCategory(ilsGlsMlsCategory.orElse(null))
        .stopway(stopway.orElse(null))
        .secondaryIlsMlsGlsIdentifier(secondaryIlsGlsMlsIdentifier.orElse(null))
        .secondaryIlsMlsGlsCategory(secondaryIlsGlsMlsCategory.orElse(null))
        .runwayDescription(runwayDescription.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdateCycle(arincRecord.requiredField("lastUpdateCycle"));
  }
}
