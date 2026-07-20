package org.mitre.boogie.xml.v23_5.convert;

import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.nonNullField;
import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.validateRecordFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_5.generated.Taa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ArincTaaValidator implements Predicate<Taa> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincTaaValidator.class);

  static final ArincTaaValidator INSTANCE = new ArincTaaValidator();

  private final BiConsumer<Taa, String> missingFieldConsumer;

  ArincTaaValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Taa {}.", field, record.getReferenceId()));
  }

  ArincTaaValidator(BiConsumer<Taa, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Taa taa) {
    return validateRecordFields(taa, missingFieldConsumer)
        && nonNullField(taa, "taaFixPositionIndicator", taa.getTaaFixPositionIndicator(), missingFieldConsumer)
        && sectorDetails(taa);
  }

  private boolean sectorDetails(Taa taa) {
    if (taa.getSectorTaaDetails().isEmpty()) {
      missingFieldConsumer.accept(taa, "sectorTaaDetails");
      return false;
    }
    return true;
  }
}
