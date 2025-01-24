package org.mitre.tdp.boogie.arinc.v18;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.arinc.ValidationHelper.containsParsedField;

public final class ControlledAirspaceValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(ControlledAirspaceValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public ControlledAirspaceValidator() {
    this((arincRecord, field) ->  LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public ControlledAirspaceValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "latitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "longitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "icaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "airspaceType", missingFieldConsumer)
        && containsParsedField(arincRecord, "airspaceCenter", missingFieldConsumer)
        && containsParsedField(arincRecord, "airspaceClassification", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "cycle", missingFieldConsumer);

  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");


    return sectionCode.filter(SectionCode.U::equals).isPresent() && subSectionCode.filter("C"::equals).isPresent();
  }
}
