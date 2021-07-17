package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Frequency Protection Distance” field provides an indication of the distance to the next nearest NAVAID on the same frequency.
 */
public final class FrequencyProtectionDistance implements NumericDouble {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.150";
  }
}
