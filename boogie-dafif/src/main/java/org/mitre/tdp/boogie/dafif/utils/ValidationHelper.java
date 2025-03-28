package org.mitre.tdp.boogie.dafif.utils;

import org.mitre.tdp.boogie.dafif.DafifRecord;

import java.util.function.BiConsumer;

import static java.util.Objects.requireNonNull;

public final class ValidationHelper {

  private ValidationHelper() {
    throw new IllegalStateException("Unable to instantiate static utility class");
  }

  public static boolean containsParsedField(DafifRecord record, String field, BiConsumer<DafifRecord, String> missingFieldConsumer) {
    requireNonNull(missingFieldConsumer, "The missing field consumer must be populated.");

    boolean containsParsedField = record.optionalField(field).isPresent();
    if (!containsParsedField) {
      missingFieldConsumer.accept(record, field);
    }
    return containsParsedField;
  }
}
