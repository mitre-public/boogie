package org.mitre.tdp.boogie.arinc.v21;

import static org.mitre.tdp.boogie.arinc.ValidationHelper.containsParsedField;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HelipadValidator implements Predicate<ArincRecord> {
  private static final Logger log = LoggerFactory.getLogger(HelipadValidator.class);
  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public HelipadValidator() {
    this((arincRecord, field) -> log.debug("Missing reuqired field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public HelipadValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && arincRecord.containsParsedField("customerAreaCode")
        && arincRecord.containsParsedField("recordType")
        && arincRecord.containsParsedField("airportOrHeliportIdentifier")
        && arincRecord.containsParsedField("icaoCode")
        && arincRecord.containsParsedField("helipadIdentifier")
        && arincRecord.containsParsedField("padShape")
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "cycle", missingFieldConsumer);
  }

  /**
   * @param arincRecord that is secretly a helipad
   * @return if it's a valid combo for a helipad
   */
  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");
    return sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter("H"::equals).isPresent()
        || sectionCode.filter(SectionCode.H::equals).isPresent() && subSectionCode.filter("H"::equals).isPresent();
  }
}
