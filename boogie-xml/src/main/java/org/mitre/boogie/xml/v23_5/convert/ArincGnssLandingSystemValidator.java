package org.mitre.boogie.xml.v23_5.convert;

import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.validatePointFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_5.generated.Gls;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ArincGnssLandingSystemValidator implements Predicate<Gls> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincGnssLandingSystemValidator.class);

  static final ArincGnssLandingSystemValidator INSTANCE = new ArincGnssLandingSystemValidator();

  private final BiConsumer<Gls, String> missingFieldConsumer;

  ArincGnssLandingSystemValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Gls {}.", field, record.getIdentifier()));
  }

  ArincGnssLandingSystemValidator(BiConsumer<Gls, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Gls gls) {
    return validatePointFields(gls, missingFieldConsumer);
  }
}
