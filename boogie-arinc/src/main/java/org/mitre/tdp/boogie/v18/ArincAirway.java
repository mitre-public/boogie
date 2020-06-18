package org.mitre.tdp.boogie.v18;

import java.time.Duration;
import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.v18.spec.field.BoundaryCode;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.Level;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.RouteHoldDistanceTime;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.record.AirwaySpec;

/**
 * Typed interface for retrieving record field and type information from {@link ArincRecord}s parsed via the {@link AirwaySpec}.
 */
@FunctionalInterface
public interface ArincAirway {

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

  default String subSectionCode() {
    return arincRecord().getRequiredField("subSectionCode");
  }

  default String routeIdentifier() {
    return arincRecord().getRequiredField("routeIdentifier");
  }

  default Optional<String> sixthCharacter() {
    return arincRecord().getOptionalField("sixthCharacter");
  }

  default Integer sequenceNumber() {
    return arincRecord().getRequiredField("sequenceNumber");
  }

  default String fixIdentifier() {
    return arincRecord().getRequiredField("fixIdentifier");
  }

  default String fixIcaoRegion() {
    return arincRecord().getRequiredField("fixIcaoRegion");
  }

  default SectionCode fixSectionCode() {
    return arincRecord().getRequiredField("fixSectionCode");
  }

  default String fixSubSectionCode() {
    return arincRecord().getRequiredField("fixSubSectionCode");
  }

  default String continuationRecordNumber() {
    return arincRecord().getRequiredField("continuationRecordNumber");
  }

  default Optional<String> fixDescription() {
    return arincRecord().getOptionalField("fixDescription");
  }

  default Optional<BoundaryCode> boundaryCode() {
    return arincRecord().getOptionalField("boundaryCode");
  }

  default String routeType() {
    return arincRecord().getRequiredField("routeType");
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
    return routeHoldDistanceTime().map(s -> new RouteHoldDistanceTime().asDuration(s));
  }

  default Optional<Double> routeDistance() {
    return routeHoldDistanceTime().map(s -> new RouteHoldDistanceTime().asDistanceInNm(s));
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

  default Integer fileRecordNumber() {
    return arincRecord().getRequiredField("fileRecordNumber");
  }

  default String cycle() {
    return arincRecord().getRequiredField("cycle");
  }

  /**
   * Wraps the given {@link ArincRecord} as an {@link ArincAirway} object.
   */
  static ArincAirway wrap(ArincRecord record) {
    return () -> record;
  }
}
