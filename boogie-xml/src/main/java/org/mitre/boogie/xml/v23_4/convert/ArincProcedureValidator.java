package org.mitre.boogie.xml.v23_4.convert;

import static org.mitre.boogie.xml.v23_4.convert.XmlValidationHelper.nonNullField;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_4.generated.Procedure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ArincProcedureValidator implements Predicate<Procedure> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincProcedureValidator.class);

  static final ArincProcedureValidator INSTANCE = new ArincProcedureValidator();

  private final BiConsumer<Procedure, String> missingFieldConsumer;

  ArincProcedureValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Procedure {}.", field, record.getIdentifier()));
  }

  ArincProcedureValidator(BiConsumer<Procedure, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Procedure procedure) {
    return nonNullField(procedure, "identifier", procedure.getIdentifier(), missingFieldConsumer)
        && nonNullField(procedure, "referenceId", procedure.getReferenceId(), missingFieldConsumer);
  }
}
