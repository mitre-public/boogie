package org.mitre.tdp.boogie.contract.arinc;

import java.time.Duration;
import java.util.Optional;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@Value.Immutable
@JsonSerialize(as = ImmutableArincProcedureLeg.class)
@JsonDeserialize(as = ImmutableArincProcedureLeg.class)
public interface ArincProcedureLeg {
  RecordType recordType();

  Optional<CustomerAreaCode> customerAreaCode();

  SectionCode sectionCode();

  String airportIdentifier();

  String airportIcaoRegion();

  Optional<String> subSectionCode();

  String sidStarIdentifier();

  String routeType();

  Optional<String> transitionIdentifier();

  int sequenceNumber();

  Optional<String> fixIdentifier();


  Optional<String> fixIcaoRegion();

  Optional<SectionCode> fixSectionCode();

  Optional<String> fixSubSectionCode();

  Optional<String> continuationRecordNumber();

  Optional<String> waypointDescription();

  Optional<TurnDirection> turnDirection();

  Optional<Double> rnp();

  PathTerminator pathTerm();

  Optional<Boolean> turnDirectionValid();

  Optional<String> recommendedNavaidIdentifier();

  Optional<String> recommendedNavaidIcaoRegion();

  Optional<Double> arcRadius();

  Optional<Double> theta();

  Optional<Double> rho();

  Optional<Double> outboundMagneticCourse();

  Optional<String> routeHoldDistanceTime();

  Optional<Duration> holdTime();

  Optional<Double> routeDistance();

  Optional<SectionCode> recommendedNavaidSectionCode();

  Optional<String> recommendedNavaidSubSectionCode();

  Optional<String> altitudeDescription();

  Optional<Double> minAltitude1();

  Optional<Double> minAltitude2();

  Optional<Double> transitionAltitude();

  Optional<Integer> speedLimit();

  Optional<Double> verticalAngle();

  Optional<String> centerFixIdentifier();

  Optional<String> centerFixIcaoRegion();

  Optional<SectionCode> centerFixSectionCode();

  Optional<String> centerFixSubSectionCode();

  Optional<String> speedLimitDescription();

  Optional<String> routeTypeQualifier1();

  Optional<String> routeTypeQualifier2();

  int fileRecordNumber();

  String lastUpdateCycle();
}
