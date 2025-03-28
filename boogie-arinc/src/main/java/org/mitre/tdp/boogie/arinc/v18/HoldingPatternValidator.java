package org.mitre.tdp.boogie.arinc.v18;

import static org.mitre.tdp.boogie.arinc.ValidationHelper.containsParsedField;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class HoldingPatternValidator implements Predicate<ArincRecord> {
  private static final Logger LOG = LoggerFactory.getLogger(HoldingPatternValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public HoldingPatternValidator() {
    this((arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public HoldingPatternValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }


  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "fixIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "turnDirection", missingFieldConsumer)
        && containsParsedField(arincRecord, "inboundHoldingCourse", missingFieldConsumer);
  }

  public static boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");
    return sectionCode.filter(SectionCode.E::equals).isPresent() && subSectionCode.filter("P"::equals).isPresent();
  }
}
