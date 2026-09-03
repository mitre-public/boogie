package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * The “Localizer Bearing” field defines the magnetic bearing of the localizer course of the ILS facility/GLS approach described
 * in the record.
 * <br>
 * This field will contain the true bearing in degrees and tenths of a degree, with the decimal point suppressed.
 * <br>
 * e.g. 0900, 2715
 */
public final class MinorAxisBearing extends ArincDouble {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.100";
  }

  @Override
  protected int suppressedDecimalPlaces() {
    return 1;
  }
}
