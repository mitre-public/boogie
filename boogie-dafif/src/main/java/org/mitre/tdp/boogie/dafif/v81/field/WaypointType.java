package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * DEFINES A WAYPOINTS FUNCTIONALLY.
 *
 *  I - UNNAMED, CHARTED, OR COMPUTER NAV FIX
 *  F - OFF ROUTE
 *  N - NDB
 *  R - NAMED
 *  V - VFR REPORTING POINT
 *  W - RNAV
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * I, F, N, R, V, W, IF, NR, NF, RF
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class WaypointType extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 342;
  }

  @Override
  public String regex() {
    return "(I|F|N|R|V|W|IF|NR|NF|RF)";
  }
}