package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The “Waypoint Name/Description” field sets the unabbreviated name of a named waypoint or a definition of an unnamed waypoint.
 */
public final class WaypointNameDescription extends TrimmableString {

  @Override
  public int fieldLength() {
    return 25;
  }

  @Override
  public String fieldCode() {
    return "5.42";
  }
}
