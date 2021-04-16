package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.ArincStrings.toEnumValue;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The spec for an instance of the customer area code spec.
 */
public enum CustomerAreaCode implements FieldSpec<CustomerAreaCode>, FilterTrimEmptyInput<CustomerAreaCode> {
  SPEC(null),
  /**
   * United States of America.
   */
  USA("U"),
  /**
   * Canada and Alaska
   */
  CAN("C"),
  /**
   * Pacific
   */
  PAC("P"),
  /**
   * Latin America
   */
  LAM("L"),
  /**
   * South America
   */
  SAM("S"),
  /**
   * South Pacific
   */
  SPA("1"),
  /**
   * Europe
   */
  EUR("E"),
  /**
   * Eastern Europe
   */
  EEU("2"),
  /**
   * Middle East-South Asia
   */
  MES("M"),
  /**
   * Africa
   */
  AFR("A");

  private final String boundaryCode;

  CustomerAreaCode(String boundaryCode) {
    this.boundaryCode = boundaryCode;
  }

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.3";
  }

  @Override
  public CustomerAreaCode parseValue(String fieldValue) {
    return toEnumValue(fieldValue, CustomerAreaCode.class);
  }

  public String boundaryCode() {
    return boundaryCode;
  }
}
