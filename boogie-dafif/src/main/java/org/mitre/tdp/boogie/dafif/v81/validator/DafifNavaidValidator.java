package org.mitre.tdp.boogie.dafif.v81.validator;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.utils.ValidationHelper.containsParsedField;

public class DafifNavaidValidator implements Predicate<DafifRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(DafifNavaidValidator.class);

  private final BiConsumer<DafifRecord, String> missingFieldConsumer;

  public DafifNavaidValidator() {
    this((dafifRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, dafifRecord.rawRecord()));
  }

  public DafifNavaidValidator(BiConsumer<DafifRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }


  @Override
  public boolean test(DafifRecord dafifRecord) {
    return dafifRecord.recordType().equals(DafifRecordType.NAV)
        && containsParsedField(dafifRecord, "navaidIdentifier", missingFieldConsumer)
        && containsParsedField(dafifRecord, "navaidType", missingFieldConsumer)
        && containsParsedField(dafifRecord, "countryCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "navaidKeyCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "icaoRegion", missingFieldConsumer)
        && containsParsedField(dafifRecord, "wAC", missingFieldConsumer)
        && containsParsedField(dafifRecord, "navaidUsageCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "navaidRadioClassCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "navaidPower", missingFieldConsumer)
        && containsParsedField(dafifRecord, "navaidRange", missingFieldConsumer)
        && containsParsedField(dafifRecord, "geodeticDatum", missingFieldConsumer)
        && containsParsedField(dafifRecord, "magneticVariation", missingFieldConsumer)
        && containsParsedField(dafifRecord, "ilsNavaidElevation", missingFieldConsumer)
        && containsParsedField(dafifRecord, "dmeElevation", missingFieldConsumer)
        && containsParsedField(dafifRecord, "navaidStatus", missingFieldConsumer)
        && containsParsedField(dafifRecord, "cycleDate", missingFieldConsumer);
  }
}
