package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A ONE CHARACTER CODE OF 'C' TO INDICATE IF THE RUNWAY OR HELIPAD IS CLOSED OR UNUSABLE. (DAFIF)
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * C OR NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public class UsableRunway extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 67;
  }

  @Override
  public String regex() {
    return "(C|^$)";
  }
}
