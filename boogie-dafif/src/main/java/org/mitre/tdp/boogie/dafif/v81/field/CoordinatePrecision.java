package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A NUMBER DEFINING THE SPECIFIC PRECISION OF A POINTS COORDINATES.  THE RANGE OF THIS PRECISION  IS 0-4.
 *
 *  DEFINITIONS OF THE ALLOWABLES AND EXAMPLES:
 *
 *  0 - COORDINATE IS RESOLVED TO THE HUNDREDTH OF A SECOND.
 *  EXAMPLE:  N12°27'28.95
 *  1 - COORDINATE IS RESOLVED TO THE TENTH OF A SECOND.
 *  EXAMPLE:  N12°27'28.9X
 *  2 - COORDINATE IS RESOLVED TO THE SECOND.
 *  EXAMPLE:  N12°27'28.XX
 *  4 - COORDINATE IS RESOLVED TO THE MINUTE.
 *  EXAMPLE:  N12°27'XX.XX
 *  NULL - INFORMATION PROVIDED BY SOURCE IS NOT ADEQUATE TO DETERMINE COORDINATE PRECISION
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 *  0,1,2,4
 *  OR
 *  NULL
 *
 * SOURCE:
 * TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class CoordinatePrecision extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 425;
  }

  @Override
  public String regex() {
    return "((0|1|2|4)?)";
  }
}
