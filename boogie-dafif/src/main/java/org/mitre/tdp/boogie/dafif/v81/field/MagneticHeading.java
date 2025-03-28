package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE MAGNETIC BEARING OF THE RUNWAY CENTERLINE.
 *
 * EXAMPLE(S):
 * 293.7
 * 253.0
 * 4.5
 * 0.7
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 *  0.1 - 360.0 (ZEROS ARE SUPPRESSED UNLESS VALUE IS LESS THAN 1.0)
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * IF IDENT EQUALS TRUE (EX. 27T), THEN THE MAGNETIC HEADING WILL EQUAL THE TRUE HEADING.
 * THIS VALUE MAY BE DERIVED IN THE ABSENSE OF HOST PROVIDED VALUES USING IMAGERY OR MENSURATION
 * TO PROVIDE PRECISE MEASUREMENT FOR THIS FIELD IN ACCORDANCE WITH DO-200B AND DO-201.
 */
public class MagneticHeading extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 384;
  }

  @Override
  public String regex() {
    return "(0\\.[1-9]|360\\.0|(([1-9][0-9]{0,1}|[1-2][0-9]{1,2}|3[0-5][0-9])\\.[0-9]))";
  }
}
