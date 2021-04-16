package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Longest Runway Surface Code” field is used to define whether or not there is a hard surface runway at the airport,
 * the length of which is indicated in the Longest Runway field.
 */
public enum LongestRunwaySurfaceCode implements FieldSpec<LongestRunwaySurfaceCode>, FilterTrimEmptyInput<LongestRunwaySurfaceCode> {
  SPEC,
  /**
   * Hard Surface, for example, asphalt or concrete.
   */
  H,
  /**
   * Soft Surface, for example, gravel, grass or soil.
   */
  S,
  /**
   * Water.
   */
  W,
  /**
   * Undefined, surface material not provided in source.
   */
  U;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.249";
  }

  @Override
  public LongestRunwaySurfaceCode parseValue(String fieldValue) {
    return toEnumValue(fieldValue, LongestRunwaySurfaceCode.class);
  }
}
