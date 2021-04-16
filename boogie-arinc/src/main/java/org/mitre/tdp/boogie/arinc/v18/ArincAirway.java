package org.mitre.tdp.boogie.arinc.v18;

import java.time.Duration;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryCode;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.RouteHoldDistanceTime;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.spec.AirwaySpec;

/**
 * Typed interface for retrieving record field and type information from {@link ArincRecord}s parsed via the {@link AirwaySpec}.
 *
 * Note this returns all fields as optionals - this allows users to handle expected/unexpected nulls on typed returns more gracefully
 * as required by the limitations of their input ARINC data source.
 */
@FunctionalInterface
public interface ArincAirway {

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

  default Optional<String> subSectionCode() {
    return arincRecord().optionalField("subSectionCode");
  }

  default Optional<String> routeIdentifier() {
    return arincRecord().optionalField("routeIdentifier");
  }

  default Optional<String> sixthCharacter() {
    return arincRecord().optionalField("sixthCharacter");
  }

  default Optional<Integer> sequenceNumber() {
    return arincRecord().optionalField("sequenceNumber");
  }

  default Optional<String> fixIdentifier() {
    return arincRecord().optionalField("fixIdentifier");
  }

  default Optional<String> fixIcaoRegion() {
    return arincRecord().optionalField("fixIcaoRegion");
  }

  default Optional<SectionCode> fixSectionCode() {
    return arincRecord().optionalField("fixSectionCode");
  }

  default Optional<String> fixSubSectionCode() {
    return arincRecord().optionalField("fixSubSectionCode");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().optionalField("continuationRecordNumber");
  }

  default Optional<String> waypointDescription() {
    return arincRecord().optionalField("waypointDescription");
  }

  default Optional<BoundaryCode> boundaryCode() {
    return arincRecord().optionalField("boundaryCode");
  }

  default Optional<String> routeType() {
    return arincRecord().optionalField("routeType");
  }

  default Optional<Level> level() {
    return arincRecord().optionalField("level");
  }

  default Optional<String> directionRestriction() {
    return arincRecord().optionalField("directionRestriction");
  }

  default Optional<String> cruiseTableIndicator() {
    return arincRecord().optionalField("cruiseTableIndicator");
  }

  default Optional<Boolean> euIndicator() {
    return arincRecord().optionalField("euIndicator");
  }

  default Optional<String> recommendedNavaid() {
    return arincRecord().optionalField("recommendedNavaid");
  }

  default Optional<String> recommendedNavaidIcaoRegion() {
    return arincRecord().optionalField("recommendedNavaidIcaoRegion");
  }

  default Optional<Double> theta() {
    return arincRecord().optionalField("theta");
  }

  default Optional<Double> rnp() {
    return arincRecord().optionalField("rnp");
  }

  default Optional<Double> rho() {
    return arincRecord().optionalField("rho");
  }

  default Optional<Double> outboundMagneticCourse() {
    return arincRecord().optionalField("outboundMagneticCourse");
  }

  default Optional<String> routeHoldDistanceTime() {
    return arincRecord().optionalField("routeHoldDistanceTime");
  }

  default Optional<Duration> holdTime() {
    return routeHoldDistanceTime().flatMap(s -> new RouteHoldDistanceTime().asDuration(s));
  }

  default Optional<Double> routeDistance() {
    return routeHoldDistanceTime().flatMap(s -> new RouteHoldDistanceTime().asDistanceInNm(s));
  }

  default Optional<Double> inboundMagneticCourse() {
    return arincRecord().optionalField("inboundMagneticCourse");
  }

  default Optional<Double> minAltitude1() {
    return arincRecord().optionalField("minAltitude1");
  }

  default Optional<Double> minAltitude2() {
    return arincRecord().optionalField("minAltitude2");
  }

  default Optional<Double> maxAltitude() {
    return arincRecord().optionalField("maxAltitude");
  }

  default Optional<Double> fixedRadiusTransitionIndicator() {
    return arincRecord().optionalField("fixedRadiusTransitionIndicator");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().optionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().optionalField("cycle");
  }

  /**
   * Wraps the given {@link ArincRecord} as an {@link ArincAirway} object.
   */
  static ArincAirway wrap(ArincRecord record) {
    return () -> record;
  }
}
