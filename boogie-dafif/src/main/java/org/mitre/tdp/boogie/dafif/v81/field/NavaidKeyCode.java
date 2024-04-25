package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * SYSTEM GENERATED KEY USED AS AN ARTIFICIAL KEY TO DISTINGUISH BETWEEN NAVAIDS WITH THE  SAME TYPE, IDENT, AND  COUNTRY CODE.
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 1 - 99 (LEADING ZERO SUPPRESSED)
 * OR
 * NULL
 *
 * SOURCE: SYSTEM GENERATED
 */
public final class NavaidKeyCode extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 210;
  }

  @Override
  public String regex() {
    return "(([1-9][0-9]{0,1})?)";
  }
}