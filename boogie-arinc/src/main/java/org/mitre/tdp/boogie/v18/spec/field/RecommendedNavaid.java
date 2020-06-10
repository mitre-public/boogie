package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Recommended Navaid” field allows the reference facility for the waypoint in a given record “Fix Ident” field or
 * for an Airport or Heliport to be specified. VHF, NDB (Enroute and Terminal), Localizer, TACAN, GLS and MLS Navaids may
 * be referenced.
 */
public class RecommendedNavaid implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.23";
  }
}
