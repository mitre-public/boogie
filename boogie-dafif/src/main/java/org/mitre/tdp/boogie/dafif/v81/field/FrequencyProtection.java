package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE DISTANCE AND ALTITUDE WITHIN WHICH FREQUENCY RECEPTION IS ASSURED TO BE INTERFERENCE  FREE,
 * EX: 040/60 WHERE 40 INDICATES DISTANCE IN NAUTICAL MILES AND 60 INDICATES THE ALTITUDE IN  THOUSANDS OF FEET.
 *
 * EXAMPLE(S)
 * NULL
 * 010/5
 * 015/45
 * 250/50
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 *  FIRST THREE POSITIONS: 000-599 (PADDED WITH LEADING ZEROS)
 *  FOURTH POSITION: /
 *  FIFTH AND SIXTH POSITIONS: 1-99 (NOT PADDED WITH LEADING ZEROS)
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class FrequencyProtection extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 6;
  }

  @Override
  public int fieldCode() {
    return 130;
  }

  @Override
  public String regex() {
    return "(([0-5][0-9]{2}\\/([1-9]|[1-9][0-9]))?)";
  }
}