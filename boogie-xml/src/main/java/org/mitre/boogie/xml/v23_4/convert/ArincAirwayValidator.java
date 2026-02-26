package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.nonNullField;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.Airway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ArincAirwayValidator implements Predicate<Airway> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincAirwayValidator.class);

  static final ArincAirwayValidator INSTANCE = new ArincAirwayValidator();

  private final BiConsumer<Airway, String> missingFieldConsumer;

  ArincAirwayValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Airway {}.", field, record.getIdentifier()));
  }

  ArincAirwayValidator(BiConsumer<Airway, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Airway airway) {
    return nonNullField(airway, "identifier", airway.getIdentifier(), missingFieldConsumer)
        && nonNullField(airway, "airwayRouteType", airway.getAirwayRouteType(), missingFieldConsumer)
        && legsThere(airway);
  }

  private boolean legsThere(Airway airway) {
    if (airway.getAirwayLeg().isEmpty()) {
      missingFieldConsumer.accept(airway, "airwayLeg");
      return false;
    }
    return true;
  }
}
