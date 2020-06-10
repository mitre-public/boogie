package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

@FunctionalInterface
public interface ArincWaypoint {

  ArincRecord arincRecord();

  default RecordType recordType() {
    return arincRecord().getRequiredField("recordType");
  }

  default CustomerAreaCode customerAreaCode() {
    return arincRecord().getRequiredField("customerAreaCode");
  }

  default SectionCode sectionCode() {
    return arincRecord().getRequiredField("sectionCode");
  }

  default String subSectionCode() {
    return arincRecord().getRequiredField("subSectionCode");
  }

  default Optional<String> airportIdentifier() {
    return arincRecord().getOptionalField("airportIdentifier");
  }

  default Optional<String> airportIcaoRegion() {
    return arincRecord().getOptionalField("airportIcaoRegion");
  }

  default Optional<String> airportSubSectionCode() {
    return arincRecord().getOptionalField("airportSubSectionCode");
  }

  default String fixIdentifier() {
    return arincRecord().getRequiredField("fixIdentifier");
  }

  default String icaoRegion() {
    return arincRecord().getRequiredField("icaoRegion");
  }

  default String continuationRecordNumber() {
    return arincRecord().getRequiredField("continuationRecordNumber");
  }

  default Optional<String> waypointType() {
    return arincRecord().getOptionalField("waypointType");
  }

  default Optional<String> waypointUsage() {
    return arincRecord().getOptionalField("waypointUsage");
  }

  default Double latitude() {
    return arincRecord().getRequiredField("latitude");
  }

  default Double longitude() {
    return arincRecord().getRequiredField("longitude");
  }

  default Optional<Double> magneticVariation() {
    return arincRecord().getOptionalField("magneticVariation");
  }

  default Optional<String> datumCode() {
    return arincRecord().getOptionalField("datumCode");
  }

  default Optional<String> nameFormat() {
    return arincRecord().getOptionalField("nameFormat");
  }

  default Optional<String> waypointNameDescription() {
    return arincRecord().getOptionalField("waypointNameDescription");
  }

  default Integer fileRecordNumber() {
    return arincRecord().getRequiredField("fileRecordNumber");
  }

  default String cycle() {
    return arincRecord().getRequiredField("cycle");
  }

  static ArincWaypoint wrap(ArincRecord arincRecord) {
    return () -> arincRecord;
  }
}
