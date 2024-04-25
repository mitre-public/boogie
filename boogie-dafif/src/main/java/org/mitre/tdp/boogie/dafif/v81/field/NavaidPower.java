package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * FACILITY CAPACITY EXPRESSED IN WATTAGE.
 *
 * EXAMPLE(S):
 * U
 * 5
 * 7500
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * 0-9999 (LEADING ZEROS ARE SUPPRESSED)
 * OR
 * U (UNKNOWN)
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class NavaidPower extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 245;
  }

  @Override
  public String regex() {
    return "([0-9]|[1-9][0-9]|[1-9][0-9]{2}|[1-9][0-9]{3}|U)";
  }
}