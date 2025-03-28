package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * FACILITY CAPACITY EXPRESSED AS EFFECTIVE RANGE IN WHOLE NAUTICAL MILES.
 *
 * EXAMPLE(S):
 * U
 * 5
 * 810
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * 1-999
 * OR
 * U (UNKNOWN)
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class NavaidRange extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 3;
  }

  @Override
  public int fieldCode() {
    return 258;
  }

  @Override
  public String regex() {
    return "([1-9][0-9]{0,2}|U)";
  }
}