package org.mitre.tdp.boogie.arinc.v18;

import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.arinc.v18.ValidationHelper.containsParsedField;

public class AirportGateValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(AirportGateValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public AirportGateValidator() {
    this((arincRecord, field) -> LOG.debug("Missing reuqired field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public AirportGateValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "gateIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "latitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "longitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "lastUpdatedCycle", missingFieldConsumer);
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");
    return sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter("B"::equals).isPresent();
  }
}