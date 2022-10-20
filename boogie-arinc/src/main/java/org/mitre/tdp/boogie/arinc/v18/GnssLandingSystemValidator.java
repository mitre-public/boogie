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

public class GnssLandingSystemValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(GnssLandingSystemValidator.class);

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;

  public GnssLandingSystemValidator() {
    this((arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord()));
  }

  public GnssLandingSystemValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "glsRefPathIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "latitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "longitude", missingFieldConsumer)
        && containsParsedField(arincRecord, "magneticVariation", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "lastUpdatedCycle", missingFieldConsumer)
        && containsParsedField(arincRecord, "glsChannel", missingFieldConsumer)
        && containsParsedField(arincRecord, "runwayIdentifier", missingFieldConsumer);
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");
    return sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter("T"::equals).isPresent();
  }
}
