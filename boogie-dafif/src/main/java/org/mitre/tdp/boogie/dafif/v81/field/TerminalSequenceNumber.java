package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * SEQUENCE NUMBERS ARE DESIGNED TO BE USED IN CONJUNCTION WITH OTHER KEY FIELDS TO FORM A METHOD OF IDENTIFYING A UNIQUE RECORD WITHIN DAFIF.
 *
 * WHEN COMBINED WITH THE APPROPRIATE KEYS THEY DEFINE THE RELATIVE POSITION OF THE SEGMENT.
 *
 * EXAMPLE(S):
 * 10
 * 170
 * 500
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 10-9990
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * TERMINAL SEQUENCE NUMBERS START WITH 10 WITH INCREMENTS OF 10.
 * NOTE:  THERE IS NO RELATIONSHIP BETWEEN SEQ_NBR AND TRACK DESCRIPTION CODE OR WAYPOINT DESCRIPTION CODES 1 THROUGH 4.
 */
public final class TerminalSequenceNumber extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 4;
  }

  @Override
  public int fieldCode() {
    return 100;
  }

  @Override
  public String regex() {
    return "([1-9]0|[1-9][0-9]0|[1-9][0-9]{2}0)";
  }
}