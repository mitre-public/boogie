package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.nonNullField;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.Heliport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincHeliportValidator implements Predicate<Heliport> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincHeliportValidator.class);

  private final BiConsumer<Heliport, String> missingFieldConsumer;

  ArincHeliportValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Heliport {}.", field, record.getName()));
  }

  ArincHeliportValidator(BiConsumer<Heliport, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Heliport heliport) {
    return nonNullField(heliport, "name", heliport.getName(), missingFieldConsumer)
        && nonNullField(heliport, "location", heliport.getLocation(), missingFieldConsumer);
  }
}
