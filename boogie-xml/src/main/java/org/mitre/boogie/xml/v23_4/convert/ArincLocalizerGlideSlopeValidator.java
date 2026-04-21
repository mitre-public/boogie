package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.nonNullField;
import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.validatePointFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.LocalizerGlideslope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincLocalizerGlideSlopeValidator implements Predicate<LocalizerGlideslope> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincLocalizerGlideSlopeValidator.class);

  static final ArincLocalizerGlideSlopeValidator INSTANCE = new ArincLocalizerGlideSlopeValidator();

  private final BiConsumer<LocalizerGlideslope, String> missingFieldConsumer;

  ArincLocalizerGlideSlopeValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in LocalizerGlideslope {}.", field, record.getIdentifier()));
  }

  ArincLocalizerGlideSlopeValidator(BiConsumer<LocalizerGlideslope, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(LocalizerGlideslope lg) {
    return validatePointFields(lg, missingFieldConsumer)
        && nonNullField(lg, "localizerGlideslopeFrequency", lg.getLocalizerGlideslopeFrequency(), missingFieldConsumer);
  }
}
