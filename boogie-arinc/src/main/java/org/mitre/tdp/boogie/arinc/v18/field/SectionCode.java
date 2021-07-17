package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Definition/Description: The “Section Code” field defines the major section of the navigation system data base in which the
 * record resides.
 */
public enum SectionCode implements FieldSpec<SectionCode>, FilterTrimEmptyInput<SectionCode> {
  SPEC,
  /**
   * Grid MORA.
   */
  A,
  /**
   * Navaid.
   */
  D,
  /**
   * Enroute.
   */
  E,
  /**
   * Heliport.
   */
  H,
  /**
   * Tables.
   */
  T,
  /**
   * Company Routes.
   */
  R,
  /**
   * Airport.
   */
  P,
  /**
   * Airspace.
   */
  U;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.4";
  }

  @Override
  public SectionCode parseValue(String fieldValue) {
    return toEnumValue(fieldValue, SectionCode.class);
  }
}
