package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE LOWEST FLIGHT LEVEL AVAILABLE FOR USE ABOVE THE TRANSITION ALTITUDE.  (P/CG).  ALTITUDE IS  EXPRESSED IN FEET ABOVE MEAN SEA LEVEL.
 *
 * EXAMPLE(S):
 * NULL
 * 3000
 * 20000
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 1-99999 (LEADING ZEROS ARE SUPPRESSED)
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class TransitionLevel extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 321;
  }

  @Override
  public String regex() {
    return "(([1-9][0-9]{0,4})?)";
  }
}