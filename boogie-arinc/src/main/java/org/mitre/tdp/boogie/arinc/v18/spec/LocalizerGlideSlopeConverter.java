package org.mitre.tdp.boogie.arinc.v18.spec;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

/**
 * Converter class for functionally transforming a {@link ArincRecord} into a {@link ArincLocalizerGlideSlope} concrete data model.
 * <br>
 * This class has some expectations on the data {@link ArincRecord} provided to it - these expectations are communicated through
 * the {@link LocalizerGlideSlopeValidator}.
 */
public final class LocalizerGlideSlopeConverter implements Function<ArincRecord, Optional<ArincLocalizerGlideSlope>> {

  private static final Predicate<ArincRecord> isInvalidRecord = new LocalizerGlideSlopeValidator().negate();

  @Override
  public Optional<ArincLocalizerGlideSlope> apply(ArincRecord arincRecord) {
    requireNonNull(arincRecord, "Converter cannot be applied to null ArincRecord");

    if (isInvalidRecord.test(arincRecord)) {
      return Optional.empty();
    }

    Optional<CustomerAreaCode> customerAreaCode = arincRecord.optionalField("customerAreaCode");
    Optional<Double> localizerFrequency = arincRecord.optionalField("localizerFrequency");
    Optional<String> ilsMlsGlsCategory = arincRecord.optionalField("ilsMlsGlsCategory");
    Optional<String> continuationRecordNumber = arincRecord.optionalField("continuationRecordNumber");
    Optional<Double> localizerLatitude = arincRecord.optionalField("localizerLatitude");
    Optional<Double> localizerLongitude = arincRecord.optionalField("localizerLongitude");
    Optional<Double> localizerBearing = arincRecord.optionalField("localizerBearing");
    Optional<Double> glideSlopeLatitude = arincRecord.optionalField("glideSlopeLatitude");
    Optional<Double> glideSlopeLongitude = arincRecord.optionalField("glideSlopeLongitude");
    Optional<Integer> localizerPosition = arincRecord.optionalField("localizerPosition");
    Optional<String> localizerPositionReference = arincRecord.optionalField("localizerPositionReference");
    Optional<Integer> glideSlopePosition = arincRecord.optionalField("glideSlopePosition");
    Optional<Double> localizerWidth = arincRecord.optionalField("localizerWidth");
    Optional<Double> glideSlopeAngle = arincRecord.optionalField("glideSlopeAngle");
    Optional<Double> stationDeclination = arincRecord.optionalField("stationDeclination");
    Optional<Integer> glideSlopeHeightAtLandingThreshold = arincRecord.optionalField("glideSlopeHeightAtLandingThreshold");
    Optional<Double> glideSlopeElevation = arincRecord.optionalField("glideSlopeElevation");
    Optional<String> supportingFacilityIdentifier = arincRecord.optionalField("supportingFacilityIdentifier");
    Optional<String> supportingFacilityIcaoRegion = arincRecord.optionalField("supportingFacilityIcaoRegion");
    Optional<SectionCode> supportingFacilitySectionCode = arincRecord.optionalField("supportingFacilitySectionCode");
    Optional<String> supportingFacilitySubSectionCode = arincRecord.optionalField("supportingFacilitySubSectionCode");

    ArincLocalizerGlideSlope localizerGlideSlope = new ArincLocalizerGlideSlope.Builder()
        .recordType(arincRecord.requiredField("recordType"))
        .customerAreaCode(customerAreaCode.orElse(null))
        .sectionCode(arincRecord.requiredField("sectionCode"))
        .airportIdentifier(arincRecord.requiredField("airportIdentifier"))
        .airportIcaoRegion(arincRecord.requiredField("airportIcaoRegion"))
        .subSectionCode(arincRecord.requiredField("subSectionCode"))
        .localizerIdentifier(arincRecord.requiredField("localizerIdentifier"))
        .localizerFrequency(localizerFrequency.orElse(null))
        .ilsMlsGlsCategory(ilsMlsGlsCategory.orElse(null))
        .continuationRecordNumber(continuationRecordNumber.orElse(null))
        .runwayIdentifier(arincRecord.requiredField("runwayIdentifier"))
        .localizerLatitude(localizerLatitude.orElse(null))
        .localizerLongitude(localizerLongitude.orElse(null))
        .localizerBearing(localizerBearing.orElse(null))
        .glideSlopeLatitude(glideSlopeLatitude.orElse(null))
        .glideSlopeLongitude(glideSlopeLongitude.orElse(null))
        .localizerPosition(localizerPosition.orElse(null))
        .localizerPositionReference(localizerPositionReference.orElse(null))
        .glideSlopePosition(glideSlopePosition.orElse(null))
        .localizerWidth(localizerWidth.orElse(null))
        .glideSlopeAngle(glideSlopeAngle.orElse(null))
        .stationDeclination(stationDeclination.orElse(null))
        .glideSlopeHeightAtLandingThreshold(glideSlopeHeightAtLandingThreshold.orElse(null))
        .glideSlopeElevation(glideSlopeElevation.orElse(null))
        .supportingFacilityIdentifier(supportingFacilityIdentifier.orElse(null))
        .supportingFacilityIcaoRegion(supportingFacilityIcaoRegion.orElse(null))
        .supportingFacilitySectionCode(supportingFacilitySectionCode.orElse(null))
        .supportingFacilitySubSectionCode(supportingFacilitySubSectionCode.orElse(null))
        .fileRecordNumber(arincRecord.requiredField("fileRecordNumber"))
        .lastUpdateCycle(arincRecord.requiredField("lastUpdateCycle"))
        .build();

    return Optional.of(localizerGlideSlope);
  }
}
