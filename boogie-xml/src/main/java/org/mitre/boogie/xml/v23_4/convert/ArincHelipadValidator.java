package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.validatePointFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.Helipad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincHelipadValidator implements Predicate<Helipad> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincHelipadValidator.class);

  static final ArincHelipadValidator INSTANCE = new ArincHelipadValidator();

  private final BiConsumer<Helipad, String> missingFieldConsumer;

  ArincHelipadValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Helipad {}.", field, record.getIdentifier()));
  }

  ArincHelipadValidator(BiConsumer<Helipad, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Helipad helipad) {
    return validatePointFields(helipad, missingFieldConsumer);
  }
}
