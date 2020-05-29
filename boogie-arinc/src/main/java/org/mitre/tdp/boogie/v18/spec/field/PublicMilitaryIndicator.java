package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;

/**
 * Airports can be classified into three categories, airports open to the general public, military airports, and airport closed to the public.
 * This field permits these airports to be categorized by their use.
 */
public enum PublicMilitaryIndicator implements FieldSpec<PublicMilitaryIndicator>, FilterTrimEmptyInput<PublicMilitaryIndicator> {
  SPEC,
  /**
   * Civil
   */
  C,
  /**
   * Military
   */
  M,
  /**
   * Private
   */
  P;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.177";
  }

  @Override
  public PublicMilitaryIndicator parseValue(String fieldValue) {
    return toEnumValue(fieldValue, PublicMilitaryIndicator.class);
  }
}
