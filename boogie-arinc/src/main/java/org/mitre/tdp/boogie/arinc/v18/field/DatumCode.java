package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Datum Code” field defines the Local Horizontal Reference Datum to which a geographical position, expressed in latitude and longitude, is associated.
 */
public class DatumCode implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.197";
  }
}
