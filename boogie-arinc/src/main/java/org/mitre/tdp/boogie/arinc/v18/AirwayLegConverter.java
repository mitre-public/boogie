package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryCode;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.RouteHoldDistanceTime;

/**
 * Converter class for functionally transforming a {@link ArincRecord} into a {@link ArincAirwayLeg} concrete data model.
 * <br>
 * This class has some expectations on the data {@link ArincRecord} provided to it - these expectations are communicated through
 * the {@link AirwayLegValidator}.
 */
public final class AirwayLegConverter implements Function<ArincRecord, Optional<ArincAirwayLeg>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new AirwayLegValidator().negate();

  @Override
  public Optional<ArincAirwayLeg> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Cannot apply conversion to null ArincRecord.");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<String> sixthCharacter = arincRecord.optionalField("sixthCharacter");
    Optional<String> fixSubSectionCode = arincRecord.optionalField("fixSubSectionCode");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<String> waypointDescription = arincRecord.optionalField("waypointDescription");
    Optional<CustomerAreaCode> boundaryCode = arincRecord.optionalField("boundaryCode");
    Optional<String> routeType = arincRecord.optionalField("routeType");
    Optional<Level> level = arincRecord.optionalField("level");
    Optional<String> directionRestriction = arincRecord.optionalField("directionRestriction");
    Optional<String> cruiseTableIndicator = arincRecord.optionalField("cruiseTableIndicator");
    Optional<Boolean> euIndicator = arincRecord.optionalField("euIndicator");
    Optional<String> recommendedNavaidIdentifier = arincRecord.optionalField("recommendedNavaidIdentifier");
    Optional<String> recommendedNavaidIcaoRegion = arincRecord.optionalField("recommendedNavaidIcaoRegion");
    Optional<Double> rnp = arincRecord.optionalField("rnp");
    Optional<Double> theta = arincRecord.optionalField("theta");
    Optional<Double> rho = arincRecord.optionalField("rho");
    Optional<Double> outboundMagneticCourse = arincRecord.optionalField("outboundMagneticCourse");
    Optional<String> routeHoldDistanceTime = arincRecord.optionalField("routeHoldDistanceTime");
    Optional<Double> inboundMagneticCourse = arincRecord.optionalField("inboundMagneticCourse");
    Optional<Double> minAltitude1 = arincRecord.optionalField("minAltitude1");
    Optional<Double> minAltitude2 = arincRecord.optionalField("minAltitude2");
    Optional<Double> maxAltitude = arincRecord.optionalField("maxAltitude");
    Optional<Double> fixedRadiusTransitionIndicator = arincRecord.optionalField("fixedRadiusTransitionIndicator");

    RouteHoldDistanceTime routeHoldDistanceTimeConverter = new RouteHoldDistanceTime();

    ArincAirwayLeg airwayLeg = new ArincAirwayLeg.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(arincRecord.requiredField("customerAreaCode"))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .routeIdentifier(arincRecord.requiredField("routeIdentifier"))
        .sixthCharacter(sixthCharacter.orElse(null))
        .sequenceNumber(arincRecord.requiredField("sequenceNumber"))
        .fixIdentifier(arincRecord.requiredField("fixIdentifier"))
        .fixIcaoRegion(arincRecord.requiredField("fixIcaoRegion"))
        .fixSectionCode(arincRecord.requiredField("fixSectionCode"))
        .fixSubSectionCode(fixSubSectionCode.orElse(null))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .waypointDescription(waypointDescription.orElse(null))
        .boundaryCode(boundaryCode.orElse(null))
        .routeType(routeType.orElse(null))
        .level(level.orElse(null))
        .directionRestriction(directionRestriction.orElse(null))
        .cruiseTableIndicator(cruiseTableIndicator.orElse(null))
        .euIndicator(euIndicator.orElse(null))
        .recommendedNavaidIdentifier(recommendedNavaidIdentifier.orElse(null))
        .recommendedNavaidIcaoRegion(recommendedNavaidIcaoRegion.orElse(null))
        .rnp(rnp.orElse(null))
        .theta(theta.orElse(null))
        .rho(rho.orElse(null))
        .outboundMagneticCourse(outboundMagneticCourse.orElse(null))
        .routeHoldDistanceTime(routeHoldDistanceTime.orElse(null))
        .routeDistance(routeHoldDistanceTime.flatMap(routeHoldDistanceTimeConverter::asDistanceInNm).orElse(null))
        .holdTime(routeHoldDistanceTime.flatMap(routeHoldDistanceTimeConverter::asDuration).orElse(null))
        .inboundMagneticCourse(inboundMagneticCourse.orElse(null))
        .minAltitude1(minAltitude1.orElse(null))
        .minAltitude2(minAltitude2.orElse(null))
        .maxAltitude(maxAltitude.orElse(null))
        .fixedRadiusTransitionIndicator(fixedRadiusTransitionIndicator.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdateCycle(arincRecord.requiredField("lastUpdateCycle"))
        .build();

    return Optional.of(airwayLeg);
  }
}
