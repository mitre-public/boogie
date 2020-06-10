package org.mitre.tdp.boogie;

public class DuplicateFieldException extends RuntimeException {
  public DuplicateFieldException(ArincField<?> field, String rawRecord) {
    super("Duplicate field: " + field + " when parsing record: " + rawRecord);
  }
}
