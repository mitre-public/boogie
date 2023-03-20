package org.mitre.tdp.boogie.contract.arinc;

import java.util.Optional;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableArincRunway.class)
@JsonDeserialize(as = ImmutableArincRunway.class)
public interface ArincRunway {
  RecordType recordType();

  Optional<CustomerAreaCode> customerAreaCode();

  SectionCode sectionCode();

  String airportIdentifier();

  String airportIcaoRegion();

  Optional<String> subSectionCode();

  String runwayIdentifier();

  Optional<String> continuationRecordNumber();

  Optional<Integer> runwayLength();

  Optional<Double> runwayMagneticBearing();

  double latitude();

  double longitude();

  Optional<Double> runwayGradient();

  Optional<Integer> landingThresholdElevation();

  Optional<Integer> thresholdDisplacementDistance();

  Optional<Integer> thresholdCrossingHeight();

  Optional<Integer> runwayWidth();

  Optional<String> ilsMlsGlsIdentifier();

  Optional<String> ilsMlsGlsCategory();

  Optional<Integer> stopway();

  Optional<String> secondaryIlsMlsGlsIdentifier();

  Optional<String> secondaryIlsMlsGlsCategory();

  Optional<String> runwayDescription();

  int fileRecordNumber();

  String lastUpdateCycle();
}
