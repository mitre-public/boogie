package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE ALTITUDE IN THE VICINITY OF AN AIRPORT AT OR BELOW WHICH THE VERTICAL POSITION OF AN  AIRCRAFT IS DETERMINED BY REFERENCE TO ALTITUDES IN FEET ABOVE MEAN SEA LEVEL WITH THE  ALTIMETER IN THE AIRCRAFT SET TO STATION PRESSURE RATHER THAN THE STANDARD OF 29.92 INCHES OF  MERCURY.
 *
 * EXAMPLE(S):
 * NULL
 * 9800
 * 19500
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 1-99999 (LEADING ZEROS ARE SUPPRESSED)
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class TransitionAltitude extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 319;
  }

  @Override
  public String regex() {
    return "(([1-9][0-9]{0,4})?)";
  }
}