package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincRecordDecorator;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.LongestRunwaySurfaceCode;
import org.mitre.tdp.boogie.v18.spec.field.MagneticTrueIndicator;
import org.mitre.tdp.boogie.v18.spec.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.record.AirportSpec;

/**
 * Typed interface for retrieving record field and type information from {@link ArincRecord}s parsed via the {@link AirportSpec}.
 */
@FunctionalInterface
public interface ArincAirport extends ArincRecordDecorator {

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
    return arincRecord().getOptionalField("icaoRegion");
  }

  default Optional<String> subSectionCode() {
    return arincRecord().getOptionalField("subSectionCode");
  }

  default Optional<String> iataDesignator() {
    return arincRecord().getOptionalField("iataDesignator");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().getOptionalField("continuationRecordNumber");
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

  default Optional<Double> latitude() {
    return arincRecord().getOptionalField("latitude");
  }

  default Optional<Double> longitude() {
    return arincRecord().getOptionalField("longitude");
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

  default Optional<Boolean> daylightTimeIndicator() {
    return arincRecord().getOptionalField("daylightTimeIndicator");
  }

  default Optional<MagneticTrueIndicator> magneticTrueIndicator() {
    return arincRecord().getOptionalField("magneticTrueIndicator");
  }

  default Optional<String> datumCode() {
    return arincRecord().getOptionalField("datumCode");
  }

  default Optional<String> airportFullName() {
    return arincRecord().getOptionalField("airportFullName");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().getOptionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().getOptionalField("cycle");
  }

  /**
   * Wraps the given {@link ArincRecord} as an {@link ArincAirport} object.
   */
  static ArincAirport wrap(ArincRecord record) {
    return () -> record;
  }
}
