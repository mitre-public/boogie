package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * 4-Character string designating the type, function, and attributes of a specific fix in enroute airway or terminal procedure
 * encoding.
 * <br>
 * This was optionally populated at the level of the fix records but is almost always encoded in the procedure when referencing
 * a fix.
 * <br>
 * The full set of valid values and meanings per column is:
 * <br>
 * <b>Column1:</b>
 * A - Airport as waypoint (STAR/APCH)
 * E - Essential waypoint (Enroute/SID/STAR/APCH)
 * F - Off airway floating waypoint (Enroute)
 * G - Runway as waypoint (SID/STAR/APCH)
 * H - Heliport as waypoint (STAR/APCH)
 * N - Ndb navaid as waypoint (Enroute/SID/STAR/APCH)
 * P - Phantom waypoint (SID/STAR/APCH)
 * R - Non-essential waypoint (Enroute)
 * T - Transition essential waypoint (Enroute)
 * V - Vhf navaid as waypoint (Enroute/SID/STAR/APCH)
 * <br>
 * <b>Column2:</b>
 * B - Flyover waypoint, ending leg (SID/STAR/APCH)
 * E - End of continuous segment (Enroute/SID/STAR/APCH)
 * U - Uncharted airway intersection (Enroute)
 * Y - Flyover waypoint (SID/STAR/APCH)
 * <br>
 * <b>Column3:</b>
 * A - Unnamed stepdown fix final approach segment (APCH)
 * B - Unnamed stepdown fix intermediate approach segment (APCH)
 * C - ATC compulsory reporting point (Enroute/SID/STAR/APCH)
 * G - Oceanic gateway point (Enroute)
 * M - First leg of missed approach procedure (APCH)
 * P - Path point fix
 * R - Fix used for turn to final (APCH)
 * S - Named stepdown fix (APCH)
 * <br>
 * <b>Column4:</b>
 * A - Initial approach fix (APCH)
 * B - Intermediate approach fix (APCH)
 * C - Holding at initial approach fix (APCH)
 * D - Initial approach fix at FACF (APCH)
 * E - Final end point (APCH)
 * F - Final approach fix (APCH)
 * G - Source provided enroute waypoint without holding (Enroute)
 * H - Source provided enroute waypoint with holding (Enroute)
 * I - Final approach course fix (APCH)
 * M - Missed approach point (APCH)
 * N - Engine out SID missed approach disarm point (SID - engine out, APCH)
 */
public final class WaypointDescription implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.17";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(s -> new StringBuilder()
            .append(allowedColumn1(s.charAt(0)) ? s.charAt(0) : ' ')
            .append(allowedColumn2(s.charAt(1)) ? s.charAt(1) : ' ')
            .append(allowedColumn3(s.charAt(2)) ? s.charAt(2) : ' ')
            .append(allowedColumn4(s.charAt(3)) ? s.charAt(3) : ' ')
            .toString());
  }

  private boolean allowedColumn1(char c) {
    return 'A' == c
        || 'E' == c
        || 'F' == c
        || 'G' == c
        || 'H' == c
        || 'N' == c
        || 'P' == c
        || 'R' == c
        || 'T' == c
        || 'V' == c;
  }

  private boolean allowedColumn2(char c) {
    return 'B' == c
        || 'E' == c
        || 'U' == c
        || 'Y' == c;
  }

  private boolean allowedColumn3(char c) {
    return 'A' == c
        || 'B' == c
        || 'C' == c
        || 'G' == c
        || 'M' == c
        || 'P' == c
        || 'R' == c
        || 'S' == c;
  }

  private boolean allowedColumn4(char c) {
    return 'A' == c
        || 'B' == c
        || 'C' == c
        || 'D' == c
        || 'E' == c
        || 'F' == c
        || 'G' == c
        || 'H' == c
        || 'I' == c
        || 'M' == c
        || 'N' == c;
  }
}
