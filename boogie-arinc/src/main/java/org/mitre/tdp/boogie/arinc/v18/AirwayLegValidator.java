package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.arinc.ValidationHelper.containsParsedField;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Codified expectations on the field-level content of {@link ArincRecord}s which can be converted to {@link ArincAirwayLeg}s.
 */
public final class AirwayLegValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(AirwayLegValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public AirwayLegValidator() {
    this((arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public AirwayLegValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "recordType", missingFieldConsumer)
        && containsParsedField(arincRecord, "customerAreaCode", missingFieldConsumer)
        && containsParsedField(arincRecord, "routeIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "sequenceNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "fixIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "fixIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "fixSectionCode", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "lastUpdateCycle", missingFieldConsumer);
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return sectionCode.filter(SectionCode.E::equals).isPresent() && subSectionCode.filter("R"::equals).isPresent();
  }
}
