package org.mitre.boogie.xml.v23_5.convert;

import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.nonNullField;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_5.generated.ProcedureRoute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class ArincTransitionValidator implements Predicate<ProcedureRoute> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincTransitionValidator.class);

  static final ArincTransitionValidator INSTANCE = new ArincTransitionValidator();

  private final BiConsumer<ProcedureRoute, String> missingFieldConsumer;

  ArincTransitionValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in ProcedureRoute {}.", field, record.getIdentifier()));
  }

  ArincTransitionValidator(BiConsumer<ProcedureRoute, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(ProcedureRoute route) {
    return nonNullField(route, "identifier", route.getIdentifier(), missingFieldConsumer)
        && legsThere(route);
  }

  private boolean legsThere(ProcedureRoute route) {
    if (route.getProcedureLeg().isEmpty()) {
      missingFieldConsumer.accept(route, "procedureLeg");
      return false;
    }
    return true;
  }
}
