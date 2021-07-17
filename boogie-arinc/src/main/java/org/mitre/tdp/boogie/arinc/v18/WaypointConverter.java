package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;

public final class WaypointConverter implements Function<ArincRecord, Optional<ArincWaypoint>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new WaypointValidator().negate();

  @Override
  public Optional<ArincWaypoint> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Input ArincRecord cannot be null.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<CustomerAreaCode> customerAreaCode = arincRecord.optionalField("customerAreaCode");
    Optional<String> enrouteSubSectionCode = arincRecord.optionalField("enrouteSubSectionCode");
    Optional<String> airportIdentifier = arincRecord.optionalField("airportIdentifier");
    Optional<String> airportIcaoRegion = arincRecord.optionalField("airportIcaoRegion");
    Optional<String> terminalSubSectionCode = arincRecord.optionalField("terminalSubSectionCode");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<String> waypointType = arincRecord.optionalField("waypointType");
    Optional<String> waypointUsage = arincRecord.optionalField("waypointUsage");
    Optional<Double> magneticVariation = arincRecord.optionalField("magneticVariation");
    Optional<String> datumCode = arincRecord.optionalField("datumCode");
    Optional<String> nameFormat = arincRecord.optionalField("nameFormat");
    Optional<String> waypointNameDescription = arincRecord.optionalField("waypointNameDescription");

    ArincWaypoint waypoint = new ArincWaypoint.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(customerAreaCode.orElse(null))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .enrouteSubSectionCode(enrouteSubSectionCode.orElse(null))
        .airportIdentifier(airportIdentifier.orElse(null))
        .airportIcaoRegion(airportIcaoRegion.orElse(null))
        .terminalSubSectionCode(terminalSubSectionCode.orElse(null))
        .waypointIdentifier(arincRecord.requiredField("waypointIdentifier"))
        .waypointIcaoRegion(arincRecord.requiredField("waypointIcaoRegion"))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .waypointType(waypointType.orElse(null))
        .waypointUsage(waypointUsage.orElse(null))
        .latitude(arincRecord.requiredField("latitude"))
        .longitude(arincRecord.requiredField("longitude"))
        .magneticVariation(magneticVariation.orElse(null))
        .datumCode(datumCode.orElse(null))
        .nameFormat(nameFormat.orElse(null))
        .waypointNameDescription(waypointNameDescription.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdateCycle(arincRecord.requiredField("lastUpdateCycle"))
        .build();

    return Optional.of(waypoint);
  }
}
