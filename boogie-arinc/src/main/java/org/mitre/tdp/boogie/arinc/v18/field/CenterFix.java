package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * When used on Airport and Heliport MSA Records and specific terminal procedure records, the “Center Fix” field represents the MSA Center,
 * that point (Navaid or Waypoint) on which the MSA is predicated. When used on Terminal Procedure Records incorporating an “RF” Path and
 * Termination, the field represents the point (Terminal Waypoint) which defines the center of the arc flight path.
 */
public final class CenterFix implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.144";
  }
}
