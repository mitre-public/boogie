package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.BooleanString;

/**
 * The “IFR Capability” field indicates if the Airport/Heliport has any published Instrument Approach Procedures.
 */
public class IfrCapability implements BooleanString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.108";
  }
}
