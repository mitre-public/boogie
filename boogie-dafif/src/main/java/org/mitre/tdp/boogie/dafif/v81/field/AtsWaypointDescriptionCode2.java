package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * ENABLES THE FUNCTION OF A WAYPOINTS LOCATION TO BE IDENTIFIED.
 *
 * WAYPOINT DESCRIPTION CODE 2:
 * E - END OF CONTINUOUS ATS ROUTE PROCEDURE
 * NULL
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * E, NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * THE CODE U (UNCHARTED A-ROUTE INTERSECTION) IS NO LONGER USED AND HAS BEEN REMOVED FROM THE RANGE OF ALLOWABLES.
 */
public final class AtsWaypointDescriptionCode2 extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 348;
  }

  @Override
  public String regex() {
    return "((E)?)";
  }
}