package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * DESIGNATES WHETHER A LOCATOR OR NAVAID IS COLLOCATED.
 *
 * 1ST CHARACTER:
 * L - LOCATOR
 * N - NAVAID OTHER THAN LOCATOR
 *
 * 2ND CHARACTER:
 * B - BACKCOURSE MARKER
 * I - INNER
 * M - MIDDLE
 * O - OUTER
 *
 * EXAMPLE(S):
 * LI
 * NM
 * NULL
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * ONE OF THE FOLLOWING:
 * LB, LI, LM, LO
 * OR
 * ONE OF THE FOLLOWING:
 * NB, NI, NM, NO
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class Collocation extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 68;
  }

  @Override
  public String regex() {
    return "((LB|LI|LM|LO|NB|NI|NM|NO)?)";
  }
}
