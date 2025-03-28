package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES WHETHER AN ATS ROUTE IS OPEN, CLOSED, RESTRICTED OR SEASONAL IN SOME WAY.
 *
 *  O - OPEN
 *  C - CLOSED
 *  R - RESTRICTED
 *  A - ALTERNATE
 *  S - SEASONAL, CONDITIONAL
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * O, C, R, A, S
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class AtsRouteStatus extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 301;
  }

  @Override
  public String regex() {
    return "(O|C|R|A|S)";
  }
}