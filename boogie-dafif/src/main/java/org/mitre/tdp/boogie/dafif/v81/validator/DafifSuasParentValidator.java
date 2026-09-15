package org.mitre.tdp.boogie.dafif.v81.validator;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.utils.ValidationHelper.containsParsedField;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DafifSuasParentValidator implements Predicate<DafifRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(DafifSuasParentValidator.class);

  private final BiConsumer<DafifRecord, String> missingFieldConsumer;

  public DafifSuasParentValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in record {}.", field, record.rawRecord()));
  }

  public DafifSuasParentValidator(BiConsumer<DafifRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(DafifRecord record) {
    return record.recordType().equals(DafifRecordType.SUAS_PAR)
        && containsParsedField(record, "suasIdentification", missingFieldConsumer)
        && containsParsedField(record, "specialUseAirspaceType", missingFieldConsumer)
        && containsParsedField(record, "icaoCode", missingFieldConsumer)
        && containsParsedField(record, "geodeticDatum", missingFieldConsumer)
        && containsParsedField(record, "level", missingFieldConsumer)
        && containsParsedField(record, "upperAltitude", missingFieldConsumer)
        && containsParsedField(record, "lowerAltitude", missingFieldConsumer)
        && containsParsedField(record, "cycleDate", missingFieldConsumer)
        && containsParsedField(record, "effectiveDate", missingFieldConsumer);
  }
}
