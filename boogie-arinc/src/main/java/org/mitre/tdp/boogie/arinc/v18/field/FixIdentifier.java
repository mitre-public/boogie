package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Fix Identifier” field contains the five-character-name-code, or other series of characters, with which
 * the fix is identified. This includes Waypoint Identifiers, VHF NA V AID Identifiers, NDB NA V AID identifier,
 * Airport Identifiers, and Runway Identifiers.
 */
public final class FixIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.13";
  }
}
