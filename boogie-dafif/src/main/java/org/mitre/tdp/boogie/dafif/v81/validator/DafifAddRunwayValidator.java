package org.mitre.tdp.boogie.dafif.v81.validator;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.utils.ValidationHelper.containsParsedField;

public final class DafifAddRunwayValidator implements Predicate<DafifRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(DafifAddRunwayValidator.class);

  private final BiConsumer<DafifRecord, String> missingFieldConsumer;

  public DafifAddRunwayValidator() {
    this((dafifRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, dafifRecord.rawRecord()));
  }

  public DafifAddRunwayValidator(BiConsumer<DafifRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(DafifRecord dafifRecord) {
    return dafifRecord.recordType().equals(DafifRecordType.ADD_RWY)
        && containsParsedField(dafifRecord, "airportIdentification", missingFieldConsumer)
        && containsParsedField(dafifRecord, "highEndRunwayIdentifier", missingFieldConsumer)
        && containsParsedField(dafifRecord, "lowEndRunwayIdentifier", missingFieldConsumer)
        && containsParsedField(dafifRecord, "icaoCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "highEndOverrunSurface", missingFieldConsumer)
        && containsParsedField(dafifRecord, "lowEndOverrunSurface", missingFieldConsumer)
        && containsParsedField(dafifRecord, "cycleDate", missingFieldConsumer);
  }
}
