package org.mitre.boogie.xml.v23_5.convert;

import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.nonNullField;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_5.generated.HoldingPattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ArincHoldingPatternValidator implements Predicate<HoldingPattern> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincHoldingPatternValidator.class);

  static final ArincHoldingPatternValidator INSTANCE = new ArincHoldingPatternValidator();

  private final BiConsumer<HoldingPattern, String> missingFieldConsumer;

  ArincHoldingPatternValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in HoldingPattern {}.", field, record.getFixIdentifier()));
  }

  ArincHoldingPatternValidator(BiConsumer<HoldingPattern, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(HoldingPattern holdingPattern) {
    return nonNullField(holdingPattern, "recordType", holdingPattern.getRecordType(), missingFieldConsumer);
  }
}
