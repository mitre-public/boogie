package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES IF AN ATS ROUTE HAS BOTH DIRECTIONS OF FLIGHT.
 *
 * EXAMPLE(S):
 * NULL
 * Y
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * Y
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class BiDirectional extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 49;
  }

  @Override
  public String regex() {
    return "((Y)?)";
  }
}