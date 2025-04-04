package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE FREQUENCY ASSIGNED TO THE SPECIFIC NAVAID BY THE CONTROLLING AUTHORITY WITH THE UNIT  OF MEASUREMENT IN KILOHERTZ (K) OR MEGAHERTZ (M). FOR BOTH KILOHERTZ AND MEGAHERTZ THE IMPLIED DECIMAL POINT IS FOUR CHARACTERS TO THE LEFT OF THE END OF THE STRING.
 *
 * EXAMPLE(S):
 * 112700M REPRESENTS 112.70 MEGAHERTZ
 * 365000K REPRESENTS 365 KILOHERTZ
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * LEFT JUSTIFIED COMBINATION OF NUMBERS (0-9) FOLLOWED BY M OR K
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class NavaidFrequencyNav extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 8;
  }

  @Override
  public int fieldCode() {
    return 382;
  }

  // TBD - allegedly
  @Override
  public String regex() {
    return "(.*)";
  }
}