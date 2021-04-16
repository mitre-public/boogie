package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

@FunctionalInterface
public interface ArincWaypoint {

  ArincRecord arincRecord();

  default Optional<RecordType> recordType() {
    return arincRecord().optionalField("recordType");
  }

  default Optional<CustomerAreaCode> customerAreaCode() {
    return arincRecord().optionalField("customerAreaCode");
  }

  default Optional<SectionCode> sectionCode() {
    return arincRecord().optionalField("sectionCode");
  }

  default Optional<String> enrouteSubSectionCode() {
    return arincRecord().optionalField("enrouteSubSectionCode");
  }

  default Optional<String> airportIdentifier() {
    return arincRecord().optionalField("airportIdentifier");
  }

  default Optional<String> airportIcaoRegion() {
    return arincRecord().optionalField("airportIcaoRegion");
  }

  default Optional<String> terminalSubSectionCode() {
    return arincRecord().optionalField("terminalSubSectionCode");
  }

  default Optional<String> fixIdentifier() {
    return arincRecord().optionalField("fixIdentifier");
  }

  default Optional<String> icaoRegion() {
    return arincRecord().optionalField("icaoRegion");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().optionalField("continuationRecordNumber");
  }

  default Optional<String> waypointType() {
    return arincRecord().optionalField("waypointType");
  }

  default Optional<String> waypointUsage() {
    return arincRecord().optionalField("waypointUsage");
  }

  default Optional<Double> latitude() {
    return arincRecord().optionalField("latitude");
  }

  default Optional<Double> longitude() {
    return arincRecord().optionalField("longitude");
  }

  default Optional<Double> magneticVariation() {
    return arincRecord().optionalField("magneticVariation");
  }

  default Optional<String> datumCode() {
    return arincRecord().optionalField("datumCode");
  }

  default Optional<String> nameFormat() {
    return arincRecord().optionalField("nameFormat");
  }

  default Optional<String> waypointNameDescription() {
    return arincRecord().optionalField("waypointNameDescription");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().optionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().optionalField("cycle");
  }

  static ArincWaypoint wrap(ArincRecord arincRecord) {
    return () -> arincRecord;
  }
}
