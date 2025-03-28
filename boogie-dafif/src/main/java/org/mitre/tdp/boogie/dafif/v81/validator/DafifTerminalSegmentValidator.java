package org.mitre.tdp.boogie.dafif.v81.validator;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.utils.ValidationHelper.containsParsedField;

public class DafifTerminalSegmentValidator implements Predicate<DafifRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(DafifTerminalSegmentValidator.class);

  private final BiConsumer<DafifRecord, String> missingFieldConsumer;

  public DafifTerminalSegmentValidator() {
    this((dafifRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, dafifRecord.rawRecord()));
  }

  public DafifTerminalSegmentValidator(BiConsumer<DafifRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(DafifRecord dafifRecord) {
    return dafifRecord.recordType().equals(DafifRecordType.TRM_SEG)
        && containsParsedField(dafifRecord, "airportIdentification", missingFieldConsumer)
        && containsParsedField(dafifRecord, "terminalProcedureType", missingFieldConsumer)
        && containsParsedField(dafifRecord, "terminalIdentifier", missingFieldConsumer)
        && containsParsedField(dafifRecord, "terminalSequenceNumber", missingFieldConsumer)
        && containsParsedField(dafifRecord, "terminalApproachType", missingFieldConsumer)
        && containsParsedField(dafifRecord, "transitionIdentifier", missingFieldConsumer)
        && containsParsedField(dafifRecord, "icaoCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "trackDescriptionCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "cycleDate", missingFieldConsumer);
  }
}
