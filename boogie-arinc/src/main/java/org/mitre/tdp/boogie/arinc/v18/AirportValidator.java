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

/**
 * The ARINC spec isn't extremely specific about the required content of records (barring some of the procedure leg types) - this
 * class serves as a proxy for us (consumers of navigation information) to put expectations on what the content is of the records
 * we want to return and use in standard processing.
 */
public final class AirportValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(AirportValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public AirportValidator() {
    this((arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public AirportValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "latitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "longitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "lastUpdateCycle", missingFieldConsumer);
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter("A"::equals).isPresent();
  }
}
