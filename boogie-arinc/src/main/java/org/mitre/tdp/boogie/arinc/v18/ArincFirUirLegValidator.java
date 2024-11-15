package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.arinc.v18.ValidationHelper.containsParsedField;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ArincFirUirLegValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincFirUirLegValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public ArincFirUirLegValidator() {
    this((arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public ArincFirUirLegValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "recordType", missingFieldConsumer)
        && containsParsedField(arincRecord, "customerAreaCode", missingFieldConsumer)
        && containsParsedField(arincRecord, "customerAreaCode", missingFieldConsumer)
        && containsParsedField(arincRecord, "firUirIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "firUirIndicator", missingFieldConsumer)
        && containsParsedField(arincRecord, "sequenceNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "firUirEntryReport", missingFieldConsumer)
        && containsParsedField(arincRecord, "boundaryVia", missingFieldConsumer)
        && containsParsedField(arincRecord, "firUirLatitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "firUirLongitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "cycle", missingFieldConsumer);
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return sectionCode.filter(SectionCode.U::equals).isPresent() && subSectionCode.filter("F"::equals).isPresent();
  }
}
