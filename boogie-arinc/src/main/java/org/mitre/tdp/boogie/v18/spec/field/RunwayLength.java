package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericInteger;

/**
 * The “Runway Length” field defines the total length of the runway surface declared suitable and available for ground
 * operations of aircraft for the runway identified in the records’ Runway Identifier field.
 */
public class RunwayLength implements NumericInteger {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.57";
  }
}
