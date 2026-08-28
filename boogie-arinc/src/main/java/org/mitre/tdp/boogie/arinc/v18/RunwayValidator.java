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

public final class RunwayValidator implements Predicate<ArincRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(RunwayValidator.class);
  private static final BiConsumer<ArincRecord, String> DEBUG_MISSING_FIELD =
      (arincRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, arincRecord.rawRecord());
  private static final BiConsumer<ArincRecord, String> TRACE_MISSING_COORDINATE =
      (arincRecord, field) -> LOG.trace("Missing required field {} in record {}.", field, arincRecord.rawRecord());

  private final BiConsumer<ArincRecord, String> missingFieldConsumer;
  /**
   * Missing runways is a 'feature' from our providers so lets not clog logs with it.
   */
  private final BiConsumer<ArincRecord, String> missingCoordinateConsumer;

  public RunwayValidator() {
    this(DEBUG_MISSING_FIELD, TRACE_MISSING_COORDINATE);
  }

  public RunwayValidator(BiConsumer<ArincRecord, String> missingFieldConsumer) {
    this(missingFieldConsumer, missingFieldConsumer);
  }

  private RunwayValidator(BiConsumer<ArincRecord, String> missingFieldConsumer, BiConsumer<ArincRecord, String> missingCoordinateConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
    this.missingCoordinateConsumer = requireNonNull(missingCoordinateConsumer);
  }

  @Override
  public boolean test(ArincRecord arincRecord) {
    return isCorrectSectionSubSection(arincRecord)
        && containsParsedField(arincRecord, "recordType", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "airportIcaoRegion", missingFieldConsumer)
        && containsParsedField(arincRecord, "runwayIdentifier", missingFieldConsumer)
        && containsParsedField(arincRecord, "latitude", missingCoordinateConsumer)
        && containsParsedField(arincRecord, "longitude", missingCoordinateConsumer)
        && containsParsedField(arincRecord, "runwayMagneticBearing", missingFieldConsumer)
        && containsParsedField(arincRecord, "fileRecordNumber", missingFieldConsumer)
        && containsParsedField(arincRecord, "lastUpdateCycle", missingFieldConsumer);
  }

  boolean isCorrectSectionSubSection(ArincRecord arincRecord) {
    Optional<SectionCode> sectionCode = arincRecord.optionalField("sectionCode");
    Optional<String> subSectionCode = arincRecord.optionalField("subSectionCode");

    return sectionCode.filter(SectionCode.P::equals).isPresent() && subSectionCode.filter("G"::equals).isPresent();
  }
}
