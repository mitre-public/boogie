package org.mitre.tdp.boogie.arinc.v18;

import static java.util.Objects.requireNonNull;

import java.util.function.BiConsumer;

import org.mitre.tdp.boogie.arinc.ArincRecord;

final class ValidationHelper {

  private ValidationHelper() {
    throw new IllegalStateException("Unable to instantiate static utility class");
  }

  public static boolean containsParsedField(ArincRecord record, String field, BiConsumer<ArincRecord, String> missingFieldConsumer) {
    requireNonNull(missingFieldConsumer, "The missing field consumer must be populated.");

    boolean containsParsedField = record.optionalField(field).isPresent();
    if (!containsParsedField) {
      missingFieldConsumer.accept(record, field);
    }
    return containsParsedField;
  }
}
