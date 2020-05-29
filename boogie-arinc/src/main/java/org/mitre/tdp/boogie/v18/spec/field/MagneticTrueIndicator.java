package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;

/**
 * The “Magnetic/True Indicator” field is used to indicate if the “Course From” and “Course To” fields of the Cruise Table record and the “Sector Bearing”
 * fields of the MSA and TAA record are in magnetic or true degrees. It is also used in the Airport Record to indicate that all detail and procedures for
 * that airport are included in the data base with a reference to either Magnetic North or True North. The field is blank in Airport Record when the data
 * base contains a mix of magnetic and true information for the airport.
 */
public enum MagneticTrueIndicator implements FieldSpec<MagneticTrueIndicator>, FilterTrimEmptyInput<MagneticTrueIndicator> {
  SPEC,
  M,
  T;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.165";
  }

  @Override
  public MagneticTrueIndicator parseValue(String fieldValue) {
    return toEnumValue(fieldValue, MagneticTrueIndicator.class);
  }

}
