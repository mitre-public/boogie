package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Waypoint Name/Description” field sets the unabbreviated name of a named waypoint or a definition of an unnamed waypoint.
 */
public class WaypointNameDescription implements FreeFormString {
  @Override
  public int fieldLength() {
    return 25;
  }

  @Override
  public String fieldCode() {
    return "5.42";
  }
}
