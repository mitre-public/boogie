package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.validateRecordFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.PortCommunication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincAirportCommunicationsValidator implements Predicate<PortCommunication> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincAirportCommunicationsValidator.class);

  static final ArincAirportCommunicationsValidator INSTANCE = new ArincAirportCommunicationsValidator();

  private final BiConsumer<PortCommunication, String> missingFieldConsumer;

  ArincAirportCommunicationsValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in PortCommunication.", field));
  }

  ArincAirportCommunicationsValidator(BiConsumer<PortCommunication, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(PortCommunication comm) {
    return validateRecordFields(comm, missingFieldConsumer);
  }
}
