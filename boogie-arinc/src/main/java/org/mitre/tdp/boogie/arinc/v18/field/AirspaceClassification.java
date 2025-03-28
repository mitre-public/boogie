package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The Controlled Airspace Classification field will contain an alpha character indicating the published
 * classification of the controlled airspace, when assigned.
 *
 * Classification codes will be derived from official government sources. If source does not provide a classification,
 * the field will be blank.
 *
 * Examples: A through G
 */

public final class AirspaceClassification extends TrimmableString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.215";
  }
}
