package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The “Route Type” field defines the type of Enroute Airway, Preferred Route, Airport and Heliport SID/STAR/Approach Routes of
 * which the record is an element.
 * <br>
 * For Airport and Heliport Approach Routes, “Route Type” includes a “primary route type,” and up to two “route type qualifiers.”
 */
public final class RouteType extends TrimmableString {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7";
  }
}
