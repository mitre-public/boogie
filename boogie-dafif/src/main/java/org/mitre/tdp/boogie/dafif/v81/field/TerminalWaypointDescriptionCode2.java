package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * ENABLES THE FUNCTION OF A WAYPOINT/FIX AT A SPECIFIC LOCATION TO BE IDENTIFIED.
 *  VARIOUS FIELDS PROVIDE INFORMATION ON THE FIX.
 *
 * WAYPOINT DESCRIPTION CODE 2:
 * B – OVERFLY AND END OF CONTINUOUS TRANSITION
 * E – FLYBY AND END OF CONTINUOUS TRANSITION
 * Y – OVERFLY
 * NULL EQUALS A FLYBY
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * B, E, Y, NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class TerminalWaypointDescriptionCode2 extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 351;
  }

  @Override
  public String regex() {
    return "((B|E|Y)?)";
  }
}