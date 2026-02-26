package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.validateRecordFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.AirwayLeg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ArincAirwayLegValidator implements Predicate<AirwayLeg> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincAirwayLegValidator.class);

  static final ArincAirwayLegValidator INSTANCE = new ArincAirwayLegValidator();

  private final BiConsumer<AirwayLeg, String> missingFieldConsumer;

  ArincAirwayLegValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in AirwayLeg seq {}.", field, record.getSequenceNumber()));
  }

  ArincAirwayLegValidator(BiConsumer<AirwayLeg, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(AirwayLeg airwayLeg) {
    return validateRecordFields(airwayLeg, missingFieldConsumer);
  }
}
