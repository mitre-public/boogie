package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * FOR TERMINAL PROCEDURES ONLY.  WHEN THE WAYPOINT IS THE END OF RUNWAY OR DISPLACED  THRESHOLD, ENTER THE ICAO OR FAA/HOST COUNTRY IDENTIFIER FOR THE AIRPORT.
 *
 * EXAMPLE(S):
 * KSTL
 * W12X
 *
 *    NOTES:
 *      WAYPOINT MUST HAVE USAGE CODE ‘G’.
 *      WAYPOINT RUNWAY IDENTIFIER MUST BE POPULATED.
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * CHARACTERS 1-2:  COMBINATIONS OF LETTERS A-Z AND NUMBERS 0-9
 * CHARACTERS 3-4:  COMBINATIONS OF LETTERS A-Z AND NUMBERS 0-9  OR SPACES
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * THIS FIELD IS NO LONGER IN USE AND WILL BE NULL
 */
public final class WaypointRwyIcao extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 275;
  }

  @Override
  public String regex() {
    return "(^$)";
  }
}