package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The Restrictive Airspace Designation field contains the number or name that uniquely identifies the restrictive airspace.
 * <p>
 * The identifiers will be derived from official government sources.The field will contain a numeric number, or when designation is by name this field
 * will contain the name up to 10 characters. When name is longer than 10 characters,
 * the 10th position will contain an asterisk indicating the name field should be used for
 * the full designator.
 */
public final class RestrictiveAirspaceDesignation extends TrimmableString {
  @Override
  public int fieldLength() {
    return 10;
  }

  @Override
  public String fieldCode() {
    return "5.129";
  }
}
