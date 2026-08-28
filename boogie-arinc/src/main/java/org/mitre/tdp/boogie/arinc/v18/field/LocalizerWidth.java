package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * The "Localizer Width" field specifies the localizer course width of the ILS facility defined in the record.
 * <br>
 * Localizer course widths from official government sources are entered into the field in degrees, tenths of a degree and hundredths
 * of a degree with the decimal point suppressed.
 * <br>
 * e.g. 0500, 0400, 0350
 */
public final class LocalizerWidth extends ArincDouble {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.51";
  }

  @Override
  protected int suppressedDecimalPlaces() {
    return 2;
  }
}
