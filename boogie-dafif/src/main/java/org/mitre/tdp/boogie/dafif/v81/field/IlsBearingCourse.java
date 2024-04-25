package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES DIRECTIONAL INFORMATION IN DEGREES AND TENTHS OF DEGREES LEADING TO THE  LOCALIZER FACILITY.  THIS BEARING/COURSE TO THE LOCALIZER FACILITY IS USED FOR FINAL APPROACH.EXAMPLES:NULL 245.6 130.T75.00.8THE LAST CHARACTER CAN BE "T" (TRUE), "G" (GRID), OR NUMERIC (IF KNOWN).
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * (LEADING ZEROS ARE SUPPRESSED UNLESS VALUE IS LESS THAN 1.0)
 * IF BEARING IS IN TRUE OR GRID, VALUE CANNOT BE LESS THAN 1.0 AND TENTHS PLACE IS FILLED WITH T OR G.
 * 0.1 - 360.0
 * OR
 * 1.T - 360.T
 * OR
 * 1.G - 360.G
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * FOR ILS.COMP_TYPE I,L,M, AND O THE BEARING IS CALCULATED BETWEEN THE COMPONENT AND THE LANDING THRESHOLD.
 */
public final class IlsBearingCourse extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 150;
  }

  @Override
  public String regex() {
    return "(((0\\.[1-9]|(360.0)|(([1-9][0-9]{0,1}|[1-2][0-9]{1,2}|3[0-5][0-9])\\.[0-9]))|((360.[GT])|(([1-9][0-9]{0,1}|[1-2][0-9]{1,2}|3[0-5][0-9])\\.[GT])))?)";
  }
}