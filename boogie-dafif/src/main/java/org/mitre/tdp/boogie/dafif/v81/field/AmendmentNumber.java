package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE NUMBER OF TIMES THE SOURCE PROVIDER HAS DESIGNATED A VERSION CHANGE/UPDATE TO PROCEDURAL DATA.
 *
 * EXAMPLE(S):
 * NULL
 * A
 * 1
 * 2A
 * 7
 * 13
 * 17A
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * COMBINATIONS OF NUMBERS 0-9 AND LETTERS A-Z
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class AmendmentNumber extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 3;
  }

  @Override
  public int fieldCode() {
    return 24;
  }

  @Override
  public String regex() {
    return "(([0-9A-Z]{1,3})?)";
  }
}