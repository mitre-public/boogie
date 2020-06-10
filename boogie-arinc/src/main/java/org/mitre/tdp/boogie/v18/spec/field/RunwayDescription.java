package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * f required, additional information concerning a runway can be included in a record in the “Runway Description” field.
 */
public class RunwayDescription implements FreeFormString {
  @Override
  public int fieldLength() {
    return 22;
  }

  @Override
  public String fieldCode() {
    return "5.59";
  }
}
