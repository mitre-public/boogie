package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The Arc Distance field is used to define the distance in nautical miles from the Arc Origin position to the arc
 * defining the lateral boundary of a FIR/UIR or Restrictive Airspace.
 *
 * ARC distances should be derived from official government sources when available, in nautical miles and tenths
 * of nautical mile, with the decimal point suppressed. The field will be entered only when Boundary Via is
 * A, C, L, or R.
 *
 * Examples: 0080, 0150, 1000
 */

public final class ArcDistance extends ArincInteger {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.119";
  }
}
