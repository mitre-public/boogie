package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericInteger;

/**
 * The “Localizer/Azimuth Position” field defines the location of the facility antenna relative to one end of the runway.
 */
public class LocalizerPosition implements NumericInteger {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.48";
  }
}
