package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * CONTAINS THE NAME (INCLUDES NAVAID IDENT) OR SERIES OF CHARACTERS WITH WHICH THE WAYPOINT  IS IDENTIFIED.
 *
 * USED ON TERMINAL SEGMENT RECORDS INCORPORATING AN "RF" PATH AND TERMINATION (TRACK CODE), THE FIELD REPRESENTS THE POINT (WAYPOINT) WHICH DEFINES THE CENTER OF THE ARC FLIGHT PATH.
 *
 * WAYPOINT NAMES WILL BE SUPPLIED BY SOURCE.
 *
 * EXAMPLES:
 * CFFWD
 * DNBSB
 * DNBTB
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * 1ST POSITION: LETTERS A-Z OR NUMBERS 0-9
 * SUBSEQUENT POSITIONS:  COMBINATIONS OF LETTERS A-Z AND NUMBERS 0-9
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * THE WAYPOINT TABLE IS THE ONLY TABLE THAT USES WPT_IDENT AS A KEY FIELD.  THE WAYPOINT KEY FIELDS (WPT_IDENT, WPT_COUNTRY) MUST EXIST IN THE WPT.TXT BEFORE ASSOCIATED IN OTHER RECORDS.
 */
public final class ArcWaypointIdentifier extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 358;
  }

  @Override
  public String regex() {
    return "(([A-Z0-9]{1,5})?)";
  }
}