package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE LATEST REVISION DATE EXPRESSED AS A FOUR DIGIT YEAR FOLLOWED BY THE JULIAN DAY.
 *
 * 9 NOVEMBER 2008 IS OUTPUT AS '2008313'
 *
 * EXAMPLE(S):
 * 2013178
 * 2002176
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * FIRST FOUR DIGITS:  COMBINATIONS OF 0-9 FOR THE YEAR
 * LAST THREE DIGITS: 001-366 (PADDED WITH LEADING ZEROS)
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class JulianDate extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 7;
  }

  @Override
  public int fieldCode() {
    return 155;
  }

  @Override
  public String regex() {
    return "([0-9]{4}([0-2][0-9][1-9]|[0-2][1-9][0-9]|[1-2][0-9]{2}|3[0-5][0-9]|36[0-6]))";
  }
}