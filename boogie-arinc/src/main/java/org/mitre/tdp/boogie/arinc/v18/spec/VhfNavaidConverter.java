package org.mitre.tdp.boogie.arinc.v18.spec;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;

public final class VhfNavaidConverter implements Function<ArincRecord, Optional<ArincVhfNavaid>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new VhfNavaidValidator().negate();

  @Override
  public Optional<ArincVhfNavaid> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Input ArincRecord cannot be null.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<CustomerAreaCode> customerAreaCode = arincRecord.optionalField("customerAreaCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");
    Optional<String> airportIdentifier = arincRecord.optionalField("airportIdentifier");
    Optional<String> airportIcaoRegion = arincRecord.optionalField("airportIcaoRegion");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<Double> vhfFrequency = arincRecord.optionalField("vhfFrequency");
    Optional<String> navaidClass = arincRecord.optionalField("navaidClass");
    Optional<String> dmeIdentifier = arincRecord.optionalField("dmeIdentifier");
    Optional<Double> dmeLatitude = arincRecord.optionalField("dmeLatitude");
    Optional<Double> dmeLongitude = arincRecord.optionalField("dmeLongitude");
    Optional<Double> stationDeclination = arincRecord.optionalField("stationDeclination");
    Optional<Double> dmeElevation = arincRecord.optionalField("dmeElevation");
    Optional<Integer> figureOfMerit = arincRecord.optionalField("figureOfMerit");
    Optional<Double> ilsDmeBias = arincRecord.optionalField("ilsDmeBias");
    Optional<Double> frequencyProtectionDistance = arincRecord.optionalField("frequencyProtectionDistance");
    Optional<String> datumCode = arincRecord.optionalField("datumCode");
    Optional<String> vhfNavaidName = arincRecord.optionalField("vhfNavaidName");

    ArincVhfNavaid vhfNavaid = new ArincVhfNavaid.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(customerAreaCode.orElse(null))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .subSectionCode(subSectionCode.orElse(null))
        .airportIdentifier(airportIdentifier.orElse(null))
        .airportIcaoRegion(airportIcaoRegion.orElse(null))
        .vhfIdentifier(arincRecord.requiredField("vhfIdentifier"))
        .vhfIcaoRegion(arincRecord.requiredField("vhfIcaoRegion"))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .vhfFrequency(vhfFrequency.orElse(null))
        .navaidClass(navaidClass.orElse(null))
        .latitude(arincRecord.requiredField("latitude"))
        .longitude(arincRecord.requiredField("longitude"))
        .dmeIdentifier(dmeIdentifier.orElse(null))
        .dmeLatitude(dmeLatitude.orElse(null))
        .dmeLongitude(dmeLongitude.orElse(null))
        .stationDeclination(stationDeclination.orElse(null))
        .dmeElevation(dmeElevation.orElse(null))
        .figureOfMerit(figureOfMerit.orElse(null))
        .ilsDmeBias(ilsDmeBias.orElse(null))
        .frequencyProtectionDistance(frequencyProtectionDistance.orElse(null))
        .datumCode(datumCode.orElse(null))
        .vhfNavaidName(vhfNavaidName.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdateCycle(arincRecord.requiredField("lastUpdateCycle"))
        .build();

    return Optional.of(vhfNavaid);
  }
}
