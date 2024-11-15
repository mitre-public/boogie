package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Route Identifier” field identifies a route of flight or traffic orientation, using the coding employed on aeronautical
 * navigation charts and related publications.
 * <br>
 * For Enroute Airways, Route Identifier codes should be derived from official government publications.
 * <br>
 * Enroute Airway - V216, C1150, J380, UA16, UB414.
 */
public final class EnrouteRouteIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.8";
  }
}
