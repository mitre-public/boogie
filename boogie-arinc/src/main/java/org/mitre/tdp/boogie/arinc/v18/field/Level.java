package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The Level field defines the airway structure of which the record is an element.
 */
public enum Level implements FieldSpec<Level>, FilterTrimEmptyInput<Level> {
  SPEC,
  /**
   * All altitudes
   */
  B,
  /**
   * High level airways.
   */
  H,
  /**
   * Low level airways
   */
  L;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.19";
  }

  @Override
  public Level parseValue(String fieldValue) {
    return toEnumValue(fieldValue, Level.class);
  }
}
