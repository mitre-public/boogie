package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE FREQUENCY ASSIGNED TO THE SPECIFIC NAVAID BY THE CONTROLLING AUTHORITY WITH THE UNIT
 * OF MEASUREMENT IN KILOHERTZ (K) OR MEGAHERTZ (M).
 *
 * EXAMPLE(S):
 * 1000000K
 * 995000K
 * U
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * LEFT JUSTIFIED COMBINATION OF NUMBERS (0-9) FOLLOWED BY M OR K
 * OR
 * U (UNKNOWN)
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class NavaidFrequency extends TrimmableString {

  @Override
  public int maxFieldLength() {
    return 8;
  }

  @Override
  public int fieldCode() {
    return 377;
  }

  @Override
  public String regex() {
    //conflicting regex coming from docs, one of them indicates it is TBD
    return "(.*)";
  }
}
