package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

@FunctionalInterface
public interface ArincRunway {

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

  default Optional<String> airportIdentifier() {
    return arincRecord().getOptionalField("airportIdentifier");
  }

  default Optional<String> airportIcaoRegion() {
    return arincRecord().getOptionalField("airportIcaoRegion");
  }

  default Optional<String> subSectionCode() {
    return arincRecord().getOptionalField("subSectionCode");
  }

  default Optional<String> runwayIdentifier() {
    return arincRecord().getOptionalField("runwayIdentifier");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().getOptionalField("continuationRecordNumber");
  }

  default Optional<Integer> runwayLength() {
    return arincRecord().getOptionalField("runwayLength");
  }

  default Optional<Double> runwayMagneticBearing() {
    return arincRecord().getOptionalField("runwayMagneticBearing");
  }

  default Optional<Double> latitude() {
    return arincRecord().getOptionalField("latitude");
  }

  default Optional<Double> longitude() {
    return arincRecord().getOptionalField("longitude");
  }

  default Optional<Double> runwayGradient() {
    return arincRecord().getOptionalField("runwayGradient");
  }

  default Optional<Integer> landingThresholdElevation() {
    return arincRecord().getOptionalField("landingThresholdElevation");
  }

  default Optional<Integer> thresholdDisplacementDistance() {
    return arincRecord().getOptionalField("thresholdDisplacementDistance");
  }

  default Optional<Integer> thresholdCrossingHeight() {
    return arincRecord().getOptionalField("thresholdCrossingHeight");
  }

  default Optional<Integer> runwayWidth() {
    return arincRecord().getOptionalField("runwayWidth");
  }

  default Optional<String> ilsMlsGlsIdentifier() {
    return arincRecord().getOptionalField("ilsMlsGlsIdentifier");
  }

  default Optional<String> ilsMlsGlsCategory() {
    return arincRecord().getOptionalField("cat");
  }

  default Optional<Integer> stopway() {
    return arincRecord().getOptionalField("stopway");
  }

  default Optional<String> secondaryIlsMlsGlsIdentifier() {
    return arincRecord().getOptionalField("secondaryIlsMlsGlsIdentifier");
  }

  default Optional<String> secondaryIlsMlsGlsCategory() {
    return arincRecord().getOptionalField("secondaryCat");
  }

  default Optional<String> runwayDescription() {
    return arincRecord().getOptionalField("runwayDescription");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().getOptionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().getOptionalField("cycle");
  }

  static ArincRunway wrap(ArincRecord record) {
    return () -> record;
  }
}
