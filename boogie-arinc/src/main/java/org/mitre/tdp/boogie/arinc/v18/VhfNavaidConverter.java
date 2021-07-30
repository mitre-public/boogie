package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;

public final class VhfNavaidConverter implements Function<ArincRecord, Optional<ArincVhfNavaid>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new VhfNavaidValidator().negate();

  @Override
  public Optional<ArincVhfNavaid> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Input ArincRecord cannot be null.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    // occasionally VHF Identifier wont be present - the DME identifier should be populated in those cases - though this
    // is less common than the case where lat/lon is only at the DME level
    Optional<String> vhfIdentifier = arincRecord.optionalField("vhfIdentifier");
    Optional<String> dmeIdentifier = arincRecord.optionalField("dmeIdentifier");

    // special case - for DME facilities it's not uncommon for the normal lat/lon to be unpopulated but for the DME lat/lon
    // to be present - when this happens we take the DME lat/lon to be the lat/lon of the overall facility
    Optional<Double> latitude = arincRecord.optionalField("latitude");
    Optional<Double> longitude = arincRecord.optionalField("longitude");
    Optional<Double> dmeLatitude = arincRecord.optionalField("dmeLatitude");
    Optional<Double> dmeLongitude = arincRecord.optionalField("dmeLongitude");

    Optional<CustomerAreaCode> customerAreaCode = arincRecord.optionalField("customerAreaCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");
    Optional<String> airportIdentifier = arincRecord.optionalField("airportIdentifier");
    Optional<String> airportIcaoRegion = arincRecord.optionalField("airportIcaoRegion");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<Double> vhfFrequency = arincRecord.optionalField("vhfFrequency");
    Optional<String> navaidClass = arincRecord.optionalField("navaidClass");
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
        .vhfIdentifier(vhfIdentifier.orElseGet(() -> dmeIdentifier.orElseThrow(IllegalStateException::new)))
        .vhfIcaoRegion(arincRecord.requiredField("vhfIcaoRegion"))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .vhfFrequency(vhfFrequency.orElse(null))
        .navaidClass(navaidClass.orElse(null))
        .latitude(latitude.orElseGet(() -> dmeLatitude.orElseThrow(IllegalStateException::new)))
        .longitude(longitude.orElseGet(() -> dmeLongitude.orElseThrow(IllegalStateException::new)))
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
