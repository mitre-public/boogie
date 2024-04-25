package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE TRUE HEADING OF THE RUNWAY ALIGNMENT ALONG THE RUNWAY CENTERLINE.
 *
 * EXAMPLE(S):
 * 0.1
 * 360.0
 * 4.0
 * 262.4
 * NULL
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 *  0.1 - 360.0 (LEADING ZEROS ARE SUPPRESSED UNLESS VALUE IS LESS THAN 1.0)
 * 	OR
 * 	NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public class RunwayTrueHeading extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 323;
  }

  @Override
  public String regex() {
    return "((0\\.[1-9]|360\\.0|(([1-9][0-9]{0,1}|[1-2][0-9]{1,2}|3[0-5][0-9])\\.[0-9]))?)";
  }
}
