package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE VALUE REPRESENTING THE ANGLE ABOVE THE GROUND WHERE THE GLIDE SLOPE IS POSITIONED, WITH A RESOLUTION TO THE HUNDREDTHS OF A DEGREE.
 * THE NORMAL GLIDE SLOPE ANGLE IS THREE DEGREES (3.00) BUT MAY VARY
 * DUE TO TERRAIN OR AIRSPACE RESTRICTIONS IN THE PROCEDURE DESIGN.
 *
 * NOTE:   THE GLIDE SLOPE ANGLE IN THE ILS RECORD MAY DIFFER FROM THE VNAV ANGLE PUBLISHED IN THE TERMINAL PROCEDURE RECORD.
 *
 * EXAMPLE(S):
 * 3.00
 * 6.65
 * 2.05
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 *  1.00 - 13.00 (LEADING ZERO IS SUPPRESSED)
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * Note: When coming from Jeppesen, does not seem to contain the appropriate padded zeros
 */
public class IlsGlideSlopeAngle extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 137;
  }

  @Override
  public String regex() {
    return "((([1-9]|1[0-2])\\.[0-9]{2}|[1-9]|[1-9]\\.[1-9]|13.00)?)";
  }
}
