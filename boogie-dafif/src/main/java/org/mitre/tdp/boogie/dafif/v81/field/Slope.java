package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * PERCENTAGE VALUE OF THE SLOPE OF THE RUNWAY MEASURED FROM END TO END DEFINED TO THE NEAREST TENTH OF A PERCENTILE.
 * A MINUS (-) SIGN PRECEDING THE VALUE WILL INDICATE DOWN SLOPE.
 *
 * EXAMPLE(S):
 * -6.4
 * 0.0
 * 0.1
 * U"
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 *  -9.9 TO 9.9
 * 	OR
 *  U
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * THE HE/LE IN THIS SLOPE FIELD REFER TO RUNWAY IDENTS. RUNWAY LOW HEADINGS ENCOMPASS 001° THRU 180°.
 * RUNWAY HIGH HEADINGS ENCOMPASS 181° THRU 360°. HIGH AND LOW RUNWAY IDENTS REFER TO HEADINGS, NOT ELEVATIONS.
 */
public class Slope extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 292;
  }

  @Override
  public String regex() {
    return "(((\\-[0-9]|[0-9])\\.[0-9])|U)";
  }
}
