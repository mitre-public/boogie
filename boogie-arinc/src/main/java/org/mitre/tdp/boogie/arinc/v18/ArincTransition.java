package org.mitre.tdp.boogie.arinc.v18;

import java.time.Duration;
import java.util.Optional;

import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.RouteHoldDistanceTime;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimitDescription;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;

@FunctionalInterface
public interface ArincTransition {

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
    return arincRecord().optionalField("airportIcaoRegion");
  }

  default Optional<String> subSectionCode() {
    return arincRecord().optionalField("subSectionCode");
  }

  default Optional<String> sidStarIdentifier() {
    return arincRecord().optionalField("sidStarIdentifier");
  }

  default Optional<String> routeType() {
    return arincRecord().optionalField("routeType");
  }

  default Optional<String> transitionIdentifier() {
    return arincRecord().optionalField("transitionIdentifier");
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

  default Optional<TurnDirection> turnDirection() {
    return arincRecord().optionalField("turnDirection");
  }

  default Optional<Double> rnp() {
    return arincRecord().optionalField("rnp");
  }

  default Optional<PathTerm> pathTerm() {
    return arincRecord().optionalField("pathTerm");
  }

  default Optional<Boolean> turnDirectionValid() {
    return arincRecord().optionalField("turnDirectionValid");
  }

  default Optional<String> recommendedNavaid() {
    return arincRecord().optionalField("recommendedNavaid");
  }

  default Optional<String> recommendedNavaidIcaoRegion() {
    return arincRecord().optionalField("recommendedNavaidIcaoRegion");
  }

  default Optional<Double> arcRadius() {
    return arincRecord().optionalField("arcRadius");
  }

  default Optional<Double> theta() {
    return arincRecord().optionalField("theta");
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

  default Optional<SectionCode> recommendedNavaidSectionCode() {
    return arincRecord().optionalField("recommendedNavaidSectionCode");
  }

  default Optional<String> recommendedNavaidSubSectionCode() {
    return arincRecord().optionalField("recommendedNavaidSubSectionCode");
  }

  default Optional<String> altitudeDescription() {
    return arincRecord().optionalField("altitudeDescription");
  }

  default Optional<Double> minAltitude1() {
    return arincRecord().optionalField("minAltitude1");
  }

  default Optional<Double> minAltitude2() {
    return arincRecord().optionalField("minAltitude2");
  }

  default Optional<Double> transitionAltitude() {
    return arincRecord().optionalField("transitionAltitude");
  }

  default Optional<Integer> speedLimit() {
    return arincRecord().optionalField("speedLimit");
  }

  default Optional<Double> verticalAngle() {
    return arincRecord().optionalField("verticalAngle");
  }

  default Optional<String> centerFix() {
    return arincRecord().optionalField("centerFix");
  }

  default Optional<String> centerFixIcaoRegion() {
    return arincRecord().optionalField("centerFixIcaoRegion");
  }

  default Optional<SectionCode> centerFixSectionCode() {
    return arincRecord().optionalField("centerFixSectionCode");
  }

  default Optional<String> centerFixSubSectionCode() {
    return arincRecord().optionalField("centerFixSubSectionCode");
  }

  default Optional<SpeedLimitDescription> speedLimitDescription() {
    return arincRecord().optionalField("speedLimitDescription");
  }

  default Optional<String> routeTypeQualifier1() {
    return arincRecord().optionalField("routeQualifier1");
  }

  default Optional<String> routeTypeQualifier2() {
    return arincRecord().optionalField("routeQualifier2");
  }

  default Optional<Integer> fileRecordNumber() {
    return arincRecord().optionalField("fileRecordNumber");
  }

  default Optional<String> cycle() {
    return arincRecord().optionalField("cycle");
  }

  static ArincTransition wrap(ArincRecord record) {
    return () -> record;
  }
}
