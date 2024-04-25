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

    String airportIdentification = dafifRecord.requiredField("airportIdentification");
    String runwayIdentifier = dafifRecord.requiredField("runwayIdentifier");
    String componentType = dafifRecord.requiredField("componentType");
    Optional<String> collocation = dafifRecord.optionalField("collocation");
    Optional<String> name = dafifRecord.optionalField("name");
    Optional<String> navaidFrequency = dafifRecord.optionalField("navaidFrequency");
    Optional<String> navaidChannel = dafifRecord.optionalField("navaidChannel");
    Optional<Double> ilsGlideSlopeAngle = dafifRecord.optionalField("ilsGlideSlopeAngle");
    Optional<String> localizerOrGlideSlopeLocation = dafifRecord.optionalField("localizerOrGlideSlopeLocation");
    Optional<String> locatorOrMarkerLocation = dafifRecord.optionalField("locatorOrMarkerLocation");
    String ilsNavaidElevation = dafifRecord.requiredField("ilsNavaidElevation");
    Optional<String> localHorizontalDatum = dafifRecord.optionalField("localHorizontalDatum");
    String geodeticDatum = dafifRecord.requiredField("geodeticDatum");
    Optional<String> ilsMlsCategory = dafifRecord.optionalField("ilsMlsCategory");
    Optional<String> geodeticLatitude = dafifRecord.optionalField("geodeticLatitude");
    Optional<Double> degreesLatitude = dafifRecord.optionalField("degreesLatitude");
    Optional<String> geodeticLongitude = dafifRecord.optionalField("geodeticLongitude");
    Optional<Double> degreesLongitude = dafifRecord.optionalField("degreesLongitude");
    Optional<String> ilsNavaidIdentifier = dafifRecord.optionalField("ilsNavaidIdentifier");
    Optional<Integer> navaidType = dafifRecord.optionalField("navaidType");
    Optional<String> countryCode = dafifRecord.optionalField("countryCode");
    Optional<Integer> navaidKeyCode = dafifRecord.optionalField("navaidKeyCode");
    String magneticVariation = dafifRecord.requiredField("magneticVariation");
    Optional<String> ilsSlaveVariation = dafifRecord.optionalField("ilsSlaveVariation");
    Optional<String> ilsBearingCourse = dafifRecord.optionalField("ilsBearingCourse");
    Optional<Double> localizerWidth = dafifRecord.optionalField("localizerWidth");
    Optional<Integer> thresholdCrossingHeight = dafifRecord.optionalField("thresholdCrossingHeight");
    Optional<Double> ilsDmeBias = dafifRecord.optionalField("ilsDmeBias");
    Integer cycleDate = dafifRecord.requiredField("cycleDate");
    Optional<String> mlsDmePrecisionNonPrecision = dafifRecord.optionalField("mlsDmePrecisionNonPrecision");
    Optional<Integer> coordinatePrecision = dafifRecord.optionalField("coordinatePrecision");

    DafifIls dafifIls = new DafifIls.Builder()
        .airportIdentification(airportIdentification)
        .runwayIdentifier(runwayIdentifier)
        .componentType(componentType)
        .collocation(collocation.orElse(null))
        .name(name.orElse(null))
        .navaidFrequency(navaidFrequency.orElse(null))
        .navaidChannel(navaidChannel.orElse(null))
        .ilsGlideSlopeAngle(ilsGlideSlopeAngle.orElse(null))
        .localizerOrGlideSlopeLocation(localizerOrGlideSlopeLocation.orElse(null))
        .locatorOrMarkerLocation(locatorOrMarkerLocation.orElse(null))
        .ilsNavaidElevation(ilsNavaidElevation)
        .localHorizontalDatum(localHorizontalDatum.orElse(null))
        .geodeticDatum(geodeticDatum)
        .ilsMlsCategory(ilsMlsCategory.orElse(null))
        .geodeticLatitude(geodeticLatitude.orElse(null))
        .degreesLatitude(degreesLatitude.orElse(null))
        .geodeticLongitude(geodeticLongitude.orElse(null))
        .degreesLongitude(degreesLongitude.orElse(null))
        .ilsNavaidIdentifier(ilsNavaidIdentifier.orElse(null))
        .navaidType(navaidType.orElse(null))
        .countryCode(countryCode.orElse(null))
        .navaidKeyCode(navaidKeyCode.orElse(null))
        .magneticVariation(magneticVariation)
        .ilsSlaveVariation(ilsSlaveVariation.orElse(null))
        .ilsBearingCourse(ilsBearingCourse.orElse(null))
        .localizerWidth(localizerWidth.orElse(null))
        .thresholdCrossingHeight(thresholdCrossingHeight.orElse(null))
        .ilsDmeBias(ilsDmeBias.orElse(null))
        .cycleDate(cycleDate)
        .mlsDmePrecisionNonPrecision(mlsDmePrecisionNonPrecision.orElse(null))
        .coordinatePrecision(coordinatePrecision.orElse(null))
        .build();

    return Optional.of(dafifIls);
  }
}
