package org.mitre.tdp.boogie.dafif.v81.validator;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.utils.ValidationHelper.containsParsedField;

public class DafifRunwayValidator implements Predicate<DafifRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(DafifRunwayValidator.class);

  private final BiConsumer<DafifRecord, String> missingFieldConsumer;

  public DafifRunwayValidator() {
    this((dafifRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, dafifRecord.rawRecord()));
  }

  public DafifRunwayValidator(BiConsumer<DafifRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(DafifRecord dafifRecord) {
    return dafifRecord.recordType().equals(DafifRecordType.RWY)
        && containsParsedField(dafifRecord, "airportIdentification", missingFieldConsumer)
        && containsParsedField(dafifRecord, "highEndIdentifier", missingFieldConsumer)
        && containsParsedField(dafifRecord, "lowEndIdentifier", missingFieldConsumer)
        && containsParsedField(dafifRecord, "length", missingFieldConsumer)
        && containsParsedField(dafifRecord, "width", missingFieldConsumer)
        && containsParsedField(dafifRecord, "highEndSlope", missingFieldConsumer)
        && containsParsedField(dafifRecord, "lowEndSlope", missingFieldConsumer)
        && containsParsedField(dafifRecord, "trueHeadingHighEnd", missingFieldConsumer)
        && containsParsedField(dafifRecord, "trueHeadingLowEnd", missingFieldConsumer)
        && containsParsedField(dafifRecord, "cycleDate", missingFieldConsumer);
  }
}