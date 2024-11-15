package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Frequency Protection Distance” field provides an indication of the distance to the next nearest NAVAID on the same frequency.
 * <br>
 * Source/Content: The distance to the next NAVAID will be computer generated values. Values will be entered on NAVAID with DME or
 * TACAN equipped facilities only and will indicate the distance, in nautical miles, to the next nearest DME or TACAN equipped
 * facility. Maximum relevant value will be 600 nautical miles.
 * <br>
 * e.g. 030, 150, 600
 */
public final class FrequencyProtectionDistance extends ArincDouble {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.150";
  }
}
