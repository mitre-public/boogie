package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * 'A' OR 'B' SYMBOL INDICATED ALONG A ROUTE WHEN IFR CRUISING LEVELS ARE NOT IN AGREEMENT WITH  THE APPROPRIATE CRUISING LEVEL DIAGRAMS.  (FLIP)
 *
 *  THIS ATTRIBUTE IS USED FOR THOSE AIRWAY SEGMENTS THAT DO NOT FOLLOW ICAO GUIDELINES.
 *
 *  A - USED FOR ONE-WAY WESTBOUND AND BI-DIRECTIONAL SEGMENTS IN WHICH HOST SOURCE REQUIRES  THE USE OF ODD LEVEL ALTITUDES (FL 30, 50, 210, 270, ETC.)
 *
 *  B - USED FOR ONE WAY EASTBOUND SEGMENTS IN WHICH HOST SOURCE REQUIRES THE USE OF EVEN  LEVEL ALTITUDES (FL60, 200, 280, ETC.)
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * A, B
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class CruiseLevelIndicator extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 84;
  }

  @Override
  public String regex() {
    return "((A|B)?)";
  }
}