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

public final class NdbNavaidValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(NdbNavaidValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public NdbNavaidValidator() {
    this((arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public NdbNavaidValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "recordType", missingFieldConsumer)
        && containsParsedField(arincRecord, "ndbIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "ndbIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "latitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "longitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "lastUpdateCycle", missingFieldConsumer);
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return (sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter("N"::equals).isPresent())
        || (sectionCode.filter(SectionCode.D::equals).isPresent() && subSectionCode.filter("B"::equals).isPresent());
  }
}
