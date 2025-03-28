package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * The “Localizer/Azimuth Position” field defines the location of the facility antenna relative to one end of the runway.
 * <br>
 * The field contains the official government source distance, in feet, from the antenna to the runway end. The resolution is
 * one foot.
 * <br>
 * e.g. 0950, 1000
 */
public final class LocalizerPosition extends ArincInteger {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.48";
  }
}
