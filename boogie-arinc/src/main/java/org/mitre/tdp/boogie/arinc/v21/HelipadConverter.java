package org.mitre.tdp.boogie.arinc.v21;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.utils.Dimensions;
import org.mitre.tdp.boogie.arinc.v18.field.LongestRunwaySurfaceCode;
import org.mitre.tdp.boogie.arinc.v21.field.HelicopterPerformanceRequirement;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;
import org.mitre.tdp.boogie.arinc.v21.field.SurfaceType;

public final class HelipadConverter implements Function<ArincRecord, Optional<ArincHelipad>> {
  private static final Predicate<ArincRecord> validRecord = new HelipadValidator();

  @Override
  public Optional<ArincHelipad> apply(ArincRecord arincRecord) {
    return Optional.ofNullable(arincRecord)
        .map(i -> requireNonNull(i, "Cannot apply conversion to null ArincRecord."))
        .filter(validRecord)
        .map(ADD_FIELDS);
  }

  private static final Function<ArincRecord, ArincHelipad> ADD_FIELDS = arincRecord -> {
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");

    PadShape padShape = arincRecord.requiredField("padShape");
    Optional<String> rawDimensions = arincRecord.optionalField("padDimensions");
    Optional<Dimensions> dimensions = rawDimensions.flatMap(raw -> DimensionsParser.INSTANCE.apply(raw, padShape));

    Optional<LongestRunwaySurfaceCode> helipadSurfaceCode = arincRecord.optionalField("helipadSurfaceCode");
    Optional<SurfaceType> surfaceType = arincRecord.optionalField("helipadSurfaceType");
    Optional<Integer> maxAllowableWeight = arincRecord.optionalField("MaximumAllowableHelicopterWeight");
    Optional<HelicopterPerformanceRequirement> performanceRequirement = arincRecord.optionalField("helicopterPerformanceRequirement");
    Optional<Integer> padElevation = arincRecord.optionalField("landingThresholdElevation");

    return new ArincHelipad.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .airportHeliportIdentifier(arincRecord.requiredField("airportOrHeliportIdentifier"))
        .icaoCode(arincRecord.requiredField("icaoCode"))
        .helipadIdentifier(arincRecord.requiredField("helipadIdentifier"))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .padShape(padShape.name())
        .padXDimension(dimensions.flatMap(Dimensions::xPossible).orElse(null))
        .padYDimension(dimensions.flatMap(Dimensions::yPossible).orElse(null))
        .padDiameter(dimensions.flatMap(Dimensions::diameterPossible).orElse(null))
        .latitude(arincRecord.requiredField("latitude"))
        .longitude(arincRecord.requiredField("longitude"))
        .helipadSurfaceCode(helipadSurfaceCode.map(Enum::name).orElse(null))
        .helipadSurfaceType(surfaceType.map(Enum::name).orElse(null))
        .maximumHelicopterWeight(maxAllowableWeight.orElse(null))
        .helicopterPerformanceRequirement(performanceRequirement.map(Enum::name).orElse(null))
        .padElevation(padElevation.map(Double::valueOf).orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .cycle(arincRecord.requiredField("cycle"))
        .build();
  };
}
