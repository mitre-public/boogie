package org.mitre.tdp.boogie.dafif.v81.validator;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.utils.ValidationHelper.containsParsedField;

public class DafifIlsValidator implements Predicate<DafifRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(DafifIlsValidator.class);

  private final BiConsumer<DafifRecord, String> missingFieldConsumer;

  public DafifIlsValidator() {
    this((dafifRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, dafifRecord.rawRecord()));
  }

  public DafifIlsValidator(BiConsumer<DafifRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(DafifRecord dafifRecord) {
    return dafifRecord.recordType().equals(DafifRecordType.ILS)
        && containsParsedField(dafifRecord, "airportIdentification", missingFieldConsumer)
        && containsParsedField(dafifRecord, "runwayIdentifier", missingFieldConsumer)
        && containsParsedField(dafifRecord, "componentType", missingFieldConsumer)
        && containsParsedField(dafifRecord, "ilsNavaidElevation", missingFieldConsumer)
        && containsParsedField(dafifRecord, "geodeticDatum", missingFieldConsumer)
        && containsParsedField(dafifRecord, "magneticVariation", missingFieldConsumer)
        && containsParsedField(dafifRecord, "cycleDate", missingFieldConsumer);
  }
}
