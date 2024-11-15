package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Fix Identifier” field contains the five-character-name-code, or other series of characters, with which the fix is identified.
 * <br>
 * This includes Waypoint Identifiers, VHF NAVAID Identifiers, NDB NAVAID identifier, Airport Identifiers, and Runway Identifiers.
 * <br>
 * e.g. SHARP, DEN43, BHM, RW27L, KGRR
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
