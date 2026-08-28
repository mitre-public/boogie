package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * The “Localizer Frequency” field specifies the VHF frequency of the facility identified in the “Localizer Identifier” field.
 * <br>
 * The official government-source localizer frequency is entered into the field with a resolution of 50 kHz. The decimal point
 * following the unit MHz entry is suppressed.
 * <br>
 * e.g. 11030, 11195
 */
public final class LocalizerFrequency extends ArincDouble {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.45";
  }

  @Override
  protected int suppressedDecimalPlaces() {
    return 2;
  }
}
