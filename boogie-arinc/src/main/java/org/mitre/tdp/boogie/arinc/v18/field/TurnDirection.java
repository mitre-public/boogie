package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

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
  public Optional<TurnDirection> apply(String fieldValue) {
    return toEnumValue(fieldValue, TurnDirection.class);
  }
}
