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

    return Optional.of(new DafifAddRunway.Builder()
        .airportIdentification(dafifRecord.requiredField("airportIdentification"))
        .highEndRunwayIdentifier(dafifRecord.requiredField("highEndRunwayIdentifier"))
        .lowEndRunwayIdentifier(dafifRecord.requiredField("lowEndRunwayIdentifier"))
        .icaoCode(dafifRecord.requiredField("icaoCode"))
        .highEndDisplacedThresholdGeodeticLatitude(dafifRecord.<String>optionalField("highEndDisplacedThresholdGeodeticLatitude").orElse(null))
        .highEndDisplacedThresholdDegreesLatitude(dafifRecord.<Double>optionalField("highEndDisplacedThresholdDegreesLatitude").orElse(null))
        .highEndDisplacedThresholdGeodeticLongitude(dafifRecord.<String>optionalField("highEndDisplacedThresholdGeodeticLongitude").orElse(null))
        .highEndDisplacedThresholdDegreesLongitude(dafifRecord.<Double>optionalField("highEndDisplacedThresholdDegreesLongitude").orElse(null))
        .highEndOverrunDistance(dafifRecord.<Integer>optionalField("highEndOverrunDistance").orElse(null))
        .highEndOverrunSurface(dafifRecord.requiredField("highEndOverrunSurface"))
        .highEndOverrunGeodeticLatitude(dafifRecord.<String>optionalField("highEndOverrunGeodeticLatitude").orElse(null))
        .highEndOverrunDegreesLatitude(dafifRecord.<Double>optionalField("highEndOverrunDegreesLatitude").orElse(null))
        .highEndOverrunGeodeticLongitude(dafifRecord.<String>optionalField("highEndOverrunGeodeticLongitude").orElse(null))
        .highEndOverrunDegreesLongitude(dafifRecord.<Double>optionalField("highEndOverrunDegreesLongitude").orElse(null))
        .lowEndDisplacedThresholdGeodeticLatitude(dafifRecord.<String>optionalField("lowEndDisplacedThresholdGeodeticLatitude").orElse(null))
        .lowEndDisplacedThresholdDegreesLatitude(dafifRecord.<Double>optionalField("lowEndDisplacedThresholdDegreesLatitude").orElse(null))
        .lowEndDisplacedThresholdGeodeticLongitude(dafifRecord.<String>optionalField("lowEndDisplacedThresholdGeodeticLongitude").orElse(null))
        .lowEndDisplacedThresholdDegreesLongitude(dafifRecord.<Double>optionalField("lowEndDisplacedThresholdDegreesLongitude").orElse(null))
        .lowEndOverrunDistance(dafifRecord.<Integer>optionalField("lowEndOverrunDistance").orElse(null))
        .lowEndOverrunSurface(dafifRecord.requiredField("lowEndOverrunSurface"))
        .lowEndOverrunGeodeticLatitude(dafifRecord.<String>optionalField("lowEndOverrunGeodeticLatitude").orElse(null))
        .lowEndOverrunDegreesLatitude(dafifRecord.<Double>optionalField("lowEndOverrunDegreesLatitude").orElse(null))
        .lowEndOverrunGeodeticLongitude(dafifRecord.<String>optionalField("lowEndOverrunGeodeticLongitude").orElse(null))
        .lowEndOverrunDegreesLongitude(dafifRecord.<Double>optionalField("lowEndOverrunDegreesLongitude").orElse(null))
        .cycleDate(dafifRecord.requiredField("cycleDate"))
        .build());
  }
}
