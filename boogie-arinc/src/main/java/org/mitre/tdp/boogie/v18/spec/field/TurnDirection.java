package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.FieldSpec;

public enum TurnDirection implements FieldSpec<TurnDirection> {
  SPEC,
  /**
   * Left
   */
  L,
  /**
   * Right
   */
  R,
  /**
   * Either
   */
  E;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.20";
  }

  @Override
  public TurnDirection parse(String fieldString) {
    return toEnumValue(fieldString, TurnDirection.class);
  }
}
