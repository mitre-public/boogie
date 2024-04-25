package org.mitre.tdp.boogie.dafif.v81.converter;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class DafifAddRunwayConverter implements Function<DafifRecord, Optional<DafifAddRunway>> {

  @Override
  public Optional<DafifAddRunway> apply(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord, "Cannot convert null DafifRecord.");

    String airportIdentification = dafifRecord.requiredField("airportIdentification");
    String highEndRunwayIdentifier = dafifRecord.requiredField("highEndRunwayIdentifier");
    String lowEndRunwayIdentifier = dafifRecord.requiredField("lowEndRunwayIdentifier");
    String icaoCode = dafifRecord.requiredField("icaoCode");
    Optional<String> highEndDisplacedThresholdGeodeticLatitude = dafifRecord.optionalField("highEndDisplacedThresholdGeodeticLatitude");
    Optional<Double> highEndDisplacedThresholdDegreesLatitude = dafifRecord.optionalField("highEndDisplacedThresholdDegreesLatitude");
    Optional<String> highEndDisplacedThresholdGeodeticLongitude = dafifRecord.optionalField("highEndDisplacedThresholdGeodeticLongitude");
    Optional<Double> highEndDisplacedThresholdDegreesLongitude = dafifRecord.optionalField("highEndDisplacedThresholdDegreesLongitude");
    Optional<Integer> highEndOverrunDistance = dafifRecord.optionalField("highEndOverrunDistance");
    String highEndOverrunSurface = dafifRecord.requiredField("highEndOverrunSurface");
    Optional<String> highEndOverrunGeodeticLatitude = dafifRecord.optionalField("highEndOverrunGeodeticLatitude");
    Optional<Double> highEndOverrunDegreesLatitude = dafifRecord.optionalField("highEndOverrunDegreesLatitude");
    Optional<String> highEndOverrunGeodeticLongitude = dafifRecord.optionalField("highEndOverrunGeodeticLongitude");
    Optional<Double> highEndOverrunDegreesLongitude = dafifRecord.optionalField("highEndOverrunDegreesLongitude");
    Optional<String> lowEndDisplacedThresholdGeodeticLatitude = dafifRecord.optionalField("lowEndDisplacedThresholdGeodeticLatitude");
    Optional<Double> lowEndDisplacedThresholdDegreesLatitude = dafifRecord.optionalField("lowEndDisplacedThresholdDegreesLatitude");
    Optional<String> lowEndDisplacedThresholdGeodeticLongitude = dafifRecord.optionalField("lowEndDisplacedThresholdGeodeticLongitude");
    Optional<Double> lowEndDisplacedThresholdDegreesLongitude = dafifRecord.optionalField("lowEndDisplacedThresholdDegreesLongitude");
    Optional<Integer> lowEndOverrunDistance = dafifRecord.optionalField("lowEndOverrunDistance");
    String lowEndOverrunSurface = dafifRecord.requiredField("lowEndOverrunSurface");
    Optional<String> lowEndOverrunGeodeticLatitude = dafifRecord.optionalField("lowEndOverrunGeodeticLatitude");
    Optional<Double> lowEndOverrunDegreesLatitude = dafifRecord.optionalField("lowEndOverrunDegreesLatitude");
    Optional<String> lowEndOverrunGeodeticLongitude = dafifRecord.optionalField("lowEndOverrunGeodeticLongitude");
    Optional<Double> lowEndOverrunDegreesLongitude = dafifRecord.optionalField("lowEndOverrunDegreesLongitude");
    Integer cycleDate = dafifRecord.requiredField("cycleDate");

    DafifAddRunway addRunway = new DafifAddRunway.Builder()
        .airportIdentification(airportIdentification)
        .highEndRunwayIdentifier(highEndRunwayIdentifier)
        .lowEndRunwayIdentifier(lowEndRunwayIdentifier)
        .icaoCode(icaoCode)
        .highEndDisplacedThresholdGeodeticLatitude(highEndDisplacedThresholdGeodeticLatitude.orElse(null))
        .highEndDisplacedThresholdDegreesLatitude(highEndDisplacedThresholdDegreesLatitude.orElse(null))
        .highEndDisplacedThresholdGeodeticLongitude(highEndDisplacedThresholdGeodeticLongitude.orElse(null))
        .highEndDisplacedThresholdDegreesLongitude(highEndDisplacedThresholdDegreesLongitude.orElse(null))
        .highEndOverrunDistance(highEndOverrunDistance.orElse(null))
        .highEndOverrunSurface(highEndOverrunSurface)
        .highEndOverrunGeodeticLatitude(highEndOverrunGeodeticLatitude.orElse(null))
        .highEndOverrunDegreesLatitude(highEndOverrunDegreesLatitude.orElse(null))
        .highEndOverrunGeodeticLongitude(highEndOverrunGeodeticLongitude.orElse(null))
        .highEndOverrunDegreesLongitude(highEndOverrunDegreesLongitude.orElse(null))
        .lowEndDisplacedThresholdGeodeticLatitude(lowEndDisplacedThresholdGeodeticLatitude.orElse(null))
        .lowEndDisplacedThresholdDegreesLatitude(lowEndDisplacedThresholdDegreesLatitude.orElse(null))
        .lowEndDisplacedThresholdGeodeticLongitude(lowEndDisplacedThresholdGeodeticLongitude.orElse(null))
        .lowEndDisplacedThresholdDegreesLongitude(lowEndDisplacedThresholdDegreesLongitude.orElse(null))
        .lowEndOverrunDistance(lowEndOverrunDistance.orElse(null))
        .lowEndOverrunSurface(lowEndOverrunSurface)
        .lowEndOverrunGeodeticLatitude(lowEndOverrunGeodeticLatitude.orElse(null))
        .lowEndOverrunDegreesLatitude(lowEndOverrunDegreesLatitude.orElse(null))
        .lowEndOverrunGeodeticLongitude(lowEndOverrunGeodeticLongitude.orElse(null))
        .lowEndOverrunDegreesLongitude(lowEndOverrunDegreesLongitude.orElse(null))
        .cycleDate(cycleDate)
        .build();

    return Optional.of(addRunway);
  }
}
