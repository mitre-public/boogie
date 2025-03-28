package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * A VISUAL NAVAID DISPLAYING FLASHES OF WHITE AND/OR COLORED LIGHT INDICATING THE LOCATION OF AN AIRPORT. (AIM)
 *
 * FIELD TYPE: A
 *
 * ALLOWABLE VALUES:
 * Y OR NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class Beacon extends TrimmableString {

  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 45;
  }

  @Override
  public String regex() {
    return "(Y?)";
  }

}
