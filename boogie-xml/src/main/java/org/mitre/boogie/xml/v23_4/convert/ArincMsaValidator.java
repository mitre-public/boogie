package org.mitre.boogie.xml.v23_4.convert;

import static java.util.Objects.nonNull;
import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.validateRecordFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.Msa;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ArincMsaValidator implements Predicate<Msa> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincMsaValidator.class);

  static final ArincMsaValidator INSTANCE = new ArincMsaValidator();

  private final BiConsumer<Msa, String> missingFieldConsumer;

  ArincMsaValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Msa {}.", field, record.getCenterFix()));
  }

  ArincMsaValidator(BiConsumer<Msa, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Msa msa) {
    return validateRecordFields(msa, missingFieldConsumer)
        && nonNull(msa.getSector())
        && sectors(msa);
  }

  private boolean sectors(Msa msa) {
    if (msa.getSector().isEmpty()) {
      missingFieldConsumer.accept(msa, "sector");
      return false;
    }
    return true;
  }
}
