package org.mitre.tdp.boogie.contract.arinc;

import java.util.Optional;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableArincLocalizerGlideSlope.class)
@JsonDeserialize(as = ImmutableArincLocalizerGlideSlope.class)
public interface ArincLocalizerGlideSlope {

  RecordType recordType();

  Optional<CustomerAreaCode> customerAreaCode();

  SectionCode sectionCode();

  String airportIdentifier();

  String airportIcaoRegion();

  Optional<String> subSectionCode();

  String localizerIdentifier();

  Optional<String> ilsMlsGlsCategory();

  Optional<String> continuationRecordNumber();

  Optional<Double> localizerFrequency();

  String runwayIdentifier();

  Optional<Double> localizerLatitude();

  Optional<Double> localizerLongitude();

  Optional<Double> localizerBearing();

  Optional<Double> glideSlopeLatitude();

  Optional<Double> glideSlopeLongitude();

  Optional<Integer> localizerPosition();

  Optional<String> localizerPositionReference();

  Optional<Integer> glideSlopePosition();

  Optional<Double> localizerWidth();

  Optional<Double> glideSlopeAngle();

  Optional<Double> stationDeclination();

  Optional<Integer> glideSlopeHeightAtLandingThreshold();

  Optional<Double> glideSlopeElevation();

  Optional<String> supportingFacilityIdentifier();

  Optional<String> supportingFacilityIcaoCode();

  Optional<SectionCode> supportingFacilitySectionCode();

  Optional<String> supportingFacilitySubSectionCode();

  int fileRecordNumber();

  String lastUpdateCycle();
}
