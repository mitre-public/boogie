package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum TurnDirection implements FieldSpec<TurnDirection>, FilterTrimEmptyInput<TurnDirection> {
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
  public TurnDirection parseValue(String fieldValue) {
    return toEnumValue(fieldValue, TurnDirection.class);
  }
}
