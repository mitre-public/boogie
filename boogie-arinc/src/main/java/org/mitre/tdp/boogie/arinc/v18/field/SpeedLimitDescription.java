package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

/**
 * The “Speed Limit Description” field will designate whether the speed limit coded at a fix in a terminal procedure
 * description is a mandatory, minimum or maximum speed.
 */
public enum SpeedLimitDescription implements FieldSpec<SpeedLimitDescription> {
  SPEC,
  /**
   * Mandatory Speed - cross fix AT the speed specified in the speed limit.
   */
  AT,
  /**
   * Maximum Speed - cross fix AT OR BELOW the speed specified in the speed limit.
   */
  AT_OR_BELOW,
  /**
   * Minimum Speed - cross fix AT OR ABOVE the speed specified in the speed limit.
   */
  AT_OR_ABOVE;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.261";
  }

  @Override
  public SpeedLimitDescription parseValue(String fieldValue) {
    if (fieldValue.trim().isEmpty() || fieldValue.equals("@")) {
      return SpeedLimitDescription.AT;
    } else if (fieldValue.equals("+")) {
      return SpeedLimitDescription.AT_OR_ABOVE;
    } else if (fieldValue.equals("-")) {
      return SpeedLimitDescription.AT_OR_BELOW;
    } else {
      throw new FieldSpecParseException(this, fieldValue);
    }
  }
}
