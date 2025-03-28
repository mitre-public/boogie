package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES THE AIRSPACE STRUCTURE IN WHICH THE ROUTE IS EFFECTIVE.
 * B - HIGH AND LOW LEVEL
 *  H - HIGH LEVEL
 *  L - LOW LEVEL
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * B, H, or L
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class Level extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 164;
  }

  @Override
  public String regex() {
    return "(B|H|L)";
  }
}