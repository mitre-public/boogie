package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticTrueIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;

public final class HeliportBuilder implements Function<ArincRecord, ArincHeliport.Builder> {
  public static final HeliportBuilder INSTANCE = new HeliportBuilder();

  private HeliportBuilder() {
  }

  @Override
  public ArincHeliport.Builder apply(ArincRecord arincRecord) {
    Optional<String> iataDesignator = arincRecord.optionalField("iataDesignator");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<Double> speedLimitAltitude = arincRecord.optionalField("speedLimitAltitude");
    Optional<Boolean> ifrCapability = arincRecord.optionalField("ifrCapability");
    Optional<Double> magneticVariation = arincRecord.optionalField("magneticVariation");
    Optional<Double> heliportElevation = arincRecord.optionalField("heliportElevation");
    Optional<Integer> speedLimit = arincRecord.optionalField("speedLimit");
    Optional<String> recommendedVhfNavaid = arincRecord.optionalField("recommendedVhfNavaid");
    Optional<String> recommendedVhfNavaidIcaoRegion = arincRecord.optionalField("recommendedVhfNavaidIcaoRegion");
    Optional<Double> transitionAltitude = arincRecord.optionalField("transitionAltitude");
    Optional<Double> transitionLevel = arincRecord.optionalField("transitionLevel");
    Optional<PublicMilitaryIndicator> publicMilitaryIndicator = arincRecord.optionalField("publicMilitaryIndicator");
    Optional<Boolean> daylightTimeIndicator = arincRecord.optionalField("daylightTimeIndicator");
    Optional<MagneticTrueIndicator> magneticTrueIndicator = arincRecord.optionalField("magneticTrueIndicator");
    Optional<String> datumCode = arincRecord.optionalField("datumCode");
    Optional<String> airportFullName = arincRecord.optionalField("heliportFullName");
    Optional<Integer> recordNumber = arincRecord.optionalField("fileRecordNumber");
    Optional<String> cycle = arincRecord.optionalField("lastUpdatedCycle");

    return ArincHeliport.builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .heliportIdentifier(arincRecord.requiredField("heliportIdentifier"))
        .heliportIcaoRegion(arincRecord.requiredField("heliportIcaoRegion"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .iataDesignator(iataDesignator.orElse(null))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .speedLimitAltitude(speedLimitAltitude.orElse(null))
        .datumCode(datumCode.orElse(null))
        .ifrCapability(ifrCapability.orElse(null))
        .latitude(arincRecord.requiredField("latitude"))
        .longitude(arincRecord.requiredField("longitude"))
        .magneticVariation(magneticVariation.orElse(null))
        .heliportElevation(heliportElevation.orElse(null))
        .speedLimit(speedLimit.orElse(null))
        .recommendedVhfNavaid(recommendedVhfNavaid.orElse(null))
        .navaidIcaoRegion(recommendedVhfNavaidIcaoRegion.orElse(null))
        .transitionAltitude(transitionAltitude.orElse(null))
        .transitionLevel(transitionLevel.orElse(null))
        .publicMilitaryIndicator(publicMilitaryIndicator.orElse(null))
        .daylightTimeIndicator(daylightTimeIndicator.orElse(null))
        .magneticTrueIndicator(magneticTrueIndicator.orElse(null))
        .heliportName(airportFullName.orElse(null))
        .fileRecordNumber(recordNumber.orElse(null))
        .cycleDate(cycle.orElse(null));
  }
}
