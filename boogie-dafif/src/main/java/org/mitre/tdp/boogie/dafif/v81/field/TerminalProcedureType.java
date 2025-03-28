package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A NUMBER CODE ASSIGNED TO THE SPECIFIC TYPE OF TERMINAL INSTRUMENT PROCEDURE.
 *
 *  1 - STANDARD TERMINAL ARRIVAL (STAR).
 *  2 - STANDARD INSTRUMENT DEPARTURE (SID).
 *  3 - INSTRUMENT APPROACH PROCEDURE (IAP).
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 1-3
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class TerminalProcedureType extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 252;
  }

  @Override
  public String regex() {
    return "([1-3])";
  }
}