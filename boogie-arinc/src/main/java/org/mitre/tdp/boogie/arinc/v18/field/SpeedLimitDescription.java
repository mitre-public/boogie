package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

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
  public Optional<SpeedLimitDescription> apply(String fieldValue) {
    if (fieldValue.trim().isEmpty() || fieldValue.equals("@")) {
      return Optional.of(SpeedLimitDescription.AT);
    } else if (fieldValue.equals("+")) {
      return Optional.of(SpeedLimitDescription.AT_OR_ABOVE);
    } else if (fieldValue.equals("-")) {
      return Optional.of(SpeedLimitDescription.AT_OR_BELOW);
    } else {
      return Optional.empty();
    }
  }
}
