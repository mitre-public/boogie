package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The “Recommended Navaid” field allows the reference facility for the waypoint in a given record “Fix Ident” field or
 * for an Airport or Heliport to be specified. VHF, NDB (Enroute and Terminal), Localizer, TACAN, GLS and MLS Navaids may
 * be referenced.
 */
public final class RecommendedNavaid extends TrimmableString {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.23";
  }
}
