package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * The Arc Bearing field contains the true bearing from the Arc Origin position to the beginning of the arc.
 * <p>
 * Arc bearings should be derived from official government sources when available. The field contains true bearing
 * in degrees and tenths of degree, with the decimal point suppressed. The field will only be entered when Boundary
 * Via is A, C, L, or R.
 * <p>
 * Examples: 0900, 1800, 3450
 */

public final class ArcBearing extends ArincDouble {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.120";
  }

  @Override
  protected int suppressedDecimalPlaces() {
    return 1;
  }
}
