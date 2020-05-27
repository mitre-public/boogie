package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Runway Identifier” field identifies the runways described in runway records and runways served by the ILS/MLS described in ILS/MLS records.
 *
 * Any other designations (suffixes), such as North, South, East, West or STOL will not be included in the ARINC file.
 */
public class RunwayIdentifier implements FreeFormString {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.46";
  }
}
