package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * Boundary shape: A point, B great circle, C circle, G generalized, H rhumb line, L counterclockwise arc, R clockwise arc.
 * <p>DAFIF 8.1 data dictionary field 289.</p>
 */
public final class Shape extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 289;
  }

  @Override
  public String regex() {
    return "(A|B|C|G|H|L|R)";
  }
}
