package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.isBlank;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * This field is used in conjunction with Turn direction to indicate that a turn is required prior to capturing the
 * path defined in a terminal procedure.
 */
public final class TurnDirectionValid implements FieldSpec<Boolean> {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.22";
  }

  @Override
  public Boolean parseValue(String fieldValue) {
    checkSpec(this, fieldValue, isBlank.test(fieldValue) || fieldValue.equalsIgnoreCase("Y"));
    return fieldValue.equalsIgnoreCase("Y");
  }
}
