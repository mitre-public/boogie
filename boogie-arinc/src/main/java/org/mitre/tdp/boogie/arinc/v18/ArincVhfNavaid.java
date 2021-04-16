package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

@FunctionalInterface
public interface ArincVhfNavaid {

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

  default Optional<String> vhfIdentifier() {
    return arincRecord().optionalField("vorNdbIdentifier");
  }

  default Optional<String> icaoRegion() {
    return arincRecord().optionalField("icaoRegion");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().optionalField("continuationRecordNumber");
  }

  default Optional<Double> vhfFrequency() {
    return arincRecord().optionalField("vorNdbFrequency");
  }

  default Optional<String> navaidClass() {
    return arincRecord().optionalField("navaidClass");
  }

  /**
   * Note not all navaid records will provide the latitude of the record in the actual latitude field - for a subset of collocated DMEs
   * this field is null and the latitude is instead in the dmeLatitude field.
   */
  default Optional<Double> latitude() {
    return arincRecord().optionalField("latitude");
  }

  /**
   * See the doc on {@link #latitude()}.
   */
  default Optional<Double> longitude() {
    return arincRecord().optionalField("longitude");
  }

  default Optional<String> dmeIdentifier() {
    return arincRecord().optionalField("dmeIdentifier");
  }

  default Optional<Double> dmeLatitude() {
    return arincRecord().optionalField("dmeLatitude");
  }

  default Optional<Double> dmeLongitude() {
    return arincRecord().optionalField("dmeLongitude");
  }

  default Optional<Double> stationDeclination() {
    return arincRecord().optionalField("stationDeclination");
  }

  default Optional<Double> dmeElevation() {
    return arincRecord().optionalField("dmeElevation");
  }

  default Optional<Integer> figureOfMerit() {
    return arincRecord().optionalField("figureOfMerit");
  }

  default Optional<Double> ilsDmeBias() {
    return arincRecord().optionalField("ilsDmeBias");
  }

  default Optional<Double> frequencyProtectionDistance() {
    return arincRecord().optionalField("frequencyProtectionDistance");
  }

  default Optional<String> datumCode() {
    return arincRecord().optionalField("datumCode");
  }

  default Optional<String> vhfNavaidName() {
    return arincRecord().optionalField("vhfNavaidName");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().optionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().optionalField("cycle");
  }

  static ArincVhfNavaid wrap(ArincRecord arincRecord) {
    return () -> arincRecord;
  }
}
