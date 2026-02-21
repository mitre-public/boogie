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

    List<Integer> highEndLighting = Stream.of(
        dafifRecord.<Integer>optionalField("highEndLightingSystem1").orElse(null),
        dafifRecord.<Integer>optionalField("highEndLightingSystem2").orElse(null),
        dafifRecord.<Integer>optionalField("highEndLightingSystem3").orElse(null),
        dafifRecord.<Integer>optionalField("highEndLightingSystem4").orElse(null),
        dafifRecord.<Integer>optionalField("highEndLightingSystem5").orElse(null),
        dafifRecord.<Integer>optionalField("highEndLightingSystem6").orElse(null),
        dafifRecord.<Integer>optionalField("highEndLightingSystem7").orElse(null),
        dafifRecord.<Integer>optionalField("highEndLightingSystem8").orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    List<Integer> lowEndLighting = Stream.of(
        dafifRecord.<Integer>optionalField("lowEndLightingSystem1").orElse(null),
        dafifRecord.<Integer>optionalField("lowEndLightingSystem2").orElse(null),
        dafifRecord.<Integer>optionalField("lowEndLightingSystem3").orElse(null),
        dafifRecord.<Integer>optionalField("lowEndLightingSystem4").orElse(null),
        dafifRecord.<Integer>optionalField("lowEndLightingSystem5").orElse(null),
        dafifRecord.<Integer>optionalField("lowEndLightingSystem6").orElse(null),
        dafifRecord.<Integer>optionalField("lowEndLightingSystem7").orElse(null),
        dafifRecord.<Integer>optionalField("lowEndLightingSystem8").orElse(null))
        .filter(Objects::nonNull)
        .collect(Collectors.toList());

    return Optional.of(new DafifRunway.Builder()
        .airportIdentification(dafifRecord.requiredField("airportIdentification"))
        .highEndIdentifier(dafifRecord.requiredField("highEndIdentifier"))
        .lowEndIdentifier(dafifRecord.requiredField("lowEndIdentifier"))
        .highEndMagneticHeading(dafifRecord.<Double>optionalField("highEndMagneticHeading").orElse(null))
        .lowEndMagneticHeading(dafifRecord.<Double>optionalField("lowEndMagneticHeading").orElse(null))
        .length(dafifRecord.requiredField("length"))
        .width(dafifRecord.requiredField("width"))
        .surface(dafifRecord.<String>optionalField("surface").orElse(null))
        .pavementClassificationNumber(dafifRecord.<String>optionalField("pavementClassificationNumber").orElse(null))
        .highEndGeodeticLatitude(dafifRecord.<String>optionalField("highEndGeodeticLatitude").orElse(null))
        .highEndDegreesLatitude(dafifRecord.<Double>optionalField("highEndDegreesLatitude").orElse(null))
        .highEndGeodeticLongitude(dafifRecord.<String>optionalField("highEndGeodeticLongitude").orElse(null))
        .highEndDegreesLongitude(dafifRecord.<Double>optionalField("highEndDegreesLongitude").orElse(null))
        .highEndElevation(dafifRecord.<String>optionalField("highEndElevation").orElse(null))
        .highEndSlope(dafifRecord.requiredField("highEndSlope"))
        .highEndTDZE(dafifRecord.<String>optionalField("highEndTDZE").orElse(null))
        .highEndDisplacedThreshold(dafifRecord.<Integer>optionalField("highEndDisplacedThreshold").orElse(null))
        .highEndDisplacedThresholdElevation(dafifRecord.<String>optionalField("highEndDisplacedThresholdElevation").orElse(null))
        .highEndLightingSystem(highEndLighting)
        .lowEndGeodeticLatitude(dafifRecord.<String>optionalField("lowEndGeodeticLatitude").orElse(null))
        .lowEndDegreesLatitude(dafifRecord.<Double>optionalField("lowEndDegreesLatitude").orElse(null))
        .lowEndGeodeticLongitude(dafifRecord.<String>optionalField("lowEndGeodeticLongitude").orElse(null))
        .lowEndDegreesLongitude(dafifRecord.<Double>optionalField("lowEndDegreesLongitude").orElse(null))
        .lowEndElevation(dafifRecord.<String>optionalField("lowEndElevation").orElse(null))
        .lowEndSlope(dafifRecord.requiredField("lowEndSlope"))
        .lowEndTDZE(dafifRecord.<String>optionalField("lowEndTDZE").orElse(null))
        .lowEndDisplacedThreshold(dafifRecord.<Integer>optionalField("lowEndDisplacedThreshold").orElse(null))
        .lowEndDisplacedThresholdElevation(dafifRecord.<String>optionalField("lowEndDisplacedThresholdElevation").orElse(null))
        .lowEndLightingSystem(lowEndLighting)
        .trueHeadingHighEnd(dafifRecord.requiredField("trueHeadingHighEnd"))
        .trueHeadingLowEnd(dafifRecord.requiredField("trueHeadingLowEnd"))
        .usableRunway(dafifRecord.<String>optionalField("usableRunway").orElse(null))
        .highEndLandingDistance(dafifRecord.<Integer>optionalField("highEndLandingDistance").orElse(null))
        .highEndRunwayDistance(dafifRecord.<Integer>optionalField("highEndTakeoffRunwayDistance").orElse(null))
        .highEndTakeOffDistance(dafifRecord.<Integer>optionalField("highEndTakeOffDistance").orElse(null))
        .highEndAccelerateStopDistance(dafifRecord.<Integer>optionalField("highEndAccelerateStopDistance").orElse(null))
        .lowEndLandingDistance(dafifRecord.<Integer>optionalField("lowEndLandingDistance").orElse(null))
        .lowEndRunwayDistance(dafifRecord.<Integer>optionalField("lowEndTakeoffRunwayDistance").orElse(null))
        .lowEndTakeOffDistance(dafifRecord.<Integer>optionalField("lowEndTakeOffDistance").orElse(null))
        .lowEndAccelerateStopDistance(dafifRecord.<Integer>optionalField("lowEndAccelerateStopDistance").orElse(null))
        .cycleDate(dafifRecord.requiredField("cycleDate"))
        .coordinatePrecision(dafifRecord.<Integer>optionalField("coordinatePrecision").orElse(null))
        .build());
  }

}
