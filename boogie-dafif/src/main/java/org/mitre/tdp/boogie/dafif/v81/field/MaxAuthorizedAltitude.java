package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE HIGHEST ALTITUDE ON A SEGMENT WITH A DESIGNATED MINIMUM FLIGHT ALTITUDE (MFA) AT  WHICH ADEQUATE RECEPTION OF NAVIGATIONAL AID SIGNALS IS ASSURED.
 *
 * FOR FLIGHT LEVELS SEE DAFIF SPECIFICATION, CHAPTER 3, TERMS AND DEFINITIONS.
 *
 * EXAMPLE(S):
 * NULL
 * 06000
 * 45000
 * FL115
 * FL660
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * 0 - 99999 (NUMERIC VALUES PADDED WITH LEADING ZEROS)
 *  OR
 *  FL10 - FL999
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class MaxAuthorizedAltitude extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 183;
  }

  @Override
  public String regex() {
    return "(([0-9]{5}|FL([1-9][0-9]{1,2}))?)";
  }
}