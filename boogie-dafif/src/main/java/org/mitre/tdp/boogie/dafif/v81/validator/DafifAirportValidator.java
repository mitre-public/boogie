package org.mitre.tdp.boogie.dafif.v81.validator;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.utils.ValidationHelper.containsParsedField;

public final class DafifAirportValidator implements Predicate<DafifRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(DafifAirportValidator.class);

  private final BiConsumer<DafifRecord, String> missingFieldConsumer;

  public DafifAirportValidator() {
    this((dafifRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, dafifRecord.rawRecord()));
  }

  public DafifAirportValidator(BiConsumer<DafifRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(DafifRecord dafifRecord) {
    return dafifRecord.recordType().equals(DafifRecordType.ARPT)
        && containsParsedField(dafifRecord, "airportIdentification", missingFieldConsumer)
        && containsParsedField(dafifRecord, "name", missingFieldConsumer)
        && containsParsedField(dafifRecord, "icaoCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "geodeticDatum", missingFieldConsumer)
        && containsParsedField(dafifRecord, "elevation", missingFieldConsumer)
        && containsParsedField(dafifRecord, "airportType", missingFieldConsumer)
        && containsParsedField(dafifRecord, "magneticVariation", missingFieldConsumer)
        && containsParsedField(dafifRecord, "wAC", missingFieldConsumer)
        && containsParsedField(dafifRecord, "primaryOperatingAgency", missingFieldConsumer)
        && containsParsedField(dafifRecord, "cycleDate", missingFieldConsumer);
  }

}