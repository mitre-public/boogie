package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.arinc.ValidationHelper.containsParsedField;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class LocalizerGlideSlopeValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(LocalizerGlideSlopeValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public LocalizerGlideSlopeValidator() {
    this((arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public LocalizerGlideSlopeValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && (!arincRecord.containsParsedField("supportingFacilityIdentifier") || containsCompleteSupportingFacilityInformation(arincRecord))
        && containsParsedField(arincRecord, "recordType", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "localizerIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "runwayIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "lastUpdateCycle", missingFieldConsumer)
        && checkLatLon(arincRecord);
  }

  boolean containsCompleteSupportingFacilityInformation(ArincRecord arincRecord) {
    return containsParsedField(arincRecord, "supportingFacilityIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "supportingFacilityIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "supportingFacilitySectionCode", missingFieldConsumer);
  }

  /**
   * Ensures that either the localizer-provided lat/lon exist in the record or the glideSlope lat/lon exist in the record.
   */
  boolean checkLatLon(ArincRecord arincRecord) {
    boolean localizerPresent = arincRecord.optionalField("localizerLatitude").isPresent() && arincRecord.optionalField("localizerLongitude").isPresent();
    boolean glideSlopePresent = arincRecord.optionalField("glideSlopeLatitude").isPresent() && arincRecord.optionalField("glideSlopeLongitude").isPresent();

    if (!(localizerPresent || glideSlopePresent)) {
      missingFieldConsumer.accept(arincRecord, "Latitude/Longitude");
    }

    return localizerPresent || glideSlopePresent;
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter("I"::equals).isPresent();
  }
}
