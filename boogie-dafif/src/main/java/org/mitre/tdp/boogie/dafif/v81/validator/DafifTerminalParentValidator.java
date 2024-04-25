package org.mitre.tdp.boogie.dafif.v81.validator;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.utils.ValidationHelper.containsParsedField;

public class DafifTerminalParentValidator implements Predicate<DafifRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(DafifTerminalParentValidator.class);

  private final BiConsumer<DafifRecord, String> missingFieldConsumer;

  public DafifTerminalParentValidator() {
    this((dafifRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, dafifRecord.rawRecord()));
  }

  public DafifTerminalParentValidator(BiConsumer<DafifRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(DafifRecord dafifRecord) {
    return dafifRecord.recordType().equals(DafifRecordType.TRM_PAR)
        && containsParsedField(dafifRecord, "airportIdentification", missingFieldConsumer)
        && containsParsedField(dafifRecord, "terminalProcedureType", missingFieldConsumer)
        && containsParsedField(dafifRecord, "terminalIdentifier", missingFieldConsumer)
        && containsParsedField(dafifRecord, "icaoCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "julianDate", missingFieldConsumer)
        && containsParsedField(dafifRecord, "officeOfPrimaryResponsibility", missingFieldConsumer)
        && containsParsedField(dafifRecord, "cycleDate", missingFieldConsumer)
        && containsParsedField(dafifRecord, "levelOfService1", missingFieldConsumer)
        && containsParsedField(dafifRecord, "levelOfService2", missingFieldConsumer)
        && containsParsedField(dafifRecord, "levelOfService3", missingFieldConsumer);
  }
}
