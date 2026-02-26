package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.validatePointFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.AirportHeliportLocalizerMarker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincLocalizerGlideslopeMarkerValidator implements Predicate<AirportHeliportLocalizerMarker> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincLocalizerGlideslopeMarkerValidator.class);

  static final ArincLocalizerGlideslopeMarkerValidator INSTANCE = new ArincLocalizerGlideslopeMarkerValidator();

  private final BiConsumer<AirportHeliportLocalizerMarker, String> missingFieldConsumer;

  ArincLocalizerGlideslopeMarkerValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in LocalizerMarker {}.", field, record.getIdentifier()));
  }

  ArincLocalizerGlideslopeMarkerValidator(BiConsumer<AirportHeliportLocalizerMarker, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(AirportHeliportLocalizerMarker marker) {
    return validatePointFields(marker, missingFieldConsumer);
  }
}
