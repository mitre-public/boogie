package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.ArincStrings.isBlank;
import static org.mitre.tdp.boogie.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * This field is used in conjunction with Turn direction to indicate that a turn is required prior to capturing the
 * path defined in a terminal procedure.
 */
public enum TurnDirectionValid implements FieldSpec<TurnDirectionValid> {
  SPEC,
  Y,
  N;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.22";
  }

  @Override
  public TurnDirectionValid parse(String fieldString) {
    return isBlank.test(fieldString) ? N : toEnumValue(fieldString, TurnDirectionValid.class);
  }
}
