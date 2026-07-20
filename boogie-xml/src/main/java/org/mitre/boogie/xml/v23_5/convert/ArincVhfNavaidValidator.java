package org.mitre.boogie.xml.v23_5.convert;

import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.nonNullField;
import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.validatePointFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_5.generated.DmeTacan;
import org.mitre.boogie.xml.v23_5.generated.Navaid;
import org.mitre.boogie.xml.v23_5.generated.PrecisionApproachNavaid;
import org.mitre.boogie.xml.v23_5.generated.Vor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincVhfNavaidValidator implements Predicate<Navaid> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincVhfNavaidValidator.class);

  static final ArincVhfNavaidValidator INSTANCE = new ArincVhfNavaidValidator();

  private final BiConsumer<Navaid, String> missingFieldConsumer;

  ArincVhfNavaidValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Navaid {}.", field, record.getIdentifier()));
  }

  ArincVhfNavaidValidator(BiConsumer<Navaid, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Navaid navaid) {
    boolean baseValid = validatePointFields(navaid, missingFieldConsumer);
    if (navaid instanceof Vor vor) {
      return baseValid
          && nonNullField(vor, "vorFrequency", vor.getVorFrequency(), missingFieldConsumer)
          && nonNullField(vor, "navaidClass", vor.getNavaidClass(), missingFieldConsumer);
    } else if (navaid instanceof DmeTacan dmeTacan) {
      return baseValid
          && nonNullField(dmeTacan, "frequency", dmeTacan.getFrequency(), missingFieldConsumer);
    } else if (navaid instanceof PrecisionApproachNavaid) {
      return false;
    }
    return false;
  }
}
