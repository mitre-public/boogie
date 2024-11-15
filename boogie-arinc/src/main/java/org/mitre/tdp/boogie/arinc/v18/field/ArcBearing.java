package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The Arc Bearing field contains the true bearing from the Arc Origin position to the beginning of the arc.
 *
 * Arc bearings should be derived from official government sources when available. The field contains true bearing
 * in degrees and tenths of degree, with the decimal point suppressed. The field will only be entered when Boundary
 * Via is A, C, L, or R.
 *
 * Examples: 0900, 1800, 3450
 */

public final class ArcBearing extends ArincInteger  {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.120";
  }
}
