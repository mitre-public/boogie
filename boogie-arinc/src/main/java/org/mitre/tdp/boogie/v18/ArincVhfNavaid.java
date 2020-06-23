package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincRecordDecorator;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

@FunctionalInterface
public interface ArincVhfNavaid extends ArincRecordDecorator {

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

  default Optional<String> subSectionCode() {
    return arincRecord().getOptionalField("subSectionCode");
  }

  default Optional<String> airportIdentifier() {
    return arincRecord().getOptionalField("airportIdentifier");
  }

  default Optional<String> airportIcaoRegion() {
    return arincRecord().getOptionalField("airportIcaoRegion");
  }

  default Optional<String> vhfIdentifier() {
    return arincRecord().getOptionalField("vorNdbIdentifier");
  }

  default Optional<String> icaoRegion() {
    return arincRecord().getOptionalField("icaoRegion");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().getOptionalField("continuationRecordNumber");
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
  default Optional<Double> latitude() {
    return arincRecord().getOptionalField("latitude");
  }

  /**
   * See the doc on {@link #latitude()}.
   */
  default Optional<Double> longitude() {
    return arincRecord().getOptionalField("longitude");
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

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().getOptionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().getOptionalField("cycle");
  }

  static ArincVhfNavaid wrap(ArincRecord arincRecord) {
    return () -> arincRecord;
  }
}
