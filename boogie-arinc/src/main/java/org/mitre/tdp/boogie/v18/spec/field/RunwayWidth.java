package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericInteger;

/**
 * The width of the runway identified in the “Runway Identifier” field is specified in the “Runway Width” field.
 */
public class RunwayWidth implements NumericInteger {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.109";
  }
}
