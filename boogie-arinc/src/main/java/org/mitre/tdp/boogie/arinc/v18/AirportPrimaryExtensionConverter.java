package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.checkerframework.checker.units.qual.A;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincAirportPrimaryExtension;

public final class AirportPrimaryExtensionConverter implements Function<ArincRecord, Optional<ArincAirportPrimaryExtension>> {
  private static final Predicate<ArincRecord> isInvalidRecord = new AirportPrimaryExtensionValidator().negate();

  @Override
  public Optional<ArincAirportPrimaryExtension> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null ArincRecord.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    // for optional fields we are forced to access them as such due to the template type return of the ArincRecord
    Optional<String> iataDesignator = arincRecord.optionalField("iataDesignator");

    ArincAirportPrimaryExtension extension = ArincAirportPrimaryExtension.builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .airportIdentifier(arincRecord.requiredField("airportIdentifier"))
        .airportIcaoRegion(arincRecord.requiredField("icaoRegion"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .iataDesignator(iataDesignator.orElse(null))
        .continuationRecordNumber(arincRecord.requiredField("continuationRecordNumber"))
        .applicationType(arincRecord.requiredField("applicationType"))
        .notes(arincRecord.requiredField("notes"))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdatedCycle(arincRecord.requiredField("cycle"))
        .build();

    return Optional.of(extension);
  }
}
