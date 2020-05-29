package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

/**
 * The “Frequency Protection Distance” field provides an indication of the distance to the next nearest NAVAID on the same frequency.
 */
public class FrequencyProtectionDistance implements NumericDouble {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.150";
  }
}
