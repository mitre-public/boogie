package org.mitre.tdp.boogie.contract.arinc;

import java.util.Optional;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.arinc.v18.field.*;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableArincAirport.class)
@JsonDeserialize(as = ImmutableArincAirport.class)
public interface ArincAirport {
  RecordType recordType();

  Optional<CustomerAreaCode> customerAreaCode();

  SectionCode sectionCode();

  String airportIdentifier();

  String airportIcaoRegion();

  Optional<String> subSectionCode();

  Optional<String> iataDesignator();

  Optional<String> continuationRecordNumber();

  Optional<Double> speedLimitAltitude();

  Optional<Integer> longestRunway();

  Optional<Boolean> ifrCapability();

  Optional<LongestRunwaySurfaceCode> longestRunwaySurfaceCode();

  double latitude();

  double longitude();

  Optional<Double> magneticVariation();

  Optional<Double> airportElevation();

  Optional<Integer> speedLimit();

  Optional<String> recommendedNavaid();

  Optional<String> recommendedNavaidIcaoRegion();

  Optional<Double> transitionAltitude();

  Optional<Double> transitionLevel();

  Optional<PublicMilitaryIndicator> publicMilitaryIndicator();

  Optional<Boolean> daylightTimeIndicator();

  Optional<MagneticTrueIndicator> magneticTrueIndicator();

  Optional<String> datumCode();

  Optional<String> airportFullName();

  int fileRecordNumber();

  String lastUpdateCycle();
}
