package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * SPEED LIMIT ALTITUDE IS THE ALTITUDE BELOW WHICH SPEED LIMITS MAY BE IMPOSED.
 *
 * FOR FLIGHT LEVELS SEE DAFIF SPECIFICATION, CHAPTER 3, TERMS AND DEFINITIONS.
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * 0 - 99999  (LEADING ZEROS SUPPRESSED)
 *  OR
 *  FL10 - FL999
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class SpeedLimitAltitude extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 297;
  }

  @Override
  public String regex() {
    return "(([0-9]{5}|(FL)[1-9][0-9]{1,2})?)";
  }
}