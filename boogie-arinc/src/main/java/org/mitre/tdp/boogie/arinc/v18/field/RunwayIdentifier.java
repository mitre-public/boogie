package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The “Runway Identifier” field identifies the runways described in runway records and runways served by the ILS/MLS described in ILS/MLS records.
 * <br>
 * Any other designations (suffixes), such as North, South, East, West or STOL will not be included in the ARINC file.
 */
public final class RunwayIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.46";
  }
}
