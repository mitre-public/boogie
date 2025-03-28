package org.mitre.tdp.boogie.dafif.v81.converter;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;

public class DafifRunwayConverter implements Function<DafifRecord, Optional<DafifRunway>> {

  @Override
  public Optional<DafifRunway> apply(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord, "Cannot convert null DafifRecord.");

    String airportIdentification = dafifRecord.requiredField("airportIdentification");
    String highEndIdentifier = dafifRecord.requiredField("highEndIdentifier");
    String lowEndIdentifier = dafifRecord.requiredField("lowEndIdentifier");
    Optional<Double> highEndMagneticHeading = dafifRecord.optionalField("highEndMagneticHeading");
    Optional<Double> lowEndMagneticHeading = dafifRecord.optionalField("lowEndMagneticHeading");
    Integer length = dafifRecord.requiredField("length");
    Integer width = dafifRecord.requiredField("width");
    Optional<String> surface = dafifRecord.optionalField("surface");
    Optional<String> pavementClassificationNumber = dafifRecord.optionalField("pavementClassificationNumber");
    Optional<String> highEndGeodeticLatitude = dafifRecord.optionalField("highEndGeodeticLatitude");
    Optional<Double> highEndDegreesLatitude = dafifRecord.optionalField("highEndDegreesLatitude");
    Optional<String> highEndGeodeticLongitude = dafifRecord.optionalField("highEndGeodeticLongitude");
    Optional<Double> highEndDegreesLongitude = dafifRecord.optionalField("highEndDegreesLongitude");
    Optional<String> highEndElevation = dafifRecord.optionalField("highEndElevation");
    String highEndSlope = dafifRecord.requiredField("highEndSlope");
    Optional<String> highEndTDZE = dafifRecord.optionalField("highEndTDZE");
    Optional<Integer> highEndDisplacedThreshold = dafifRecord.optionalField("highEndDisplacedThreshold");
    Optional<String> highEndDisplacedThresholdElevation = dafifRecord.optionalField("highEndDisplacedThresholdElevation");
    Optional<Integer> highEndLightingSystem1 = dafifRecord.optionalField("highEndLightingSystem1");
    Optional<Integer> highEndLightingSystem2 = dafifRecord.optionalField("highEndLightingSystem2");
    Optional<Integer> highEndLightingSystem3 = dafifRecord.optionalField("highEndLightingSystem3");
    Optional<Integer> highEndLightingSystem4 = dafifRecord.optionalField("highEndLightingSystem4");
    Optional<Integer> highEndLightingSystem5 = dafifRecord.optionalField("highEndLightingSystem5");
    Optional<Integer> highEndLightingSystem6 = dafifRecord.optionalField("highEndLightingSystem6");
    Optional<Integer> highEndLightingSystem7 = dafifRecord.optionalField("highEndLightingSystem7");
    Optional<Integer> highEndLightingSystem8 = dafifRecord.optionalField("highEndLightingSystem8");
    Optional<String> lowEndGeodeticLatitude = dafifRecord.optionalField("lowEndGeodeticLatitude");
    Optional<Double> lowEndDegreesLatitude = dafifRecord.optionalField("lowEndDegreesLatitude");
    Optional<String> lowEndGeodeticLongitude = dafifRecord.optionalField("lowEndGeodeticLongitude");
    Optional<Double> lowEndDegreesLongitude = dafifRecord.optionalField("lowEndDegreesLongitude");
    Optional<String> lowEndElevation = dafifRecord.optionalField("lowEndElevation");
    String lowEndSlope = dafifRecord.requiredField("lowEndSlope");
    Optional<String> lowEndTDZE = dafifRecord.optionalField("lowEndTDZE");
    Optional<Integer> lowEndDisplacedThreshold = dafifRecord.optionalField("lowEndDisplacedThreshold");
    Optional<String> lowEndDisplacedThresholdElevation = dafifRecord.optionalField("lowEndDisplacedThresholdElevation");
    Optional<Integer> lowEndLightingSystem1 = dafifRecord.optionalField("lowEndLightingSystem1");
    Optional<Integer> lowEndLightingSystem2 = dafifRecord.optionalField("lowEndLightingSystem2");
    Optional<Integer> lowEndLightingSystem3 = dafifRecord.optionalField("lowEndLightingSystem3");
    Optional<Integer> lowEndLightingSystem4 = dafifRecord.optionalField("lowEndLightingSystem4");
    Optional<Integer> lowEndLightingSystem5 = dafifRecord.optionalField("lowEndLightingSystem5");
    Optional<Integer> lowEndLightingSystem6 = dafifRecord.optionalField("lowEndLightingSystem6");
    Optional<Integer> lowEndLightingSystem7 = dafifRecord.optionalField("lowEndLightingSystem7");
    Optional<Integer> lowEndLightingSystem8 = dafifRecord.optionalField("lowEndLightingSystem8");
    Double highEndTrueHeading = dafifRecord.requiredField("trueHeadingHighEnd");
    Double lowEndTrueHeading = dafifRecord.requiredField("trueHeadingLowEnd");
    Optional<String> usableRunway = dafifRecord.optionalField("usableRunway");
    Optional<Integer> highEndLandingDistance = dafifRecord.optionalField("highEndLandingDistance");
    Optional<Integer> highEndRunwayDistance = dafifRecord.optionalField("highEndTakeoffRunwayDistance");
    Optional<Integer> highEndTakeOffDistance = dafifRecord.optionalField("highEndTakeOffDistance");
    Optional<Integer> highEndAccelerateStopDistance = dafifRecord.optionalField("highEndAccelerateStopDistance");
    Optional<Integer> lowEndLandingDistance = dafifRecord.optionalField("lowEndLandingDistance");
    Optional<Integer> lowEndRunwayDistance = dafifRecord.optionalField("lowEndTakeoffRunwayDistance");
    Optional<Integer> lowEndTakeOffDistance = dafifRecord.optionalField("lowEndTakeOffDistance");
    Optional<Integer> lowEndAccelerateStopDistance = dafifRecord.optionalField("lowEndAccelerateStopDistance");
    Integer cycleDate = dafifRecord.requiredField("cycleDate");
    Optional<Integer> coordinatePrecision = dafifRecord.optionalField("coordinatePrecision");

