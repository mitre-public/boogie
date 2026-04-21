package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.nonNullField;
import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.validatePointFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.Runway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincRunwayValidator implements Predicate<Runway> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincRunwayValidator.class);

  static final ArincRunwayValidator INSTANCE = new ArincRunwayValidator();

  private final BiConsumer<Runway, String> missingFieldConsumer;

  ArincRunwayValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Runway {}.", field, record.getIdentifier()));
  }

  ArincRunwayValidator(BiConsumer<Runway, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Runway runway) {
    return validatePointFields(runway, missingFieldConsumer)
        && nonNullField(runway, "runwayIdentifier", runway.getRunwayIdentifier(), missingFieldConsumer);
  }
}
