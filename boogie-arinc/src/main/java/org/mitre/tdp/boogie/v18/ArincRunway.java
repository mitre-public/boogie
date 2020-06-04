package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

@FunctionalInterface
public interface ArincRunway {

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

  default String airportIdentifier() {
    return arincRecord().getRequiredField("airportIdentifier");
  }

  default String airportIcaoRegion() {
    return arincRecord().getRequiredField("airportIcaoRegion");
  }

  default String subSectionCode() {
    return arincRecord().getRequiredField("subSectionCode");
  }

  default String runwayIdentifier() {
    return arincRecord().getRequiredField("runwayIdentifier");
  }

  default String continuationRecordNumber() {
    return arincRecord().getRequiredField("continuationRecordNumber");
  }

  default Optional<Integer> runwayLength() {
    return arincRecord().getOptionalField("runwayLength");
  }

  default Optional<Double> runwayMagneticBearing() {
    return arincRecord().getOptionalField("runwayMagneticBearing");
  }

  default Double latitude() {
    return arincRecord().getRequiredField("latitude");
  }

  default Double longitude() {
    return arincRecord().getRequiredField("longitude");
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

  default Integer fileRecordNumber() {
    return arincRecord().getRequiredField("fileRecordNumber");
  }

  default String cycle() {
    return arincRecord().getRequiredField("cycle");
  }

  static ArincRunway wrap(ArincRecord record) {
    return () -> record;
  }
}
