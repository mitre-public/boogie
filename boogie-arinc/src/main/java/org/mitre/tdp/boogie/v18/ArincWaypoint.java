package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincRecordDecorator;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

@FunctionalInterface
public interface ArincWaypoint extends ArincRecordDecorator {

  @Override
  ArincRecord arincRecord();

  default Optional<RecordType> recordType() {
    return arincRecord().getOptionalField("recordType");
  }

  default Optional<CustomerAreaCode> customerAreaCode() {
    return arincRecord().getOptionalField("customerAreaCode");
  }

  default Optional<SectionCode> sectionCode() {
    return arincRecord().getOptionalField("sectionCode");
  }

  default Optional<String> enrouteSubSectionCode() {
    return arincRecord().getOptionalField("subSectionCode");
  }

  default Optional<String> airportIdentifier() {
    return arincRecord().getOptionalField("airportIdentifier");
  }

  default Optional<String> airportIcaoRegion() {
    return arincRecord().getOptionalField("airportIcaoRegion");
  }

  default Optional<String> terminalSubSectionCode() {
    return arincRecord().getOptionalField("terminalSubSectionCode");
  }

  default Optional<String> fixIdentifier() {
    return arincRecord().getOptionalField("fixIdentifier");
  }

  default Optional<String> icaoRegion() {
    return arincRecord().getOptionalField("icaoRegion");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().getOptionalField("continuationRecordNumber");
  }

  default Optional<String> waypointType() {
    return arincRecord().getOptionalField("waypointType");
  }

  default Optional<String> waypointUsage() {
    return arincRecord().getOptionalField("waypointUsage");
  }

  default Optional<Double> latitude() {
    return arincRecord().getOptionalField("latitude");
  }

  default Optional<Double> longitude() {
    return arincRecord().getOptionalField("longitude");
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

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().getOptionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().getOptionalField("cycle");
  }

  static ArincWaypoint wrap(ArincRecord arincRecord) {
    return () -> arincRecord;
  }
}
