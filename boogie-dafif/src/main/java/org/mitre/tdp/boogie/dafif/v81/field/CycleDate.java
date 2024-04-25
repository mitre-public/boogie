package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * IDENTIFIES THE CALENDAR PERIOD IN WHICH THE RECORD WAS ADDED TO THE FILE OR LAST REVISED.
 *
 * THE FIRST TWO DIGITS INDICATE THE CENTURY (CC), THE THIRD AND FOURTH DIGITS INDICATE THE YEAR (YY), AND THE LAST TWO DIGITS INDICATE THE 28-DAY CYCLE (CD) IN WHICH THE ADDITION/CHANGE WAS
 * MADE. (DAFIF)
 *
 * EXAMPLE(S):
 * 201308
 * 200411
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * (CCYYCD)
 * 000001-999914
 *
 * SOURCE: SYSTEM GENERATED
 */
public final class CycleDate extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 6;
  }

  @Override
  public int fieldCode() {
    return 59;
  }

  @Override
  public String regex() {
    return "([0-9]{4}(0[1-9]|1[0-4]))";
  }
}