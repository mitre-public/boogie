package org.mitre.tdp.boogie.arinc;

/**
 * Specific subclass of a RuntimeException for use when a configured {@link RecordSpec} contains a duplicate named field.
 */
public final class DuplicateFieldException extends RuntimeException {

  public DuplicateFieldException(RecordField<?> field, String rawRecord) {
    super("Duplicate field: " + field + " when parsing record: " + rawRecord);
  }
}
