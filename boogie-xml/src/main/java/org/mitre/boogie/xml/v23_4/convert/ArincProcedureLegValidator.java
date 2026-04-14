package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.nonNullField;
import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.validateRecordFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.ProcedureLeg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ArincProcedureLegValidator implements Predicate<ProcedureLeg> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincProcedureLegValidator.class);

  static final ArincProcedureLegValidator INSTANCE = new ArincProcedureLegValidator();

  private final BiConsumer<ProcedureLeg, String> missingFieldConsumer;

  ArincProcedureLegValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in ProcedureLeg seq {}.", field, record.getSequenceNumber()));
  }

  ArincProcedureLegValidator(BiConsumer<ProcedureLeg, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(ProcedureLeg leg) {
    return validateRecordFields(leg, missingFieldConsumer)
        && nonNullField(leg, "pathAndTermination", leg.getPathAndTermination(), missingFieldConsumer);
  }
}
