package org.mitre.tdp.boogie.arinc.v18.spec;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RouteHoldDistanceTime;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SpeedLimitDescription;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;

public final class ProcedureLegConverter implements Function<ArincRecord, Optional<ArincProcedureLeg>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new ProcedureLegValidator().negate();

  @Override
  public Optional<ArincProcedureLeg> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Input ArincRecord cannot be null.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<CustomerAreaCode> customerAreaCode = arincRecord.optionalField("customerAreaCode");
    Optional<String> transitionIdentifier = arincRecord.optionalField("transitionIdentifier");
    Optional<String> fixIdentifier = arincRecord.optionalField("fixIdentifier");
    Optional<String> fixIcaoRegion = arincRecord.optionalField("fixIcaoRegion");
    Optional<SectionCode> fixSectionCode = arincRecord.optionalField("fixSectionCode");
    Optional<String> fixSubSectionCode = arincRecord.optionalField("fixSubSectionCode");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<String> waypointDescription = arincRecord.optionalField("waypointDescription");
    Optional<TurnDirection> turnDirection = arincRecord.optionalField("turnDirection");
    Optional<Double> rnp = arincRecord.optionalField("rnp");
    Optional<Boolean> turnDirectionValid = arincRecord.optionalField("turnDirectionValid");
    Optional<String> recommendedNavaidIdentifier = arincRecord.optionalField("recommendedNavaidIdentifier");
    Optional<String> recommendedNavaidIcaoRegion = arincRecord.optionalField("recommendedNavaidIcaoRegion");
    Optional<SectionCode> recommendedNavaidSectionCode = arincRecord.optionalField("recommendedNavaidSectionCode");
    Optional<String> recommendedNavaidSubSectionCode = arincRecord.optionalField("recommendedNavaidSubSectionCode");
    Optional<Double> arcRadius = arincRecord.optionalField("arcRadius");
    Optional<Double> theta = arincRecord.optionalField("theta");
    Optional<Double> rho = arincRecord.optionalField("rho");
    Optional<Double> outboundMagneticCourse = arincRecord.optionalField("outboundMagneticCourse");
    Optional<String> routeHoldDistanceTime = arincRecord.optionalField("routeHoldDistanceTime");

    RouteHoldDistanceTime routeHoldDistanceTimeConverter = new RouteHoldDistanceTime();
    Optional<Duration> holdTime = routeHoldDistanceTime.flatMap(routeHoldDistanceTimeConverter::asDuration);
    Optional<Double> routeDistance = routeHoldDistanceTime.flatMap(routeHoldDistanceTimeConverter::asDistanceInNm);

    Optional<String> altitudeDescription = arincRecord.optionalField("altitudeDescription");
    Optional<Double> minAltitude1 = arincRecord.optionalField("minAltitude1");
    Optional<Double> minAltitude2 = arincRecord.optionalField("minAltitude2");
    Optional<Double> transitionAltitude = arincRecord.optionalField("transitionAltitude");
    Optional<Integer> speedLimit = arincRecord.optionalField("speedLimit");
    Optional<Double> verticalAngle = arincRecord.optionalField("verticalAngle");
    Optional<String> centerFixIdentifier = arincRecord.optionalField("centerFixIdentifier");
    Optional<String> centerFixIcaoRegion = arincRecord.optionalField("centerFixIcaoRegion");
    Optional<SectionCode> centerFixSectionCode = arincRecord.optionalField("centerFixSectionCode");
    Optional<String> centerFixSubSectionCode = arincRecord.optionalField("centerFixSubSectionCode");
    Optional<SpeedLimitDescription> speedLimitDescription = arincRecord.optionalField("speedLimitDescription");
    Optional<String> routeTypeQualifier1 = arincRecord.optionalField("routeTypeQualifier1");
    Optional<String> routeTypeQualifier2 = arincRecord.optionalField("routeTypeQualifier2");

    ArincProcedureLeg procedureLeg = new ArincProcedureLeg.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(customerAreaCode.orElse(null))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .airportIdentifier(arincRecord.requiredField("airportIdentifier"))
        .airportIcaoRegion(arincRecord.requiredField("airportIcaoRegion"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .sidStarIdentifier(arincRecord.requiredField("sidStarIdentifier"))
        .routeType(arincRecord.requiredField("routeType"))
        .transitionIdentifier(transitionIdentifier.orElse(null))
        .sequenceNumber(arincRecord.requiredField("sequenceNumber"))
        .fixIdentifier(fixIdentifier.orElse(null))
        .fixIcaoRegion(fixIcaoRegion.orElse(null))
        .fixSectionCode(fixSectionCode.orElse(null))
        .fixSubSectionCode(fixSubSectionCode.orElse(null))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .waypointDescription(waypointDescription.orElse(null))
        .turnDirection(turnDirection.orElse(null))
        .rnp(rnp.orElse(null))
        .pathTerm(arincRecord.requiredField("pathTerm"))
        .turnDirectionValid(turnDirectionValid.orElse(null))
        .recommendedNavaidIdentifier(recommendedNavaidIdentifier.orElse(null))
        .recommendedNavaidIcaoRegion(recommendedNavaidIcaoRegion.orElse(null))
        .recommendedNavaidSectionCode(recommendedNavaidSectionCode.orElse(null))
        .recommendedNavaidSubSectionCode(recommendedNavaidSubSectionCode.orElse(null))
        .arcRadius(arcRadius.orElse(null))
        .theta(theta.orElse(null))
        .rho(rho.orElse(null))
        .outboundMagneticCourse(outboundMagneticCourse.orElse(null))
        .routeHoldDistanceTime(routeHoldDistanceTime.orElse(null))
        .holdTime(holdTime.orElse(null))
        .routeDistance(routeDistance.orElse(null))
        .altitudeDescription(altitudeDescription.orElse(null))
        .minAltitude1(minAltitude1.orElse(null))
        .minAltitude2(minAltitude2.orElse(null))
        .transitionAltitude(transitionAltitude.orElse(null))
        .speedLimit(speedLimit.orElse(null))
        .verticalAngle(verticalAngle.orElse(null))
        .centerFixIdentifier(centerFixIdentifier.orElse(null))
        .centerFixIcaoRegion(centerFixIcaoRegion.orElse(null))
        .centerFixSectionCode(centerFixSectionCode.orElse(null))
        .centerFixSubSectionCode(centerFixSubSectionCode.orElse(null))
        .speedLimitDescription(speedLimitDescription.orElse(null))
        .routeTypeQualifier1(routeTypeQualifier1.orElse(null))
        .routeTypeQualifier2(routeTypeQualifier2.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdateCycle(arincRecord.requiredField("lastUpdateCycle"))
        .build();

    return Optional.of(procedureLeg);
  }
}
