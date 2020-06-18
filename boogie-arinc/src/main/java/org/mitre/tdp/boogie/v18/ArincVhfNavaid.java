package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

@FunctionalInterface
public interface ArincVhfNavaid {

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

  default Optional<String> subSectionCode() {
    return arincRecord().getOptionalField("subSectionCode");
  }

  default Optional<String> airportIdentifier() {
    return arincRecord().getOptionalField("airportIdentifier");
  }

  default Optional<String> airportIcaoRegion() {
    return arincRecord().getOptionalField("airportIcaoRegion");
  }

  default String vhfIdentifier() {
    return arincRecord().getRequiredField("vorNdbIdentifier");
  }

  default String icaoRegion() {
    return arincRecord().getRequiredField("icaoRegion");
  }

  default String continuationRecordNumber() {
    return arincRecord().getRequiredField("continuationRecordNumber");
  }

  default Optional<Double> vhfFrequency() {
    return arincRecord().getOptionalField("vorNdbFrequency");
  }

  default Optional<String> navaidClass() {
    return arincRecord().getOptionalField("navaidClass");
  }

  /**
   * Note not all navaid records will provide the latitude of the record in the actual latitude field - for a subset of collocated DMEs
   * this field is null and the latitude is instead in the dmeLatitude field.
   */
  default Double latitude() {
    return arincRecord().getRequiredField("latitude");
  }

  /**
   * See the doc on {@link #latitude()}.
   */
  default Double longitude() {
    return arincRecord().getRequiredField("longitude");
  }

  default Optional<String> dmeIdentifier() {
    return arincRecord().getOptionalField("dmeIdentifier");
  }

  default Optional<Double> dmeLatitude() {
    return arincRecord().getOptionalField("dmeLatitude");
  }

  default Optional<Double> dmeLongitude() {
    return arincRecord().getOptionalField("dmeLongitude");
  }

  default Optional<Double> stationDeclination() {
    return arincRecord().getOptionalField("stationDeclination");
  }

  default Optional<Double> dmeElevation() {
    return arincRecord().getOptionalField("dmeElevation");
  }

  default Optional<Integer> figureOfMerit() {
    return arincRecord().getOptionalField("figureOfMerit");
  }

  default Optional<Double> ilsDmeBias() {
    return arincRecord().getOptionalField("ilsDmeBias");
  }

  default Optional<Double> frequencyProtectionDistance() {
    return arincRecord().getOptionalField("frequencyProtectionDistance");
  }

  default Optional<String> datumCode() {
    return arincRecord().getOptionalField("datumCode");
  }

  default Optional<String> vhfNavaidName() {
    return arincRecord().getOptionalField("vhfNavaidName");
  }

  default Integer fileRecordNumber() {
    return arincRecord().getRequiredField("fileRecordNumber");
  }

  default String cycle() {
    return arincRecord().getRequiredField("cycle");
  }

  static ArincVhfNavaid wrap(ArincRecord arincRecord) {
    return () -> arincRecord;
  }
}
