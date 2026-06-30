package org.mitre.boogie.xml.v23_5.convert;

import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.validatePointFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_5.generated.AirportGate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincAirportGateValidator implements Predicate<AirportGate> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincAirportGateValidator.class);

  static final ArincAirportGateValidator INSTANCE = new ArincAirportGateValidator();

  private final BiConsumer<AirportGate, String> missingFieldConsumer;

  ArincAirportGateValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in AirportGate {}.", field, record.getIdentifier()));
  }

  ArincAirportGateValidator(BiConsumer<AirportGate, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(AirportGate gate) {
    return validatePointFields(gate, missingFieldConsumer);
  }
}
