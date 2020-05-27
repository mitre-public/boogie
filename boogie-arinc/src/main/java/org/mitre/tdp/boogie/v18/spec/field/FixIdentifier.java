package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Fix Identifier” field contains the five-character-name-code, or other series of characters, with which
 * the fix is identified. This includes Waypoint Identifiers, VHF NA V AID Identifiers, NDB NA V AID identifier,
 * Airport Identifiers, and Runway Identifiers.
 */
public class FixIdentifier implements FreeFormString {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.13";
  }
}
