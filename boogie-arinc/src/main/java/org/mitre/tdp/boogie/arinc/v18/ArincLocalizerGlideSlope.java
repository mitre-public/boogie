package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

@FunctionalInterface
public interface ArincLocalizerGlideSlope {

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

  default Optional<String> localizerIdentifier() {
    return arincRecord().optionalField("localizerIdentifier");
  }

  default Optional<String> ilsMlsGlsCategory() {
    return arincRecord().optionalField("cat");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().optionalField("continuationRecordNumber");
  }

  default Optional<Double> localizerFrequency() {
    return arincRecord().optionalField("localizerFrequency");
  }

  default Optional<String> runwayIdentifier() {
    return arincRecord().optionalField("runwayIdentifier");
  }

  default Optional<Double> localizerLatitude() {
    return arincRecord().optionalField("localizerLatitude");
  }

  default Optional<Double> localizerLongitude() {
    return arincRecord().optionalField("localizerLongitude");
  }

  default Optional<Double> localizerBearing() {
    return arincRecord().optionalField("localizerBearing");
  }

  default Optional<Double> glideSlopeLatitude() {
    return arincRecord().optionalField("glideSlopeLatitude");
  }

  default Optional<Double> glideSlopeLongitude() {
    return arincRecord().optionalField("glideSlopeLongitude");
  }

  default Optional<Integer> localizerPosition() {
    return arincRecord().optionalField("localizerPosition");
  }

  default Optional<String> localizerPositionReference() {
    return arincRecord().optionalField("localizerPositionReference");
  }

  default Optional<Integer> glideslopePosition() {
    return arincRecord().optionalField("glideslopePosition");
  }

  default Optional<Double> localizerWidth() {
    return arincRecord().optionalField("localizerWidth");
  }

  default Optional<Double> glideSlopeAngle() {
    return arincRecord().optionalField("glideSlopeAngle");
  }

  default Optional<Double> stationDeclination() {
    return arincRecord().optionalField("stationDeclination");
  }

  default Optional<Integer> glideSlopeHeightAtLandingThreshold() {
    return arincRecord().optionalField("glideSlopeHeightAtLandingThreshold");
  }

  default Optional<Double> glideSlopeElevation() {
    return arincRecord().optionalField("glideSlopeElevation");
  }

  default Optional<String> supportingFacilityId() {
    return arincRecord().optionalField("supportingFacilityId");
  }

  default Optional<String> supportingFacilityIcaoCode() {
    return arincRecord().optionalField("supportingFacilityIcaoCode");
  }

  default Optional<String> supportingFacilitySectionCode() {
    return arincRecord().optionalField("supportingFacilitySectionCode");
  }

  default Optional<String> supportingFacilitySubsectionCode() {
    return arincRecord().optionalField("supportingFacilitySubsectionCode");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().optionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().optionalField("cycle");
  }

  static ArincLocalizerGlideSlope wrap(ArincRecord record) {
    return () -> record;
  }
}
