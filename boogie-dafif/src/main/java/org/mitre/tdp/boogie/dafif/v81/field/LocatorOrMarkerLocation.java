package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES THE MARKER POSITION (IN THOUSANDTHS OF NAUTICAL MILES) FROM THE APPROACH END OF  THE RUNWAY.
 *
 * EXAMPLE(S):
 * NULL
 * 00001
 * 12694
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 *  00001 - 19999 (DECIMAL IS SUPPRESSED) (PADDED WITH LEADING ZEROS)
 *  OR
 *  U
 *  OR
 *  NULL
 *
 * SOURCE: SYSTEM GENERATED
 */
public class LocatorOrMarkerLocation extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 170;
  }

  @Override
  public String regex() {
    return "((1[0-9]{4}|0([0-9]{3}[1-9]|[0-9]{2}[1-9][0-9]|[0-9][1-9][0-9]{2}|[1-9][0-9]{3})|U)?)";
  }
}
