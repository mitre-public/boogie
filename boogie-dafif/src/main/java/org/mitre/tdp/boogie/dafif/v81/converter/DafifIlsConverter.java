package org.mitre.tdp.boogie.dafif.v81.converter;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.model.DafifIls;

import java.util.Optional;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;

public class DafifIlsConverter implements Function<DafifRecord, Optional<DafifIls>> {

  @Override
  public Optional<DafifIls> apply(DafifRecord dafifRecord) {
    requireNonNull(dafifRecord, "Cannot convert null DafifRecord.");

    return Optional.of(new DafifIls.Builder()
        .airportIdentification(dafifRecord.requiredField("airportIdentification"))
        .runwayIdentifier(dafifRecord.requiredField("runwayIdentifier"))
        .componentType(dafifRecord.requiredField("componentType"))
        .collocation(dafifRecord.<String>optionalField("collocation").orElse(null))
        .name(dafifRecord.<String>optionalField("name").orElse(null))
        .navaidFrequency(dafifRecord.<String>optionalField("navaidFrequency").orElse(null))
        .navaidChannel(dafifRecord.<String>optionalField("navaidChannel").orElse(null))
        .ilsGlideSlopeAngle(dafifRecord.<Double>optionalField("ilsGlideSlopeAngle").orElse(null))
        .localizerOrGlideSlopeLocation(dafifRecord.<String>optionalField("localizerOrGlideSlopeLocation").orElse(null))
        .locatorOrMarkerLocation(dafifRecord.<String>optionalField("locatorOrMarkerLocation").orElse(null))
        .ilsNavaidElevation(dafifRecord.requiredField("ilsNavaidElevation"))
        .localHorizontalDatum(dafifRecord.<String>optionalField("localHorizontalDatum").orElse(null))
        .geodeticDatum(dafifRecord.requiredField("geodeticDatum"))
        .ilsMlsCategory(dafifRecord.<String>optionalField("ilsMlsCategory").orElse(null))
        .geodeticLatitude(dafifRecord.<String>optionalField("geodeticLatitude").orElse(null))
        .degreesLatitude(dafifRecord.<Double>optionalField("degreesLatitude").orElse(null))
        .geodeticLongitude(dafifRecord.<String>optionalField("geodeticLongitude").orElse(null))
        .degreesLongitude(dafifRecord.<Double>optionalField("degreesLongitude").orElse(null))
        .ilsNavaidIdentifier(dafifRecord.<String>optionalField("ilsNavaidIdentifier").orElse(null))
        .navaidType(dafifRecord.<Integer>optionalField("navaidType").orElse(null))
        .countryCode(dafifRecord.<String>optionalField("countryCode").orElse(null))
        .navaidKeyCode(dafifRecord.<Integer>optionalField("navaidKeyCode").orElse(null))
        .magneticVariation(dafifRecord.requiredField("magneticVariation"))
        .ilsSlaveVariation(dafifRecord.<String>optionalField("ilsSlaveVariation").orElse(null))
        .ilsBearingCourse(dafifRecord.<String>optionalField("ilsBearingCourse").orElse(null))
        .localizerWidth(dafifRecord.<Double>optionalField("localizerWidth").orElse(null))
        .thresholdCrossingHeight(dafifRecord.<Integer>optionalField("thresholdCrossingHeight").orElse(null))
        .ilsDmeBias(dafifRecord.<Double>optionalField("ilsDmeBias").orElse(null))
        .cycleDate(dafifRecord.requiredField("cycleDate"))
        .mlsDmePrecisionNonPrecision(dafifRecord.<String>optionalField("mlsDmePrecisionNonPrecision").orElse(null))
        .coordinatePrecision(dafifRecord.<Integer>optionalField("coordinatePrecision").orElse(null))
        .build());
  }
}
