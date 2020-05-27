package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * Enum for the ARINC record type.
 */
public enum RecordType implements FieldSpec<RecordType> {
  SPEC,
  /**
   * Standard record types used across all aircraft and airlines.
   */
  S,
  /**
   * Tailored records generally for use by a particular airline (company routes, etc.).
   */
  T;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.2";
  }

  @Override
  public RecordType parse(String fieldString) {
    return toEnumValue(fieldString, RecordType.class);
  }
}
