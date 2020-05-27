package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Route Type” field defines the type of Enroute Airway, Preferred Route, Airport and Heliport SID/STAR/Approach Routes of
 * which the record is an element. For Airport and Heliport Approach Routes, “Route Type” includes a “primary route type,” and
 * up to two “route type qualifiers.”
 */
public class RouteType implements FreeFormString {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7";
  }
}
