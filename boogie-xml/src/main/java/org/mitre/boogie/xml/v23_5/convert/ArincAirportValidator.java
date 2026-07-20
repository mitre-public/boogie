package org.mitre.boogie.xml.v23_5.convert;

import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.nonNullField;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_5.generated.Airport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincAirportValidator implements Predicate<Airport> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincAirportValidator.class);

  private final BiConsumer<Airport, String> missingFieldConsumer;

  ArincAirportValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Airport {}.", field, record.getName()));
  }

  ArincAirportValidator(BiConsumer<Airport, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Airport airport) {
    return nonNullField(airport, "name", airport.getName(), missingFieldConsumer)
        && nonNullField(airport, "location", airport.getLocation(), missingFieldConsumer);
  }
}
