package org.mitre.tdp.boogie.arinc.v20.field;

import org.mitre.tdp.boogie.arinc.ArincInteger;

/**
 * The “Threshold Crossing Height” specifies the height above the landing threshold on a normal glide path.
 */
public final class ThresholdCrossingHeight extends ArincInteger {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.67";
  }
}