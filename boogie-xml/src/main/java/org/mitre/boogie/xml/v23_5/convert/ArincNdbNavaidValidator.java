package org.mitre.boogie.xml.v23_5.convert;

import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.nonNullField;
import static org.mitre.boogie.xml.v23_5.convert.XmlValidationHelper.validatePointFields;

import java.util.function.BiConsumer;
import java.util.function.Predicate;

import org.mitre.boogie.xml.v23_5.generated.Ndb;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ArincNdbNavaidValidator implements Predicate<Ndb> {

  private static final Logger LOG = LoggerFactory.getLogger(ArincNdbNavaidValidator.class);

  static final ArincNdbNavaidValidator INSTANCE = new ArincNdbNavaidValidator();

  private final BiConsumer<Ndb, String> missingFieldConsumer;

  ArincNdbNavaidValidator() {
    this((record, field) -> LOG.debug("Missing required field {} in Ndb {}.", field, record.getIdentifier()));
  }

  ArincNdbNavaidValidator(BiConsumer<Ndb, String> missingFieldConsumer) {
    this.missingFieldConsumer = missingFieldConsumer;
  }

  @Override
  public boolean test(Ndb ndb) {
    return validatePointFields(ndb, missingFieldConsumer)
        && nonNullField(ndb, "ndbFrequency", ndb.getNdbFrequency(), missingFieldConsumer)
        && nonNullField(ndb, "ndbClass", ndb.getNdbClass(), missingFieldConsumer);
  }
}
