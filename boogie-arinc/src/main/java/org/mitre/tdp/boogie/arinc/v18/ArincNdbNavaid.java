package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

@FunctionalInterface
public interface ArincNdbNavaid {

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

  default Optional<String> subSectionCode() {
    return arincRecord().optionalField("subSectionCode");
  }

  default Optional<String> airportIdentifier() {
    return arincRecord().optionalField("airportIdentifier");
  }

  default Optional<String> airportIcaoRegion() {
    return arincRecord().optionalField("airportIcaoRegion");
  }

  default Optional<String> ndbIdentifier() {
    return arincRecord().optionalField("vorNdbIdentifier");
  }

  default Optional<String> icaoRegion() {
    return arincRecord().optionalField("icaoRegion");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().optionalField("continuationRecordNumber");
  }

  default Optional<Double> ndbFrequency() {
    return arincRecord().optionalField("vorNdbFrequency");
  }

  default Optional<String> navaidClass() {
    return arincRecord().optionalField("navaidClass");
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

  default Optional<String> ndbNavaidName() {
    return arincRecord().optionalField("ndbNavaidName");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().optionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().optionalField("cycle");
  }

  static ArincNdbNavaid wrap(ArincRecord record) {
    return () -> record;
  }
}
