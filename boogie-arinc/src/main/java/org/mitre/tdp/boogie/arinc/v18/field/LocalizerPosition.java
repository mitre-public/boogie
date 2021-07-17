package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Localizer/Azimuth Position” field defines the location of the facility antenna relative to one end of the runway.
 */
public final class LocalizerPosition implements NumericInteger {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.48";
  }
}
