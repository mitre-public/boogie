package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.FieldSpec;

public enum SectionCode implements FieldSpec<SectionCode> {
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
  public SectionCode parse(String fieldString) {
    return toEnumValue(fieldString, SectionCode.class);
  }
}
