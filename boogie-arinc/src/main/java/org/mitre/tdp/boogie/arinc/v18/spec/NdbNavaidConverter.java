package org.mitre.tdp.boogie.arinc.v18.spec;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;

public final class NdbNavaidConverter implements Function<ArincRecord, Optional<ArincNdbNavaid>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new NdbNavaidValidator().negate();

  @Override
  public Optional<ArincNdbNavaid> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Input ArincRecord cannot be null.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<CustomerAreaCode> customerAreaCode = arincRecord.optionalField("customerAreaCode");
    Optional<String> airportIdentifier = arincRecord.optionalField("airportIdentifier");
    Optional<String> airportIcaoRegion = arincRecord.optionalField("airportIcaoRegion");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<Double> ndbFrequency = arincRecord.optionalField("ndbFrequency");
    Optional<String> navaidClass = arincRecord.optionalField("navaidClass");
    Optional<Double> magneticVariation = arincRecord.optionalField("magneticVariation");
    Optional<String> datumCode = arincRecord.optionalField("datumCode");
    Optional<String> ndbNavaidName = arincRecord.optionalField("ndbNavaidName");

    ArincNdbNavaid arincNdbNavaid = new ArincNdbNavaid.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(customerAreaCode.orElse(null))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .airportIdentifier(airportIdentifier.orElse(null))
        .airportIcaoRegion(airportIcaoRegion.orElse(null))
        .ndbIdentifier(arincRecord.requiredField("ndbIdentifier"))
        .ndbIcaoRegion(arincRecord.requiredField("ndbIcaoRegion"))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .ndbFrequency(ndbFrequency.orElse(null))
        .navaidClass(navaidClass.orElse(null))
        .latitude(arincRecord.requiredField("latitude"))
        .longitude(arincRecord.requiredField("longitude"))
        .magneticVariation(magneticVariation.orElse(null))
        .datumCode(datumCode.orElse(null))
        .ndbNavaidName(ndbNavaidName.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdateCycle(arincRecord.requiredField("lastUpdateCycle"))
        .build();

    return Optional.of(arincNdbNavaid);
  }
}
