package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES THE AIRSPACE STRUCTURE IN WHICH THE NAVAID IS UTILIZED.
 *
 * B - BOTH (HIGH AND LOW LEVEL)
 * H - HIGH LEVEL
 * L - LOW LEVEL
 * T - TERMINAL
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * B, H, L, T
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class NavaidUsageCode extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 213;
  }

  @Override
  public String regex() {
    return "(H|L|B|T)";
  }
}