package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Airports can be classified into three categories, airports open to the general public, military airports, and airport closed to the public.
 * This field permits these airports to be categorized by their use.
 */
public enum PublicMilitaryIndicator implements FieldSpec<PublicMilitaryIndicator> {
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
  P,
  /**
   * Join operations.
   */
  J;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.177";
  }

  @Override
  public Optional<PublicMilitaryIndicator> apply(String fieldValue) {
    return toEnumValue(fieldValue, PublicMilitaryIndicator.class);
  }
}
