package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Enum for the ARINC record type.
 */
public enum RecordType implements FieldSpec<RecordType>, FilterTrimEmptyInput<RecordType> {
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
  public RecordType parseValue(String fieldValue) {
    return toEnumValue(fieldValue, RecordType.class);
  }
}
