package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

@FunctionalInterface
public interface ArincNdbNavaid {

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

  default String ndbIdentifier() {
    return arincRecord().getRequiredField("vorNdbIdentifier");
  }

  default String icaoRegion() {
    return arincRecord().getRequiredField("icaoRegion");
  }

  default String continuationRecordNumber() {
    return arincRecord().getRequiredField("continuationRecordNumber");
  }

  default Optional<Double> ndbFrequency() {
    return arincRecord().getOptionalField("vorNdbFrequency");
  }

  default Optional<String> navaidClass() {
    return arincRecord().getOptionalField("navaidClass");
  }

  default Double latitude() {
    return arincRecord().getRequiredField("latitude");
  }

  default Double longitude() {
    return arincRecord().getRequiredField("longitude");
  }

  default Double magneticVariation() {
    return arincRecord().getRequiredField("magneticVariation");
  }

  default Optional<String> datumCode() {
    return arincRecord().getOptionalField("datumCode");
  }

  default Optional<String> ndbNavaidName() {
    return arincRecord().getOptionalField("ndbNavaidName");
  }

  default Integer fileRecordNumber() {
    return arincRecord().getRequiredField("fileRecordNumber");
  }

  default String cycle() {
    return arincRecord().getRequiredField("cycle");
  }

  static ArincNdbNavaid wrap(ArincRecord record) {
    return () -> record;
  }
}
