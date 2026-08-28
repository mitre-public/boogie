package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * This field is used to specify the DME offset.
 * <br>
 * The field contains a 2-digit bias term in nautical miles and tenths of a nautical mile with the decimal point suppressed.
 * Field is blank for unbiased DME’s.
 */
public final class IlsDmeBias extends ArincDouble {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.90";
  }

  @Override
  protected int suppressedDecimalPlaces() {
    return 1;
  }
}
