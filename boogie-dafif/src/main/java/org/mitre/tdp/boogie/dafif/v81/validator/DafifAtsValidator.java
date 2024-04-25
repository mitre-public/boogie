package org.mitre.tdp.boogie.dafif.v81.validator;

import org.mitre.tdp.boogie.dafif.DafifRecord;
import org.mitre.tdp.boogie.dafif.DafifRecordType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.dafif.utils.ValidationHelper.containsParsedField;

public class DafifAtsValidator implements Predicate<DafifRecord> {

  private static final Logger LOG = LoggerFactory.getLogger(DafifAtsValidator.class);

  private final BiConsumer<DafifRecord, String> missingFieldConsumer;

  public DafifAtsValidator() {
    this((dafifRecord, field) -> LOG.debug("Missing required field {} in record {}.", field, dafifRecord.rawRecord()));
  }

  public DafifAtsValidator(BiConsumer<DafifRecord, String> missingFieldConsumer) {
    this.missingFieldConsumer = requireNonNull(missingFieldConsumer);
  }

  @Override
  public boolean test(DafifRecord dafifRecord) {
    return dafifRecord.recordType().equals(DafifRecordType.ATS)
        && containsParsedField(dafifRecord, "atsIdentifier", missingFieldConsumer)
        && containsParsedField(dafifRecord, "atsRouteSequenceNumber", missingFieldConsumer)
        && containsParsedField(dafifRecord, "atsRouteDirection", missingFieldConsumer)
        && containsParsedField(dafifRecord, "atsRouteType", missingFieldConsumer)
        && containsParsedField(dafifRecord, "icaoCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "frequencyClass", missingFieldConsumer)
        && containsParsedField(dafifRecord, "level", missingFieldConsumer)
        && containsParsedField(dafifRecord, "atsRouteStatus", missingFieldConsumer)
        && containsParsedField(dafifRecord, "waypoint1IcaoCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "waypoint1WaypointIdentifierWptIdent", missingFieldConsumer)
        && containsParsedField(dafifRecord, "waypoint1CountryCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "waypoint1AtsWaypointDescriptionCode1", missingFieldConsumer)
        && containsParsedField(dafifRecord, "waypoint2IcaoCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "waypoint2WaypointIdentifierWptIdent", missingFieldConsumer)
        && containsParsedField(dafifRecord, "waypoint2CountryCode", missingFieldConsumer)
        && containsParsedField(dafifRecord, "waypoint2AtsWaypointDescriptionCode1", missingFieldConsumer)
        && containsParsedField(dafifRecord, "cycleDate", missingFieldConsumer);
  }
}
