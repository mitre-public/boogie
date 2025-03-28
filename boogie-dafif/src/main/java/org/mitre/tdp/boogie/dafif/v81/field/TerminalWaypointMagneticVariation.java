package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE ANGULAR DIFFERENCE BETWEEN TRUE NORTH AND MAGNETIC NORTH IN DEGREES/DECIMAL DEGREES.  (GENERAL PLANNING)
 *
 * EXAMPLE(S):
 * 2.217460
 * -7.060340
 * -3.000000
 * 21.452497
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * -179.999999 - 180.000000 (TRAILING ZEROS ARE NOT TRIMMED--ALWAYS SIX NUMERICS TO THE RIGHT OF THE DECIMAL)
 *  OR
 *  NULL
 *
 * SOURCE: SYSTEM GENERATED
 */
public final class TerminalWaypointMagneticVariation extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 11;
  }

  @Override
  public int fieldCode() {
    return 143;
  }

  @Override
  public String regex() {
    return "(((\\-)?([0-9]|[1-9][0-9]|1[0-7][0-9])\\.[0-9]{6}|180.000000)?)";
  }
}