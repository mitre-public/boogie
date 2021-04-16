package org.mitre.tdp.boogie.arinc.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.LongestRunwaySurfaceCode;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticTrueIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.PublicMilitaryIndicator;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.spec.AirportSpec;

/**
 * Typed interface for retrieving record field and type information from {@link ArincRecord}s parsed via the {@link AirportSpec}.
 */
@FunctionalInterface
public interface ArincAirport {
  
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
    return arincRecord().optionalField("icaoRegion");
  }

  default Optional<String> subSectionCode() {
    return arincRecord().optionalField("subSectionCode");
  }

  default Optional<String> iataDesignator() {
    return arincRecord().optionalField("iataDesignator");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().optionalField("continuationRecordNumber");
  }

  default Optional<Double> speedLimitAltitude() {
    return arincRecord().optionalField("speedLimitAltitude");
  }

  default Optional<Integer> longestRunway() {
    return arincRecord().optionalField("longestRunway");
  }

  default Optional<Boolean> ifrCapability() {
    return arincRecord().optionalField("ifrCapability");
  }

  default Optional<LongestRunwaySurfaceCode> longestRunwaySurfaceCode() {
    return arincRecord().optionalField("longestRunwaySurfaceCode");
  }

  default Optional<Double> latitude() {
    return arincRecord().optionalField("latitude");
  }

  default Optional<Double> longitude() {
    return arincRecord().optionalField("longitude");
  }

  default Optional<Double> magneticVariation() {
    return arincRecord().optionalField("magneticVariation");
  }

  default Optional<Double> airportElevation() {
    return arincRecord().optionalField("airportElevation");
  }

  default Optional<Integer> speedLimit() {
    return arincRecord().optionalField("speedLimit");
  }

  default Optional<String> recommendedNavaid() {
    return arincRecord().optionalField("recommendedNavaid");
  }

  default Optional<String> recommendedNavaidIcaoRegion() {
    return arincRecord().optionalField("recommendedNavaidIcaoRegion");
  }

  default Optional<Double> transitionAltitude() {
    return arincRecord().optionalField("transitionAltitude");
  }

  default Optional<Double> transitionLevel() {
    return arincRecord().optionalField("transitionLevel");
  }

  default Optional<PublicMilitaryIndicator> publicMilitaryIndicator() {
    return arincRecord().optionalField("publicMilitaryIndicator");
  }

  default Optional<Boolean> daylightTimeIndicator() {
    return arincRecord().optionalField("daylightTimeIndicator");
  }

  default Optional<MagneticTrueIndicator> magneticTrueIndicator() {
    return arincRecord().optionalField("magneticTrueIndicator");
  }

  default Optional<String> datumCode() {
    return arincRecord().optionalField("datumCode");
  }

  default Optional<String> airportFullName() {
    return arincRecord().optionalField("airportFullName");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().optionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().optionalField("cycle");
  }

  /**
   * Wraps the given {@link ArincRecord} as an {@link ArincAirport} object.
   */
  static ArincAirport wrap(ArincRecord record) {
    return () -> record;
  }
}
