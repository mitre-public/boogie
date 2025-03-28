package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES DIRECTIONAL INFORMATION IN DEGREES AND TENTHS OF DEGREES LEADING TO THE  WAYPOINT IDENTIFIED IN THE RECORDS WAYPOINT IDENTIFIER.
 *
 * EXAMPLE(S):
 * 50.0
 * 222.5
 * 0.2
 * 6.T
 * 233.T
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * (LEADING ZEROS ARE SUPPRESSED UNLESS VALUE IS LESS THAN 1.0)
 * IF COURSE IS IN TRUE, VALUE CANNOT BE LESS THAN 1.0 AND TENTHS PLACE IS FILLED WITH T.
 * 0.1 - 360.0
 * OR
 * 1.T-360.T
 * OR
 * NULL
 *
 * **THE LAST CHARACTER CAN BE "T" (TRUE), OR NUMERIC (IF KNOWN).
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 */
public final class TerminalMagneticCourse extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 178;
  }

  @Override
  public String regex() {
    return "(((0\\.[1-9]|360\\.0|(([1-9][0-9]{0,1}|[1-2][0-9]{1,2}|3[0-5][0-9])\\.[0-9]))|((360\\.[T])|(([1-9][0-9]{0,1}|[1-2][0-9]{1,2}|3[0-5][0-9])\\.[T])))?)";
  }
}