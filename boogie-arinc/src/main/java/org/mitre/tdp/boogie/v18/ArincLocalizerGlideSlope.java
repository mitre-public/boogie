package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincRecordDecorator;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

@FunctionalInterface
public interface ArincLocalizerGlideSlope extends ArincRecordDecorator {

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

  default Optional<String> airportIdentifier() {
    return arincRecord().getOptionalField("airportIdentifier");
  }

  default Optional<String> airportIcaoRegion() {
    return arincRecord().getOptionalField("airportIcaoRegion");
  }

  default Optional<String> subSectionCode() {
    return arincRecord().getOptionalField("subSectionCode");
  }

  default Optional<String> localizerIdentifier() {
    return arincRecord().getOptionalField("localizerIdentifier");
  }

  default Optional<String> ilsMlsGlsCategory() {
    return arincRecord().getOptionalField("cat");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().getOptionalField("continuationRecordNumber");
  }

  default Optional<Double> localizerFrequency() {
    return arincRecord().getOptionalField("localizerFrequency");
  }

  default Optional<String> runwayIdentifier() {
    return arincRecord().getOptionalField("runwayIdentifier");
  }

  default Optional<Double> localizerLatitude() {
    return arincRecord().getOptionalField("localizerLatitude");
  }

  default Optional<Double> localizerLongitude() {
    return arincRecord().getOptionalField("localizerLongitude");
  }

  default Optional<Double> localizerBearing() {
    return arincRecord().getOptionalField("localizerBearing");
  }

  default Optional<Double> glideSlopeLatitude() {
    return arincRecord().getOptionalField("glideSlopeLatitude");
  }

  default Optional<Double> glideSlopeLongitude() {
    return arincRecord().getOptionalField("glideSlopeLongitude");
  }

  default Optional<Integer> localizerPosition() {
    return arincRecord().getOptionalField("localizerPosition");
  }

  default Optional<String> localizerPositionReference() {
    return arincRecord().getOptionalField("localizerPositionReference");
  }

  default Optional<Integer> glideslopePosition() {
    return arincRecord().getOptionalField("glideslopePosition");
  }

  default Optional<Double> localizerWidth() {
    return arincRecord().getOptionalField("localizerWidth");
  }

  default Optional<Double> glideSlopeAngle() {
    return arincRecord().getOptionalField("glideSlopeAngle");
  }

  default Optional<Double> stationDeclination() {
    return arincRecord().getOptionalField("stationDeclination");
  }

  default Optional<Integer> glideSlopeHeightAtLandingThreshold() {
    return arincRecord().getOptionalField("glideSlopeHeightAtLandingThreshold");
  }

  default Optional<Double> glideSlopeElevation() {
    return arincRecord().getOptionalField("glideSlopeElevation");
  }

  default Optional<String> supportingFacilityId() {
    return arincRecord().getOptionalField("supportingFacilityId");
  }

  default Optional<String> supportingFacilityIcaoCode() {
    return arincRecord().getOptionalField("supportingFacilityIcaoCode");
  }

  default Optional<String> supportingFacilitySectionCode() {
    return arincRecord().getOptionalField("supportingFacilitySectionCode");
  }

  default Optional<String> supportingFacilitySubsectionCode() {
    return arincRecord().getOptionalField("supportingFacilitySubsectionCode");
  }
  default Optional<Integer> fileRecordNumber() {
    return arincRecord().getOptionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().getOptionalField("cycle");
  }

  static ArincLocalizerGlideSlope wrap(ArincRecord record) {
    return () -> record;
  }
}
