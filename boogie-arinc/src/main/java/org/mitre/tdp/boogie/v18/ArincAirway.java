package org.mitre.tdp.boogie.v18;

import java.time.Duration;
import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincRecordDecorator;
import org.mitre.tdp.boogie.v18.spec.field.BoundaryCode;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.Level;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.RouteHoldDistanceTime;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.record.AirwaySpec;

/**
 * Typed interface for retrieving record field and type information from {@link ArincRecord}s parsed via the {@link AirwaySpec}.
 *
 * Note this returns all fields as optionals - this allows users to handle expected/unexpected nulls on typed returns more gracefully
 * as required by the limitations of their input ARINC data source.
 */
@FunctionalInterface
public interface ArincAirway extends ArincRecordDecorator {

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

  default Optional<String> subSectionCode() {
    return arincRecord().getOptionalField("subSectionCode");
  }

  default Optional<String> routeIdentifier() {
    return arincRecord().getOptionalField("routeIdentifier");
  }

  default Optional<String> sixthCharacter() {
    return arincRecord().getOptionalField("sixthCharacter");
  }

  default Optional<Integer> sequenceNumber() {
    return arincRecord().getOptionalField("sequenceNumber");
  }

  default Optional<String> fixIdentifier() {
    return arincRecord().getOptionalField("fixIdentifier");
  }

  default Optional<String> fixIcaoRegion() {
    return arincRecord().getOptionalField("fixIcaoRegion");
  }

  default Optional<SectionCode> fixSectionCode() {
    return arincRecord().getOptionalField("fixSectionCode");
  }

  default Optional<String> fixSubSectionCode() {
    return arincRecord().getOptionalField("fixSubSectionCode");
  }

  default Optional<String> continuationRecordNumber() {
    return arincRecord().getOptionalField("continuationRecordNumber");
  }

  default Optional<String> waypointDescription() {
    return arincRecord().getOptionalField("waypointDescription");
  }

  default Optional<BoundaryCode> boundaryCode() {
    return arincRecord().getOptionalField("boundaryCode");
  }

  default Optional<String> routeType() {
    return arincRecord().getOptionalField("routeType");
  }

  default Optional<Level> level() {
    return arincRecord().getOptionalField("level");
  }

  default Optional<String> directionRestriction() {
    return arincRecord().getOptionalField("directionRestriction");
  }

  default Optional<String> cruiseTableIndicator() {
    return arincRecord().getOptionalField("cruiseTableIndicator");
  }

  default Optional<Boolean> euIndicator() {
    return arincRecord().getOptionalField("euIndicator");
  }

  default Optional<String> recommendedNavaid() {
    return arincRecord().getOptionalField("recommendedNavaid");
  }

  default Optional<String> recommendedNavaidIcaoRegion() {
    return arincRecord().getOptionalField("recommendedNavaidIcaoRegion");
  }

  default Optional<Double> theta() {
    return arincRecord().getOptionalField("theta");
  }

  default Optional<Double> rnp() {
    return arincRecord().getOptionalField("rnp");
  }

  default Optional<Double> rho() {
    return arincRecord().getOptionalField("rho");
  }

  default Optional<Double> outboundMagneticCourse() {
    return arincRecord().getOptionalField("outboundMagneticCourse");
  }

  default Optional<String> routeHoldDistanceTime() {
    return arincRecord().getOptionalField("routeHoldDistanceTime");
  }

  default Optional<Duration> holdTime() {
    return routeHoldDistanceTime().flatMap(s -> new RouteHoldDistanceTime().asDuration(s));
  }

  default Optional<Double> routeDistance() {
    return routeHoldDistanceTime().flatMap(s -> new RouteHoldDistanceTime().asDistanceInNm(s));
  }

  default Optional<Double> inboundMagneticCourse() {
    return arincRecord().getOptionalField("inboundMagneticCourse");
  }

  default Optional<Double> minAltitude1() {
    return arincRecord().getOptionalField("minAltitude1");
  }

  default Optional<Double> minAltitude2() {
    return arincRecord().getOptionalField("minAltitude2");
  }

  default Optional<Double> maxAltitude() {
    return arincRecord().getOptionalField("maxAltitude");
  }

  default Optional<Double> fixedRadiusTransitionIndicator() {
    return arincRecord().getOptionalField("fixedRadiusTransitionIndicator");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().getOptionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().getOptionalField("cycle");
  }

  /**
   * Wraps the given {@link ArincRecord} as an {@link ArincAirway} object.
   */
  static ArincAirway wrap(ArincRecord record) {
    return () -> record;
  }
}
