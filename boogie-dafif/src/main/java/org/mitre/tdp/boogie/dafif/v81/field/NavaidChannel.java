package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE CHANNEL ASSIGNED TO THE SPECIFIC NAVAID BY THE CONTROLLING AUTHORITY.
 *
 * EXAMPLE(S):
 * 001X
 * 017Y
 * NULL
 *
 * FIELD TYPE:	A/N
 *
 * ALLOWED VALUES:
 * 001X-126X
 * OR
 * 001Y-126Y
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public class NavaidChannel extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 60;
  }

  @Override
  public String regex() {
    return "(((00[1-9]|0[1-9][0-9]|1[0-1][0-9]|12[0-6])[X|Y])?)";
  }
}
