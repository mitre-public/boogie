package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “ATA/IATA” field contains the Airport/Heliport ATA/IATA designator code to which the data contained in the record relates.
 */
public class IataDesignator implements FreeFormString {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.107";
  }
}
