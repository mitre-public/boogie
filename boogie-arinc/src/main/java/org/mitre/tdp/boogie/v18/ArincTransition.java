package org.mitre.tdp.boogie.v18;

import java.util.Optional;

import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.field.TurnDirection;

@FunctionalInterface
public interface ArincTransition {

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
    return arincRecord().getRequiredField("airportIcaoRegion");
  }

  default String subSectionCode() {
    return arincRecord().getRequiredField("subSectionCode");
  }

  default String sidStarIdentifier() {
    return arincRecord().getRequiredField("sidStarIdentifier");
  }

  default String routeType() {
    return arincRecord().getRequiredField("routeType");
  }

  default Optional<String> transitionIdentifier() {
    return arincRecord().getOptionalField("transitionIdentifier");
  }

  default Integer sequenceNumber() {
    return arincRecord().getRequiredField("sequenceNumber");
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

  default String continuationRecordNumber() {
    return arincRecord().getRequiredField("continuationRecordNumber");
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

  default PathTerm pathTerm() {
    return arincRecord().getRequiredField("pathTerm");
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

  default Optional<String> recommendedNavaidSectionCode() {
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

  default Optional<String> centerFixSectionCode() {
    return arincRecord().getOptionalField("centerFixSectionCode");
  }

  default Optional<String> centerFixSubSectionCode() {
    return arincRecord().getOptionalField("centerFixSubSectionCode");
  }

//  default Optional<String> speedLimitDescription(){
//
//  }

  default Integer fileRecordNumber() {
    return arincRecord().getRequiredField("fileRecordNumber");
  }

  default String cycle() {
    return arincRecord().getRequiredField("cycle");
  }

  static ArincTransition wrap(ArincRecord record) {
    return () -> record;
  }
}
