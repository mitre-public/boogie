package org.mitre.tdp.boogie.v18;

import java.time.Duration;
import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.RouteHoldDistanceTime;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.field.SpeedLimitDescription;
import org.mitre.tdp.boogie.v18.spec.field.TurnDirection;

@FunctionalInterface
public interface ArincTransition {

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
    return arincRecord().getOptionalField("airportIcaoRegion");
  }

  default Optional<String> subSectionCode() {
    return arincRecord().getOptionalField("subSectionCode");
  }

  default Optional<String> sidStarIdentifier() {
    return arincRecord().getOptionalField("sidStarIdentifier");
  }

  default Optional<String> routeType() {
    return arincRecord().getOptionalField("routeType");
  }

  default Optional<String> transitionIdentifier() {
    return arincRecord().getOptionalField("transitionIdentifier");
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

  default Optional<TurnDirection> turnDirection() {
    return arincRecord().getOptionalField("turnDirection");
  }

  default Optional<Double> rnp() {
    return arincRecord().getOptionalField("rnp");
  }

  default Optional<PathTerm> pathTerm() {
    return arincRecord().getOptionalField("pathTerm");
  }

  default Optional<Boolean> turnDirectionValid() {
    return arincRecord().getOptionalField("turnDirectionValid");
  }

  default Optional<String> recommendedNavaid() {
    return arincRecord().getOptionalField("recommendedNavaid");
  }

  default Optional<String> recommendedNavaidIcaoRegion() {
    return arincRecord().getOptionalField("recommendedNavaidIcaoRegion");
  }

  default Optional<Double> arcRadius() {
    return arincRecord().getOptionalField("arcRadius");
  }

  default Optional<Double> theta() {
    return arincRecord().getOptionalField("theta");
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

  default Optional<SectionCode> recommendedNavaidSectionCode() {
    return arincRecord().getOptionalField("recommendedNavaidSectionCode");
  }

  default Optional<String> recommendedNavaidSubSectionCode() {
    return arincRecord().getOptionalField("recommendedNavaidSubSectionCode");
  }

  default Optional<String> altitudeDescription() {
    return arincRecord().getOptionalField("altitudeDescription");
  }

//  default Optional<String> atc(){
//    return arincRecord().getOptionalField("atc");
//  }

  default Optional<Double> minAltitude1() {
    return arincRecord().getOptionalField("minAltitude1");
  }

  default Optional<Double> minAltitude2() {
    return arincRecord().getOptionalField("minAltitude2");
  }

  default Optional<Double> transitionAltitude() {
    return arincRecord().getOptionalField("transitionAltitude");
  }

  default Optional<Integer> speedLimit() {
    return arincRecord().getOptionalField("speedLimit");
  }

  default Optional<Double> verticalAngle() {
    return arincRecord().getOptionalField("verticalAngle");
  }

  default Optional<String> centerFix() {
    return arincRecord().getOptionalField("centerFix");
  }

  default Optional<String> centerFixIcaoRegion() {
    return arincRecord().getOptionalField("centerFixIcaoRegion");
  }

  default Optional<SectionCode> centerFixSectionCode() {
    return arincRecord().getOptionalField("centerFixSectionCode");
  }

  default Optional<String> centerFixSubSectionCode() {
    return arincRecord().getOptionalField("centerFixSubSectionCode");
  }

  default Optional<SpeedLimitDescription> speedLimitDescription() {
    return arincRecord().getOptionalField("speedLimitDescription");
  }

  default Optional<String> routeTypeQualifier1() {
    return arincRecord().getOptionalField("routeQualifier1");
  }

  default Optional<String> routeTypeQualifier2() {
    return arincRecord().getOptionalField("routeQualifier2");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().getOptionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().getOptionalField("cycle");
  }

  static ArincTransition wrap(ArincRecord record) {
    return () -> record;
  }
}
