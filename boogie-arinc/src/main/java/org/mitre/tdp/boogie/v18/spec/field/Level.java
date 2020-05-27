package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * The Level field defines the airway structure of which the record is an element.
 */
public enum Level implements FieldSpec<Level> {
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
  public Level parse(String fieldString) {
    return toEnumValue(fieldString, Level.class);
  }
}