    List<Integer> highEndLighting = Stream.of(
        highEndLightingSystem1.orElse(null),
        highEndLightingSystem2.orElse(null),
        highEndLightingSystem3.orElse(null),
        highEndLightingSystem4.orElse(null),
        highEndLightingSystem5.orElse(null),
        highEndLightingSystem6.orElse(null),
        highEndLightingSystem7.orElse(null),
        highEndLightingSystem8.orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    List<Integer> lowEndLighting = Stream.of(
        lowEndLightingSystem1.orElse(null),
        lowEndLightingSystem2.orElse(null),
        lowEndLightingSystem3.orElse(null),
        lowEndLightingSystem4.orElse(null),
        lowEndLightingSystem5.orElse(null),
        lowEndLightingSystem6.orElse(null),
        lowEndLightingSystem7.orElse(null),
        lowEndLightingSystem8.orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());
    DafifRunway dafifRunway = new DafifRunway.Builder()
        .airportIdentification(airportIdentification)
        .highEndIdentifier(highEndIdentifier)
        .lowEndIdentifier(lowEndIdentifier)
        .highEndMagneticHeading(highEndMagneticHeading.orElse(null))
        .lowEndMagneticHeading(lowEndMagneticHeading.orElse(null))
        .length(length)
        .width(width)
        .surface(surface.orElse(null))
        .pavementClassificationNumber(pavementClassificationNumber.orElse(null))
        .highEndGeodeticLatitude(highEndGeodeticLatitude.orElse(null))
        .highEndDegreesLatitude(highEndDegreesLatitude.orElse(null))
        .highEndGeodeticLongitude(highEndGeodeticLongitude.orElse(null))
        .highEndDegreesLongitude(highEndDegreesLongitude.orElse(null))
        .highEndElevation(highEndElevation.orElse(null))
        .highEndSlope(highEndSlope)
        .highEndTDZE(highEndTDZE.orElse(null))
        .highEndDisplacedThreshold(highEndDisplacedThreshold.orElse(null))
        .highEndDisplacedThresholdElevation(highEndDisplacedThresholdElevation.orElse(null))
        .highEndLightingSystem(highEndLighting)
        .lowEndGeodeticLatitude(lowEndGeodeticLatitude.orElse(null))
        .lowEndDegreesLatitude(lowEndDegreesLatitude.orElse(null))
        .lowEndGeodeticLongitude(lowEndGeodeticLongitude.orElse(null))
        .lowEndDegreesLongitude(lowEndDegreesLongitude.orElse(null))
        .lowEndElevation(lowEndElevation.orElse(null))
        .lowEndSlope(lowEndSlope)
        .lowEndTDZE(lowEndTDZE.orElse(null))
        .lowEndDisplacedThreshold(lowEndDisplacedThreshold.orElse(null))
        .lowEndDisplacedThresholdElevation(lowEndDisplacedThresholdElevation.orElse(null))
        .lowEndLightingSystem(lowEndLighting)
        .trueHeadingHighEnd(highEndTrueHeading)
        .trueHeadingLowEnd(lowEndTrueHeading)
        .usableRunway(usableRunway.orElse(null))
        .highEndLandingDistance(highEndLandingDistance.orElse(null))
        .highEndRunwayDistance(highEndRunwayDistance.orElse(null))
        .highEndTakeOffDistance(highEndTakeOffDistance.orElse(null))
        .highEndAccelerateStopDistance(highEndAccelerateStopDistance.orElse(null))
        .lowEndLandingDistance(lowEndLandingDistance.orElse(null))
        .lowEndRunwayDistance(lowEndRunwayDistance.orElse(null))
        .lowEndTakeOffDistance(lowEndTakeOffDistance.orElse(null))
        .lowEndAccelerateStopDistance(lowEndAccelerateStopDistance.orElse(null))
        .cycleDate(cycleDate)
        .coordinatePrecision(coordinatePrecision.orElse(null))
        .build();

    return Optional.of(dafifRunway);
  }

}
