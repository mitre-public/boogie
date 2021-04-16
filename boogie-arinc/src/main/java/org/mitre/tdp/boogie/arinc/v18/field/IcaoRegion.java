package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “ICAO Code” field permits records to be categorized geographically within the limits of the categorization performed
 * by the {@link CustomerAreaCode} field.
 *
 * e.g. K1, K7, PA, MM, EG, UT
 */
public class IcaoRegion implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.14";
  }
}
