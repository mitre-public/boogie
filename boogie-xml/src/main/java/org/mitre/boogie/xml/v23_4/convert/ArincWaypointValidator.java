package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.validatePointFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.Waypoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincWaypointValidator implements Predicate<Waypoint> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincWaypointValidator.class);

  public static final ArincWaypointValidator INSTANCE = new ArincWaypointValidator();

  private final BiConsumer<Waypoint, String> missingFieldConsumer;

  ArincWaypointValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Waypoint {}.", field, record.getIdentifier()));
  }

  ArincWaypointValidator(BiConsumer<Waypoint, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Waypoint waypoint) {
    return validatePointFields(waypoint, missingFieldConsumer);
  }
}
