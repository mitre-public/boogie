package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The “Datum Code” field defines the Local Horizontal Reference Datum to which a geographical position, expressed in latitude and longitude, is associated.
 */
public final class DatumCode extends TrimmableString {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.197";
  }
}
