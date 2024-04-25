package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE LOWER VERTICAL LIMIT OF THE ATS ROUTE STRUCTURE OR MINIMUM FLIGHT ALTITUDE (MFA) OF THE  CONTROLLING AIRSPACE WHICHEVER IS HIGHER.
 *
 * FOR FLIGHT LEVELS SEE DAFIF SPECIFICATION, CHAPTER 3, TERMS AND DEFINITIONS.
 *
 * EXAMPLE(S):
 * IF ATS LOWER VERTICAL LIMIT = FL225 AND UIR LOWER VERTICAL LIMIT = FL245 THEN LOWER_LIMIT=FL245
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * 01000 - 99999 (NUMERIC VALUES PADDED WITH LEADING ZEROS)
 * OR
 * FL10 - FL999
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class LowerLimit extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 173;
  }

  @Override
  public String regex() {
    return "((([0-9][1-9][0-9]{3}|[1-9][0-9]{4})|FL([1-9][0-9]{1,2}))?)";
  }
}