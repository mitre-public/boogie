package org.mitre.tdp.boogie.arinc.v18;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.utils.LegTimeFromString;

/**
 * Because the holding pattern only changed by ARINC version by adding fields, this class creates the baseline
 * builder which can be used to make an 18 directly, or used in 19 by adding a few more items to the builder.
 */
public final class HoldingPatternBuilder implements Function<ArincRecord, ArincHoldingPattern.Builder> {

  public static final HoldingPatternBuilder INSTANCE = new HoldingPatternBuilder();

  private HoldingPatternBuilder() {

  }

  @Override
  public ArincHoldingPattern.Builder apply(ArincRecord arincRecord) {
    Optional<String> regionCode = arincRecord.optionalField("regionCode");
    Optional<String> icaoRegion = arincRecord.optionalField("icaoRegion");
    Optional<String> duplicateIdentifier = arincRecord.optionalField("duplicateIdentifier");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<Integer> legTime = arincRecord.optionalField("legTime");
    Optional<Duration> legDuration = legTime.map(Object::toString).map(LegTimeFromString.INSTANCE); //same as leg but separate field in holds
    Optional<Double> legLength = arincRecord.optionalField("legLength");
    Optional<Double> min = arincRecord.optionalField("minimumAltitude");
    Optional<Double> maximum = arincRecord.optionalField("maxAltitude");
    Optional<Integer> speed = arincRecord.optionalField("holdingSpeed");
    Optional<Double> rnp = arincRecord.optionalField("rnp");
    Optional<Double> arcRadius = arincRecord.optionalField("arcRadius");
    Optional<String> name = arincRecord.optionalField("holdingName");
    Optional<Integer> fileRecordNumber = arincRecord.optionalField("fileRecordNumber");
    Optional<String> lastUpdatedCycle = arincRecord.optionalField("lastUpdatedCycle");
    Optional<String> fixSubsectionCode = arincRecord.optionalField("fixSubSectionCode");

    return new ArincHoldingPattern.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .regionCode(regionCode.orElse(null))
        .icaoRegion(icaoRegion.orElse(null))
        .duplicateIdentifier(duplicateIdentifier.orElse(null))
        .fixIdentifier(arincRecord.requiredField("fixIdentifier"))
        .fixIcaoRegion(arincRecord.requiredField("fixIcaoRegion"))
        .fixSectionCode(arincRecord.requiredField("fixSectionCode"))
        .fixSubsectionCode(fixSubsectionCode.orElse(null))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .inboundHoldingCourse(arincRecord.requiredField("inboundHoldingCourse"))
        .turnDirection(arincRecord.requiredField("turnDirection"))
        .legTime(legDuration.orElse(null))
        .legLength(legLength.orElse(null))
        .minimumAltitude(min.orElse(null))
        .maxAltitude(maximum.orElse(null))
        .holdingSpeed(speed.orElse(null))
        .rnp(rnp.orElse(null))
        .arcRadius(arcRadius.orElse(null))
        .holdingName(name.orElse(null))
        .fileRecordNumber(fileRecordNumber.orElse(null))
        .lastUpdatedCycle(lastUpdatedCycle.orElse(null));
  }
}
