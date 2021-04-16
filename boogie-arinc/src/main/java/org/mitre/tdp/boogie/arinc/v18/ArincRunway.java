package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

@FunctionalInterface
public interface ArincRunway {

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

  default Optional<String> airportIdentifier() {
    return arincRecord().optionalField("airportIdentifier");
  }

  default Optional<String> airportIcaoRegion() {
    return arincRecord().optionalField("airportIcaoRegion");
  }

  default Optional<String> subSectionCode() {
    return arincRecord().optionalField("subSectionCode");
  }

  default Optional<String> runwayIdentifier() {
    return arincRecord().optionalField("runwayIdentifier");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().optionalField("continuationRecordNumber");
  }

  default Optional<Integer> runwayLength() {
    return arincRecord().optionalField("runwayLength");
  }

  default Optional<Double> runwayMagneticBearing() {
    return arincRecord().optionalField("runwayMagneticBearing");
  }

  default Optional<Double> latitude() {
    return arincRecord().optionalField("latitude");
  }

  default Optional<Double> longitude() {
    return arincRecord().optionalField("longitude");
  }

  default Optional<Double> runwayGradient() {
    return arincRecord().optionalField("runwayGradient");
  }

  default Optional<Integer> landingThresholdElevation() {
    return arincRecord().optionalField("landingThresholdElevation");
  }

  default Optional<Integer> thresholdDisplacementDistance() {
    return arincRecord().optionalField("thresholdDisplacementDistance");
  }

  default Optional<Integer> thresholdCrossingHeight() {
    return arincRecord().optionalField("thresholdCrossingHeight");
  }

  default Optional<Integer> runwayWidth() {
    return arincRecord().optionalField("runwayWidth");
  }

  default Optional<String> ilsMlsGlsIdentifier() {
    return arincRecord().optionalField("ilsMlsGlsIdentifier");
  }

  default Optional<String> ilsMlsGlsCategory() {
    return arincRecord().optionalField("cat");
  }

  default Optional<Integer> stopway() {
    return arincRecord().optionalField("stopway");
  }

  default Optional<String> secondaryIlsMlsGlsIdentifier() {
    return arincRecord().optionalField("secondaryIlsMlsGlsIdentifier");
  }

  default Optional<String> secondaryIlsMlsGlsCategory() {
    return arincRecord().optionalField("secondaryCat");
  }

  default Optional<String> runwayDescription() {
    return arincRecord().optionalField("runwayDescription");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().optionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().optionalField("cycle");
  }

  static ArincRunway wrap(ArincRecord record) {
    return () -> record;
  }
}
