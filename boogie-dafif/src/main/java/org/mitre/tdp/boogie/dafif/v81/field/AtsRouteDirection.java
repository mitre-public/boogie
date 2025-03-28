package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A ONE LETTER CODE INDICATING PREDOMINANT DIRECTION OF THE ATS ROUTE BASED ON THE ROUTES FIRST AND LAST POINTS.
 *
 * E = EAST
 * W = WEST
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * E, W
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class AtsRouteDirection extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 37;
  }

  @Override
  public String regex() {
    return "(E|W)";
  }
}