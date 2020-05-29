package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.LongestRunwaySurfaceCode;
import org.mitre.tdp.boogie.v18.spec.field.MagneticTrueIndicator;
import org.mitre.tdp.boogie.v18.spec.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

/**
 * Interface for an {@link Airport} which decorates a parsed {@link ArincRecord}.
 */
@FunctionalInterface
public interface ArincAirport {

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
    return arincRecord().getRequiredField("icaoRegion");
  }

  default String subSectionCode() {
    return arincRecord().getRequiredField("subSectionCode");
  }

  default Optional<String> iataDesignator() {
    return arincRecord().getOptionalField("iataDesignator");
  }

  default String continuationRecordNumber() {
    return arincRecord().getRequiredField("continuationRecordNumber");
  }

  default Optional<Double> speedLimitAltitude() {
    return arincRecord().getOptionalField("speedLimitAltitude");
  }

  default Optional<Integer> longestRunway() {
    return arincRecord().getOptionalField("longestRunway");
  }

  default Optional<Boolean> ifrCapability() {
    return arincRecord().getOptionalField("ifrCapability");
  }

  default Optional<LongestRunwaySurfaceCode> longestRunwaySurfaceCode() {
    return arincRecord().getOptionalField("longestRunwaySurfaceCode");
  }

  default Double latitude() {
    return arincRecord().getRequiredField("latitude");
  }

  default Double longitude() {
    return arincRecord().getRequiredField("longitude");
  }

  default Optional<Double> magneticVariation() {
    return arincRecord().getOptionalField("magneticVariation");
  }

  default Optional<Double> airportElevation() {
    return arincRecord().getOptionalField("airportElevation");
  }

  default Optional<Integer> speedLimit() {
    return arincRecord().getOptionalField("speedLimit");
  }

  default Optional<String> recommendedNavaid() {
    return arincRecord().getOptionalField("recommendedNavaid");
  }

  default Optional<String> recommendedNavaidIcaoRegion() {
    return arincRecord().getOptionalField("recommendedNavaidIcaoRegion");
  }

  default Optional<Double> transitionAltitude() {
    return arincRecord().getOptionalField("transitionAltitude");
  }

  default Optional<Double> transitionLevel() {
    return arincRecord().getOptionalField("transitionLevel");
  }

  default Optional<PublicMilitaryIndicator> publicMilitaryIndicator() {
    return arincRecord().getOptionalField("publicMilitaryIndicator");
  }

  default Optional<Boolean> dayTimeIndicator() {
    return arincRecord().getOptionalField("dayTimeIndicator");
  }

  default Optional<MagneticTrueIndicator> magneticTrueIndicator() {
    return arincRecord().getOptionalField("magneticTrueIndicator");
  }

  default Optional<String> datumCode() {
    return arincRecord().getOptionalField("datumCode");
  }

  default String airportName() {
    return arincRecord().getRequiredField("airportName");
  }

  default Integer fileRecordNumber() {
    return arincRecord().getRequiredField("fileRecordNumber");
  }

  default String cycle() {
    return arincRecord().getRequiredField("cycle");
  }

  /**
   * Wraps the given {@link ArincRecord} as an {@link ArincAirport} object.
   */
  static ArincAirport wrap(ArincRecord record) {
    return () -> record;
  }
}
