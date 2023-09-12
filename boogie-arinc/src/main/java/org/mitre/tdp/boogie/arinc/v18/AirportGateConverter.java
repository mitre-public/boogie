package org.mitre.tdp.boogie.arinc.v18;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincAirportGate;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;

public class AirportGateConverter implements Function<ArincRecord, Optional<ArincAirportGate>> {
  private static final Predicate<ArincRecord> isInvalidRecord = new AirportGateValidator().negate();

  @Override
  public Optional<ArincAirportGate> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot convert null ArincRecord");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<String> name = arincRecord.optionalField("gateName");

    ArincAirportGate airportGate = new ArincAirportGate.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .airportIdentifier(arincRecord.requiredField("airportIdentifier"))
        .airportIcaoRegion(arincRecord.requiredField("airportIcaoRegion"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .gateIdentifier(arincRecord.requiredField("gateIdentifier"))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .latitude(arincRecord.requiredField("latitude"))
        .longitude(arincRecord.requiredField("longitude"))
        .name(name.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdatedCycle(arincRecord.requiredField("lastUpdatedCycle"))
        .build();

    return Optional.of(airportGate);
  }
}