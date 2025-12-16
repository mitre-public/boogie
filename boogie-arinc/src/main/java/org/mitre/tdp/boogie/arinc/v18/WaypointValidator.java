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

public final class WaypointValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(WaypointValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public WaypointValidator() {
    this((arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public WaypointValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "recordType", missingFieldConsumer)
        && containsParsedField(arincRecord, "waypointIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "waypointIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "latitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "longitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "lastUpdateCycle", missingFieldConsumer);
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> enrouteSubSectionCode = arincRecord.optionalField("enrouteSubSectionCode");
    Optional<String> terminalSubSectionCode = arincRecord.optionalField("terminalSubSectionCode");

    return (sectionCode.filter(SectionCode.P::equals).isPresent() && terminalSubSectionCode.filter("C"::equals).isPresent())
        || (sectionCode.filter(SectionCode.E::equals).isPresent() && enrouteSubSectionCode.filter("A"::equals).isPresent())
        || (sectionCode.filter(SectionCode.H::equals).isPresent() && terminalSubSectionCode.filter("C"::equals).isPresent());
  }
}
